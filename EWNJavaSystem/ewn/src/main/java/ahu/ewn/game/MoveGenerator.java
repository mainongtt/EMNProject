package ahu.ewn.game;

import ahu.ewn.board.ChessBoard;
import ahu.ewn.board.PieceType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * 走法生成器。根据需要产生所有合法下棋动作以及动作执行后的新棋盘
 *
 * @author 陆梦轩
 *
 */
public class MoveGenerator {

    /**
     * 生成当前棋盘下指定一方的所有合法动作以及动作执行后的棋盘
     *
     * @param board 棋盘
     * @param turn 棋子颜色
     * @return Map 所有合法动作以及动作执行后的棋盘
     */
    public static Map<Move, ChessBoard> getLegalMoves(ChessBoard board, PieceType turn){
        Map<Move,ChessBoard> moves=new HashMap<Move,ChessBoard>();

        List<Byte> allPieces=board.getPieces(turn);
        for(byte piece:allPieces){
            moves.putAll(getLegalMovesByPiece(board,piece));
        }

        return moves;
    }

	/**
	 * 生成当前棋盘下指定棋子的所有合法动作以及动作执行后的棋盘
	 *
	 * @param board
	 *            棋盘
	 * @param piece
	 *            棋子
	 * @return Map 返回当前棋盘下指定棋子的所有合法动作以及动作执行后的棋盘
	 */
    public static Map<Move,ChessBoard> getLegalMovesByPiece(ChessBoard board,byte piece){
        Map<Move,ChessBoard> moves=new HashMap<Move,ChessBoard>();

        Vector<MoveDirection> allDirections=new Vector<MoveDirection>();
        allDirections.add(MoveDirection.FORWARD);
        allDirections.add(MoveDirection.LEFT);
        allDirections.add(MoveDirection.RIGHT);

        while(allDirections.isEmpty()==false){
            MoveDirection dir=allDirections.remove(0);
            Move move=new Move(piece,dir);

            ChessBoard newBoard=board.clone();
            if(newBoard.processMove(move)!=-1) moves.put(move, newBoard);
        }

        return moves;
    }

    /**
     * 生成当前棋盘下指定一方指定骰子点数的所有合法动作以及动作执行后的棋盘
     *
     * @param board 棋盘
     * @param turn 棋子颜色
     * @param dice 骰子点数
     * @return Map 返回当前棋盘下指定一方指定骰子点数的所有合法动作以及动作执行后的棋盘
     */
    public static Map<Move,ChessBoard> getLegalMovesByDice(ChessBoard board,PieceType turn,byte dice){
        Map<Move,ChessBoard> moves=new HashMap<Move,ChessBoard>();
        List<Byte> pieces=board.getPiecesByDice(turn, dice);
        for(byte piece:pieces){
            moves.putAll(getLegalMovesByPiece(board, piece));
        }

        return moves;
    }
}
