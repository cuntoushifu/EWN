package me.aichina.strategy.evaluation;

import me.aichina.board.ChessBoard;
import me.aichina.board.PieceType;

/**
 * 自定义估值函数，需要在此类中完善估值的代码。界面中的“直接估值”策略里的“超级估值”就是指这个类
 *
 * @author 陆梦轩
 *
 */
public class MySuperEvaluate extends EvaluationFunction{

    @Override
    public double getValue(ChessBoard board, PieceType type) {
        // TODO 自动生成的方法存根
        //-------------------------------------------------------------
        //		在这里完成你的评估函数，计算并返回棋盘对玩家type的价值，值越大越对玩家type有利
        //		例如：
        //		PieceType winner = board.getWinner();
        //		if(winner == type) return 1;  //我方获胜
        //		else if(winner != PieceType.NULL) return -1;  //敌方获胜
        //		else return 0; //没有人获胜
        //-------------------------------------------------------------
        // 默认返回值为0，评估函数设计完成之后，可以将这一行删除



        return 0;
    }

}
