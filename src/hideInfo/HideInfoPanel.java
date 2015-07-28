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

//窗口中的主Panel,处理所有消息,安放按钮等控件
public class HideInfoPanel extends JPanel implements ActionListener
{	
	private boolean isHideMode;		//两种模式：隐藏信息和提取信息， true代表隐藏模式
	private MediaTracker mt;		//用于监控多媒体对象的类
	private JFileChooser jfc;		//文件选择器
	private Image im;				//图像对象
	private JButton bFile1;			//按钮：选择原图像按钮
	private JButton bFile2;			//按钮：选择源文本文件按钮
	private JButton bFile3;			//按钮：选择目的图像
	private JButton bMode;			//模式切换按钮
	private JButton bOp;			//进行处理（在隐藏模式下启动隐藏操作，提取模式下提取操作）	
	private File originBMP;			//原图像文件对象
	private File targetBMP;			//目的图像文件对象
	private File originTxt;			//源文本文件对象
	private File targetTxt;			//目的文本文件对象
	private BMP originImage;		//第三方类把BMP文件转换为Image对象，因为JAVA不直接支持载入BMP文件。隐藏和提取时不用它，因为直接把BMP文件当作文件来处理，不需要转换为Image对象
	JLabel tag1,tag2,tag3;   		//三个显示“路径”字样的标签
	JLabel status;					//最下方提示栏
	JTextField pathFile1,pathFile2,pathFile3;	//显示上面三个文件的路径 在Panel的右上方
	ShowPanel show1,show2;			//显示两个BMP图像 （Panel的右下）

	public HideInfoPanel()
	{
		//初始化
		this.isHideMode=true;  		//默认隐藏模式
		this.setLayout(null);		//使用坐标方式指定各个按钮的位置
		this.mt=new MediaTracker(this);  
		jfc=new JFileChooser();  	//文件选择器
		this.im=Toolkit.getDefaultToolkit().getImage("resource/background.jpg");//载入背景	
		mt.addImage(this.im,1);		//把Image类的对象image添加到当前媒体跟踪器要追踪的图像列表中，整型对象id表示该image的标识
		try
 		{
 			mt.waitForID(1);		//跟踪多媒体文件
 		}
 		catch (InterruptedException e)
 		{
 		}
 		this.setVisible(true);
		
		this.bFile1=new JButton("选择原图像");		//显示的字样 （按钮）
		this.bFile1.setBounds(70,75,120,35);		//位置和大小
		this.bFile1.setFont(new Font("宋体",Font.BOLD,12));		//字体
		this.bFile1.setOpaque(true); 				//透明
		this.bFile1.setForeground(Color.black);   	//字体颜色
		this.bFile1.setBackground(new Color(240,255,240));  //按钮背景色
		this.bFile1.addActionListener(this); 		//如果这个按钮被按动，事件将被发送到本类下面的处理函数中
		this.add(bFile1);
		
		this.bFile2=new JButton("选择隐藏信息");
		this.bFile2.setBounds(70,120,120,35);
		this.bFile2.setFont(new Font("宋体",Font.BOLD,12));
		this.bFile2.setOpaque(true);
		this.bFile2.setForeground(Color.black);
		this.bFile2.setBackground(new Color(240,255,240));
		this.bFile2.addActionListener(this);
		this.add(bFile2);
		
		this.bFile3=new JButton("选择目标图像");
		this.bFile3.setBounds(70,165,120,35);
		this.bFile3.setFont(new Font("宋体",Font.BOLD,12));
		this.bFile3.setOpaque(true);
		this.bFile3.setForeground(Color.black);
		this.bFile3.setBackground(new Color(240,255,240));
		this.bFile3.addActionListener(this);	
		this.add(bFile3);
		
		this.bOp=new JButton("开始隐藏");
		this.bOp.setBounds(70,220,120,100);
		this.bOp.setFont(new Font("宋体",Font.BOLD,12));
		this.bOp.setOpaque(true);
		this.bOp.setForeground(Color.black);
		this.bOp.setBackground(new Color(240,255,240));
		this.bOp.addActionListener(this);
		this.add(bOp);
		
		this.bMode=new JButton("切换模式");
		this.bMode.setBounds(70,340,120,80);
		this.bMode.setFont(new Font("宋体",Font.BOLD,12));
		this.bMode.setOpaque(true);
		this.bMode.setForeground(Color.black);
		this.bMode.setBackground(new Color(240,255,240));
		this.bMode.addActionListener(this);
		this.add(bMode);
		
		this.tag1=new JLabel("路径:");
		this.tag1.setBounds(250,75,40,35);
		this.tag1.setFont(new Font("宋体",Font.PLAIN,14));
		this.tag1.setForeground(Color.black);
		this.add(this.tag1);
		
		this.tag2=new JLabel("路径:");
		this.tag2.setBounds(250,120,40,35);
		this.tag2.setFont(new Font("宋体",Font.PLAIN,14));
		this.tag2.setForeground(Color.black);
		this.add(this.tag2);
		
		this.tag3=new JLabel("路径:");
		this.tag3.setBounds(250,165,40,35);
		this.tag3.setFont(new Font("宋体",Font.PLAIN,14));
		this.tag3.setForeground(Color.black);
		this.add(this.tag3);
		
		
		this.status=new JLabel("当前模式：信息隐藏模式");
		this.status.setBounds(490,450,680,30);
		this.status.setHorizontalAlignment(SwingConstants.LEFT);  //左对齐
		this.status.setFont(new Font("宋体",Font.ITALIC,16));
		this.status.setForeground(Color.black);
		this.status.setOpaque(false);
		this.add(this.status);
		
		this.pathFile1=new JTextField(100);
		this.pathFile1.setBounds(300,75,490,35);
		this.pathFile1.setFont(new Font("宋体",Font.PLAIN,12));
		this.pathFile1.setEditable(false);				//不可编辑
		this.pathFile1.setOpaque(false);				//透明
		this.pathFile1.setBorder(null);					//无边框
		this.add(this.pathFile1);
		
		this.pathFile2=new JTextField(100);
		this.pathFile2.setBounds(300,120,490,35);
		this.pathFile2.setFont(new Font("宋体",Font.PLAIN,12));
		this.pathFile2.setEditable(false);
		this.pathFile2.setOpaque(false);
		this.pathFile2.setBorder(null);
		this.add(this.pathFile2);
		
		this.pathFile3=new JTextField(100);
		this.pathFile3.setBounds(300,165,490,35);
		this.pathFile3.setFont(new Font("宋体",Font.PLAIN,12));
		this.pathFile3.setEditable(false);
		this.pathFile3.setOpaque(false);
		this.pathFile3.setBorder(null);
		this.add(this.pathFile3);
		
		this.show1=new ShowPanel();
		this.show1.setBorder(new TitledBorder(new LineBorder(new Color(69,167,116)),"隐藏前的图像",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,new Font("宋体",Font.PLAIN,12),Color.black));
		this.show1.setBounds(250,200,320,230);
		this.show1.setBackground(Color.WHITE);
		this.show1.setOpaque(true);
		this.add(this.show1);
		show1.repaint();
		
		this.show2=new ShowPanel();
		this.show2.setBorder(new TitledBorder(new LineBorder(new Color(69,167,116)),"隐藏后的图像",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,new Font("宋体",Font.PLAIN,12),Color.black));  //带有标题的边框
		this.show2.setBounds(600,200,320,230);
		this.show2.setBackground(Color.WHITE);
		this.show2.setOpaque(true);
		this.add(this.show2);
		show2.repaint();
		
		this.repaint();
	}
	
	//这个函数负责处理所有按钮被按动所产生的事件
	public void actionPerformed(ActionEvent e) {
		
		//按动的按钮是选择原图像按钮 
		if(((JButton)e.getSource()).equals(this.bFile1))  
		{		
			int retVal=jfc.showOpenDialog(this);  //打开文件选择器
			if (retVal==JFileChooser.APPROVE_OPTION)
			{
				this.originBMP=jfc.getSelectedFile();//选定文件
				this.pathFile1.setText(this.originBMP.getAbsolutePath());//显示它的路径
				if(this.originBMP.getName().endsWith(".BMP")||this.originBMP.getName().endsWith(".bmp"))
				{	//如果是.bmp文件 则显示它
					this.originImage=new BMP(this.originBMP);
					this.show1.setImage(this.originImage.getImage());
					this.show1.repaint();
					this.status.setText("原图像文件大小："+this.originBMP.length()+" Byte");
				}
				else
				{
					//否则在状态栏显示错误信息
					this.status.setText("提示：原图像文件不是BMP格式，请重新指定");
					this.show1.setImage(null);
					this.originBMP=null;
					this.show1.repaint();
				}
				this.repaint();
			}
			else this.originBMP=null;
		}
		
		//按下的是选择隐藏信息按钮
		if(((JButton)e.getSource()).equals(this.bFile2))  
		{		
			int retVal=jfc.showOpenDialog(this);
			if(this.isHideMode)		//判断是不是隐藏模式  两个模式下文本文件的作用不同
			{
				if (retVal==JFileChooser.APPROVE_OPTION)
				{
					this.originTxt=jfc.getSelectedFile();
					this.pathFile2.setText(this.originTxt.getAbsolutePath());
					this.status.setText("待隐藏文件大小："+this.originTxt.length()+" Byte");
				}
				else this.originTxt=null;
			}
			else     //解隐藏模式
			{
				if (retVal==JFileChooser.APPROVE_OPTION)
				{
					this.targetTxt=jfc.getSelectedFile();
					this.pathFile2.setText(this.targetTxt.getAbsolutePath());
				}
				else this.targetTxt=null;
			}
		}
		
		//按下的是选择目标图像文件的按钮，只有隐藏模式有用
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
		
		//按下的是切换模式按钮
		if(((JButton)e.getSource()).equals(this.bMode)) 
		{	
			//切换到解隐藏模式
			if (this.isHideMode)
			{
				this.isHideMode=false;
				this.bFile1.setText("选择解隐图像");
				this.bFile2.setText("选择目的文本");
				this.bFile3.setEnabled(false);
				this.bFile3.setVisible(false);		//隐藏第三个按钮
				this.tag3.setEnabled(false);
				this.tag3.setVisible(false);		//隐藏第三个标签
				this.pathFile1.setText("");
				this.pathFile2.setText("");
				this.pathFile3.setText("");
				this.pathFile3.setEnabled(false);
				this.pathFile3.setVisible(false);	//隐藏第三个路径
				this.bOp.setText("开始提取操作");
				this.status.setText("当前模式：信息提取模式");
				this.originBMP=null;
				this.originTxt=null;
				this.targetBMP=null;
				this.targetTxt=null;
				this.show2.setVisible(false);		//隐藏第二个图片框
				this.show2.setImage(null);
				this.show1.setImage(null);
				this.show1.setBorder(new TitledBorder(new LineBorder(new Color(69,167,116)),"含隐藏信息的图像",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,new Font("宋体",Font.PLAIN,12),new Color(117,60,27)));
				this.bOp.setBounds(70,200,120,80);
				this.bMode.setBounds(70,310,120,80);
				this.show1.setBounds(410,200,320,230);
				
				this.repaint();
				this.show1.repaint();
				this.show2.repaint();
			}
			else 			//解隐藏模式
			{
				this.isHideMode=true;
				this.bFile1.setText("选择原图像");
				this.bFile2.setText("选择隐藏信息");
				this.bFile3.setEnabled(true);
				this.bFile3.setVisible(true);
				this.tag3.setEnabled(true);
				this.tag3.setVisible(true);
				this.pathFile1.setText("");
				this.pathFile2.setText("");
				this.pathFile3.setText("");
				this.pathFile3.setEnabled(true);
				this.pathFile3.setVisible(true);
				this.bOp.setText("开始隐藏操作");
				this.status.setText("当前模式：信息隐藏模式");
				this.originBMP=null;
				this.originTxt=null;
				this.targetBMP=null;
				this.targetTxt=null;
				this.show2.setVisible(true);
				this.show2.setImage(null);
				this.show1.setImage(null);
				this.show1.setBorder(new TitledBorder(new LineBorder(new Color(69,167,116)),"隐藏前的图像",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,new Font("宋体",Font.PLAIN,12),Color.black));
				this.show2.setBorder(new TitledBorder(new LineBorder(new Color(69,167,116)),"隐藏后的图像",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,new Font("宋体",Font.PLAIN,12),Color.black));
				this.show1.setBounds(250,200,320,230);
				this.bOp.setBounds(70,220,120,100);
				this.bMode.setBounds(70,340,120,80);
				this.repaint();
				this.show1.repaint();
				this.show2.repaint();
				
			}
		}
		
		//按下了开始按钮
		if(((JButton)e.getSource()).equals(this.bOp))  
		{
			//由模式来决定什么操作这是隐藏模式
			if (this.isHideMode)  
			{
				//判断是否存在 
				if (this.originBMP==null || !this.originBMP.exists())
				{
					this.status.setText("提示：原图像文件不存在，请重新指定原图像文件");
					return;  //先看原图像是不是存在
				}
				if (!this.originBMP.getName().substring(this.originBMP.getName().length()-3).toLowerCase().equals("bmp"))
				{
					this.status.setText("提示：原图像文件不是BMP格式，请重新指定");
					return;    //再看原图像是不是bmp文件
				}
				//看源文本是不是存在
				if (this.originTxt==null || !this.originTxt.exists())
				{
					this.status.setText("提示：待隐藏文本文件不存在，请重新指定");
					return;  
				}
				//看目的图像是不是已经指定
				if (this.targetBMP==null)
				{
					this.status.setText("提示：请指定目标图像文件");
					return;	    
				}
				//看目的图像是不是BMP
				if (!this.targetBMP.getName().substring(this.targetBMP.getName().length()-3).toLowerCase().equals("bmp"))
				{
					this.status.setText("提示：不是BMP格式，请重新指定目的图像文件");
					return;
				}
				
				//进行隐藏操作
				int result=InfoHiding.hide(this.originBMP,this.originTxt,this.targetBMP);
				
				//查看返回信息，根据返回的值来确定程序运行是不是成功，发生什么错误
				switch (result)
				{
				case 0: 
					this.status.setText("成功：已完成信息隐藏操作");
					this.show2.setImage(new BMP(new File(this.pathFile3.getText())).getImage());
					this.show2.repaint();
					break;
				case 1: 
					this.status.setText("提示：源文件和目标文件不能是同一个文件，请重新指定");
					break;
				case 23:
					this.status.setText("提示:不能打开目的文件，请重新指定目的文件");
					break;
				case 28: 
					this.status.setText("提示:读取原图像头部信息失败，请重新指定原图像");
					break;
				case 31: 
					this.status.setText("提示:原图像不是BMP文件，请重新指定原图像");
					break;
				case 32: 
					this.status.setText("提示:原图像文件已包含隐藏信息，请重新指定原图像");
					break;
				case 51: 
					this.status.setText("提示:原图像容量不足以隐藏指定文件，请重新指定原图像");
					break;
				case 33: 
					this.status.setText("提示:原图像经过压缩，请重新指定原图像");
					break;
				case 34: 
					this.status.setText("提示:颜色位数不在支持范围，请重新指定原图像");
					break;
					
				default:
					this.status.setText("错误号："+result);
					break;
				}
				
				this.originBMP=null;
				this.pathFile1.setText("");
				this.originTxt=null;
				this.pathFile2.setText("");
				this.targetBMP=null;
				this.pathFile3.setText(""); //操作完成后清除不必要的量
				
			}
			//提取操作
			else    
			{
				if (this.originBMP==null || !this.originBMP.exists())
				{
					this.status.setText("提示：原图像文件不存在，请重新指定原图像文件");
					return;
				}
				if (!this.originBMP.getName().substring(this.originBMP.getName().length()-3).toLowerCase().equals("bmp"))
				{
					this.status.setText("提示：目标图像不是BMP格式，请重新指定原图像文件");
					return;
				}
				if (this.targetTxt==null)
				{
					this.status.setText("提示：请指定目标文本文件");
					return;
				}
				int result=InfoGetting.get(this.originBMP,this.targetTxt);
				switch (result)
				{
				case 0: 
					this.status.setText("成功:已完成信息提取操作");
					break;
				case 1: 
					this.status.setText("提示：源文件和目标文件不能是同一个文件，请重新指定");
					break;
				case 28: 
					this.status.setText("提示:读取原图像头部信息失败，请重新指定原图像");
					break;
				case 31: 
					this.status.setText("提示:原图像不是BMP文件，请重新指定原图像");
					break;
				case 32: 
					this.status.setText("提示:原图像文件不包含隐藏信息，请重新指定原图像");
					break;
				case 33: 
					this.status.setText("提示:原图像经过压缩，请重新指定原图像");
					break;
				case 34: 
					this.status.setText("提示:颜色位数不在支持范围，请重新指定原图像");
					break;	
				case 51: 
					this.status.setText("提示:原图像容量不足以隐藏指定文件，请重新指定原图像");
					break;
				case 23:
					this.status.setText("提示:不能打开目的文件，请重新指定目的文件");
					break;
				case 43:
					this.status.setText("提示:向目的文件写入时发生错误");
					break;
				default:
					this.status.setText("错误号："+result);
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
	
	//绘制背景
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(im,0,0,800,500,this);   
	}
}


