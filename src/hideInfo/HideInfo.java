package hideInfo;

import java.awt.Toolkit;
import javax.swing.JFrame;

public class HideInfo {
	public static void main(String[] args) {
		HideInfoFrame mf=new HideInfoFrame();   //�������򴰿�
		mf.setBounds(200,100,1000,600);			//����λ�úͳߴ�
		mf.setVisible(true);					//�ɼ�
		mf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);		//�رղ�ִ���κβ���
		mf.setResizable(false);					//�ߴ粻�ɱ�
		mf.setTitle("����LSB�㷨����Ϣ����");		//����
		mf.setIconImage(Toolkit.getDefaultToolkit().getImage("resource/title.jpg"));	//��־ͼ
	}
}
