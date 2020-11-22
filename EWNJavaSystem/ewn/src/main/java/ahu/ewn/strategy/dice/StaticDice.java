package ahu.ewn.strategy.dice;

import java.util.ArrayList;
import java.util.List;

/**
 * 固定骰子点数循环序列策略。
 * 该策略包含一个骰子序列，
 * getDice方法每次从序列中按顺序读取一个骰子点数，并从序列的结尾位置自动跳回开头位置。可以用来测试下棋的策略。<br>
 * 例如：<br>
 * 骰子序列为[3, 1, 6]<br>
 * 调用5次getDice方法将依次得到骰子点数：[3, 1, 6, 3, 1]
 *
 * @author 陆梦轩
 *
 */
public class StaticDice extends DiceStrategy{

    /**
     * 骰子点数序列
     */
    private List<Byte> diceList;

    /**
     * 下一个即将输出的点数的下标
     */
    private int index=0;

    /**
     * 构造函数
     */
    public StaticDice(){
        super();
        setLabel("StaticDice");

        diceList=new ArrayList<Byte>();
        index=0;
    }

    @Override
    public byte getDice() {
        // TODO 自动生成的方法存根
        byte dice=diceList.get(index);
        index=(index+1)%diceList.size();
        return dice;
    }

    /**
     * 获取点数序列
     *
     * @return 点数序列
     */
    public List<Byte> getDiceList(){
        return this.diceList;
    }

    /**
     * 设置点数序列
     *
     * @param diceList 点数序列
     */
    public void setDiceList(List<Byte> diceList){
        this.diceList=diceList;
    }

    /**
     * 添加一个新点数至序列结尾
     *
     * @param dice 点数
     */
    public void addDice(byte dice){
        diceList.add(dice);
    }

    /**
     * 添加点数至序列结尾
     *
     * @param dices 多个骰子点数
     */
    public void addDice(byte... dices){
        for(byte dice:dices){
            diceList.add(dice);
        }
    }

    /**
     * 获取下一个点数的序列下标
     *
     * @return 下标
     */
    public int getNextIndex(){
        return this.index;
    }

    /**
     * 设置序列下标，使下次骰子点数为该下标指向的点数
     *
     * @param index 下标
     */
    public void setNextIndex(int index){
        this.index=index;
    }

    /**
     * 清空点数列表
     */
    public void clear(){
        diceList.clear();
        index=0;
    }
}
