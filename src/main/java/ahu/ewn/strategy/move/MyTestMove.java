package ahu.ewn.strategy.move;

import ahu.ewn.board.ChessBoard;
import ahu.ewn.board.PieceType;
import ahu.ewn.game.GameState;
import ahu.ewn.game.Move;
import ahu.ewn.game.MoveGenerator;
import ahu.ewn.mcts.Mcts;
import ahu.ewn.mcts.UCT;
import ahu.ewn.mcts.UCTCopy;
import ahu.ewn.mcts.UCTCopy2;
import ahu.ewn.strategy.move.MySuperMove;

import java.util.Map;


public class MyTestMove extends MySuperMove {

    @Override
    public Move getMove(GameState gameState, byte dice) {
        ChessBoard board = gameState.getCurrentBoard();
        //获取当前的玩家颜色
        PieceType player = gameState.getCurrentPlayer().getTurn();
        //通过走法产生器得到符合规则的走法
        Map<Move, ChessBoard> legalMoves = MoveGenerator.getLegalMovesByDice(board, player, dice);
       // return new UCTCopy().getMaxUCTValueMove(legalMoves, player);//蓝335--165红327--173
        return new UCT().getMaxUCTValueMove(legalMoves, player);
        //return new UCT().getMaxUCTValueMove(legalMoves, player);
    }
}
