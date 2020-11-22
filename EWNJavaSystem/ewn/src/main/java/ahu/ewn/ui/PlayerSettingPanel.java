package ahu.ewn.ui;

import javax.swing.JPanel;
import ahu.ewn.board.ChessBoard;
import ahu.ewn.board.Piece;
import ahu.ewn.board.PieceType;
import ahu.ewn.game.Player;
import ahu.ewn.strategy.initial.InitialStrategy;
import ahu.ewn.strategy.initial.RandomInitial;
import ahu.ewn.strategy.initial.StaticInitial;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;

/**
 * Player设置面板，用于设置Player的各项信息
 * 
 * @author 陆梦轩
 *
 */
public class PlayerSettingPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4671248824274000889L;

	private Player player;
	
	private JTextField nameTextField;
	private JComboBox<String> initStrategyComboBox;
	
	
	private ArrayList<InitialStrategy> initStrategies;
	private JTextField staticInitialBoardTextField;
	private JButton staticInitialBoardConfirm;
	private JTabbedPane moveStrategyTabbedPane;

	/**
	 * Create the panel.
	 * 
	 * @param type 玩家颜色
	 */
	public PlayerSettingPanel(PieceType type) {
		initialize(type);
	}
	
	private void initialize(PieceType type) {
		initData(type);
		initUI();
	}
	
	private void initData(PieceType type) {
		this.player = new Player(type);
		
		initStrategies = new ArrayList<InitialStrategy>();
		InitialStrategy strategy = new RandomInitial();
		strategy.setLabel("随机布局");
		initStrategies.add(strategy);
		strategy = new StaticInitial();
		strategy.setLabel("固定布局");
		initStrategies.add(strategy);
	}
	
	private void initUI() {
		setLayout(null);
		
		JLabel nameLabel = new JLabel("名称");
		nameLabel.setBounds(10, 10, 54, 15);
		add(nameLabel);
		
		nameTextField = new JTextField();
		nameTextField.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				player.setLabel(nameTextField.getText());
			}
		});
		nameTextField.setBounds(51, 7, 120, 21);
		nameTextField.setText(player.getLabel());
		add(nameTextField);
		nameTextField.setColumns(10);
		
		JLabel initStrategyLabel = new JLabel("布局策略");
		initStrategyLabel.setBounds(10, 41, 54, 15);
		add(initStrategyLabel);
		
		initStrategyComboBox = new JComboBox<String>();
		initStrategyComboBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				InitialStrategy strategy = initStrategies.get(initStrategyComboBox.getSelectedIndex());
				player.setInitStrategy(strategy);
				if(initStrategyComboBox.getSelectedIndex() == 1) {
					StaticInitial s = (StaticInitial)strategy;
					staticInitialBoardTextField.setText(chessBoard2String(player.getTurn(), s.getBoard(player.getTurn())));
					staticInitialBoardTextField.setEditable(true);
					staticInitialBoardConfirm.setEnabled(true);
					staticInitialBoardTextField.setVisible(true);
					staticInitialBoardConfirm.setVisible(true);
				}
			}
		});
		initStrategyComboBox.setBounds(85, 38, 86, 21);
		for(InitialStrategy strategy: initStrategies) {
			initStrategyComboBox.addItem(strategy.toString());
		}
		initStrategyComboBox.setSelectedIndex(0);
		player.setInitStrategy(initStrategies.get(0));
		add(initStrategyComboBox);
		
		staticInitialBoardTextField = new JTextField();
		staticInitialBoardTextField.setBounds(85, 68, 86, 21);
		add(staticInitialBoardTextField);
		staticInitialBoardTextField.setEditable(false);
		staticInitialBoardTextField.setColumns(10);
		
		staticInitialBoardConfirm = new JButton("确认");
		staticInitialBoardConfirm.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					String str = staticInitialBoardTextField.getText();
					ChessBoard board = string2ChessBoard(player.getTurn(), str);
					StaticInitial initStrategy = (StaticInitial)player.getInitStrategy();
					initStrategy.setBoard(player.getTurn(), board);
					staticInitialBoardTextField.setEditable(false);
					staticInitialBoardConfirm.setEnabled(false);
				}
				catch(Exception ex){
					staticInitialBoardTextField.setText("错误");
				}
			}
		});
		staticInitialBoardConfirm.setBounds(10, 66, 65, 23);
		staticInitialBoardConfirm.setEnabled(false);
		staticInitialBoardConfirm.setVisible(false);
		add(staticInitialBoardConfirm);
		
		JLabel moveStrategyLabel = new JLabel("走子策略");
		moveStrategyLabel.setBounds(10, 99, 54, 15);
		add(moveStrategyLabel);
		
		moveStrategyTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		moveStrategyTabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				MoveStrategyPanel panel = (MoveStrategyPanel)moveStrategyTabbedPane.getSelectedComponent();
				player.setMoveStrategy(panel.getMoveStrategy());
			}
		});
		moveStrategyTabbedPane.setBounds(10, 114, 161, 236);
		moveStrategyTabbedPane.addTab("最强策略", new RandomMovePanel(this.player));
		moveStrategyTabbedPane.addTab("估值", new StaticEvaluationMovePanel(this.player));
		moveStrategyTabbedPane.addTab("保守策略", new MySuperMovePanel(this.player));
		
		moveStrategyTabbedPane.setSelectedIndex(0);
		MoveStrategyPanel panel = (MoveStrategyPanel)moveStrategyTabbedPane.getSelectedComponent();
		player.setMoveStrategy(panel.getMoveStrategy());
		add(moveStrategyTabbedPane);
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	private ChessBoard string2ChessBoard(PieceType turn, String string) {
		ChessBoard board = new ChessBoard();
		if(turn == PieceType.RED) {
			board.setPieceLocation(Piece.create(turn, Byte.valueOf(string.substring(0, 1))), 0, 0);
			board.setPieceLocation(Piece.create(turn, Byte.valueOf(string.substring(1, 2))), 0, 1);
			board.setPieceLocation(Piece.create(turn, Byte.valueOf(string.substring(2, 3))), 0, 2);
			board.setPieceLocation(Piece.create(turn, Byte.valueOf(string.substring(3, 4))), 1, 0);
			board.setPieceLocation(Piece.create(turn, Byte.valueOf(string.substring(4, 5))), 1, 1);
			board.setPieceLocation(Piece.create(turn, Byte.valueOf(string.substring(5, 6))), 2, 0);
		}
		else {
			board.setPieceLocation(Piece.create(turn, Byte.valueOf(string.substring(0, 1))), 2, 4);
			board.setPieceLocation(Piece.create(turn, Byte.valueOf(string.substring(1, 2))), 3, 3);
			board.setPieceLocation(Piece.create(turn, Byte.valueOf(string.substring(2, 3))), 3, 4);
			board.setPieceLocation(Piece.create(turn, Byte.valueOf(string.substring(3, 4))), 4, 2);
			board.setPieceLocation(Piece.create(turn, Byte.valueOf(string.substring(4, 5))), 4, 3);
			board.setPieceLocation(Piece.create(turn, Byte.valueOf(string.substring(5, 6))), 4, 4);
		}
		return board;
	}
	
	private String chessBoard2String(PieceType turn, ChessBoard board) {
		StringBuilder str = new StringBuilder();
		if(turn == PieceType.RED) {
			str.append(Piece.getNumber(board.getPieceByPoint(0, 0)));
			str.append(Piece.getNumber(board.getPieceByPoint(0, 1)));
			str.append(Piece.getNumber(board.getPieceByPoint(0, 2)));
			str.append(Piece.getNumber(board.getPieceByPoint(1, 0)));
			str.append(Piece.getNumber(board.getPieceByPoint(1, 1)));
			str.append(Piece.getNumber(board.getPieceByPoint(2, 0)));
		}
		else {
			str.append(Piece.getNumber(board.getPieceByPoint(2, 4)));
			str.append(Piece.getNumber(board.getPieceByPoint(3, 3)));
			str.append(Piece.getNumber(board.getPieceByPoint(3, 4)));
			str.append(Piece.getNumber(board.getPieceByPoint(4, 2)));
			str.append(Piece.getNumber(board.getPieceByPoint(4, 3)));
			str.append(Piece.getNumber(board.getPieceByPoint(4, 4)));
		}
		return str.toString();
	}
}
