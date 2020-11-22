package ahu.ewn.ui;

import ahu.ewn.game.Player;
import ahu.ewn.strategy.move.MoveStrategy;
import ahu.ewn.strategy.move.MySuperMove;
import javax.swing.JTextArea;

/**
 * 自定义走子策略面板，
 * 
 * @author 陆梦轩
 *
 */
public class MySuperMovePanel extends MoveStrategyPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2676196779865514206L;
	public MySuperMove moveStrategy;

	public MySuperMovePanel(Player player) {
		super(player);
		setLayout(null);
		
		JTextArea txtrmysupermovegetmove = new JTextArea();
		txtrmysupermovegetmove.setText("注：超级策略调用MySuperMove.getMove()");
		txtrmysupermovegetmove.setLineWrap(true);
		txtrmysupermovegetmove.setBounds(10, 10, 135, 68);
		add(txtrmysupermovegetmove);
		// TODO 自动生成的构造函数存根
		moveStrategy = new MySuperMove();
	}

	@Override
	public MoveStrategy getMoveStrategy() {
		// TODO 自动生成的方法存根
		return this.moveStrategy;
	}

}
