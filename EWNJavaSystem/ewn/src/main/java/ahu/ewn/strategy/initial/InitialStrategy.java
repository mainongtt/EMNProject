package ahu.ewn.strategy.initial;


import ahu.ewn.board.ChessBoard;
import ahu.ewn.board.PieceType;

/**
 * 棋盘初始布局生成策略基类，用于生成棋盘的初始布局
 *
 * @author 陆梦轩
 *
 */
abstract public class InitialStrategy{

    /**
     * 标签
     */
    protected String label;

    /**
     * 获取标签
     *
     * @return 标签
     */
    public String getLabel(){
        return this.label;
    }

    /**
     * 设置标签
     *
     * @param label 标签
     */
    public void setLabel(String label){
        this.label=label;
    }

    /**
     * 创建指定颜色的新布局<br>
     * <br>
     * 例如：<br>
     * getBoard(PieceType.RED)可能会得到这个棋盘<br>
     * [21, 22, 23, 00, 00]<br>
     * [24, 25, 00, 00, 00]<br>
     * [26, 00, 00, 00, 00]<br>
     * [00, 00, 00, 00, 00]<br>
     * [00, 00, 00, 00, 00]<br>
     *
     * @param myTurn 我方棋子颜色
     * @return ChessBoard 棋盘
     */
    abstract public ChessBoard getBoard(PieceType myTurn);
    
    /**
     * 将双方的初始棋盘布局合并成一张棋盘
     * 
     * @param initBoard1 初始布局1
     * @param initBoard2 初始布局2
     * @return 融合后的棋盘
     */
    public static ChessBoard fuseBoards(ChessBoard initBoard1, ChessBoard initBoard2) {
    	ChessBoard board = initBoard1.clone();
    	for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                byte piece=initBoard2.getPieceByPoint(i, j);
                if(piece!=0) board.setPieceLocation(piece, i, j);
            }
        }
    	return board;
    }
    
    @Override
    public String toString(){
        return label;
    }
}