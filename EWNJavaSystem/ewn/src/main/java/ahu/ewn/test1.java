package ahu.ewn;

import java.util.ArrayList;
import java.util.List;

public class test1 {
	public static void main(String[] args) {
//		int boardvred[][] = { { 0, 1, 1, 1, 1 }, { 1, 2, 2, 2, 3 }, { 1, 2, 4, 4, 6 }, { 1, 2, 4, 8, 12 }, { 1, 3, 6, 12, 100 } };
//		for (int i =0;i<5;i++)
//			for (int j =0 ;j<5;j++)
//				System.out.print(boardvred[i][j] + " ");
		
//		List<Byte> dices = new ArrayList<Byte>(6);
//		 
//		dices.add((byte) 1);
//		dices.add((byte) 2);
//		System.out.println(dices.size());
		
		
		double a =1.1,b=1,c =1.2;
		
		double maxvalue = (a > b) ? a : b;
		maxvalue = (maxvalue > c) ? maxvalue : c;
		System.out.println(maxvalue);
	}
	
}
