package hideInfo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//程序窗口
public class HideInfoFrame extends JFrame {
  
	JPanel contentPane;   //窗口中的Panel
	
  //构建frame
  public HideInfoFrame() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);   
    try { 
      jbInit();   		//窗口初始化
    }
    catch(Exception e) {   
      e.printStackTrace();  //处理异常
    }
  }
  //组件初始化
  private void jbInit() throws Exception  {
	  this.contentPane=new HideInfoPanel();		//建立Panel
	  this.setSize(800,500);				 	//Panel尺寸
	  this.contentPane.repaint();				//重绘
	  this.setContentPane(this.contentPane); 	//把Panel和窗口联系在一起
  }
  //重写，关闭窗口时退出
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {  //收到关闭信息则关闭窗口推出程序
      System.exit(0);							//正常退出程序
    }
  }
}