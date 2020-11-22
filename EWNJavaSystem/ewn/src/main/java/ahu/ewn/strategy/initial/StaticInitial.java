package ahu.ewn.strategy.initial;

import java.util.HashMap;
import java.util.Map;

import ahu.ewn.board.ChessBoard;
import ahu.ewn.board.Piece;
import ahu.ewn.board.PieceType;

/**
 * 固定棋盘布局。默认情况下该策略将生成以下棋盘,因为该布局是别人统计的最优的布局方式<br>
 * [26 22 24 00 00]<br>
 * [21 25 00 00 00]<br>
 * [23 00 00 00 13]<br>
 * [00 00 00 15 11]<br>
 * [00 00 14 12 16]<br>
 * 除此之外，也可以指定一种棋盘布局
 * 
 * @author 陆梦轩
 *
 */
public class StaticInitial extends InitialStrategy{

    /**
     * 指定的棋盘布局
     */
    private Map<PieceType, ChessBoard> boards;

    /**
     * 构造函数，生成默认布局
     */
    public StaticInitial() {
        super();
        setLabel("StaticInitial");

        boards=new HashMap<PieceType, ChessBoard>();

        ChessBoard redBoard=new ChessBoard();
        redBoard.setPieceLocation(Piece.create(PieceType.RED, (byte) 1), 1, 0);
        redBoard.setPieceLocation(Piece.create(PieceType.RED, (byte) 2), 0, 1);
        redBoard.setPieceLocation(Piece.create(PieceType.RED, (byte) 3), 2, 0);
        redBoard.setPieceLocation(Piece.create(PieceType.RED, (byte) 4), 0, 2);
        redBoard.setPieceLocation(Piece.create(PieceType.RED, (byte) 5), 1, 1);
        redBoard.setPieceLocation(Piece.create(PieceType.RED, (byte) 6), 0, 0);

        ChessBoard blueBoard=new ChessBoard();
        blueBoard.setPieceLocation(Piece.create(PieceType.BLUE, (byte) 1), 3, 4);
        blueBoard.setPieceLocation(Piece.create(PieceType.BLUE, (byte) 2), 4, 3);
        blueBoard.setPieceLocation(Piece.create(PieceType.BLUE, (byte) 3), 2, 4);
        blueBoard.setPieceLocation(Piece.create(PieceType.BLUE, (byte) 4), 4, 2);
        blueBoard.setPieceLocation(Piece.create(PieceType.BLUE, (byte) 5), 3, 3);
        blueBoard.setPieceLocation(Piece.create(PieceType.BLUE, (byte) 6), 4, 4);

        boards.put(PieceType.BLUE, blueBoard);
        boards.put(PieceType.RED, redBoard);
    }

    /**
     * 构造函数，指定双方的布局
     * 
     * @param blueBoard 蓝方棋盘布局
     * @param redBoard 红方棋盘布局
     */
    public StaticInitial(ChessBoard blueBoard, ChessBoard redBoard) {
        boards=new HashMap<PieceType, ChessBoard>();
        boards.put(PieceType.BLUE, blueBoard);
        boards.put(PieceType.RED, redBoard);
    }

    @Override
    public ChessBoard getBoard(PieceType myTurn) {
        // TODO 自动生成的方法存根
        return boards.get(myTurn);
    }

    /**
     * 设置某一方的布局
     * 
     * @param type 玩家
     * @param board 初始布局
     */
    public void setBoard(PieceType type, ChessBoard board) {
        boards.put(type, board);
    }
}
