package hideInfo;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.*;

public class ShowPanel extends JPanel {
	
	//显示载入的图片
	private Image image;

	public ShowPanel() {
		super();	
	}
	
	public void setImage(Image image)
	{
		this.image=image;	
	}
	
	public void paintComponent(Graphics gg)
	{
		super.paintComponent(gg);
		if(this.image!=null) {
			int w=this.image.getWidth(this);
			int h=this.image.getHeight(this);
			if (w<=0 && h<=0 ) return;
			if (w>200 || h>200) 
			{
				if(w>h)
				{
					double tmp=((double)w)/270;
					h=(int) (h/tmp);
					w=270;  //把图像按照比例缩小到可以放在Panel中显示
				}
				else 
				{
					double tmp=((double)h)/210;
					w=(int) (w/tmp);
					h=210;
				}
			}
			gg.drawImage(this.image,(320-w)/2,(230-h)/2,w,h,this);	//绘图
		}
	
	}
	
}
