package ahu.ewn.mcts;

import ahu.ewn.board.ChessBoard;
import ahu.ewn.board.PieceType;
import ahu.ewn.constant.ParamConstant;
import ahu.ewn.constant.StepsConstant;
import ahu.ewn.game.Move;
import ahu.ewn.game.MoveGenerator;
import ahu.ewn.game.Player;
import ahu.ewn.strategy.evaluation.ODEMAEvaluate;
import ahu.ewn.strategy.initial.StaticInitial;
import ahu.ewn.strategy.move.StaticEvaluationMove;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


/**
 * @author YangYe
 * @email imissyou5201314@outlook.com
 * @date 2020/10/6
 */
public class UCTCopy {

    private Random random = new Random();

    private static final ThreadPoolExecutor pool = new ThreadPoolExecutor(
            12,
            12,
            10,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100000),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy()
    );

    /**
     * 获得UCTValue最高的走法
     *
     * @param legalMoves 当前合法走法集合
     * @param player     当前玩家
     * @return 当前UCTValue值最大的的走法
     */
    public Move getMaxUCTValueMove(Map<Move, ChessBoard> legalMoves, PieceType player) {
        return getMoveUCTList(legalMoves, player).get(0).getMove();
    }

    public Move getBestMoveByUCT(Map<Move, ChessBoard> legalMoves, PieceType player, Integer steps) {
        List<MoveUCT> moveUCTS = getMoveUCTList(legalMoves, player);
        Integer total = moveUCTS.get(0).getTotalCount();
        List<MoveUCT> moveUCTList = moveUCTS.stream().filter(item -> item.getUCTValue() > 0.255).collect(Collectors.toList());
        for (Integer i = 0; i < steps; i++) {
            for (MoveUCT moveUCT : moveUCTList) {
                executeMCTS(moveUCT, 1);
                total++;
                moveUCT.setTotalCount(total);
                moveUCT.calculateUCTValue(total);
            }

        }
        Collections.sort(moveUCTS);

//        for (MoveUCT moveUCT : moveUCTS) {
//            System.out.println("Move:"+ moveUCT.getMove()+" UCBValue："+moveUCT.getUCTValue());
//        }
//        System.out.println("选中走法的UCBValue："+moveUCTS.get(0).getUCTValue());

        return moveUCTS.get(0).getMove();
    }

    public MoveUCT executeMCTS(MoveUCT moveUCT, int stepCount) {
        PieceType player = moveUCT.getType();
        ChessBoard currentBoard = moveUCT.getBoard();

        int winCount = 0;
        PieceType currentPlayer = (player == PieceType.RED) ? PieceType.BLUE : PieceType.RED;
        ChessBoard nowBoard = currentBoard.clone();
        for (int i = 0; i < stepCount; i++) {
            while (true) {
                if (nowBoard.isEnd()) {
                    if (nowBoard.isWin(player)) {
                        winCount++;
                    }
                    break;
                }
                byte dice = getRandomDice();
                Map<Move, ChessBoard> legalMovesByDice = MoveGenerator.getLegalMovesByDice(nowBoard, currentPlayer, dice);
                Move randomMove = getRandomMove(nowBoard, currentPlayer, dice);
                nowBoard = legalMovesByDice.get(randomMove);
                currentPlayer = (currentPlayer == PieceType.RED) ? PieceType.BLUE : PieceType.RED;
            }
            nowBoard = currentBoard.clone();
        }
        moveUCT.addSearchCount(stepCount);
        //  moveUCT.addTotalCount(stepCount);
        moveUCT.addWinCount(winCount);
        return moveUCT;
    }

    public List<MoveUCT> getMoveUCTList(Map<Move, ChessBoard> legalMoves, PieceType player) {
        ArrayList<FutureTask<MoveUCT>> futureTasks = new ArrayList<>();
        legalMoves.forEach((keyMove, valueBoard) -> {
            FutureTask<MoveUCT> futureTask = new FutureTask<MoveUCT>(() -> executeMCTSPK(keyMove, valueBoard, player, StepsConstant.steps.getCode()));
            futureTasks.add(futureTask);
            pool.execute(futureTask);
        });
        AtomicInteger total = new AtomicInteger();
        List<MoveUCT> moveUCTList = futureTasks.stream().map(item -> {
            try {
                MoveUCT moveUCT = item.get();
                total.addAndGet(moveUCT.getSearchCount());
                return moveUCT;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());

        int finalTotal = total.get();
        List<MoveUCT> moveUCTS = moveUCTList.stream().peek(item -> /*getUCTValue(finalTotal, item)*/ item.calculateUCTValue(finalTotal)).sorted((o1, o2) -> {
            if (o1.getUCTValue() >= o2.getUCTValue()) return -1;
            if (o1.getUCTValue() < o2.getUCTValue()) return 1;
            return 0;
        }).collect(Collectors.toList());
        for (MoveUCT moveUCT : moveUCTS) {
            System.out.println("Move:"+ moveUCT.getMove()+" UCBValue："+moveUCT.getUCTValue());
        }
        System.out.println("选中走法的UCBValue："+moveUCTS.get(0).getUCTValue());
        System.out.println();
        System.out.println();

        return moveUCTS;
    }

//    private void getUCTValue(int finalTotal, MoveUCT moveUCT) {
//        moveUCT.setUCTValue(0.4 * moveUCT.getWinRate() + 1.0 * Math.sqrt(Math.log(finalTotal) / moveUCT.getCurrentTotalCount()));
//    }


    /**
     * @param move         走法
     * @param currentBoard 当前棋盘
     * @param player       当前玩家
     * @param stepCount    搜索次数
     * @return MoveUCT
     */
    public MoveUCT executeMCTS(Move move, ChessBoard currentBoard, PieceType player, int stepCount) {

        int winCount = 0;
        PieceType currentPlayer = (player == PieceType.RED) ? PieceType.BLUE : PieceType.RED;
        ChessBoard nowBoard = currentBoard.clone();
        for (int i = 0; i < stepCount; i++) {
            while (true) {
                if (nowBoard.isEnd()) {
                    if (nowBoard.isWin(player)) {
                        winCount++;
                    }
                    break;
                }
                byte dice = getRandomDice();
                Map<Move, ChessBoard> legalMovesByDice = MoveGenerator.getLegalMovesByDice(nowBoard, currentPlayer, dice);
                Move randomMove = getRandomMove(nowBoard, currentPlayer, dice);
                nowBoard = legalMovesByDice.get(randomMove);
                currentPlayer = (currentPlayer == PieceType.RED) ? PieceType.BLUE : PieceType.RED;
            }
            nowBoard = currentBoard.clone();
        }
        return new MoveUCT(move, winCount, stepCount, currentBoard, player);

    }

    public MoveUCT executeMCTSPK(Move move, ChessBoard currentBoard, PieceType player, int stepCount) {

        int winCount = 0;
        PieceType currentPlayer = (player == PieceType.RED) ? PieceType.BLUE : PieceType.RED;
        ChessBoard nowBoard = currentBoard.clone();
        for (int i = 0; i < stepCount; i++) {
            while (true) {
                if (nowBoard.isEnd()) {
                    if (nowBoard.isWin(player)) {
                        winCount++;
                    }
                    break;
                }
                byte dice = getRandomDice();
                Move move1 = null;
                Map<Move, ChessBoard> legalMovesByDice = MoveGenerator.getLegalMovesByDice(nowBoard, currentPlayer, dice);
                if (currentPlayer != player) {
                    move1 = moveByEvaluate(currentPlayer, nowBoard, dice);
                } else {
                    move1 = getRandomMove(nowBoard, currentPlayer, dice);
                }

                nowBoard = legalMovesByDice.get(move1);
                currentPlayer = (currentPlayer == PieceType.RED) ? PieceType.BLUE : PieceType.RED;
            }
            nowBoard = currentBoard.clone();
        }
        return new MoveUCT(move, winCount, stepCount, currentBoard, player);

    }

    public Move moveByEvaluate(PieceType type, ChessBoard board, byte dice) {


        return new StaticEvaluationMove(new ODEMAEvaluate()).getMove(type, board, dice);
    }

    /**
     * 产生一个合理的随机走法
     *
     * @param board 当前棋盘
     * @param turn  当前玩家
     * @param dice  当前骰子数
     * @return 合适的随机走法
     */
    public Move getRandomMove(ChessBoard board, PieceType turn, byte dice) {
        Map<Move, ChessBoard> legalMoves = MoveGenerator.getLegalMovesByDice(board, turn, dice);
        List<Move> keyList = new ArrayList<Move>(legalMoves.keySet());
        Move randomKey = null;
        try {
            randomKey = keyList.get(new Random().nextInt(keyList.size()));
        } catch (Exception e) {
            System.out.println(keyList);
            e.printStackTrace();
        }
        return randomKey;
    }

    public byte getRandomDice() {
        return (byte) (1 + random.nextInt(6));
    }

}
