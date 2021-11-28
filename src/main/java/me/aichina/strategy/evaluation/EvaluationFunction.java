package me.aichina.strategy.evaluation;

import me.aichina.board.ChessBoard;
import me.aichina.board.PieceType;

/**
 * 评估函数基类，getValue方法用于对棋盘进行评估
 *
 * @author 陆梦轩
 *
 */
abstract public class EvaluationFunction {

    /**
     * 标签
     */
    private String label;

    /**
     * 计算评估局面价值
     *
     * @param board 棋盘
     * @param type 棋子类型
     * @return double，值越高表示type的一方越有利
     */
    public abstract double getValue(ChessBoard board,PieceType type);

    /**
     * 获取标签
     *
     * @return String 标签
     */
    public String getLabel(){
        return this.label;
    }

    /**
     * 设置标签
     *
     * @param label 标签
     */
    public void setLabel(String label){
        this.label=label;
    }

    @Override
    public String toString(){
        return this.label;
    }
}
