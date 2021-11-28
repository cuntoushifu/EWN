package me.aichina.strategy.move;

import me.aichina.board.ChessBoard;
import me.aichina.board.PieceType;
import me.aichina.game.GameState;
import me.aichina.game.Move;
import me.aichina.game.MoveDirection;
import me.aichina.game.MoveGenerator;
import me.aichina.mcts.*;

import java.util.Map;

/**
 * 自定义走子策略，你可以实现该类的getMove策略，界面中的“超级策略”就是指这个策略
 *
 * @author 陆梦轩
 */
public class MySuperMove extends MoveStrategy {

    @Override
    public Move getMove(GameState gameState, byte dice) {
        // TODO 自动生成的方法存根
        //获取当前的棋盘
        ChessBoard board = gameState.getCurrentBoard();
        //获取当前的玩家颜色
        PieceType player = gameState.getCurrentPlayer().getTurn();



        Byte mode = board.method(player, dice);
        if (mode!=null){
            return new Move(mode, MoveDirection.FORWARD);
        }



        //通过走法产生器得到符合规则的走法
//        System.out.println("原本棋盘:\n" + board);
//        System.out.println("原本:\n" + board.getBoard().toString());
//        byte[][] bytes = board.getBoard();
//        for (byte[] aByte : bytes) {
//            for (byte b : aByte) {
//                System.out.println(Piece.toString(b));
//            }
//        }

        Map<Move, ChessBoard> legalMoves = MoveGenerator.getLegalMovesByDice(board, player, dice);

//        legalMoves.forEach((key, value) -> {
//            System.out.println("走法：\n" + key);
//            System.out.println();
//            System.out.println("棋盘：\n" + value);
//
//        });

      //  return new Mcts().getUCB(legalMoves, player);


       return new UCT2().getMaxUCTValueMove(legalMoves, player);  //  首选 //蓝297---203，红339--161
        // return new UCTCopy2().getBestMoveByUCT(legalMoves, player, 100000);//蓝335--165红327--173
       //return new UCTCopy().getMaxUCTValueMove(legalMoves, player);// 首选 蓝319--181，红339--161
      //  return new UCTCopy().getMaxUCTValueMove(legalMoves, player);
//1蓝2红--247--253 3蓝1红--241--259
//2蓝1红--240--260 1蓝3红--241--259 2蓝3红--253--247


        //------------------------------------------------
        //在这里完成你的策略，从legalMoves中选择一个Move,可以参考StaticEvaluationMove
        //例如
        //Move bestMove = ... ;
        //
        //如果希望界面能显示策略的价值、搜索深度、迭代次数等信息，就必须在这里对value、maxDepth、visitNum赋值
        //this.value = 0;
        //this.maxDepth = 0;
        //this.visitNum = 0;
        //
        //return bestMove;
        //------------------------------------------------

        //系统默认输出随机的走法，策略编码完成后可以将这一行删除
        //  return new RandomMove().getMove(gameState, dice);
    }

    @Override
    public Move getMove(PieceType turn, ChessBoard board, byte dice) {
        return null;
    }

    @Override
    public void processEnemyMove(Move move) {

    }

    @Override
    public void processStart(GameState gameState, PieceType myTurn) {

    }

    @Override
    public void processBack(GameState gameState, Move move) {


    }

    @Override
    public void processEnd() {


    }

}
