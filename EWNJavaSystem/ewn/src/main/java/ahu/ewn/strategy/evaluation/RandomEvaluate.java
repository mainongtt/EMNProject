package ahu.ewn.strategy.evaluation;

import java.util.Random;

import ahu.ewn.board.ChessBoard;
import ahu.ewn.board.PieceType;

/**
 * 随机估值，返回随机数
 * 
 * @author 陆梦轩
 *
 */
public class RandomEvaluate extends EvaluationFunction {
	
	/**
	 * 构造函数，创建一个随机估值函数
	 */
	public RandomEvaluate() {
		setLabel("RandomEvaluate");
	}

	@Override
	public double getValue(ChessBoard board, PieceType type) {
		// TODO 自动生成的方法存根
		

		return new Random().nextDouble()*2 - 1;
	}

}
