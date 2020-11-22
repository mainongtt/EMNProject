package ahu.ewn.strategy.move;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


import ahu.ewn.board.ChessBoard;
import ahu.ewn.board.PieceType;
import ahu.ewn.game.GameState;
import ahu.ewn.game.Move;
import ahu.ewn.game.MoveDirection;
import ahu.ewn.game.MoveGenerator;
import ahu.ewn.strategy.evaluation.*;
import java.lang.Math;
/**
 * 自定义走子策略，你可以实现该类的getMove策略，界面中的“超级策略”就是指这个策略
 * 
 * @author 陆梦轩
 *
 */
public class MySuperMove extends MoveStrategy{

	public Move getMove(GameState gameState, byte dice) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void processEnemyMove(Move move) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void processStart(GameState gameState, PieceType myTurn) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void processBack(GameState gameState, Move move) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void processEnd() {
		// TODO 自动生成的方法存根
		
	}
	
}
