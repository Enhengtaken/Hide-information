package hideInfo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//���򴰿�
public class HideInfoFrame extends JFrame {
  
	JPanel contentPane;   //�����е�Panel
	
  //����frame
  public HideInfoFrame() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);   
    try { 
      jbInit();   		//���ڳ�ʼ��
    }
    catch(Exception e) {   
      e.printStackTrace();  //�����쳣
    }
  }
  //�����ʼ��
  private void jbInit() throws Exception  {
	  this.contentPane=new HideInfoPanel();		//����Panel
	  this.setSize(800,500);				 	//Panel�ߴ�
	  this.contentPane.repaint();				//�ػ�
	  this.setContentPane(this.contentPane); 	//��Panel�ʹ�����ϵ��һ��
  }
  //��д���رմ���ʱ�˳�
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {  //�յ��ر���Ϣ��رմ����Ƴ�����
      System.exit(0);							//�����˳�����
    }
  }
}