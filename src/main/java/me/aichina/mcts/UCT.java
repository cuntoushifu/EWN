package me.aichina.mcts;

import me.aichina.board.ChessBoard;
import me.aichina.board.PieceType;
import me.aichina.constant.StepsConstant;
import me.aichina.game.Move;
import me.aichina.game.MoveGenerator;
import me.aichina.strategy.dice.RandomDice;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


/**
 *
 * 一级
 * @author YangYe
 * @email imissyou5201314@outlook.com
 * @date 2020/10/6
 */
public class UCT {
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
     * @param legalMoves 当前合法走法集合
     * @param player 当前玩家
     * @return 当前UCTValue值最大的的走法
     */
    public Move getMaxUCTValueMove(Map<Move, ChessBoard> legalMoves, PieceType player) {
        return getMoveUCTList(legalMoves, player).get(0).getMove();
    }
    public List<MoveUCT> getMoveUCTList(Map<Move, ChessBoard> legalMoves, PieceType player) {
        ArrayList<FutureTask<MoveUCT>> futureTasks = new ArrayList<>();
        legalMoves.forEach((keyMove, valueBoard) -> {
            FutureTask<MoveUCT> futureTask = new FutureTask<MoveUCT>(() ->
                    executeMCTS(keyMove, valueBoard, player,
                            new RandomDice(), StepsConstant.steps.getCode()));
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
        List<MoveUCT> moveUCTS = moveUCTList.stream().peek(item ->                                            /*getUCTValue(finalTotal, item)*/
                item.calculateUCB(finalTotal)).sorted((o1, o2) -> {
            if (o1.getUCTValue() >= o2.getUCTValue()) return -1;
            if (o1.getUCTValue() < o2.getUCTValue()) return 1;
            return 0;
        }).collect(Collectors.toList());
        return moveUCTS;
    }

//    private void getUCTValue(int finalTotal, MoveUCT moveUCT) {
//        moveUCT.setUCTValue(0.4 * moveUCT.getWinRate() + 1.0 * Math.sqrt(Math.log(finalTotal) / moveUCT.getCurrentTotalCount()));
//    }


    /**
     *
     * @param move 走法
     * @param currentBoard 当前棋盘
     * @param player 当前玩家
     * @param randomDice 随机骰子数工厂
     * @param stepCount 搜索次数
     * @return  MoveUCT
     */
    public MoveUCT executeMCTS(Move move, ChessBoard currentBoard, PieceType player, RandomDice randomDice, int stepCount) {

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
                byte dice = randomDice.getDice();
                Map<Move, ChessBoard> legalMovesByDice = MoveGenerator.getLegalMovesByDice(nowBoard, currentPlayer, dice);
                Move randomMove = getRandomMove(nowBoard, currentPlayer, dice);
                nowBoard = legalMovesByDice.get(randomMove);
                currentPlayer = (currentPlayer == PieceType.RED) ? PieceType.BLUE : PieceType.RED;
            }
            nowBoard = currentBoard.clone();
        }
        return new MoveUCT(move, winCount, stepCount);
    }


    /**
     * 产生一个合理的随机走法
     * @param board 当前棋盘
     * @param turn 当前玩家
     * @param dice 当前骰子数
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

}
