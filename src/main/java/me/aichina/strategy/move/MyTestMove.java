package me.aichina.strategy.move;

import me.aichina.board.ChessBoard;
import me.aichina.board.PieceType;
import me.aichina.game.GameState;
import me.aichina.game.Move;
import me.aichina.game.MoveDirection;
import me.aichina.game.MoveGenerator;
import me.aichina.mcts.UCTCopy;

import java.util.Map;


public class MyTestMove extends MySuperMove {

    @Override
    public Move getMove(GameState gameState, byte dice) {
        ChessBoard board = gameState.getCurrentBoard();
        //获取当前的玩家颜色
        PieceType player = gameState.getCurrentPlayer().getTurn();

        Byte mode = board.method(player, dice);
        if (mode!=null){
            return new Move(mode, MoveDirection.FORWARD);
        }

        //通过走法产生器得到符合规则的走法
        Map<Move, ChessBoard> legalMoves = MoveGenerator.getLegalMovesByDice(board, player, dice);

       // return new UCTCopy().getMaxUCTValueMove(legalMoves, player);//蓝335--165红327--173
     //   return new UCT().getMaxUCTValueMove(legalMoves, player);
        //return new UCT().getMaxUCTValueMove(legalMoves, player);
        return new UCTCopy().getMaxUCTValueMove(legalMoves, player);// 首选 蓝319--181，红339--161
    }
}
