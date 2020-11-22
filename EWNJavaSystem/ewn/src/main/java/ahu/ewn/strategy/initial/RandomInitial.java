package ahu.ewn.strategy.initial;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ahu.ewn.board.ChessBoard;
import ahu.ewn.board.Piece;
import ahu.ewn.board.PieceType;

/**
 * 随机布局策略，六个棋子随机放置
 *
 * @author 陆梦轩
 */
public class RandomInitial extends InitialStrategy {

    /**
     * 构造函数
     */
    public RandomInitial() {
        super();
        setLabel("RandomInitial");
    }

    @Override
    public ChessBoard getBoard(PieceType myTurn) {
        // TODO 自动生成的方法存根
        ChessBoard board = new ChessBoard();

        List<Byte> allPiece = new ArrayList<Byte>();
        if (myTurn == PieceType.RED) {
            allPiece.add(Piece.create(PieceType.RED, (byte) 1));
            allPiece.add(Piece.create(PieceType.RED, (byte) 2));
            allPiece.add(Piece.create(PieceType.RED, (byte) 3));
            allPiece.add(Piece.create(PieceType.RED, (byte) 4));
            allPiece.add(Piece.create(PieceType.RED, (byte) 5));
            allPiece.add(Piece.create(PieceType.RED, (byte) 6));

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3 - i; j++) {
                    int index = new Random().nextInt(allPiece.size());
                    board.setPieceLocation(allPiece.get(index), i, j);
                    allPiece.remove(index);
                }
            }

        } else {

            allPiece.add(Piece.create(PieceType.BLUE, (byte) 1));
            allPiece.add(Piece.create(PieceType.BLUE, (byte) 2));
            allPiece.add(Piece.create(PieceType.BLUE, (byte) 3));
            allPiece.add(Piece.create(PieceType.BLUE, (byte) 4));
            allPiece.add(Piece.create(PieceType.BLUE, (byte) 5));
            allPiece.add(Piece.create(PieceType.BLUE, (byte) 6));

            int index;
            index = new Random().nextInt(allPiece.size());
            board.setPieceLocation(allPiece.get(index), 4, 4);
            allPiece.remove(index);

            index = new Random().nextInt(allPiece.size());
            board.setPieceLocation(allPiece.get(index), 4, 3);
            allPiece.remove(index);

            index = new Random().nextInt(allPiece.size());
            board.setPieceLocation(allPiece.get(index), 4, 2);
            allPiece.remove(index);

            index = new Random().nextInt(allPiece.size());
            board.setPieceLocation(allPiece.get(index), 3, 4);
            allPiece.remove(index);

            index = new Random().nextInt(allPiece.size());
            board.setPieceLocation(allPiece.get(index), 3, 3);
            allPiece.remove(index);

            index = new Random().nextInt(allPiece.size());
            board.setPieceLocation(allPiece.get(index), 2, 4);
            allPiece.remove(index);
        }
        return board;
    }

}
