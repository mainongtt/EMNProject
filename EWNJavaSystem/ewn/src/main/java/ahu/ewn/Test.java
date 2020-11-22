package ahu.ewn;

import java.awt.print.Printable;

import ahu.ewn.board.PieceType;
import ahu.ewn.game.GameState;
import ahu.ewn.game.Move;
import ahu.ewn.game.Player;
import ahu.ewn.strategy.evaluation.MySuperEvaluate;
import ahu.ewn.strategy.initial.StaticInitial;
import ahu.ewn.strategy.move.MySuperMove;
import ahu.ewn.strategy.move.MySuperMove;
import ahu.ewn.strategy.move.RandomMove;
import ahu.ewn.strategy.move.StaticEvaluationMove;

/**
 * 程序测试，比界面中的自动对弈快很多
 * 
 * @author 陆梦轩
 *
 */
public class Test {

	public static void main(String[] args) {
		// 对弈轮数
		int gameNum = 100;
		// 蓝方获胜轮数
		int blueWinNum = 0;
		// 红获胜轮数
		int redWinNum = 0;
		// 先手方，即蓝方先下棋
		PieceType firstPlayer = PieceType.BLUE;
		// 指定蓝方的布局策略和下棋策略
		Player bluePlayer = new Player(PieceType.BLUE, new StaticInitial(), new MySuperMove());
		// 指定红方的布局策略和下棋策略
		Player redPlayer = new Player(PieceType.RED, new StaticInitial(), new StaticEvaluationMove(new MySuperEvaluate()));
		// 定义一局游戏，并设置玩家为上面定义的玩家
		GameState game = new GameState();
		game.setPlayer(bluePlayer);
		game.setPlayer(redPlayer);
		// 对弈轮数的迭代
		for(int cnt = 1; cnt <= gameNum; cnt++) {
			// 重置游戏，生成初始布局
			game.reset(firstPlayer);
			// 红蓝双方轮流行棋，直至游戏结束
			while(game.isEnd()==false) {
				// 随机生成一个骰子点数
				byte dice = game.getDice();
				// 行棋方的走子策略产生一个合法走法
				Move move = game.getCurrentPlayer().getMoveStrategy().getMove(game, dice);
				// 执行合法走法，即下一步棋
				game.step(dice, move);
			}
			// 统计获胜方
			if(game.getWinner()==PieceType.BLUE) blueWinNum += 1;
			else redWinNum += 1;
			// 切换先手方
			firstPlayer = firstPlayer==PieceType.BLUE? PieceType.RED: PieceType.BLUE;
		}// for(int cnt = 1; cnt <= gameNum; cnt++)
		// 打印结果
		System.out.println("blue vs red: "+blueWinNum+" vs "+redWinNum);
	}

}
