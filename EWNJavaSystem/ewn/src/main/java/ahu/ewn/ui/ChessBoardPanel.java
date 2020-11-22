 package ahu.ewn.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ahu.ewn.board.ChessBoard;
import ahu.ewn.board.Piece;
import ahu.ewn.board.PieceType;
import ahu.ewn.game.Move;
import ahu.ewn.game.MoveDirection;

import javax.swing.JLayeredPane;

/**
 * 棋盘面板，用于显示和控制棋盘界面
 * 
 * @author 陆梦轩
 *
 */
public class ChessBoardPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9018364710864079724L;
	/**
	 * 棋盘背景
	 */
	private JLabel boardBG;
	/**
	 * 位于棋盘背景之上的容器，用于摆放棋子
	 */
	private JLayeredPane layeredPane;
	/**
	 * 用于存放所有棋子对应的JLabel
	 */
	private ConcurrentHashMap<Byte, PieceLabel> allPieces;
	
	/**
	 * 人为拖动棋子所产生的走法，一般为null，当设置成人为下棋时才有数据
	 */
	private Move move;

	/**
	 * Create the panel.
	 */
	public ChessBoardPanel() {
		this.setBounds(0, 0, 400, 400);
		
		layeredPane = new JLayeredPane();
		layeredPane.setBounds(0, 0, 400, 400);
		add(layeredPane);
		
		//设置棋盘的背景图片
		boardBG=new JLabel(new ImageIcon(this.getClass().getClassLoader().getResource("res/chessBoard.png")));
		boardBG.setBounds(0, 0, 400, 400);
		layeredPane.add(boardBG);
		layeredPane.setLayer(boardBG, 1);
		
		//设置棋子的jlabel信息和对应的图片路径
		allPieces = new ConcurrentHashMap<Byte, PieceLabel>();
		for(int i=1;i<=2;i++){
			for(int j=1;j<=6;j++){
				PieceLabel piece=new PieceLabel((byte)(i*10+j), this);
				piece.setBounds(col2x(-1),row2y(-1),80,80);
				piece.setVisible(false);
				allPieces.put((byte)(i*10+j), piece);
				layeredPane.add(piece);
				layeredPane.setLayer(piece, 2);
			}
		}
	}
	
	/**
	 * 根据棋盘数据更新界面
	 * 
	 * @param board 棋盘数据
	 */
	public void updateBoardPanel(ChessBoard board) {
		setVisible(false);
		
		for(PieceLabel piece: allPieces.values()) {
			piece.setVisible(false);
		}
		
		for(int row=0;row<5; row++) {
			for(int col=0; col<5; col++) {
				byte id = board.getPieceByPoint(row, col);
				if(id==0) continue;
				allPieces.get(id).setVisible(true);
				allPieces.get(id).setBounds(col2x(col),row2y(row),80,80);
			}
		}
		
		setVisible(true);
	}
	
	/**
	 * 根据棋盘界面生成棋盘数据
	 * 
	 * @return ChessBoard 与界面相对应的棋盘
	 */
	public ChessBoard getChessBoard() {
		int[][] minPositions = new int[5][5];
		for(int[] arr: minPositions) Arrays.fill(arr, 100);
		
		for(PieceLabel piece: allPieces.values()) {
			int position = layeredPane.getPosition(piece);
			int row = y2row(piece.getY());
			int col = x2col(piece.getX());
			if(position < minPositions[row][col]) {
				minPositions[row][col] = position;
			}
		}
		
		ChessBoard board = new ChessBoard();
		
		for(PieceLabel piece: allPieces.values()) {
			int position = layeredPane.getPosition(piece);
			int row = y2row(piece.getY());
			int col = x2col(piece.getX());
			if(position == minPositions[row][col]) {
				board.setPieceLocation(piece.id, row, col);
			}
		}
		
		return board;
	}
	
	/**
	 * 将棋子移至顶层，覆盖下层的棋子
	 * 
	 * @param piece 棋子
	 */
	public void moveUp(byte piece) {
		this.layeredPane.moveToFront(allPieces.get(piece));
	}
	
	/**
	 * 将棋子移至底层
	 * 
	 * @param piece 棋子
	 */
	public void moveDown(byte piece) {
		this.layeredPane.moveToBack(allPieces.get(piece));
	}
	
	/**
	 * 打开人类布局开关，打开后，可以通过鼠标拖动棋子来完成布局
	 * 
	 * @param turn 打开turn方的布局开关
	 */
	public void openHumanInitMode(PieceType turn) {
		for(PieceLabel piece: allPieces.values()) {
			if(Piece.getPieceType(piece.id) == turn) piece.setInitable(true);
		}
	}
	
	/**
	 * 关闭人类布局开关，关闭后，无法鼠标无法拖动棋子
	 * 
	 * @param turn 关闭turn方的布局开关
	 */
	public void closeHumanInitMode(PieceType turn) {
		for(PieceLabel piece: allPieces.values()) {
			if(Piece.getPieceType(piece.id) == turn) piece.setInitable(false);
		}
	}
	
	/**
	 * 打开人类走子开关，打开后，人类可以通过拖动合法的棋子来进行下棋，并且只能将棋子拖动至合法的走动位置
	 * 
	 * @param turn 打开turn方的走子开关
	 * @param dice 指定当前骰子点数为dice，系统会根据规则设置哪些棋子可以拖动
	 */
	public void openHumanMoveMode(PieceType turn, byte dice) {
		closeHumanMoveMode(turn);
//		List<Byte> pieces = this.getChessBoard().getPiecesByDice(turn, dice);
//		for(Byte piece: pieces) {
//			this.allPieces.get(piece).setMoveable(true);
//		}
		ChessBoard board = this.getChessBoard();
//		List<Byte> pieces = board.getPiecesByDice(turn, dice);
		List<Byte> pieces = board.getPieces(turn);
		for(Byte piece: pieces) {
			this.allPieces.get(piece).setMoveable(true);
		}
	}
	
	/**
	 * 关闭人类走子开关，关闭后，人类无法拖动棋子
	 * 
	 * @param turn 关闭turn方的走子开关
	 */
	public void closeHumanMoveMode(PieceType turn) {
		for(PieceLabel piece: allPieces.values()) {
			if(Piece.getPieceType(piece.id) == turn) piece.setMoveable(false);
		}
		this.move=null;
	}
	
	/**
	 * 列号转变成x轴坐标
	 * 
	 * @param col 列号
	 * @return x轴坐标
	 */
	private int col2x(int col){
		return 80*col;
	}
	/**
	 * 行号转变成y轴坐标
	 * 
	 * @param row 行号
	 * @return y轴坐标
	 */
	private int row2y(int row){
		return 80*row;
	}
	/**
	 * y轴坐标转变成行号
	 * 
	 * @param y y轴坐标
	 * @return 行号
	 */
	private int y2row(int y) {
		return y / 80;
	}
	/**
	 * x轴坐标转变成列号
	 * 
	 * @param x x轴坐标
	 * @return 列号
	 */
	private int x2col(int x) {
		return x / 80;
	}

	/**
	 * 获取人类产生的走法，尽在人类下棋开关打开后才有用，否则返回null
	 * 
	 * @return 人类产生的走法
	 */
	public Move getMove() {
		return move;
	}

	/**
	 * 设置走法，仅给PieceLabel使用
	 * 
	 * @param move 走法
	 */
	public void setMove(Move move) {
		this.move = move;
	}
	
}

/**
 * 棋子UI以及鼠标监听事件
 * 
 * @author 陆梦轩
 *
 */
class PieceLabel extends JLabel implements MouseMotionListener, MouseListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5600918267493658251L;

	/**
	 * 棋子的ID
	 */
	public byte id;
	
	/**
	 * 人类布局开关，打开后，可以通过鼠标拖动棋子来完成布局
	 */
	private boolean initable;
	/**
	 * 人类走子开关，打开后，人类可以通过拖动合法的棋子来进行下棋，并且只能将棋子拖动至合法的走动位置
	 */
	private boolean moveable;
	
	/**
	 * 棋子UI所在的棋盘UI
	 */
	private ChessBoardPanel boardPanel;
	
	/**
	 * 人类走子开关打开后，该属性用于记录棋子原来的位置
	 */
	private int[] fromPoint;
	/**
	 * 人类走子开关打开后，该属性用于记录拖动的棋位变化，产生相应走法
	 * 例如：
	 * 棋子21由(2,2)位置
	 * [00 00 00 00 00]
	 * [00 00 00 00 00]
	 * [00 00 21 00 00]
	 * [00 00 00 00 00]
	 * [00 00 00 00 00]
	 * 拖动到(2,3)位置
	 * [00 00 00 00 00]
	 * [00 00 00 00 00]
	 * [00 00 00 21 00]
	 * [00 00 00 00 00]
	 * [00 00 00 00 00]
	 * 则moveVector=[0, 1], 即move = R1 Left
	 */
	private int[] movedVector;
	
	/**
	 * 创建棋子UI
	 * 
	 * @param id 棋子的ID
	 * @param parent 该棋子所在的棋盘
	 */
	public PieceLabel(byte id, ChessBoardPanel parent) {
		super();
		this.id = id;
		this.initable = false;
		this.moveable = false;
		this.boardPanel = parent;
		this.fromPoint = new int[2];
		this.movedVector = new int[2];
		setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("res/piece"+id+".png")));
		setName(String.valueOf(id));
		addMouseMotionListener(this);
		addMouseListener(this);
	}

	/* （非 Javadoc）
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 * 
	 * 鼠标拖动事件
	 */
	public void mouseDragged(MouseEvent e) {
		// TODO 自动生成的方法存根
		if(this.initable == true) {
			int newX = this.getX() + e.getX();
			int newY = this.getY() + e.getY();
			int row = y2row(newY);
			int col = x2col(newX);
			if(0<=row && row<=4 && 0<=col && col<=4) {
				this.setLocation(col2x(col), row2y(row));
			}
		}
		else if(this.moveable == true) {
			int newX = this.getX() + e.getX();
			int newY = this.getY() + e.getY();
			int row = y2row(newY);
			int col = x2col(newX);
			int movedRow = row - fromPoint[0];
			int movedCol = col - fromPoint[1];
			if(Piece.getPieceType(id) == PieceType.BLUE) {
				if((movedRow == 0 && movedCol == 0) || (movedRow == 0 && movedCol == -1) || (movedRow == -1 && movedCol == -1) || (movedRow == -1 && movedCol == 0)) {
					if(0<=row && row<=4 && 0<=col && col<=4) {
						movedVector[0] = movedRow;
						movedVector[1] = movedCol;
						this.setLocation(col2x(col), row2y(row));
					}
				}
			}
			else {
				if((movedRow == 0 && movedCol == 0) || (movedRow == 0 && movedCol == 1) || (movedRow == 1 && movedCol == 1) || (movedRow == 1 && movedCol == 0)) {
					if(0<=row && row<=4 && 0<=col && col<=4) {
						movedVector[0] = movedRow;
						movedVector[1] = movedCol;
						this.setLocation(col2x(col), row2y(row));
					}
				}
			}//if(Piece.getPieceType(id) == PieceType.BLUE)
		}//else if(this.moveable == true)
	}

	public void mouseMoved(MouseEvent e) {
		// TODO 自动生成的方法存根
		
	}

	public void mouseClicked(MouseEvent e) {
		// TODO 自动生成的方法存根
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO 自动生成的方法存根
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO 自动生成的方法存根
		
	}

	/* （非 Javadoc）
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 * 鼠标按下事件
	 */
	public void mousePressed(MouseEvent e) {
		// TODO 自动生成的方法存根
		boardPanel.moveUp(this.id);
	}

	/* （非 Javadoc）
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 * 鼠标弹起事件
	 */
	public void mouseReleased(MouseEvent e) {
		// TODO 自动生成的方法存根
		if(moveable == true) {
			if(movedVector[0] == 0 && movedVector[1] == 0) {
				boardPanel.setMove(null);
			}
			else {
				MoveDirection direction = null;
				if(Piece.getPieceType(id) == PieceType.BLUE) {
					if(movedVector[0] == 0 && movedVector[1] == -1) direction = MoveDirection.LEFT;
					else if(movedVector[0] == -1 && movedVector[1] == -1) direction = MoveDirection.FORWARD;
					else if(movedVector[0] == -1 && movedVector[1] == 0) direction = MoveDirection.RIGHT;
				}
				else {
					if(movedVector[0] == 0 && movedVector[1] == 1) direction = MoveDirection.LEFT;
					else if(movedVector[0] == 1 && movedVector[1] == 1) direction = MoveDirection.FORWARD;
					else if(movedVector[0] == 1 && movedVector[1] == 0) direction = MoveDirection.RIGHT;
				}
				boardPanel.setMove(new Move(id, direction));
			}
		}
	}
	
	/**
	 * 列号转变成x轴坐标
	 * 
	 * @param col 列号
	 * @return x轴坐标
	 */
	private int col2x(int col){
		return 80*col;
	}
	/**
	 * 行号转变成y轴坐标
	 * 
	 * @param row 行号
	 * @return y轴坐标
	 */
	private int row2y(int row){
		return 80*row;
	}
	/**
	 * y轴坐标转变成行号
	 * 
	 * @param y y轴坐标
	 * @return 行号
	 */
	private int y2row(int y) {
		return y / 80;
	}
	/**
	 * x轴坐标转变成列号
	 * 
	 * @param x x轴坐标
	 * @return 列号
	 */
	private int x2col(int x) {
		return x / 80;
	}

	/**
	 * 获取人类走子开关是否打开
	 * 
	 * @return true 打开<br>
	 *         false 关闭
	 */
	public boolean isMoveable() {
		return moveable;
	}

	/**
	 * 设置人类走子开关
	 * 
	 * @param moveable 是否允许人类走子
	 */
	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
		if(moveable == true) {
			this.fromPoint[0] = y2row(this.getY());
			this.fromPoint[1] = x2col(this.getX());
		}
	}

	/**
	 * 获取人类布局开关是否打开
	 * 
	 * @return true 打开<br>
	 *         false 关闭
	 */
	public boolean isInitable() {
		return initable;
	}

	/**
	 * 设置人类布局开关
	 * 
	 * @param initable 是否允许人类布局
	 */
	public void setInitable(boolean initable) {
		this.initable = initable;
	}
}