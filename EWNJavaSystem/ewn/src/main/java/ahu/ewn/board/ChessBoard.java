package ahu.ewn.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ahu.ewn.game.Move;
import ahu.ewn.game.MoveDirection;

/**
 * 棋盘类。以byte型5x5数组表示棋盘，存储棋子ID号（详见ahu.ewn.board.Piece类）。
 *
 * @author 陆梦轩
 */
public class ChessBoard {

    /**
     * 棋盘大小（行数和列数）
     */
    public static final short SIZE = 5;

    /**
     * 二维byte类型数组表示的棋盘,存储棋子ID
     */
    private byte[][] board;

    /**
     * 蓝方在场的棋子数量(定义此属性只为加快isWin()执行速度)
     */
    private byte pieceNum_blue = 0;

    /**
     * 红方在场的棋子数量(定义此属性只为加快isWin()执行速度)
     */
    private byte pieceNum_red = 0;

    /**
     * 构造空的棋盘对象
     */
    public ChessBoard() {
        board = new byte[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            Arrays.fill(board[i], (byte) 0);
        }

        pieceNum_blue = 0;
        pieceNum_red = 0;
    }


    /**
     * 将指定棋子放置于指定棋位上
     *
     * @param id  棋子ID
     * @param row 行号
     * @param col 列号
     */
    public void setPieceLocation(byte id, int row, int col) {
        this.board[row][col] = id;
        if (Piece.getPieceType(id) == PieceType.BLUE) pieceNum_blue++;
        else if (Piece.getPieceType(id) == PieceType.RED) pieceNum_red++;
    }

    /**
     * 获取指定棋位上的棋子
     *
     * @param row 行号
     * @param col 列号
     * @return Piece 棋子
     */
    public byte getPieceByPoint(int row, int col) {
        return this.board[row][col];
    }

    /**
     * 获取指定棋子的棋位
     *
     * @param piece 棋子
     * @return int[2]数组  res[0]为行号，res[1]为列号
     */
    public int[] getPointByPiece(byte piece) {
        int[] rowcol = {0, 0};
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == piece) {
                    rowcol[0] = i;
                    rowcol[1] = j;
                    return rowcol;
                }
            }
        }
        return rowcol;
    }

    /**
     * 获取棋盘数组
     *
     * @return byte[5][5] board
     */
    public byte[][] getBoard() {
        return this.board;
    }

    /**
     * 获取红方在场棋子个数
     *
     * @return int 红方在场棋子数
     */
    public int getPieceCount_RED() {
        return this.pieceNum_red;
    }

    /**
     * 获取蓝方在场棋子个数
     *
     * @return int 蓝方在场棋子数
     */
    public int getPieceCount_BLUE() {
        return this.pieceNum_blue;
    }

    /**
     * 获取当前棋盘的棋子数
     *
     * @param type 棋子类型
     * @return int 
     * <br>type=BLUE时返回蓝方棋子数;
     * <br>type=RED 时返回红方棋子数;
     * <br>type=NULL时返回空棋位的个数
     */
    public int getPieceCount(PieceType type) {
        if (type == PieceType.BLUE) return getPieceCount_BLUE();
        else if (type == PieceType.RED) return getPieceCount_RED();
        else return 25 - getPieceCount_BLUE() - getPieceCount_RED();
    }

    /**
     * 根据指定骰子点数获取符合游戏规则的可移动的棋子<br><br>
     * 例：<br>
     * 假设当前棋盘数组如下：<br>
     * [21 00 00 00 00]<br>
     * [00 12 00 00 00]<br>
     * [00 00 00 14 00]<br>
     * [00 00 11 00 00]<br>
     * [00 00 00 00 00]<br>
     * 则<br>
     * getPiecesByDice(PieceType.BLUE, (byte)1) = [11]<br>
     * getPiecesByDice(PieceType.BLUE, (byte)3) = [12, 14]<br>
     * getPiecesByDice(PieceType.BLUE, (byte)5) = [14]<br>
     * 
     * @param type 棋子类型
     * @param dice 骰子
     * @return List 棋子列表
     */
    public List<Byte> getPiecesByDice(PieceType type, byte dice) {
        List<Byte> vector = new ArrayList<Byte>(2);
        byte[] allPiece = new byte[7];
        Arrays.fill(allPiece, (byte) 0);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (Piece.getPieceType(board[i][j]) == type) allPiece[Piece.getNumber(board[i][j])] = 1;
            }
        }

        if (allPiece[dice] == 1) {
            vector.add(Piece.create(type, dice));
        } else {
            for (byte i = (byte) (dice + 1); i < 7; i++) {
                if (allPiece[i] == 1) {
                    vector.add(Piece.create(type, i));
                    break;
                }
            }
            for (byte i = (byte) (dice - 1); i > 0; i--) {
                if (allPiece[i] == 1) {
                    vector.add(Piece.create(type, i));
                    break;
                }
            }
        }

        return vector;
    }

    /**
     * 获取指定一方的所有在场棋子
     *
     * @param type 棋子类型
     * @return List 玩家type在场的所有棋子
     */
    public List<Byte> getPieces(PieceType type) {
        List<Byte> vector = new ArrayList<Byte>(6);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (Piece.getPieceType(board[i][j]) == type) vector.add(board[i][j]);
            }
        }

        return vector;
    }

    /**
     * 根据指定的棋子，求能使该棋子移动的骰子点数；若该棋子不在棋盘上，返回空集。<br>
     * <br>
     * 例：<br>
     * 假设当前棋盘数组如下：<br>
     * [21 00 00 00 00]<br>
     * [00 12 00 00 00]<br>
     * [00 00 00 14 00]<br>
     * [00 00 11 00 00]<br>
     * [00 00 00 00 00]<br>
     * 则<br>
     * getDicesByPiece(11) = [1]<br>
     * getDicesByPiece(12) = [2 3]<br>
     * getDicesByPiece(13) = []<br>
     * getDicesByPiece(14) = [3 4 5 6]<br>
     *
     * @param piece 棋子ID
     * @return 返回能使该棋子移动的骰子点数集合。若该棋子不在棋盘上，返回空集。
     */
    public List<Byte> getDicesByPiece(byte piece) {
        List<Byte> dices = new ArrayList<Byte>(6);
        byte pieceNumber = Piece.getNumber(piece);

        byte[] allPiece = new byte[7];
        Arrays.fill(allPiece, (byte) 0);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (Piece.getPieceType(board[i][j]) == Piece.getPieceType(piece))
                    allPiece[Piece.getNumber(board[i][j])] = 1;
            }
        }

        //若该棋子不在棋盘上，返回空集
        if (allPiece[pieceNumber] == 0) return dices;

        dices.add(pieceNumber);

        for (int i = pieceNumber + 1; i < 7 && allPiece[i] == 0; i++) {
            dices.add((byte) i);
        }

        for (int i = pieceNumber - 1; i > 0 && allPiece[i] == 0; i--) {
            dices.add((byte) i);
        }

        return dices;
    }

    /**
     * 处理走子动作，更新棋盘，返回处理结果
     *
     * @param move 动作
     * @return 若有棋子被吃，返回被吃的棋子；若无棋子被吃，返回0；若棋子位置越界，返回-1
     */
    public byte processMove(Move move) {
        byte piece = move.getPiece();
        MoveDirection dir = move.getDirection();

        int[] p_old = getPointByPiece(piece);
        int[] p_new = getPointByPiece(piece);
        if (Piece.getPieceType(piece) == PieceType.BLUE) {
            if (dir == MoveDirection.FORWARD) {
                p_new[0]--;
                p_new[1]--;
            } else if (dir == MoveDirection.LEFT) {
                p_new[1]--;
            } else {
                p_new[0]--;
            }
        } else {
            if (dir == MoveDirection.FORWARD) {
                p_new[0]++;
                p_new[1]++;
            } else if (dir == MoveDirection.LEFT) {
                p_new[1]++;
            } else {
                p_new[0]++;
            }
        }

        if (p_new[0] >= SIZE || p_new[0] < 0 || p_new[1] >= SIZE || p_new[1] < 0) {
            return -1;
        }

        board[p_old[0]][p_old[1]] = 0;
        byte id = board[p_new[0]][p_new[1]];
        board[p_new[0]][p_new[1]] = piece;

        if (Piece.getPieceType(id) == PieceType.BLUE) pieceNum_blue--;
        else if (Piece.getPieceType(id) == PieceType.RED) pieceNum_red--;

        return id;
    }

    /**
     * 判断某一方是否获胜
     *
     * @param turn 一方
     * @return boolean turn方是否获胜
     */
    public final boolean isWin(PieceType turn) {
        if(turn == PieceType.BLUE){
            if (Piece.getPieceType(getPieceByPoint(0, 0)) == PieceType.BLUE) return true;
            if (pieceNum_red == 0) return true;
        }
        else{
            if (Piece.getPieceType(getPieceByPoint(4, 4)) == PieceType.RED) return true;
            if (pieceNum_blue == 0) return true;
        }
        return false;
    }

    /**
     * 判断游戏是否结束
     *
     * @return boolean 游戏结束返回true；没有结束返回false
     */
    public final boolean isEnd(){
        return isWin(PieceType.BLUE) || isWin(PieceType.RED);
    }

    /**
     * 获取获胜方
     *
     * @return PieceType 获胜方。如果游戏没有结束，返回PieceType.NULL
     */
    public final PieceType getWinner(){
        if(isWin(PieceType.RED)) return PieceType.RED;
        if(isWin(PieceType.BLUE)) return PieceType.BLUE;
        return PieceType.NULL;
    }

    /**
     * 移除指定坐标的棋子
     *
     * @param row 行号
     * @param col 列号
     */
    public void removePieceByPoint(int row, int col) {
        byte id = board[row][col];
        board[row][col] = 0;

        if (Piece.getPieceType(id) == PieceType.BLUE) pieceNum_blue--;
        else if (Piece.getPieceType(id) == PieceType.RED) pieceNum_red--;
    }

    /**
     * 打印至控制台
     *
     * @param tabNum 每行之前的Tab数量
     */
    public void printBoard(int tabNum) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                for (int k = 0; k < tabNum; k++) System.out.print("	");
                System.out.print(Piece.toString(this.board[i][j]) + "	");
            }
            System.out.println();
        }
    }

    /* （非 Javadoc）
     * @see java.lang.Object#toString()
     * 
     * 在调试和System.out.println(board)时使用，可以直接打印棋盘对象
     */
    @Override
    public String toString() {
    	
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                string.append(Piece.toString(board[i][j]));
            }
            if (i != 4) string.append("\n");
        }
        return string.toString();
    }

    /**
     * 清空棋盘
     */
    public void clear() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = 0;
            }
        }
        pieceNum_red = 0;
        pieceNum_blue = 0;
    }

    /* （非 Javadoc）
     * @see java.lang.Object#clone()
     * 
     * 克隆一个新的棋盘对象
     */
    @Override
    public ChessBoard clone() {
        ChessBoard newBoard = new ChessBoard();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                newBoard.board[i][j] = this.board[i][j];
            }
        }
        newBoard.pieceNum_blue = this.pieceNum_blue;
        newBoard.pieceNum_red = this.pieceNum_red;
        return newBoard;
    }

    /**
     * 判断本棋盘是否与指定棋盘相同
     *
     * @param board 被比较的棋盘
     * @return true  相同<br>
     * false 不同
     */
    public boolean compareTo(ChessBoard board) {
        // TODO 自动生成的方法存根
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (this.board[i][j] != board.board[i][j]) return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object object) {
        ChessBoard board = (ChessBoard) object;
        return this.compareTo(board);
    }
}
