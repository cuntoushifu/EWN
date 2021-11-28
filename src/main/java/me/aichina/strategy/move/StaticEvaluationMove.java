package me.aichina.strategy.move;

import me.aichina.board.ChessBoard;
import me.aichina.board.PieceType;
import me.aichina.game.GameState;
import me.aichina.game.Move;
import me.aichina.game.MoveGenerator;
import me.aichina.strategy.evaluation.EvaluationFunction;

import java.util.Map;

/**
 * 根据指定的估值函数选择估值最高的走法，即界面中的“直接估值”策略<br>
 * 例如：<br>
 * 假设目前的合法走法有三个：[蓝3左  蓝3前  蓝3右]<br>
 * 这三个走法行棋后产生三个对应的棋盘：[board1 board2 board3]<br>
 * 经evaluateFunction计算，这三个棋盘对蓝方的价值分别为[0.2 0.6 0.1]<br>
 * 则该策略会认为 蓝3前 是最佳走法
 *
 * @author 陆梦轩
 *
 */
public class StaticEvaluationMove extends MoveStrategy{

    /**
     * 估值函数
     */
    private EvaluationFunction evaluateFunction;

    /**
     * 构造函数，指定估值函数
     *
     * @param function 估值函数
     */
    public StaticEvaluationMove(EvaluationFunction function){
        super();
        this.evaluateFunction=function;
        setLabel("StaticEvaluationMove");
    }

    /**
     * 获取估值函数
     *
     * @return EvaluateFunction 估值函数
     */
    public EvaluationFunction getEvaluateFunction(){
        return this.evaluateFunction;
    }

    /**
     * 设置估值函数
     *
     * @param evaluateFunction 估值函数
     */
    public void setEvaluationFunciton(EvaluationFunction evaluateFunction){
        this.evaluateFunction=evaluateFunction;
    }

    @Override
    public Move getMove(GameState gameState, byte dice) {
        // TODO 自动生成的方法存根

        PieceType turn=gameState.getCurrentPlayer().getTurn();
        ChessBoard board=gameState.getCurrentBoard();

        Map<Move,ChessBoard> moves= MoveGenerator.getLegalMovesByDice(board, turn, dice);

        double maxValue=Integer.MIN_VALUE;

        Move bestMove=null;
        for(Map.Entry<Move, ChessBoard> entry:moves.entrySet()){
            if(evaluateFunction.getValue(entry.getValue(), turn)>=maxValue){
                maxValue=evaluateFunction.getValue(entry.getValue(), turn);
                bestMove=entry.getKey();
            }
        }

        this.value=maxValue;

        if(bestMove==null){
//			bestMove=moves.keySet().iterator().next();
        }

        return bestMove;
    }

    @Override
    public Move getMove( PieceType turn,   ChessBoard board,byte dice) {



        Map<Move,ChessBoard> moves= MoveGenerator.getLegalMovesByDice(board, turn, dice);

        double maxValue=Integer.MIN_VALUE;

        Move bestMove=null;
        for(Map.Entry<Move, ChessBoard> entry:moves.entrySet()){
            if(evaluateFunction.getValue(entry.getValue(), turn)>=maxValue){
                maxValue=evaluateFunction.getValue(entry.getValue(), turn);
                bestMove=entry.getKey();
            }
        }

        this.value=maxValue;

        if(bestMove==null){
//			bestMove=moves.keySet().iterator().next();
        }

        return bestMove;
    }

    @Override
    public void processEnemyMove(Move move) {
        // TODO 自动生成的方法存根

    }

    @Override
    public void processStart(GameState gameState, PieceType myTurn) {
        // TODO 自动生成的方法存根

    }

    @Override
    public void processBack(GameState gameState, Move move) {
        // TODO 自动生成的方法存根

    }

    @Override
    public void processEnd() {
        // TODO 自动生成的方法存根

    }

}
