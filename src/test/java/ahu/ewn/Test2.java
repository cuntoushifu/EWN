package ahu.ewn;

import me.aichina.board.PieceType;
import me.aichina.game.GameState;
import me.aichina.game.Move;
import me.aichina.game.Player;
import me.aichina.strategy.evaluation.ODEMAEvaluate;
import me.aichina.strategy.initial.StaticInitial;
import me.aichina.strategy.move.MySuperMove;
import me.aichina.strategy.move.StaticEvaluationMove;

/**
 * 程序测试，比界面中的自动对弈快很多
 *
 * @author 陆梦轩
 */
public class Test2 {

    public static void main(String[] args) {
        // 对弈轮数
        int gameNum =1000;
        // 蓝方获胜轮数
        int blueWinNum = 0;
        // 红获胜轮数
        int redWinNum = 0;
        // 先手方，即蓝方先下棋
        PieceType firstPlayer = PieceType.RED;

        // 指定蓝方的布局策略和下棋策略
        Player bluePlayer = new Player(PieceType.BLUE, new StaticInitial(), new MySuperMove());
        // Player bluePlayer = new Player(PieceType.BLUE, new StaticInitial(),  new StaticEvaluationMove(new MySuperEvaluate()));
        // 指定红方的布局策略和下棋策略
        // Player redPlayer = new Player(PieceType.RED, new StaticInitial(), new StaticEvaluationMove(new MySuperEvaluate()));
      //  Player redPlayer = new Player(PieceType.RED, new StaticInitial(), new MySuperMove());
        Player redPlayer = new Player(PieceType.RED, new StaticInitial(),new StaticEvaluationMove(new ODEMAEvaluate()) );

        // 定义一局游戏，并设置玩家为上面定义的玩家
        GameState game = new GameState();
        game.setPlayer(bluePlayer);
        game.setPlayer(redPlayer);
        // 对弈轮数的迭代
        for (int cnt = 1; cnt <= gameNum; cnt++) {
            // 重置游戏，生成初始布局
            game.reset(firstPlayer);
            // 红蓝双方轮流行棋，直至游戏结束
            while (!game.isEnd()) {
                // 随机生成一个骰子点数
                byte dice = game.getDice();
                // 行棋方的走子策略产生一个合法走法
                Move move = game.getCurrentPlayer().getMoveStrategy().getMove(game, dice);
                // 执行合法走法，即下一步棋
                game.step(dice, move);
            }
            // 统计获胜方
            if (game.getWinner() == PieceType.BLUE) blueWinNum += 1;
            else redWinNum += 1;
            System.out.println("第"+cnt+"局"+"获胜方："+game.getWinner()+"   红方获胜场次："+redWinNum+"   蓝方获胜场次："+blueWinNum);
            // 切换先手方
          //  firstPlayer = firstPlayer == PieceType.BLUE ? PieceType.RED : PieceType.BLUE;
        }// for(int cnt = 1; cnt <= gameNum; cnt++)
        // 打印结果
        System.out.println("blue 改良UCT算法（后手） vs red 估值: " + blueWinNum + " vs " + redWinNum);
    }

}
