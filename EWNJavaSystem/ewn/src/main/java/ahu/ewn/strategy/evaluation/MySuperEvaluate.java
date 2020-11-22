package ahu.ewn.strategy.evaluation;

import java.util.List;

import ahu.ewn.board.ChessBoard;
import ahu.ewn.board.PieceType;

/**
 * 自定义估值函数，需要在此类中完善估值的代码。界面中的“直接估值”策略里的“超级估值”就是指这个类
 * 
 * @author 陆梦轩
 *
 */
public class MySuperEvaluate extends EvaluationFunction{

	@Override
	public double getValue(ChessBoard board, PieceType type) {
		// TODO 自动生成的方法存根
		
		//-------------------------------------------------------------
		//在这里完成你的评估函数，计算并返回棋盘对玩家type的价值，值越大越对玩家type有利
		//例如：
		//PieceType winner = board.getWinner();
		//if(winner == type) return 1;  //我方获胜
		//else if(winner != PieceType.NULL) return -1;  //敌方获胜
		//else return 0; //没有人获胜
		//-------------------------------------------------------------
		
		//默认返回值为0，评估函数设计完成之后，可以将这一行删除
		//棋盘的价值
		double boardvred[][] = { { 0, 1, 1, 1, 1 }, { 1, 2, 2, 2, 2.5 }, { 1, 2, 4, 4, 5 }, { 1, 2, 4, 8, 10 }, { 1, 2.5, 5, 10, 16 } };
		double boardvblue[][] = { { 16, 10, 5, 2.5, 1 }, { 10, 8, 4, 2, 1 }, { 5, 4, 4, 2, 1 }, { 2.5, 2, 2, 2, 1 }, { 1, 1, 1, 1, 0 } };
//		int distancered[][] = { { 0, 1, 2, 3, 4 }, { 1, 1, 2, 3, 4 }, { 2, 2, 2, 3, 4 }, { 3, 3, 3, 3, 4 }, { 4, 4, 4, 4, 4 } };
//		int distanceblue[][] = { { 4, 4, 4, 4, 4 }, { 4, 3, 3, 3, 3 }, { 4, 3, 2, 2, 2 }, { 4, 3, 2, 1, 1 }, { 4, 3, 2, 1, 0 } };
		//概率数组
		int p[] = new int[6];
		int p2[] = new int[6];
		//定义每个进攻值，狙击值，威胁值
		double attackred = 0.0,threatred = 0.0;
		double attackblue = 0.0,threatblue = 0.0;
		//定义最终的进攻值，狙击值，威胁值
		double exp1=0.0,exp2=0.0,theat1 = 0.0,theat2 = 0.0;
		//定义棋盘价值
		double value[] = new double [6];
		double value2[] = new double [6];
		//定义棋子 三个方向的棋子的价值
		double a=0,b=0,c=0;
		//定义威胁值数组
		double maxvalue[] = new double[6];
		//若玩家为BLUE
		if(type == PieceType.BLUE){
			//获取我方的所有棋子
			List<Byte> pieces = board.getPieces(type);
			//遍历
			for (int i =0; i<pieces.size();i++){
				//根据指定的棋子，求能使该棋子移动的骰子点数；是p[i]
				List<Byte> count = board.getDicesByPiece(pieces.get(i));
				p[i] = count.size();
				//获得棋子的位置
				int[] rowcol = board.getPointByPiece(pieces.get(i));
				//求出该棋子在棋盘上的价值
				value[i] = boardvblue[rowcol[0]][rowcol[1]];
				//求威胁值
				if(rowcol[0] >0 && rowcol[1] > 0 && board.getBoard()[rowcol[0] -1][rowcol[1]] > 20){
					a = boardvred[rowcol[0] -1][rowcol[1]];
				}else if(rowcol[0] >0 && rowcol[1] > 0 && board.getBoard()[rowcol[0]][rowcol[1] -1] > 20 ){
					b = boardvred[rowcol[0] -1][rowcol[1] - 1];
				}else if(rowcol[0] >0 && rowcol[1] > 0 && board.getBoard()[rowcol[0] -1][rowcol[1] -1] > 20){
					c = boardvred[rowcol[0] -1][rowcol[1] -1];
				}
				double max = (a > b) ? a : b;
				max = (max > c) ? max : c;
				maxvalue[i] = max;
				
				
			}
			//我方的进攻值期望和威胁值期望
			for (int i =0; i<pieces.size();i++){
				attackblue = attackblue + p[i]*value[i];
				threatblue = threatblue + maxvalue[i]*p[i];		
			}
			
			//获取对方的进攻值
			List<Byte> pieces2 = board.getPieces(PieceType.RED);
			//遍历
			for (int i =0; i<pieces2.size();i++){
				//根据指定的棋子，求能使该棋子移动的骰子点数；是p2[i]
				List<Byte> count2 = board.getDicesByPiece(pieces2.get(i));
				p2[i] = count2.size();
				//获得棋子的位置
				int[] rowcol2 = board.getPointByPiece(pieces2.get(i));
				//求出该棋子在棋盘上的价值
				value2[i] = boardvred[rowcol2[0]][rowcol2[1]];
			}	
			//对方的进攻值期望
			for (int i =0; i<pieces2.size();i++){
				attackred = attackred + p2[i]*value2[i];
				
			}
		}
		else if(type == PieceType.RED){
			//获取我方的所有棋子
			List<Byte> pieces = board.getPieces(type);
			//遍历
			for (int i =0; i<pieces.size();i++){
				List<Byte> count = board.getDicesByPiece(pieces.get(i));
				p[i] = count.size();
				//获得棋子的位置
				int[] rowcol = board.getPointByPiece(pieces.get(i));
				//求出该棋子在棋盘上的价值
				value[i] = boardvred[rowcol[0]][rowcol[1]];
				//求威胁值
				if(rowcol[0] <4 && rowcol[1] <4 && board.getBoard()[rowcol[0] + 1][rowcol[1]] > 10 && board.getBoard()[rowcol[0] + 1][rowcol[1]] < 20){
					a = boardvblue[rowcol[0] +1][rowcol[1]];
				}else if(rowcol[0] <4 && rowcol[1] <4 && board.getBoard()[rowcol[0]][rowcol[1] + 1] > 10 && board.getBoard()[rowcol[0]][rowcol[1] + 1] < 20){
					b = boardvblue[rowcol[0]][rowcol[1] + 1];
				}else if(rowcol[0] <4 && rowcol[1] <4 && board.getBoard()[rowcol[0] + 1][rowcol[1] + 1] > 10 && board.getBoard()[rowcol[0] + 1][rowcol[1] + 1] < 20){
					c = boardvblue[rowcol[0] +1][rowcol[1] +1];
				}
				double max = (a > b) ? a : b;
				max = (max > c) ? max : c;
				maxvalue[i] = max;
			}
			//我方的进攻值期望和威胁值期望
			for (int i =0; i<pieces.size();i++){
				attackred  = attackred  + p[i]*value[i];
				threatred = threatred + maxvalue[i]*p[i];
			}
			
			//获取对方的进攻值
			List<Byte> pieces2 = board.getPieces(PieceType.BLUE);
			//遍历
			for (int i =0; i<pieces2.size();i++){
				//根据指定的棋子，求能使该棋子移动的骰子点数；是p[i]
				List<Byte> count = board.getDicesByPiece(pieces2.get(i));
				p2[i] = count.size();
				//获得棋子的位置
				int[] rowcol2 = board.getPointByPiece(pieces2.get(i));
				//求出该棋子在棋盘上的价值
				value2[i] = boardvblue[rowcol2[0]][rowcol2[1]];
			}	
			//对方的进攻值期望
			for (int i =0; i<pieces2.size();i++){
				attackblue = attackblue + p2[i]*value2[i];
				
			}
		}
		
		if(type == PieceType.RED){
			exp1 = attackred;
			exp2 = -attackblue;
			theat1 = -threatred;
			theat2 = threatblue;
		}else if(type == PieceType.BLUE){
			exp1 = attackblue;
			exp2 = -attackred;
			theat1 = -threatblue;
			theat2 = threatred;
		}
		return 10*exp1 + 5*exp2 + 1*theat1 + 0*theat2;
	}
	

}
