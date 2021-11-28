package me.aichina.ui;

import me.aichina.game.Player;
import me.aichina.strategy.move.MoveStrategy;
import me.aichina.strategy.move.RandomMove;

public class RandomMovePanel extends MoveStrategyPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4593992634958175356L;
	public RandomMove moveStrategy;
	
	/**
	 * Create the panel.
	 * 
	 * @param player 玩家
	 */
	public RandomMovePanel(Player player) {
		super(player);
		moveStrategy = new RandomMove();
	}

	@Override
	public MoveStrategy getMoveStrategy() {
		// TODO 自动生成的方法存根
		return this.moveStrategy;
	}

}
