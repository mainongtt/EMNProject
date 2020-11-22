package ahu.ewn.game;

import ahu.ewn.board.ChessBoard;
import ahu.ewn.board.PieceType;
import ahu.ewn.record.GameRecord;
import ahu.ewn.strategy.dice.DiceStrategy;
import ahu.ewn.strategy.dice.RandomDice;

import java.util.HashMap;
import java.util.Map;

/**
 * 游戏状态，用于表示某一局游戏，包含了表示爱恩斯坦棋博弈的必要信息：玩家信息、当前棋盘、当前行棋方、骰子策略，以及棋谱
 *
 * @author 陆梦轩
 */
public class GameState {

    /**
     * 当前行棋方
     */
    private Player currentPlayer;

    /**
     * 当前棋盘
     */
    private ChessBoard currentBoard;

    /**
     * 玩家信息(详见Player类)
     */
    private Map<PieceType, Player> players;

    /**
     * 骰子策略
     */
    private DiceStrategy diceStrategy;
    
    /**
     * 棋谱
     */
    private GameRecord record;

    /**
     * 构造函数，根据指定的初始布局和玩家创建一局游戏。默认的骰子策略为RandomDice
     *
     * @param board   初始棋盘布局
     * @param player1 玩家1
     * @param player2 玩家2
     */
    public GameState(ChessBoard board, Player player1, Player player2) {
        this.currentBoard = board;
        players = new HashMap<PieceType, Player>(2);
        this.players.put(player1.getTurn(), player1);
        this.players.put(player2.getTurn(), player2);
        this.diceStrategy = new RandomDice();
        this.record = new GameRecord();
        this.record.setPlayer(player1, player2);
    }

    /**
     * 构造函数，创建一局新游戏，且玩家默认策略如下：<br>
     * <br>
     * 布局策略：RandomInit<br>
     * 走子策略：RandomMove<br>
     * 骰子策略：RandomDice<br>
     */
    public GameState() {
        players = new HashMap<PieceType, Player>(2);
        this.players.put(PieceType.BLUE, new Player(PieceType.BLUE));
        this.players.put(PieceType.RED, new Player(PieceType.RED));
        this.diceStrategy = new RandomDice();
        this.record = new GameRecord();
        this.record.setPlayer(players.get(PieceType.BLUE), players.get(PieceType.RED));
    }

    /**
     * 获取当前玩家
     *
     * @return Player 玩家
     */
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * 设置当前玩家
     *
     * @param player 玩家颜色
     */
    public void setCurrentPlayer(PieceType player) {
        this.currentPlayer = players.get(player);
    }

    /**
     * 获取当前棋盘布局
     *
     * @return ChessBoard 棋盘布局
     */
    public ChessBoard getCurrentBoard() {
        return this.currentBoard;
    }

    /**
     * 获取玩家
     *
     * @return Map 玩家1和玩家2
     */
    public Map<PieceType, Player> getPlayers() {
        return this.players;
    }
    
    /**
     * 获取指定颜色的玩家
     * 
     * @param pieceType 棋子颜色
     * @return Player 玩家
     */
    public Player getPlayer(PieceType pieceType) {
    	return this.players.get(pieceType);
    }

    /**
     * 设置玩家
     *
     * @param player 玩家
     */
    public void setPlayer(Player player) {
        this.players.remove(player.getTurn());
        this.players.put(player.getTurn(), player);
        this.record.setPlayer(player);
    }

    /**
     * 获取骰子策略
     * 
     * @return DiceStrategy 骰子策略
     */
    public DiceStrategy getDiceStrategy(){
        return this.diceStrategy;
    }

    /**
     * 设置骰子策略
     * 
     * @param diceStrategy 骰子策略
     */
    public void setDiceStrategy(DiceStrategy diceStrategy){
        this.diceStrategy = diceStrategy;
    }

    /**
     * 判断某一方是否获胜
     *
     * @param turn 棋子颜色
     * @return boolean turn方是否获胜
     */
    public final boolean isWin(PieceType turn) {
        return this.currentBoard.isWin(turn);
    }

    /**
     * 判断游戏是否结束
     *
     * @return boolean 游戏结束返回true；没有结束返回false
     */
    public final boolean isEnd(){
        return currentBoard.isEnd();
    }

    /**
     * 获取获胜一方
     *
     * @return PieceType 获胜方。如果游戏没有结束，返回PieceType.NULL
     */
    public final PieceType getWinner(){
        return currentBoard.getWinner();
    }

    /**
     * 重置游戏，并设置先手方，初始布局根据玩家的布局策略生成
     * 
     * @param firstTurn 先行下棋的玩家的颜色
     */
    public void reset(PieceType firstTurn){
    	//重置当前棋盘
        this.currentBoard = new ChessBoard();
        //重置当前玩家
        this.currentPlayer = this.players.get(firstTurn);
        this.players.get(PieceType.BLUE).setRunningTime(0);
        this.players.get(PieceType.RED).setRunningTime(0);

        //根据initialStrategy生成布局
        for(Player player:this.players.values()){
            PieceType turn=player.getTurn();
            ChessBoard board=player.getInitStrategy().getBoard(turn);

            for(int i=0;i<5;i++){
                for(int j=0;j<5;j++){
                    byte piece=board.getPieceByPoint(i, j);
                    if(piece!=0) this.currentBoard.setPieceLocation(piece, i, j);
                }
            }
        }//for(Player player:gameState.getPlayers())
        
        //创建新棋谱
        this.record = new GameRecord();
        this.record.setPlayer(players.get(PieceType.BLUE), players.get(PieceType.RED));
        this.record.setFirstPlayer(firstTurn);
        this.record.setInitBoard(this.currentBoard.clone());
    }
    
    /**
     * 重置游戏，并设置先手方和初始布局
     * 
     * @param firstTurn 先行下棋的玩家的颜色
     * @param initBoard 初始棋盘布局
     */
    public void reset(PieceType firstTurn, ChessBoard initBoard){
    	//重置当前棋盘
        this.currentBoard = initBoard;
        //重置当前玩家
        this.currentPlayer = this.players.get(firstTurn);
        this.players.get(PieceType.BLUE).setRunningTime(0);
        this.players.get(PieceType.RED).setRunningTime(0);
        //重置棋谱
        this.record = new GameRecord();
        this.record.setPlayer(players.get(PieceType.BLUE), players.get(PieceType.RED));
        this.record.setFirstPlayer(firstTurn);
        this.record.setInitBoard(currentBoard.clone());
    }

    /**
     * 根据骰子策略生成一个骰子点数
     * 
     * @return byte 骰子点数
     */
    public byte getDice(){
        return this.diceStrategy.getDice();
    }

    /**
     * 根据指定的骰子点数和走子动作下棋，更新当前棋盘、行棋方、棋谱。如果走子动作不合法，则不做任何操作
     * 
     * @param dice 骰子点数
     * @param move 走子动作
     */
    public void step(byte dice, Move move){
        if(isEnd()) return;
        // 处理走子动作
        byte eated_piece = currentBoard.processMove(move);
        if(eated_piece == -1) return;
        // 处理棋谱
        this.record.push(dice, move);
        this.record.setWinner(this.getWinner());
        // 处理行棋方
        if(this.currentPlayer.getTurn() == PieceType.BLUE){
            this.currentPlayer = this.players.get(PieceType.RED);
        }
        else{
            this.currentPlayer = this.players.get(PieceType.BLUE);
        }
    }
    
    /**
     * 悔棋，向前悔一步棋
     * 
     * @return true：成功悔一步棋<br>
     * false：悔棋失败，已经悔至初始棋盘布局
     */
    public boolean pushBack() {
    	//判断是否可以悔棋
    	if(this.record.size() == 0) return false;
    	//切换玩家
    	if(this.currentPlayer.getTurn() == PieceType.BLUE){
            this.currentPlayer = this.players.get(PieceType.RED);
        }
        else{
            this.currentPlayer = this.players.get(PieceType.BLUE);
        }
    	//移除棋谱，将当前棋盘更新成棋谱中储存的上一步棋盘
    	this.record.pop();
    	this.currentBoard = this.record.getBoard(this.record.size());
    	this.record.setWinner(this.getWinner());

    	if(this.record.size() == 0) this.reset(this.currentPlayer.getTurn(), this.record.getInitBoard());
    	return true;
    }
    
    /**
     * 获取当前棋谱
     * 
     * @return GameRecord 棋谱
     */
    public GameRecord getRecord() {
    	return this.record;
    }
    
    /**
     * 更新棋谱中的比赛时间为此刻的时间（官方棋谱格式要求）
     */
    public void updateDate() {
		this.record.updateDate();
	}

	/**
	 * 获取比赛时间
	 * 
	 * @return String 比赛时间（官方棋谱格式）
	 */
	public String getDate() {
		return this.record.getDate();
	}

	/**
	 * 手动设置比赛时间
	 * 
	 * @param date 比赛时间（格式参考官方棋谱格式）
	 */
	public void setDate(String date) {
		this.record.setDate(date);
	}

	/**
	 * 获取比赛地点
	 * 
	 * @return String 比赛地点
	 */
	public String getPlace() {
		return this.record.getPlace();
	}

	/**
	 * 设置比赛地点（官方棋谱格式要求）
	 * 
	 * @param place 比赛地点
	 */
	public void setPlace(String place) {
		this.record.setPlace(place);
	}

	/**
	 * 获取赛事名称
	 * 
	 * @return String 比赛名称
	 */
	public String getCompetitionName() {
		return this.record.getCompetitionName();
	}

	/**
	 * 设置赛事名称（官方棋谱格式要求）
	 * 
	 * @param competitionName 赛事名称
	 */
	public void setCompetitionName(String competitionName) {
		this.record.setCompetitionName(competitionName);
	}
	
	/* （非 Javadoc）
	 * @see java.lang.Object#toString()
	 * 将GameState转化为文本，文本格式参考ahu.ewn.record.GameRecord
	 * 
	 */
	public String toString() {
		return this.record.toString();
	}

}
