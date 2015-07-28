package hideInfo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.*;

//�����е���Panel,����������Ϣ,���Ű�ť�ȿؼ�
public class HideInfoPanel extends JPanel implements ActionListener
{	
	private boolean isHideMode;		//����ģʽ��������Ϣ����ȡ��Ϣ�� true��������ģʽ
	private MediaTracker mt;		//���ڼ�ض�ý��������
	private JFileChooser jfc;		//�ļ�ѡ����
	private Image im;				//ͼ�����
	private JButton bFile1;			//��ť��ѡ��ԭͼ��ť
	private JButton bFile2;			//��ť��ѡ��Դ�ı��ļ���ť
	private JButton bFile3;			//��ť��ѡ��Ŀ��ͼ��
	private JButton bMode;			//ģʽ�л���ť
	private JButton bOp;			//���д���������ģʽ���������ز�������ȡģʽ����ȡ������	
	private File originBMP;			//ԭͼ���ļ�����
	private File targetBMP;			//Ŀ��ͼ���ļ�����
	private File originTxt;			//Դ�ı��ļ�����
	private File targetTxt;			//Ŀ���ı��ļ�����
	private BMP originImage;		//���������BMP�ļ�ת��ΪImage������ΪJAVA��ֱ��֧������BMP�ļ������غ���ȡʱ����������Ϊֱ�Ӱ�BMP�ļ������ļ�����������Ҫת��ΪImage����
	JLabel tag1,tag2,tag3;   		//������ʾ��·���������ı�ǩ
	JLabel status;					//���·���ʾ��
	JTextField pathFile1,pathFile2,pathFile3;	//��ʾ���������ļ���·�� ��Panel�����Ϸ�
	ShowPanel show1,show2;			//��ʾ����BMPͼ�� ��Panel�����£�

	public HideInfoPanel()
	{
		//��ʼ��
		this.isHideMode=true;  		//Ĭ������ģʽ
		this.setLayout(null);		//ʹ�����귽ʽָ��������ť��λ��
		this.mt=new MediaTracker(this);  
		jfc=new JFileChooser();  	//�ļ�ѡ����
		this.im=Toolkit.getDefaultToolkit().getImage("resource/background.jpg");//���뱳��	
		mt.addImage(this.im,1);		//��Image��Ķ���image��ӵ���ǰý�������Ҫ׷�ٵ�ͼ���б��У����Ͷ���id��ʾ��image�ı�ʶ
		try
 		{
 			mt.waitForID(1);		//���ٶ�ý���ļ�
 		}
 		catch (InterruptedException e)
 		{
 		}
 		this.setVisible(true);
		
		this.bFile1=new JButton("ѡ��ԭͼ��");		//��ʾ������ ����ť��
		this.bFile1.setBounds(70,75,120,35);		//λ�úʹ�С
		this.bFile1.setFont(new Font("����",Font.BOLD,12));		//����
		this.bFile1.setOpaque(true); 				//͸��
		this.bFile1.setForeground(Color.black);   	//������ɫ
		this.bFile1.setBackground(new Color(240,255,240));  //��ť����ɫ
		this.bFile1.addActionListener(this); 		//��������ť���������¼��������͵���������Ĵ�������
		this.add(bFile1);
		
		this.bFile2=new JButton("ѡ��������Ϣ");
		this.bFile2.setBounds(70,120,120,35);
		this.bFile2.setFont(new Font("����",Font.BOLD,12));
		this.bFile2.setOpaque(true);
		this.bFile2.setForeground(Color.black);
		this.bFile2.setBackground(new Color(240,255,240));
		this.bFile2.addActionListener(this);
		this.add(bFile2);
		
		this.bFile3=new JButton("ѡ��Ŀ��ͼ��");
		this.bFile3.setBounds(70,165,120,35);
		this.bFile3.setFont(new Font("����",Font.BOLD,12));
		this.bFile3.setOpaque(true);
		this.bFile3.setForeground(Color.black);
		this.bFile3.setBackground(new Color(240,255,240));
		this.bFile3.addActionListener(this);	
		this.add(bFile3);
		
		this.bOp=new JButton("��ʼ����");
		this.bOp.setBounds(70,220,120,100);
		this.bOp.setFont(new Font("����",Font.BOLD,12));
		this.bOp.setOpaque(true);
		this.bOp.setForeground(Color.black);
		this.bOp.setBackground(new Color(240,255,240));
		this.bOp.addActionListener(this);
		this.add(bOp);
		
		this.bMode=new JButton("�л�ģʽ");
		this.bMode.setBounds(70,340,120,80);
		this.bMode.setFont(new Font("����",Font.BOLD,12));
		this.bMode.setOpaque(true);
		this.bMode.setForeground(Color.black);
		this.bMode.setBackground(new Color(240,255,240));
		this.bMode.addActionListener(this);
		this.add(bMode);
		
		this.tag1=new JLabel("·��:");
		this.tag1.setBounds(250,75,40,35);
		this.tag1.setFont(new Font("����",Font.PLAIN,14));
		this.tag1.setForeground(Color.black);
		this.add(this.tag1);
		
		this.tag2=new JLabel("·��:");
		this.tag2.setBounds(250,120,40,35);
		this.tag2.setFont(new Font("����",Font.PLAIN,14));
		this.tag2.setForeground(Color.black);
		this.add(this.tag2);
		
		this.tag3=new JLabel("·��:");
		this.tag3.setBounds(250,165,40,35);
		this.tag3.setFont(new Font("����",Font.PLAIN,14));
		this.tag3.setForeground(Color.black);
		this.add(this.tag3);
		
		
		this.status=new JLabel("��ǰģʽ����Ϣ����ģʽ");
		this.status.setBounds(490,450,680,30);
		this.status.setHorizontalAlignment(SwingConstants.LEFT);  //�����
		this.status.setFont(new Font("����",Font.ITALIC,16));
		this.status.setForeground(Color.black);
		this.status.setOpaque(false);
		this.add(this.status);
		
		this.pathFile1=new JTextField(100);
		this.pathFile1.setBounds(300,75,490,35);
		this.pathFile1.setFont(new Font("����",Font.PLAIN,12));
		this.pathFile1.setEditable(false);				//���ɱ༭
		this.pathFile1.setOpaque(false);				//͸��
		this.pathFile1.setBorder(null);					//�ޱ߿�
		this.add(this.pathFile1);
		
		this.pathFile2=new JTextField(100);
		this.pathFile2.setBounds(300,120,490,35);
		this.pathFile2.setFont(new Font("����",Font.PLAIN,12));
		this.pathFile2.setEditable(false);
		this.pathFile2.setOpaque(false);
		this.pathFile2.setBorder(null);
		this.add(this.pathFile2);
		
		this.pathFile3=new JTextField(100);
		this.pathFile3.setBounds(300,165,490,35);
		this.pathFile3.setFont(new Font("����",Font.PLAIN,12));
		this.pathFile3.setEditable(false);
		this.pathFile3.setOpaque(false);
		this.pathFile3.setBorder(null);
		this.add(this.pathFile3);
		
		this.show1=new ShowPanel();
		this.show1.setBorder(new TitledBorder(new LineBorder(new Color(69,167,116)),"����ǰ��ͼ��",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,new Font("����",Font.PLAIN,12),Color.black));
		this.show1.setBounds(250,200,320,230);
		this.show1.setBackground(Color.WHITE);
		this.show1.setOpaque(true);
		this.add(this.show1);
		show1.repaint();
		
		this.show2=new ShowPanel();
		this.show2.setBorder(new TitledBorder(new LineBorder(new Color(69,167,116)),"���غ��ͼ��",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,new Font("����",Font.PLAIN,12),Color.black));  //���б���ı߿�
		this.show2.setBounds(600,200,320,230);
		this.show2.setBackground(Color.WHITE);
		this.show2.setOpaque(true);
		this.add(this.show2);
		show2.repaint();
		
		this.repaint();
	}
	
	//����������������а�ť���������������¼�
	public void actionPerformed(ActionEvent e) {
		
		//�����İ�ť��ѡ��ԭͼ��ť 
		if(((JButton)e.getSource()).equals(this.bFile1))  
		{		
			int retVal=jfc.showOpenDialog(this);  //���ļ�ѡ����
			if (retVal==JFileChooser.APPROVE_OPTION)
			{
				this.originBMP=jfc.getSelectedFile();//ѡ���ļ�
				this.pathFile1.setText(this.originBMP.getAbsolutePath());//��ʾ����·��
				if(this.originBMP.getName().endsWith(".BMP")||this.originBMP.getName().endsWith(".bmp"))
				{	//�����.bmp�ļ� ����ʾ��
					this.originImage=new BMP(this.originBMP);
					this.show1.setImage(this.originImage.getImage());
					this.show1.repaint();
					this.status.setText("ԭͼ���ļ���С��"+this.originBMP.length()+" Byte");
				}
				else
				{
					//������״̬����ʾ������Ϣ
					this.status.setText("��ʾ��ԭͼ���ļ�����BMP��ʽ��������ָ��");
					this.show1.setImage(null);
					this.originBMP=null;
					this.show1.repaint();
				}
				this.repaint();
			}
			else this.originBMP=null;
		}
		
		//���µ���ѡ��������Ϣ��ť
		if(((JButton)e.getSource()).equals(this.bFile2))  
		{		
			int retVal=jfc.showOpenDialog(this);
			if(this.isHideMode)		//�ж��ǲ�������ģʽ  ����ģʽ���ı��ļ������ò�ͬ
			{
				if (retVal==JFileChooser.APPROVE_OPTION)
				{
					this.originTxt=jfc.getSelectedFile();
					this.pathFile2.setText(this.originTxt.getAbsolutePath());
					this.status.setText("�������ļ���С��"+this.originTxt.length()+" Byte");
				}
				else this.originTxt=null;
			}
			else     //������ģʽ
			{
				if (retVal==JFileChooser.APPROVE_OPTION)
				{
					this.targetTxt=jfc.getSelectedFile();
					this.pathFile2.setText(this.targetTxt.getAbsolutePath());
				}
				else this.targetTxt=null;
			}
		}
		
		//���µ���ѡ��Ŀ��ͼ���ļ��İ�ť��ֻ������ģʽ����
		if(((JButton)e.getSource()).equals(this.bFile3)) 
		{		
			int retVal=jfc.showOpenDialog(this);
			if (retVal==JFileChooser.APPROVE_OPTION)
			{
				this.targetBMP=jfc.getSelectedFile();
				this.pathFile3.setText(this.targetBMP.getAbsolutePath());
			}
			else this.targetBMP=null;
		}
		
		//���µ����л�ģʽ��ť
		if(((JButton)e.getSource()).equals(this.bMode)) 
		{	
			//�л���������ģʽ
			if (this.isHideMode)
			{
				this.isHideMode=false;
				this.bFile1.setText("ѡ�����ͼ��");
				this.bFile2.setText("ѡ��Ŀ���ı�");
				this.bFile3.setEnabled(false);
				this.bFile3.setVisible(false);		//���ص�������ť
				this.tag3.setEnabled(false);
				this.tag3.setVisible(false);		//���ص�������ǩ
				this.pathFile1.setText("");
				this.pathFile2.setText("");
				this.pathFile3.setText("");
				this.pathFile3.setEnabled(false);
				this.pathFile3.setVisible(false);	//���ص�����·��
				this.bOp.setText("��ʼ��ȡ����");
				this.status.setText("��ǰģʽ����Ϣ��ȡģʽ");
				this.originBMP=null;
				this.originTxt=null;
				this.targetBMP=null;
				this.targetTxt=null;
				this.show2.setVisible(false);		//���صڶ���ͼƬ��
				this.show2.setImage(null);
				this.show1.setImage(null);
				this.show1.setBorder(new TitledBorder(new LineBorder(new Color(69,167,116)),"��������Ϣ��ͼ��",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,new Font("����",Font.PLAIN,12),new Color(117,60,27)));
				this.bOp.setBounds(70,200,120,80);
				this.bMode.setBounds(70,310,120,80);
				this.show1.setBounds(410,200,320,230);
				
				this.repaint();
				this.show1.repaint();
				this.show2.repaint();
			}
			else 			//������ģʽ
			{
				this.isHideMode=true;
				this.bFile1.setText("ѡ��ԭͼ��");
				this.bFile2.setText("ѡ��������Ϣ");
				this.bFile3.setEnabled(true);
				this.bFile3.setVisible(true);
				this.tag3.setEnabled(true);
				this.tag3.setVisible(true);
				this.pathFile1.setText("");
				this.pathFile2.setText("");
				this.pathFile3.setText("");
				this.pathFile3.setEnabled(true);
				this.pathFile3.setVisible(true);
				this.bOp.setText("��ʼ���ز���");
				this.status.setText("��ǰģʽ����Ϣ����ģʽ");
				this.originBMP=null;
				this.originTxt=null;
				this.targetBMP=null;
				this.targetTxt=null;
				this.show2.setVisible(true);
				this.show2.setImage(null);
				this.show1.setImage(null);
				this.show1.setBorder(new TitledBorder(new LineBorder(new Color(69,167,116)),"����ǰ��ͼ��",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,new Font("����",Font.PLAIN,12),Color.black));
				this.show2.setBorder(new TitledBorder(new LineBorder(new Color(69,167,116)),"���غ��ͼ��",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,new Font("����",Font.PLAIN,12),Color.black));
				this.show1.setBounds(250,200,320,230);
				this.bOp.setBounds(70,220,120,100);
				this.bMode.setBounds(70,340,120,80);
				this.repaint();
				this.show1.repaint();
				this.show2.repaint();
				
			}
		}
		
		//�����˿�ʼ��ť
		if(((JButton)e.getSource()).equals(this.bOp))  
		{
			//��ģʽ������ʲô������������ģʽ
			if (this.isHideMode)  
			{
				//�ж��Ƿ���� 
				if (this.originBMP==null || !this.originBMP.exists())
				{
					this.status.setText("��ʾ��ԭͼ���ļ������ڣ�������ָ��ԭͼ���ļ�");
					return;  //�ȿ�ԭͼ���ǲ��Ǵ���
				}
				if (!this.originBMP.getName().substring(this.originBMP.getName().length()-3).toLowerCase().equals("bmp"))
				{
					this.status.setText("��ʾ��ԭͼ���ļ�����BMP��ʽ��������ָ��");
					return;    //�ٿ�ԭͼ���ǲ���bmp�ļ�
				}
				//��Դ�ı��ǲ��Ǵ���
				if (this.originTxt==null || !this.originTxt.exists())
				{
					this.status.setText("��ʾ���������ı��ļ������ڣ�������ָ��");
					return;  
				}
				//��Ŀ��ͼ���ǲ����Ѿ�ָ��
				if (this.targetBMP==null)
				{
					this.status.setText("��ʾ����ָ��Ŀ��ͼ���ļ�");
					return;	    
				}
				//��Ŀ��ͼ���ǲ���BMP
				if (!this.targetBMP.getName().substring(this.targetBMP.getName().length()-3).toLowerCase().equals("bmp"))
				{
					this.status.setText("��ʾ������BMP��ʽ��������ָ��Ŀ��ͼ���ļ�");
					return;
				}
				
				//�������ز���
				int result=InfoHiding.hide(this.originBMP,this.originTxt,this.targetBMP);
				
				//�鿴������Ϣ�����ݷ��ص�ֵ��ȷ�����������ǲ��ǳɹ�������ʲô����
				switch (result)
				{
				case 0: 
					this.status.setText("�ɹ����������Ϣ���ز���");
					this.show2.setImage(new BMP(new File(this.pathFile3.getText())).getImage());
					this.show2.repaint();
					break;
				case 1: 
					this.status.setText("��ʾ��Դ�ļ���Ŀ���ļ�������ͬһ���ļ���������ָ��");
					break;
				case 23:
					this.status.setText("��ʾ:���ܴ�Ŀ���ļ���������ָ��Ŀ���ļ�");
					break;
				case 28: 
					this.status.setText("��ʾ:��ȡԭͼ��ͷ����Ϣʧ�ܣ�������ָ��ԭͼ��");
					break;
				case 31: 
					this.status.setText("��ʾ:ԭͼ����BMP�ļ���������ָ��ԭͼ��");
					break;
				case 32: 
					this.status.setText("��ʾ:ԭͼ���ļ��Ѱ���������Ϣ��������ָ��ԭͼ��");
					break;
				case 51: 
					this.status.setText("��ʾ:ԭͼ����������������ָ���ļ���������ָ��ԭͼ��");
					break;
				case 33: 
					this.status.setText("��ʾ:ԭͼ�񾭹�ѹ����������ָ��ԭͼ��");
					break;
				case 34: 
					this.status.setText("��ʾ:��ɫλ������֧�ַ�Χ��������ָ��ԭͼ��");
					break;
					
				default:
					this.status.setText("����ţ�"+result);
					break;
				}
				
				this.originBMP=null;
				this.pathFile1.setText("");
				this.originTxt=null;
				this.pathFile2.setText("");
				this.targetBMP=null;
				this.pathFile3.setText(""); //������ɺ��������Ҫ����
				
			}
			//��ȡ����
			else    
			{
				if (this.originBMP==null || !this.originBMP.exists())
				{
					this.status.setText("��ʾ��ԭͼ���ļ������ڣ�������ָ��ԭͼ���ļ�");
					return;
				}
				if (!this.originBMP.getName().substring(this.originBMP.getName().length()-3).toLowerCase().equals("bmp"))
				{
					this.status.setText("��ʾ��Ŀ��ͼ����BMP��ʽ��������ָ��ԭͼ���ļ�");
					return;
				}
				if (this.targetTxt==null)
				{
					this.status.setText("��ʾ����ָ��Ŀ���ı��ļ�");
					return;
				}
				int result=InfoGetting.get(this.originBMP,this.targetTxt);
				switch (result)
				{
				case 0: 
					this.status.setText("�ɹ�:�������Ϣ��ȡ����");
					break;
				case 1: 
					this.status.setText("��ʾ��Դ�ļ���Ŀ���ļ�������ͬһ���ļ���������ָ��");
					break;
				case 28: 
					this.status.setText("��ʾ:��ȡԭͼ��ͷ����Ϣʧ�ܣ�������ָ��ԭͼ��");
					break;
				case 31: 
					this.status.setText("��ʾ:ԭͼ����BMP�ļ���������ָ��ԭͼ��");
					break;
				case 32: 
					this.status.setText("��ʾ:ԭͼ���ļ�������������Ϣ��������ָ��ԭͼ��");
					break;
				case 33: 
					this.status.setText("��ʾ:ԭͼ�񾭹�ѹ����������ָ��ԭͼ��");
					break;
				case 34: 
					this.status.setText("��ʾ:��ɫλ������֧�ַ�Χ��������ָ��ԭͼ��");
					break;	
				case 51: 
					this.status.setText("��ʾ:ԭͼ����������������ָ���ļ���������ָ��ԭͼ��");
					break;
				case 23:
					this.status.setText("��ʾ:���ܴ�Ŀ���ļ���������ָ��Ŀ���ļ�");
					break;
				case 43:
					this.status.setText("��ʾ:��Ŀ���ļ�д��ʱ��������");
					break;
				default:
					this.status.setText("����ţ�"+result);
					break;
				}	
				this.originBMP=null;
				this.pathFile1.setText("");
				this.targetTxt=null;
				this.pathFile2.setText("");
				this.pathFile3.setText("");
				
			}	
		}
	}
	
	//���Ʊ���
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(im,0,0,800,500,this);   
	}
}


