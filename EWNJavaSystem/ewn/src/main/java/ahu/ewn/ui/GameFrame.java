package ahu.ewn.ui;

import java.awt.EventQueue;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ahu.ewn.board.ChessBoard;
import ahu.ewn.board.PieceType;
import ahu.ewn.game.GameState;
import ahu.ewn.game.Move;
import ahu.ewn.game.Player;
import ahu.ewn.record.GameRecord;
import ahu.ewn.strategy.dice.RandomDice;

import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.io.File;

import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;

/**
 * 游戏主界面
 * 
 * @author 陆梦轩
 *
 */
public class GameFrame extends JFrame {
	
	/**
	 * 游戏状态
	 * 
	 * @author 陆梦轩
	 *
	 */
	enum State{
		/**
		 * 游戏未开始或已结束
		 */
		stop,
		/**
		 * 正在布局
		 */
		init,
		/**
		 * 正在下棋
		 */
		move,
		/**
		 * 人类布局或下棋
		 */
		human,
		/**
		 * 电脑布局或下棋
		 */
		computer,
		/**
		 * 自动对弈中
		 */
		run
	};
	/**
	 * 
	 */
	private static final long serialVersionUID = -2339906164422135479L;

	/**
	 * 游戏状态，存储对弈过程所需的数据结构
	 */
	private GameState gameState;
	/**
	 * 先手方
	 */
	private PieceType firstPlayer;
	/**
	 * 当前骰子点数
	 */
	private byte dice;
	/**
	 * 当前走法
	 */
	private Move move;
	/**
	 * 游戏状态
	 */
	private State state;
	/**
	 * 人机布局标志，双方的布局策略是人类手动布局还是电脑生成布局
	 */
	private Map<PieceType, State> initState;
	/**
	 * 人机走子标志，双方的走子策略是人类手动下棋还是电脑生成走法
	 */
	private Map<PieceType, State> moveState;
	/**
	 * 自动下棋标志
	 */
	private State autoState;
	/**
	 * 自动下棋线程
	 */
	private AutoPlayThread autoPlayThread;

	/**
	 * 主容器，所有组件放置于该容器中
	 */
	private JPanel contentPane;
	
	/**
	 * 棋盘面板
	 */
	private ChessBoardPanel boardPanel;
	/**
	 * 游戏控制面板
	 */
	private JPanel gameControlPanel;
	/**
	 * 配置玩家信息的组件
	 */
	private JTabbedPane playerSettingTabbedPane;
	/**
	 * 显示对弈信息的面板
	 */
	private JPanel gameStateDisplayPanel;
	
	/**
	 * 配置玩家信息的组件
	 */
	private Map<PieceType, PlayerSettingPanel> playerSettingPanels;
	private JLabel diceLabel;
	/**
	 * 骰子按钮集合，包含6个骰子按钮
	 */
	private JRadioButton[] diceButtons;
	/**
	 * 骰子按钮组，与diceButtons配合使用
	 */
	private final ButtonGroup diceButtonGroup = new ButtonGroup();
	/**
	 * 投骰子按钮
	 */
	private JButton diceCreateButton;
	/**
	 * 布局按钮
	 */
	private JButton initButton;
	/**
	 * 下棋按钮
	 */
	private JButton moveButton;
	/**
	 * 悔棋按钮
	 */
	private JButton pushBackButton;
	private JLabel currentPlayerLabel;
	private JLabel label;
	private JLabel label_1;
	private JLabel label_2;
	private JLabel label_3;
	/**
	 * 布局按钮组，人类、电脑
	 */
	private final ButtonGroup redInitButtonGroup = new ButtonGroup();
	private final ButtonGroup blueInitButtonGroup = new ButtonGroup();
	/**
	 * 下棋按钮组，人类、电脑
	 */
	private final ButtonGroup redMoveButtonGroup = new ButtonGroup();
	private final ButtonGroup blueMoveButtonGroup = new ButtonGroup();
	/**
	 * 运行时间
	 */
	private JTextField redRunTimeTextField;
	private JTextField blueRunTimeTextField;
	private JLabel label_8;
	private JLabel label_9;
	private JLabel label_10;
	/**
	 * 比赛信息
	 */
	private JTextField competitionTimeTextField;
	private JTextField competitionPlaceTextField;
	private JTextField competitionNameTextField;
	private JLabel label_11;
	private JLabel label_12;
	private JLabel label_13;
	private JLabel lblNewLabel_1;
	/**
	 * 搜索信息
	 */
	private JTextField redValueTextField;
	private JTextField redDepthTextField;
	private JTextField blueValueTextField;
	private JTextField blueDepthTextField;
	private JLabel label_4;
	private JLabel label_14;
	private JTextField redIterationNumTextField;
	private JTextField blueIterationNumTextField;
	private JLabel label_15;
	private final ButtonGroup firstMoveButtonGroup = new ButtonGroup();
	private JRadioButton redFirstRadioButton;
	private JRadioButton blueFirstRadioButton;
	/**
	 * 棋谱显示面板
	 */
	private JPanel recordDisplayPanel;
	/**
	 * 棋谱显示区
	 */
	private JTextArea recordDisplayTextArea;
	/**
	 * 自动下棋面板
	 */
	private JPanel autoPanel;
	private JLabel lblNewLabel_2;
	private JLabel label_16;
	private JTextField autoNumTextField;
	private JTextField autoBlueWinNumTextField;
	private JLabel label_17;
	/**
	 * 自动下棋开始按钮
	 */
	private JButton autoStartButton;
	private JRadioButton redHumanInitButton;
	private JRadioButton redComputerInitButton;
	private JRadioButton redHumanMoveButton;
	private JRadioButton redComputerMoveButton;
	private JRadioButton blueHumanInitButton;
	private JRadioButton blueComputerInitButton;
	private JRadioButton blueHumanMoveButton;
	private JRadioButton blueComputerMoveButton;
	private JLabel label_18;
	private JTextField autoRedWinNumTextField;
	private JLabel label_19;
	private JLabel label_20;
	/**
	 * 调节自动下棋“观察毫秒数”的组件
	 */
	private JSpinner autoSleepSpinner;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				GameFrame frame = null;
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					frame = new GameFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(frame, "出错啦", "系统崩溃了！！", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GameFrame() {
		setTitle("合肥工业大学");
		this.initialize();
		
	}
	
	/**
	 * 程序初始化，先初始化数据，后界面
	 */
	private void initialize() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 789, 587);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		initData();
		initUI();
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		this.gameState = new GameState();
		dice = 1;
		firstPlayer = PieceType.BLUE;
		state = State.stop;
		autoState = State.stop;
		gameState.reset(firstPlayer);
		initState = new ConcurrentHashMap<PieceType, GameFrame.State>();
		moveState = new ConcurrentHashMap<PieceType, GameFrame.State>();
	}
	
	/**
	 * 初始化六个面板
	 */
	private void initUI() {
		playerSettingPanels = new ConcurrentHashMap<PieceType, PlayerSettingPanel>();
		
		// 初始化棋盘面板
		initBoardPanel();
		initGameControlPanel();
		initPlayerSettingTabbedPane();
		initGameStateDisplayPanel();
		initRecordDisplayPanel();
		initAutoPanel();
	}
	
	/**
	 * 初始化棋盘面板
	 */
	private void initBoardPanel() {
		boardPanel = new ChessBoardPanel();
		contentPane.add(boardPanel);
		boardPanel.setLayout(null);
		
		
		boardPanel.setVisible(true);
	}
	
	/**
	 * 初始化游戏控制面板
	 */
	private void initGameControlPanel() {
		gameControlPanel = new JPanel();
		gameControlPanel.setBounds(0, 410, 400, 134);
		contentPane.add(gameControlPanel);
		gameControlPanel.setLayout(null);
		
		diceLabel = new JLabel("骰子");
		diceLabel.setBounds(10, 105, 36, 15);
		gameControlPanel.add(diceLabel);
		
		this.diceButtons = new JRadioButton[7];
		diceButtons[1] = new JRadioButton("1");
		diceButtons[1].addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				dice = 1;
				if(state == State.move) {
					if(moveState.get(gameState.getCurrentPlayer().getTurn()) == State.human) {
						boardPanel.updateBoardPanel(gameState.getCurrentBoard());
						boardPanel.openHumanMoveMode(gameState.getCurrentPlayer().getTurn(), dice);
					}
				}
			}
		});
		diceButtonGroup.add(diceButtons[1]);
		diceButtons[1].setBounds(48, 101, 36, 23);
		gameControlPanel.add(diceButtons[1]);
		
		diceButtons[2] = new JRadioButton("2");
		diceButtons[2].addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				dice = 2;
				if(state == State.move) {
					if(moveState.get(gameState.getCurrentPlayer().getTurn()) == State.human) {
						boardPanel.updateBoardPanel(gameState.getCurrentBoard());
						boardPanel.openHumanMoveMode(gameState.getCurrentPlayer().getTurn(), dice);
					}
				}
			}
		});
		diceButtonGroup.add(diceButtons[2]);
		diceButtons[2].setBounds(86, 101, 36, 23);
		gameControlPanel.add(diceButtons[2]);
		
		diceButtons[3] = new JRadioButton("3");
		diceButtons[3].addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				dice = 3;
				if(state == State.move) {
					if(moveState.get(gameState.getCurrentPlayer().getTurn()) == State.human) {
						boardPanel.updateBoardPanel(gameState.getCurrentBoard());
						boardPanel.openHumanMoveMode(gameState.getCurrentPlayer().getTurn(), dice);
					}
				}
			}
		});
		diceButtonGroup.add(diceButtons[3]);
		diceButtons[3].setBounds(124, 101, 36, 23);
		gameControlPanel.add(diceButtons[3]);
		
		diceButtons[4] = new JRadioButton("4");
		diceButtons[4].addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				dice = 4;
				if(state == State.move) {
					if(moveState.get(gameState.getCurrentPlayer().getTurn()) == State.human) {
						boardPanel.updateBoardPanel(gameState.getCurrentBoard());
						boardPanel.openHumanMoveMode(gameState.getCurrentPlayer().getTurn(), dice);
					}
				}
			}
		});
		diceButtonGroup.add(diceButtons[4]);
		diceButtons[4].setBounds(162, 101, 36, 23);
		gameControlPanel.add(diceButtons[4]);
		
		diceButtons[5] = new JRadioButton("5");
		diceButtons[5].addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				dice = 5;
				if(state == State.move) {
					if(moveState.get(gameState.getCurrentPlayer().getTurn()) == State.human) {
						boardPanel.updateBoardPanel(gameState.getCurrentBoard());
						boardPanel.openHumanMoveMode(gameState.getCurrentPlayer().getTurn(), dice);
					}
				}
			}
		});
		diceButtonGroup.add(diceButtons[5]);
		diceButtons[5].setBounds(200, 101, 36, 23);
		gameControlPanel.add(diceButtons[5]);
		
		diceButtons[6] = new JRadioButton("6");
		diceButtons[6].addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				dice = 6;
				if(state == State.move) {
					if(moveState.get(gameState.getCurrentPlayer().getTurn()) == State.human) {
						boardPanel.updateBoardPanel(gameState.getCurrentBoard());
						boardPanel.closeHumanMoveMode(gameState.getCurrentPlayer().getTurn());						boardPanel.openHumanMoveMode(gameState.getCurrentPlayer().getTurn(), dice);
					}
				}
			}
		});
		diceButtonGroup.add(diceButtons[6]);
		diceButtons[6].setBounds(238, 101, 36, 23);
		gameControlPanel.add(diceButtons[6]);
		
		diceButtons[dice].setSelected(true);
		diceCreateButton = new JButton("投骰子");
		diceCreateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				diceCreateButtonCallback();
			}
		});
		diceCreateButton.setBounds(302, 40, 69, 23);
		gameControlPanel.add(diceCreateButton);
		
		initButton = new JButton("布局");
		initButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initButtonCallback();
			}
		});
		initButton.setBounds(302, 9, 69, 23);
		gameControlPanel.add(initButton);
		
		moveButton = new JButton("下棋");
		moveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					
					public void run() {
						// TODO 自动生成的方法存根
						moveButtonCallback();
					}
					
				}).start();
			}
		});
		moveButton.setBounds(302, 71, 69, 23);
		gameControlPanel.add(moveButton);
		
		pushBackButton = new JButton("悔棋");
		pushBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pushBackButtonCallback();
			}
		});
		pushBackButton.setBounds(302, 102, 69, 23);
		gameControlPanel.add(pushBackButton);
		
		label = new JLabel("蓝方");
		label.setBounds(10, 75, 36, 15);
		gameControlPanel.add(label);
		
		label_1 = new JLabel("红方");
		label_1.setBounds(10, 54, 36, 15);
		gameControlPanel.add(label_1);
		
		label_2 = new JLabel("布局");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setBounds(47, 31, 105, 15);
		gameControlPanel.add(label_2);
		
		label_3 = new JLabel("走子");
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		label_3.setBounds(162, 31, 100, 15);
		gameControlPanel.add(label_3);
		
		redHumanInitButton = new JRadioButton("玩家");
		redHumanInitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				initState.put(PieceType.RED, State.human);
				if(state == State.init) {
					boardPanel.openHumanInitMode(PieceType.RED);
				}
			}
		});
		redInitButtonGroup.add(redHumanInitButton);
		redHumanInitButton.setBounds(47, 50, 53, 23);
		gameControlPanel.add(redHumanInitButton);
		
		redComputerInitButton = new JRadioButton("电脑");
		redComputerInitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initState.put(PieceType.RED, State.computer);
				boardPanel.closeHumanInitMode(PieceType.RED);
			}
		});
		redComputerInitButton.setSelected(true);
		initState.put(PieceType.RED, State.computer);
		redInitButtonGroup.add(redComputerInitButton);
		redComputerInitButton.setBounds(99, 50, 53, 23);
		gameControlPanel.add(redComputerInitButton);
		
		redHumanMoveButton = new JRadioButton("玩家");
		redHumanMoveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveState.put(PieceType.RED, State.human);
				if(state == State.move && gameState.getCurrentPlayer().getTurn() == PieceType.RED) {
					boardPanel.openHumanMoveMode(PieceType.RED, dice);
				}
			}
		});
		redMoveButtonGroup.add(redHumanMoveButton);
		redHumanMoveButton.setBounds(154, 50, 53, 23);
		gameControlPanel.add(redHumanMoveButton);
		
		redComputerMoveButton = new JRadioButton("电脑");
		redComputerMoveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveState.put(PieceType.RED, State.computer);
				boardPanel.updateBoardPanel(gameState.getCurrentBoard());
				boardPanel.closeHumanMoveMode(PieceType.RED);
			}
		});
		redComputerMoveButton.setSelected(true);
		moveState.put(PieceType.RED, State.computer);
		redMoveButtonGroup.add(redComputerMoveButton);
		redComputerMoveButton.setBounds(209, 50, 53, 23);
		gameControlPanel.add(redComputerMoveButton);
		
		blueHumanInitButton = new JRadioButton("玩家");
		blueHumanInitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initState.put(PieceType.BLUE, State.human);
				if(state == State.init) {
					boardPanel.openHumanInitMode(PieceType.BLUE);
				}
			}
		});
		blueInitButtonGroup.add(blueHumanInitButton);
		blueHumanInitButton.setBounds(47, 71, 53, 23);
		gameControlPanel.add(blueHumanInitButton);
		
		blueComputerInitButton = new JRadioButton("电脑");
		blueComputerInitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initState.put(PieceType.BLUE, State.computer);
				boardPanel.closeHumanInitMode(PieceType.BLUE);
			}
		});
		blueComputerInitButton.setSelected(true);
		initState.put(PieceType.BLUE, State.computer);
		blueInitButtonGroup.add(blueComputerInitButton);
		blueComputerInitButton.setBounds(99, 71, 53, 23);
		gameControlPanel.add(blueComputerInitButton);
		
		blueHumanMoveButton = new JRadioButton("玩家");
		blueHumanMoveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveState.put(PieceType.BLUE, State.human);
				if(state == State.move && gameState.getCurrentPlayer().getTurn() == PieceType.BLUE) {
					boardPanel.openHumanMoveMode(PieceType.BLUE, dice);
				}
			}
		});
		blueMoveButtonGroup.add(blueHumanMoveButton);
		blueHumanMoveButton.setBounds(154, 71, 53, 23);
		gameControlPanel.add(blueHumanMoveButton);
		
		blueComputerMoveButton = new JRadioButton("电脑");
		blueComputerMoveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveState.put(PieceType.BLUE, State.computer);
				boardPanel.updateBoardPanel(gameState.getCurrentBoard());
				boardPanel.closeHumanMoveMode(PieceType.BLUE);
			}
		});
		blueComputerMoveButton.setSelected(true);
		moveState.put(PieceType.BLUE, State.computer);
		blueMoveButtonGroup.add(blueComputerMoveButton);
		blueComputerMoveButton.setBounds(209, 71, 53, 23);
		gameControlPanel.add(blueComputerMoveButton);
		
		label_20 = new JLabel("游戏控制：");
		label_20.setBounds(10, 9, 69, 15);
		gameControlPanel.add(label_20);
		gameControlPanel.setVisible(true);
	}
	
	/**
	 * 初始化玩家的面板
	 */
	private void initPlayerSettingTabbedPane() {
		playerSettingTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		playerSettingTabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				PlayerSettingPanel panel = (PlayerSettingPanel)(playerSettingTabbedPane.getSelectedComponent());
				gameState.setPlayer(panel.getPlayer());
			}
		});
		playerSettingTabbedPane.setBounds(593, 10, 178, 390);
		contentPane.add(playerSettingTabbedPane);
		
		initPlayerSettingPanel(PieceType.RED);
		initPlayerSettingPanel(PieceType.BLUE);
		playerSettingTabbedPane.addTab("红方", this.playerSettingPanels.get(PieceType.RED));
		playerSettingTabbedPane.addTab("蓝方", this.playerSettingPanels.get(PieceType.BLUE));
		
		gameState.setPlayer(playerSettingPanels.get(PieceType.BLUE).getPlayer());
		gameState.setPlayer(playerSettingPanels.get(PieceType.RED).getPlayer());
		playerSettingTabbedPane.setVisible(true);
	}
	
	private void initPlayerSettingPanel(PieceType player) {
		PlayerSettingPanel panel = new PlayerSettingPanel(player);
		this.playerSettingPanels.put(player, panel);
	}
	
	/**
	 * 初始化对弈信息显示面板
	 */
	private void initGameStateDisplayPanel() {
		gameStateDisplayPanel = new JPanel();
		gameStateDisplayPanel.setBounds(410, 10, 173, 390);
		contentPane.add(gameStateDisplayPanel);
		gameStateDisplayPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("红方：");
		lblNewLabel.setBounds(10, 142, 54, 15);
		gameStateDisplayPanel.add(lblNewLabel);
		
		JLabel label_5 = new JLabel("蓝方：");
		label_5.setBounds(10, 262, 54, 15);
		gameStateDisplayPanel.add(label_5);
		
		JLabel label_6 = new JLabel("执行时间");
		label_6.setBounds(20, 167, 54, 15);
		gameStateDisplayPanel.add(label_6);
		
		JLabel label_7 = new JLabel("执行时间");
		label_7.setBounds(20, 287, 54, 15);
		gameStateDisplayPanel.add(label_7);
		
		redRunTimeTextField = new JTextField();
		redRunTimeTextField.setText(String.valueOf(gameState.getPlayer(PieceType.RED).getRunningTime()));
		redRunTimeTextField.setEditable(false);
		redRunTimeTextField.setBounds(84, 164, 79, 21);
		gameStateDisplayPanel.add(redRunTimeTextField);
		redRunTimeTextField.setColumns(10);
		
		blueRunTimeTextField = new JTextField();
		blueRunTimeTextField.setText(String.valueOf(gameState.getPlayer(PieceType.BLUE).getRunningTime()));
		blueRunTimeTextField.setEditable(false);
		blueRunTimeTextField.setBounds(84, 284, 79, 21);
		gameStateDisplayPanel.add(blueRunTimeTextField);
		blueRunTimeTextField.setColumns(10);
		
		label_8 = new JLabel("比赛日期");
		label_8.setBounds(10, 10, 54, 15);
		gameStateDisplayPanel.add(label_8);
		
		label_9 = new JLabel("比赛地点");
		label_9.setBounds(10, 35, 54, 15);
		gameStateDisplayPanel.add(label_9);
		
		label_10 = new JLabel("比赛名称");
		label_10.setBounds(10, 60, 54, 15);
		gameStateDisplayPanel.add(label_10);
		
		competitionTimeTextField = new JTextField();
		competitionTimeTextField.setEditable(false);
		competitionTimeTextField.setBounds(84, 7, 79, 21);
		competitionTimeTextField.setText(gameState.getDate());
		gameStateDisplayPanel.add(competitionTimeTextField);
		competitionTimeTextField.setColumns(10);
		
		competitionPlaceTextField = new JTextField();
		competitionPlaceTextField.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				gameState.setPlace(competitionPlaceTextField.getText());
			}
		});
		competitionPlaceTextField.setBounds(84, 32, 79, 21);
		competitionPlaceTextField.setText(gameState.getPlace());
		gameStateDisplayPanel.add(competitionPlaceTextField);
		competitionPlaceTextField.setColumns(10);
		
		competitionNameTextField = new JTextField();
		competitionNameTextField.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				gameState.setCompetitionName(competitionNameTextField.getText());
			}
		});
		competitionNameTextField.setBounds(84, 57, 79, 21);
		competitionNameTextField.setText(gameState.getCompetitionName());
		gameStateDisplayPanel.add(competitionNameTextField);
		competitionNameTextField.setColumns(10);
		
		label_11 = new JLabel("估值");
		label_11.setBounds(20, 192, 54, 15);
		gameStateDisplayPanel.add(label_11);
		
		label_12 = new JLabel("估值");
		label_12.setBounds(20, 312, 54, 15);
		gameStateDisplayPanel.add(label_12);
		
		label_13 = new JLabel("搜索深度");
		label_13.setBounds(20, 217, 54, 15);
		gameStateDisplayPanel.add(label_13);
		
		lblNewLabel_1 = new JLabel("搜索深度");
		lblNewLabel_1.setBounds(20, 337, 54, 15);
		gameStateDisplayPanel.add(lblNewLabel_1);
		
		redValueTextField = new JTextField();
		redValueTextField.setEditable(false);
		redValueTextField.setText("0");
		redValueTextField.setBounds(84, 189, 79, 21);
		gameStateDisplayPanel.add(redValueTextField);
		redValueTextField.setColumns(10);
		
		redDepthTextField = new JTextField();
		redDepthTextField.setEditable(false);
		redDepthTextField.setText("0");
		redDepthTextField.setBounds(84, 214, 79, 21);
		gameStateDisplayPanel.add(redDepthTextField);
		redDepthTextField.setColumns(10);
		
		blueValueTextField = new JTextField();
		blueValueTextField.setEditable(false);
		blueValueTextField.setText("0");
		blueValueTextField.setBounds(84, 309, 79, 21);
		gameStateDisplayPanel.add(blueValueTextField);
		blueValueTextField.setColumns(10);
		
		blueDepthTextField = new JTextField();
		blueDepthTextField.setEditable(false);
		blueDepthTextField.setText("0");
		blueDepthTextField.setBounds(84, 334, 79, 21);
		gameStateDisplayPanel.add(blueDepthTextField);
		blueDepthTextField.setColumns(10);
		
		label_4 = new JLabel("迭代次数");
		label_4.setBounds(20, 242, 54, 15);
		gameStateDisplayPanel.add(label_4);
		
		label_14 = new JLabel("迭代次数");
		label_14.setBounds(20, 362, 54, 15);
		gameStateDisplayPanel.add(label_14);
		
		redIterationNumTextField = new JTextField();
		redIterationNumTextField.setEditable(false);
		redIterationNumTextField.setText("0");
		redIterationNumTextField.setBounds(84, 239, 79, 21);
		gameStateDisplayPanel.add(redIterationNumTextField);
		redIterationNumTextField.setColumns(10);
		
		blueIterationNumTextField = new JTextField();
		blueIterationNumTextField.setEditable(false);
		blueIterationNumTextField.setText("0");
		blueIterationNumTextField.setBounds(84, 359, 79, 21);
		gameStateDisplayPanel.add(blueIterationNumTextField);
		blueIterationNumTextField.setColumns(10);
		
		label_15 = new JLabel("先手方：");
		label_15.setBounds(10, 92, 54, 15);
		gameStateDisplayPanel.add(label_15);
		
		redFirstRadioButton = new JRadioButton("红");
		redFirstRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				firstPlayer = PieceType.RED;
			}
		});
		firstMoveButtonGroup.add(redFirstRadioButton);
		redFirstRadioButton.setBounds(84, 88, 37, 23);
		gameStateDisplayPanel.add(redFirstRadioButton);
		
		blueFirstRadioButton = new JRadioButton("蓝");
		blueFirstRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				firstPlayer = PieceType.BLUE;
			}
		});
		firstMoveButtonGroup.add(blueFirstRadioButton);
		blueFirstRadioButton.setBounds(130, 88, 37, 23);
		gameStateDisplayPanel.add(blueFirstRadioButton);
		
		currentPlayerLabel = new JLabel("行棋方：");
		currentPlayerLabel.setBounds(10, 117, 153, 15);
		gameStateDisplayPanel.add(currentPlayerLabel);
		
		if(firstPlayer == PieceType.BLUE) blueFirstRadioButton.setSelected(true);
		else redFirstRadioButton.setSelected(true);
	}
	
	/**
	 * 初始化棋谱显示面板
	 */
	private void initRecordDisplayPanel() {
		recordDisplayPanel = new JPanel();
		recordDisplayPanel.setBounds(410, 410, 173, 134);
		contentPane.add(recordDisplayPanel);
		recordDisplayPanel.setLayout(null);
		
		JLabel label_5 = new JLabel("棋谱：");
		label_5.setBounds(10, 5, 54, 15);
		recordDisplayPanel.add(label_5);
		
		//实例化文本框
		recordDisplayTextArea = new JTextArea();
		
	    //在文本框上添加滚动条
	    JScrollPane jsp = new JScrollPane(recordDisplayTextArea);
	    //设置矩形大小.参数依次为(矩形左上角横坐标x,矩形左上角纵坐标y，矩形长度，矩形宽度)
	    jsp.setBounds(0, 25, 173, 109);
	    //默认的设置是超过文本框才会显示滚动条，以下设置让滚动条一直显示
	    jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    jsp.setAutoscrolls(true);
	    //把滚动条添加到容器里面
	    recordDisplayPanel.add(jsp);
	    
	    JButton saveRecordButton = new JButton("保存");
	    saveRecordButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		saveRecordButtonCallback();
	    	}
	    });
	    saveRecordButton.setBounds(104, 1, 69, 23);
	    recordDisplayPanel.add(saveRecordButton);
	}
	
	/**
	 * 初始化自动下棋面板
	 */
	private void initAutoPanel() {
		autoPanel = new JPanel();
	    autoPanel.setBounds(593, 410, 178, 134);
	    contentPane.add(autoPanel);
	    autoPanel.setLayout(null);
	    
	    lblNewLabel_2 = new JLabel("棋力测试：");
	    lblNewLabel_2.setBounds(10, 5, 68, 15);
	    autoPanel.add(lblNewLabel_2);
	    
	    label_16 = new JLabel("剩余对弈轮数");
	    label_16.setBounds(10, 30, 76, 15);
	    autoPanel.add(label_16);
	    
	    autoNumTextField = new JTextField();
	    autoNumTextField.setText("0");
	    autoNumTextField.setBounds(96, 27, 66, 21);
	    autoPanel.add(autoNumTextField);
	    autoNumTextField.setColumns(10);
	    
	    autoBlueWinNumTextField = new JTextField();
	    autoBlueWinNumTextField.setText("0");
	    autoBlueWinNumTextField.setBounds(96, 52, 66, 21);
	    autoPanel.add(autoBlueWinNumTextField);
	    autoBlueWinNumTextField.setColumns(10);
	    
	    label_17 = new JLabel("蓝方获胜轮数");
	    label_17.setBounds(10, 55, 76, 15);
	    autoPanel.add(label_17);
	    
	    autoStartButton = new JButton("开始");
	    autoStartButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		autoStartButtonCallback();
	    	}
	    });
	    autoStartButton.setBounds(94, 1, 68, 23);
	    autoPanel.add(autoStartButton);
	    
	    label_18 = new JLabel("红方获胜轮数");
	    label_18.setBounds(10, 80, 76, 15);
	    autoPanel.add(label_18);
	    
	    autoRedWinNumTextField = new JTextField();
	    autoRedWinNumTextField.setText("0");
	    autoRedWinNumTextField.setBounds(96, 77, 66, 21);
	    autoPanel.add(autoRedWinNumTextField);
	    autoRedWinNumTextField.setColumns(10);
	    
	    label_19 = new JLabel("观察毫秒数");
	    label_19.setBounds(10, 105, 68, 15);
	    autoPanel.add(label_19);
	    
	    autoSleepSpinner = new JSpinner();
	    autoSleepSpinner.setBounds(96, 102, 66, 22);
	    autoPanel.add(autoSleepSpinner);
	    autoSleepSpinner.setModel(new SpinnerNumberModel(new Long(100), new Long(0), new Long(20000), new Long(100)));
	}
	
	/**
	 * 投骰子按钮点击事件
	 */
	private void diceCreateButtonCallback() {
		dice = new RandomDice().getDice();
		diceButtons[dice].setSelected(true);
		if(state == State.move) {
			if(moveState.get(gameState.getCurrentPlayer().getTurn()) == State.human) {
				boardPanel.updateBoardPanel(gameState.getCurrentBoard());
				boardPanel.openHumanMoveMode(gameState.getCurrentPlayer().getTurn(), dice);
			}
		}
	}
	
	/**
	 * 布局按钮点击事件
	 */
	private void initButtonCallback() {
		if(state == State.move) {
			//重置操作
			state = State.stop;
			initButtonCallback();
		}
		else if(state == State.init) {
			// 确认布局操作
			ChessBoard board = boardPanel.getChessBoard();
			gameState.reset(firstPlayer, board);
			boardPanel.closeHumanInitMode(PieceType.BLUE);
			boardPanel.closeHumanInitMode(PieceType.RED);
			if(moveState.get(PieceType.BLUE) == State.human) {
				boardPanel.openHumanMoveMode(PieceType.BLUE, dice);
			}
			if(moveState.get(PieceType.RED) == State.human) {
				boardPanel.openHumanMoveMode(PieceType.BLUE, dice);
			}
			state = State.move;
			initButton.setText("重置");
			competitionTimeTextField.setText(gameState.getDate());
			
			recordDisplayTextArea.setText(gameState.getRecord().toString());
		}
		else if(state == State.stop) {
			//开始布局操作
			initButton.setText("确认");
			state = State.init;
		
			gameState.reset(firstPlayer);
			boardPanel.updateBoardPanel(gameState.getCurrentBoard());
		
			if(initState.get(PieceType.BLUE) == State.human) {
				boardPanel.openHumanInitMode(PieceType.BLUE);
			}
			if(initState.get(PieceType.RED) == State.human) {
				boardPanel.openHumanInitMode(PieceType.RED);
			}
			
			competitionTimeTextField.setText(gameState.getDate());
			
			updateGameControlPanel();
		}
	}
	
	/**
	 * 下棋按钮点击事件
	 */
	private void moveButtonCallback() {
		if(state == State.stop || state == State.init) return;
		
		state = State.move;
		
		if(moveState.get(gameState.getCurrentPlayer().getTurn()) == State.computer) {
			moveButton.setEnabled(false);
			moveButton.setText("稍等");
			boardPanel.closeHumanMoveMode(PieceType.BLUE);
			boardPanel.closeHumanMoveMode(PieceType.RED);
			
			Player player = gameState.getCurrentPlayer();
			long startTime = System.currentTimeMillis();
			move = player.getMoveStrategy().getMove(gameState, dice);
			long runTime = System.currentTimeMillis() - startTime;
			player.addRunningTime(runTime);
			
			gameState.step(dice, move);
			
			if(player.getTurn() == PieceType.RED) {
				redRunTimeTextField.setText(String.valueOf(player.getRunningTime() / 1000.0));
				redValueTextField.setText(String.valueOf(player.getMoveStrategy().getMoveValue()));
				redDepthTextField.setText(String.valueOf(player.getMoveStrategy().getMaxDepth()));
				redIterationNumTextField.setText(String.valueOf(player.getMoveStrategy().getVisitNum()));
			}
			else {
				blueRunTimeTextField.setText(String.valueOf(player.getRunningTime() / 1000.0));
				blueValueTextField.setText(String.valueOf(player.getMoveStrategy().getMoveValue()));
				blueDepthTextField.setText(String.valueOf(player.getMoveStrategy().getMaxDepth()));
				blueIterationNumTextField.setText(String.valueOf(player.getMoveStrategy().getVisitNum()));
			}
			
			moveButton.setEnabled(true);
			moveButton.setText("下棋");
		}
		else {
		
			
			move = boardPanel.getMove();
			if(move == null) return;
			boardPanel.closeHumanMoveMode(PieceType.BLUE);
			boardPanel.closeHumanMoveMode(PieceType.RED);
			gameState.step(dice, move);
//			boardPanel.openHumanMoveMode(gameState.getCurrentPlayer().getTurn(), dice);
		}
		
		boardPanel.updateBoardPanel(gameState.getCurrentBoard());
		updateGameControlPanel();
		diceCreateButtonCallback();
		
		recordDisplayTextArea.setText(gameState.getRecord().toString());
		
		if(gameState.isEnd() == true) {
			state = State.stop;
		}
		move = null;
	}
	
	/**
	 * 悔棋按钮点击事件
	 */
	private void pushBackButtonCallback() {
		if(state == State.init) return;
		
		if(gameState.pushBack()==true) {
			state = State.move;
			boardPanel.updateBoardPanel(gameState.getCurrentBoard());
			updateGameControlPanel();
//			if(gameState.getRecord().size() == 0) {
//				state = State.init;
//				if(initState.get(PieceType.BLUE) == State.human) {
//					boardPanel.openHumanInitMode(PieceType.BLUE);
//				}
//				if(initState.get(PieceType.RED) == State.human) {
//					boardPanel.openHumanInitMode(PieceType.RED);
//				}
//			}
			recordDisplayTextArea.setText(gameState.getRecord().toString());
		}
	}
	
	/**
	 * 保存棋谱按钮点击事件
	 */
	private void saveRecordButtonCallback() {
		GameRecord record = gameState.getRecord();
		record.setCompetitionName(competitionNameTextField.getText());
		record.setPlace(competitionPlaceTextField.getText());
		
		String defaultFileName = record.getFileName();
		String defaultFilePath = "C:/Users/" + new String(System.getProperty("user.name").getBytes()) + "/Desktop/";
		
		JFileChooser fileChooser=new JFileChooser(new File(defaultFilePath));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setSelectedFile(new File(defaultFilePath + defaultFileName + ".txt"));
		fileChooser.setFileFilter(new FileNameExtensionFilter("txt","txt"));
		fileChooser.showSaveDialog(new JLabel());
		
		if(fileChooser.getSelectedFile()!=null){
			record.save(fileChooser.getSelectedFile().getAbsolutePath());
		}
	}
	
	/**
	 * 根据数据更新游戏控制面板里的相关信息
	 */
	private void updateGameControlPanel() {
		if(gameState.isEnd()==false) {
			currentPlayerLabel.setText("行棋方：" + gameState.getCurrentPlayer());
		}
		else {
			currentPlayerLabel.setText("行棋方：");
		}
	}
	
	/**
	 * 自动下棋按钮点击事件
	 */
	private void autoStartButtonCallback() {
		// TODO 自动生成的方法存根
		if(autoState==State.stop) {
			autoStartButton.setText("暂停");
			autoState = State.run;
			initState.put(PieceType.RED, State.computer);
			redComputerInitButton.setSelected(true);
			initState.put(PieceType.BLUE, State.computer);
			blueComputerInitButton.setSelected(true);
			moveState.put(PieceType.RED, State.computer);
			redComputerMoveButton.setSelected(true);
			moveState.put(PieceType.BLUE, State.computer);
			blueComputerMoveButton.setSelected(true);
			
			autoPlayThread = new AutoPlayThread();
			autoPlayThread.start();
			
			autoNumTextField.setEditable(false);
			autoBlueWinNumTextField.setEditable(false);
			autoRedWinNumTextField.setEditable(false);
		}
		else if(autoState == State.run) {
			autoStartButton.setText("开始");
			autoPlayThread.stopAutoPlay();
			autoState = State.stop;
			autoNumTextField.setEditable(true);
			autoBlueWinNumTextField.setEditable(true);
			autoRedWinNumTextField.setEditable(true);
		}
	}
	
	/**
	 * 自动下棋中的一步执行动作，模拟人操作去触发按钮点击事件
	 * 
	 * @return false 自动下棋结束，无需再下棋<br>
	 * true 自动下棋未结束
	 */
	private boolean autoPlayStep() {
		if(Integer.valueOf(autoNumTextField.getText()) == 0) {
			autoStartButton.setText("开始");
			autoPlayThread.stopAutoPlay();
			autoState = State.stop;
			autoNumTextField.setEditable(true);
			autoBlueWinNumTextField.setEditable(true);
			autoRedWinNumTextField.setEditable(true);
			return false;
		}
		
		if(state == State.stop) {
			initButtonCallback();
		}
		else if(state == State.init) {
			initButtonCallback();
		}
		else if(state == State.move) {
			diceCreateButtonCallback();
			moveButtonCallback();
			if(state == State.stop) {
				if(firstPlayer==PieceType.RED) {
					firstPlayer=PieceType.BLUE;
					blueFirstRadioButton.setSelected(true);
				}
				else {
					firstPlayer=PieceType.RED;
					redFirstRadioButton.setSelected(true);
				}
				
				PieceType winner = gameState.getWinner();
				if(winner == PieceType.BLUE) {
					int winNum = Integer.valueOf(autoBlueWinNumTextField.getText());
					autoBlueWinNumTextField.setText(String.valueOf(winNum + 1));
				}
				else if(winner == PieceType.RED){
					int winNum = Integer.valueOf(autoRedWinNumTextField.getText());
					autoRedWinNumTextField.setText(String.valueOf(winNum + 1));
				}
				int playNum = Integer.valueOf(autoNumTextField.getText());
				autoNumTextField.setText(String.valueOf(playNum - 1));
			}
			
			try {
				long time = (Long)autoSleepSpinner.getValue();
				Thread.sleep(time);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		return true;
	}
	
	/**
	 * 自动下棋线程，执行按钮点击操作
	 * 
	 * @author 陆梦轩
	 *
	 */
	class AutoPlayThread extends Thread{
		
		/**
		 * 是否暂停标志，自动下棋开始时为false，当鼠标点击暂停按钮后变为true
		 */
		private boolean shouldStop;
		
		public void run() {
			shouldStop=false;
			while(shouldStop == false) {
				boolean running = autoPlayStep();
				if(running == false) shouldStop = true;
			}
		}
		
		public void stopAutoPlay() {
			this.shouldStop = true;
		}
	}
}
