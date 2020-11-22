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
public class RandomMove1 extends MoveStrategy{

    /**
     * 构造函数
     */
    public RandomMove1(){
        super();
        setLabel("RandomMove");
    }

    @Override
    public Move getMove(GameState gameState, byte dice) {
    	
        this.value = 0.5;
        ChessBoard board = gameState.getCurrentBoard();
        PieceType turn = gameState.getCurrentPlayer().getTurn();
        Map<Move, ChessBoard> legalMoves = MoveGenerator.getLegalMovesByDice(board, turn, dice);
        List<Move> keyList = new ArrayList<Move>(legalMoves.keySet());
        Move randomKey = keyList.get(new Random().nextInt(keyList.size()));
        return randomKey;
			
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
