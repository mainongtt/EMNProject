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
import ahu.ewn.game.Player;
import ahu.ewn.strategy.evaluation.MySuperEvaluate;
import ahu.ewn.strategy.initial.StaticInitial;

/**
 * 随机产生下棋动作
 *
 * @author 陆梦轩
 *
 */
public class RandomMove extends MoveStrategy{

    /**
     * 构造函数
     */
    public RandomMove(){
        super();
        setLabel("RandomMove");
    }

    @Override
    public Move getMove(GameState gameState, byte dice) {
    	/**
		 * 采用蒙特卡洛搜索算法实现的代码。对局次数设为2000次
		 */
		
		//获取当前的棋盘
		ChessBoard board = gameState.getCurrentBoard();
		//获取当前的玩家颜色
		PieceType player = gameState.getCurrentPlayer().getTurn();
//		Player = player;
		Map<Move, ChessBoard> legalMoves = MoveGenerator.getLegalMovesByDice(board, player, dice);
		List<Move> keyList = new ArrayList<Move>(legalMoves.keySet());
		Move bestMove = null;
		//定义最好的走法得到的评估函数的评分
		int max_winNum = -1;
		
		for (int i = 0;i<keyList.size();i++){
			ChessBoard boardStep = legalMoves.get(keyList.get(i));
			int winNum = MonCa(boardStep, player);
			if(winNum > max_winNum){
				max_winNum = winNum;
				bestMove = keyList.get(i);
			}
		}
		this.value = max_winNum;
		this.maxDepth = 15;
		return bestMove;
			
    }

    private int MonCa(ChessBoard board, PieceType player) {
    	  // 对弈轮数
			int gameNum = 10000;
			// 蓝方获胜轮数
			int pieceWinNum = 0;
			
			// 获取当前状态相关信息
//    			Player player = game.getCurrentPlayer();
//    			ChessBoard board = game.getCurrentBoard();
//    			Map<PieceType, Player> map = game.getPlayers();
			
			// 先手方，即红方先下棋
			PieceType firstPlayer = Reverse(player);
			
			// 创建初始布局对象，并给初始布局设置成我们当前布局
//    			StaticInitial statin = new StaticInitial();
//    			statin.setBoard(player, board);
//    			statin.setBoard(Reverse(player), board);
//    			ChessBoard blueBoard = statin.getBoard(player);
//    			ChessBoard redBoard = statin.getBoard(Reverse(player));
			
			// 设置红蓝双方棋手，我方棋手
			Player play1 = new Player(player, new StaticInitial(),new StaticEvaluationMove(new MySuperEvaluate()));
//    			Player play1 = new Player(player, new StaticInitial(),new RandomMove());
//    			Player bluePlayer = new Player(PieceType.BLUE, new StaticInitial(), new StaticEvaluationMove(new RandomEvaluate()));
			// 指定红方的布局策略和下棋策略
			Player play2 = new Player(Reverse(player), new StaticInitial(), new RandomMove1());
			
			// 初始化红蓝双方
			Player bluePlayer = null;
			Player redPlayer  = null;
			
			// 判断红蓝双方
			if (player == PieceType.BLUE) {
				bluePlayer = play1;
				redPlayer  = play2;
			}else{
				bluePlayer = play2;
				redPlayer  = play1;
			}
			
			// 定义一局游戏，并设置玩家为上面定义的玩家
			GameState game1 = new GameState();
			game1.setPlayer(bluePlayer);
			game1.setPlayer(redPlayer);

			// 对弈轮数的迭代
			for(int cnt = 1; cnt <= gameNum; cnt++) {
				// 存储之前的棋盘
				ChessBoard board1 = board.clone();
				// 重置游戏，生成初始布局
				game1.reset(firstPlayer, board1);
				// 红蓝双方轮流行棋，直至游戏结束
				while(game1.isEnd()==false) {
					// 随机生成一个骰子点数
					byte dice = game1.getDice();
					// 行棋方的走子策略产生一个合法走法
					Move move = game1.getCurrentPlayer().getMoveStrategy().getMove(game1, dice);
					// 执行合法走法，即下一步棋
					game1.step(dice, move);
				}
				// 统计获胜方
				if (player == PieceType.BLUE) {
					if(game1.getWinner()==PieceType.BLUE) {
						pieceWinNum += 1;
					}
				}else {
					if(game1.getWinner()==PieceType.RED) {
						pieceWinNum += 1;
					}
				}
			}
			return pieceWinNum;
	}

	private PieceType Reverse(PieceType player) {
		return (player == PieceType.BLUE ? PieceType.RED : PieceType.BLUE);
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
