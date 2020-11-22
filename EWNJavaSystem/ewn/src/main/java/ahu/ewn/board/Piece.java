package ahu.ewn.board;

/**
 * 棋子。每个棋子用独有的编码id表示，id的取值范围：｛0，11~16，21~26｝<br>
 * 0表示无棋子；<br>
 * 11~16表示蓝方的1~6号棋子；<br>
 * 21~26表示红方的1~6号棋子。
 *
 * @author 陆梦轩
 *
 */
public class Piece{

	/**
	 * 创建一个棋子,根据棋子类型和编号转换成编码id
	 *
	 * @param type 棋子类型
	 * @param number 棋子编号
	 * 
	 * @return 棋子ID
	 */
	public static byte create(PieceType type, byte number) {
		byte id = 0;
		if (type == PieceType.RED)
			id = (byte) (20 + number);
		else if (type == PieceType.BLUE)
			id = (byte) (10 + number);
		else id=0;
		return id;
	}

	/**
	 * 获取棋子类型
	 *
	 * @param piece 棋子ID
	 * @return PieceType 棋子类型
	 */
	public static final PieceType getPieceType(byte piece){
		if (piece / 10 == 2)
			return PieceType.RED;
		else if (piece / 10 == 1)
			return PieceType.BLUE;
		else
			return PieceType.NULL;
	}

	/**
	 * 获取棋子编号
	 *
	 * @param piece 棋子id
	 * @return 编号
	 */
	public static final byte getNumber(byte piece) {
		return (byte) (piece % 10);
	}

	/**
	 * 获取ID号
	 *
	 * @param type 棋子类型
	 * @param number 棋子编号
	 * @return id
	 */
	public static final byte getID(PieceType type, byte number) {
		byte id=0;
		if (type == PieceType.RED)
			id = (byte) (20 + number);
		if (type == PieceType.BLUE)
			id = (byte) (10 + number);
		else
			id = 0;
		return id;
	}

	/**
	 * 将棋子转换成文本，使棋子可直接打印至控制台<br>
	 * <br>
	 * 例：<br>
	 * System.out.println(12) 输出结果：  B2<br>
	 * System.out.println(22) 输出结果：  R2<br>
	 * 
	 * @param piece 棋子
	 * @return String
	 */
	public static String toString(byte piece){
		if(Piece.getPieceType(piece) == PieceType.BLUE){
			return new String("B"+String.valueOf(Piece.getNumber(piece))+"\t");
		}
		else if(Piece.getPieceType(piece) == PieceType.RED){
			return new String("R"+String.valueOf(Piece.getNumber(piece))+"\t");
		}
		else return new String("-" + "\t");
	}
}