package ahu.ewn.strategy.move;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ahu.ewn.board.ChessBoard;
import ahu.ewn.board.PieceType;
import ahu.ewn.game.GameState;
import ahu.ewn.game.Move;
import ahu.ewn.game.MoveGenerator;
import ahu.ewn.strategy.evaluation.*;
import java.lang.Math;
/**
 * 自定义走子策略，你可以实现该类的getMove策略，界面中的“超级策略”就是指这个策略
 * 
 * @author 陆梦轩
 *
 */
public class MySuperMove1 extends MoveStrategy{

	private static final int DEPTH = 1;
	private static final double INNF = 99999999;

	@Override
	public Move getMove(GameState gameState, byte dice) {
		/**
		 * 采用极大极小搜索算法实现的代码。深度搜索为1层
		 */
		//获取当前的棋盘
		ChessBoard board = gameState.getCurrentBoard();
		//获取当前的玩家颜色
		PieceType player = gameState.getCurrentPlayer().getTurn();
		//通过走法产生器得到符合规则的走法
		Map<Move, ChessBoard> legalMoves = MoveGenerator.getLegalMovesByDice(board, player, dice);
		//将其转成List
		List<Move> keyList = new ArrayList<Move>(legalMoves.keySet());
		//定义最好的走法
		Move bestMove = null;
		//定义最好的走法得到的评估函数的评分
		double best_code = -INNF;
		double code = 0.0;
		//MySuperEvaluate evl = new MySuperEvaluate();
		//遍历所有符合规则的走法
		for (int i = 0;i<keyList.size();i++){
			//对当前的棋盘进行克隆，方便进行回溯
			ChessBoard board1 = board.clone();
			Move move = keyList.get(i);
			//处理走子动作，更新棋盘，返回处理结果
			//若有棋子被吃，返回被吃的棋子；若无棋子被吃，返回0；若棋子位置越界，返回-1
			board1.processMove(move);
			//评估当前棋盘的评分
			if(player == PieceType.RED){
				code = odem(1,0,1,move,board1);
			}else{
				code = odem(1,0,0,move,board1);
			}
			
				//code = -NegaMax(gameState,dice,depth);
			//得到最好的评分
			if(code > best_code){
				best_code = code;
				bestMove = move;
			}
		}
			this.value = best_code;
			this.maxDepth = DEPTH;
           
		
//      Move randomKey = keyList.get(new Random().nextInt(keyList.size()));
//		for (int i =0 ; i<5;i++)
//			for (int j =0;j<5;j++)
//				System.out.println(board.getBoard()[i][j]);
		
		//------------------------------------------------
		//在这里完成你的策略，从legalMoves中选择一个Move,可以参考StaticEvaluationMove
		//例如
		//Move bestMove = ... ;
		//
		//如果希望界面能显示策略的价值、搜索深度、迭代次数等信息，就必须在这里对value、maxDepth、visitNum赋值
		//this.value = 0;
		//this.maxDepth = 0;
		//this.visitNum = 0;
		//
		//return bestMove;
		//------------------------------------------------
		
		//系统默认输出随机的走法，策略编码完成后可以将这一行删除
		
		//return new RandomMove().getMove(gameState, dice);
			//System.out.println("新策略");
		return bestMove;
		
	}
	
	// max 代表是否为随机层， dep代表深度，side 代表游戏玩家，move 代表移动
	double odem(int max,int dep,int side,Move move,ChessBoard board){
		double value=0;
		if(dep == DEPTH){
			MySuperEvaluate evl = new MySuperEvaluate();
			PieceType type = null;
			if(side == 0){
				type = PieceType.RED;
			}else{
				type = PieceType.BLUE;
			}
			value = evl.getValue(board, type);
			return value;
		}
		
		if(max == 1 && side ==1){  //蓝色棋子
			PieceType type = null;
			if(side == 0){
				type = PieceType.RED;
			}else{
				type = PieceType.BLUE;
			}
			double valuemin = INNF;
			List<Byte> pieces = board.getPieces(type);				
			for (int i =0; i<pieces.size();i++){
				double maxvalue = -INNF;
				//根据指定的棋子，求能使该棋子移动的骰子点数；是p[i]
				List<Byte> count = board.getDicesByPiece(pieces.get(i));
				int p = count.size();
				//指定棋子移动的走法
				Map<Move,ChessBoard> moves = MoveGenerator.getLegalMovesByPiece(board,pieces.get(i));
				List<Move> keyList = new ArrayList<Move>(moves.keySet());
				for (int j = 0;j<keyList.size();j++){
					ChessBoard	boardtemp = board.clone();						
					Move moveblue = keyList.get(j);
					if(board.isWin(type)){
						value = INNF;
						return value;
					}
					boardtemp.processMove(moveblue);
					double valueBlue = odem(1,dep+1,0,moveblue,boardtemp);
					if(valueBlue > maxvalue)
						maxvalue = valueBlue;
					
				}
				double value1 = maxvalue * p;
				if(value1 < valuemin)
					valuemin = value1;
			}
		value = valuemin;
		}else if(max == 1 && side ==0){  //红色棋子

			PieceType type = null;
			if(side == 0){
				type = PieceType.RED;
			}else{
				type = PieceType.BLUE;
			}
			double minvalue = INNF;
			List<Byte> pieces = board.getPieces(type);
			for (int i =0; i<pieces.size();i++){			
				double maxvalue = -INNF;
				//根据指定的棋子，求能使该棋子移动的骰子点数；是p[i]
				List<Byte> count = board.getDicesByPiece(pieces.get(i));
				int p = count.size();
				Map<Move,ChessBoard> moves = MoveGenerator.getLegalMovesByPiece(board,pieces.get(i));
				List<Move> keyList = new ArrayList<Move>(moves.keySet());
				for (int j = 0;j<keyList.size();j++){			
					ChessBoard	boardtemp = board.clone();	
					Move movered = keyList.get(j);
					boardtemp.processMove(movered);
					if(board.isWin(type)){
						value = INNF;
						return value;
					}
					double valuered = odem(1,dep+1,1,movered,boardtemp);	
					if(valuered > maxvalue)
						maxvalue = valuered;
				}
				double value1 = maxvalue * p;
				if(value1 < minvalue)
					minvalue = value1;
			}
			value = minvalue;
		}
					
		return value;
		
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
