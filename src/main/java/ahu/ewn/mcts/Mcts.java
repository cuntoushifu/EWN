package ahu.ewn.mcts;

import ahu.ewn.board.ChessBoard;
import ahu.ewn.board.PieceType;
import ahu.ewn.game.Move;
import ahu.ewn.game.MoveGenerator;
import ahu.ewn.strategy.dice.RandomDice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author YangYe
 * @email imissyou5201314@outlook.com
 * @date 2020/10/6
 */
public class Mcts {

    private static final ThreadPoolExecutor pool = new ThreadPoolExecutor(
            6,
            12,
            10,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100000),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy()
    );


    public Move getUCB(Map<Move, ChessBoard> legalMoves, PieceType player) {
        ArrayList<FutureTask<MoveWinRate>> futureTasks = new ArrayList<>();
        legalMoves.forEach((keyMove, valueBoard) -> {
            FutureTask<MoveWinRate> futureTask = new FutureTask<MoveWinRate>(() ->
            {
                MoveWinRate winRate = getWinRate(keyMove, valueBoard, player, new RandomDice());
                return winRate;
            });
            futureTasks.add(futureTask);
            pool.execute(futureTask);
        });
        List<MoveWinRate> moveWinRates = futureTasks.stream().map(item -> {
            try {
                return item.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }).sorted((o1, o2) -> {
            if (o1.getWinRate() >= o2.getWinRate()) return -1;

            if (o1.getWinRate() < o2.getWinRate()) return 1;

            return 0;
        }).collect(Collectors.toList());
        return moveWinRates.get(0).getMove();
    }

    public MoveWinRate getWinRate(Move move, ChessBoard currentBoard, PieceType player, RandomDice randomDice) {
        int step = 5000;
        int winCount = 0;
        PieceType currentPlayer = (player == PieceType.RED) ? PieceType.BLUE : PieceType.RED;
        ChessBoard nowBoard = currentBoard.clone();
        for (int i = 0; i < step; i++) {
            while (true) {
                PieceType winner = nowBoard.getWinner();
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
        return new MoveWinRate(move, (winCount * 1.0) / (step * 1.0));
    }

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
