package ahu.ewn.ui;

import javax.swing.JPanel;

import ahu.ewn.game.Player;
import ahu.ewn.strategy.move.MoveStrategy;

/**
 * 走子策略面板基类，用于设置Player的MoveStrategy
 * 
 * @author 陆梦轩
 *
 */
public abstract class MoveStrategyPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4073663085914745169L;
	
	protected Player player;

	/**
	 * Create the panel.
	 * 
	 * @param player 玩家
	 */
	public MoveStrategyPanel(Player player) {
		this.player = player;
	}
	
	public abstract MoveStrategy getMoveStrategy();

}
