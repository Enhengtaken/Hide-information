package hideInfo;

import java.awt.Toolkit;
import javax.swing.JFrame;

public class HideInfo {
	public static void main(String[] args) {
		HideInfoFrame mf=new HideInfoFrame();   //建立程序窗口
		mf.setBounds(200,100,1000,600);			//窗口位置和尺寸
		mf.setVisible(true);					//可见
		mf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);		//关闭不执行任何操作
		mf.setResizable(false);					//尺寸不可变
		mf.setTitle("基于LSB算法的信息隐藏");		//标题
		mf.setIconImage(Toolkit.getDefaultToolkit().getImage("resource/title.jpg"));	//标志图
	}
}
