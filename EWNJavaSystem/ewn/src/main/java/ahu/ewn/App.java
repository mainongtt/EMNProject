package ahu.ewn;

import java.awt.EventQueue;

import javax.swing.UIManager;

import ahu.ewn.ui.GameFrame;

/**
 * 平台启动程序
 *
 */
public class App
{
	
    /**
     * 程序入口，创建界面
     * 
     * @param args 参数
     */
    public static void main( String[] args )
    {
    	EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// windows主题
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				} catch (Exception e) {
					e.printStackTrace();
				}
				GameFrame frame = new GameFrame();
				frame.setVisible(true);
			}
		});
    }
}
