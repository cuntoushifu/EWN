package me.aichina.mcts;

import me.aichina.game.Move;

/**
 * @author YangYe
 * @email imissyou5201314@outlook.com
 * @date 2020/10/6
 */
public class MoveWinRate {

    private Move move;
    private double winRate;

    public MoveWinRate(Move move, Double winRate) {
        this.move = move;
        this.winRate = winRate;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public double getWinRate() {
        return winRate;
    }

    public void setWinRate(double winRate) {
        this.winRate = winRate;
    }


    @Override
    public String toString() {
        return "MoveWinRate{" +
                "move=" + move +
                ", winRate=" + winRate +
                '}';
    }
}
