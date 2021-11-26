package ahu.ewn.ui;

import ahu.ewn.game.Player;
import ahu.ewn.strategy.move.MoveStrategy;
import ahu.ewn.strategy.move.MySuperMove;
import ahu.ewn.strategy.move.MyTestMove;

import javax.swing.*;

/**
 * 自定义走子策略面板，
 * 
 * @author 陆梦轩
 *
 */
public class MySuperMovePanel2 extends MoveStrategyPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = -2676196779865514206L;
	public MySuperMove moveStrategy;

	public MySuperMovePanel2(Player player) {
		super(player);
		setLayout(null);
		
		JTextArea txtrmysupermovegetmove = new JTextArea();
		txtrmysupermovegetmove.setText("注：策略2调用MyTestMove.getMove()");
		txtrmysupermovegetmove.setLineWrap(true);
		txtrmysupermovegetmove.setBounds(10, 10, 135, 68);
		add(txtrmysupermovegetmove);

		moveStrategy = new MyTestMove();
	}

	@Override
	public MoveStrategy getMoveStrategy() {
		// TODO 自动生成的方法存根
		return this.moveStrategy;
	}

}
