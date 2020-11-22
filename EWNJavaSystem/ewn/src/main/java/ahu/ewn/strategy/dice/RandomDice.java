package ahu.ewn.strategy.dice;

import java.util.Random;

/**
 * 随机骰子策略，从1~6中随机生成一个骰子点数
 * 
 * @author 陆梦轩
 *
 */
public class RandomDice extends DiceStrategy {

    public RandomDice(){
        super();
        setLabel("RandomDice");
    }

    @Override
    public byte getDice() {
        Random random = new Random();
        return (byte)(1 + random.nextInt(6));
    }
}
