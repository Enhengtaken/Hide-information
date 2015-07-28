package hideInfo;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


class InfoHiding {
	BmpHeader bh;        	//bmp文件头,里面很多信息
	File originalBMP;		//文件
	FileInputStream fs;		//从源图像中读取内容的流
	Image origBMPImage;		//源图像对象
	byte[] originalImageData;   //源图像被读入内存后得到的byte序列
	
	File originalText;
	FileInputStream fs2;
	byte[] originalTextData;
	String originalTextString;  //与上面类似 名字很有规律 

	File targetBMP;
	FileOutputStream fos;
	byte[] targetImageData;
	
	int forReserved;   //暂存文本的长度
	
	//把源图像文件对象读入
	public void setOriginalBMP(File origin) throws FileNotFoundException  
	{
		this.originalBMP=origin;
		this.fs=new FileInputStream(this.originalBMP);
	}
	
	public void getBMPHeaderInfo() throws IOException
	{
		this.bh=new BmpHeader();	
		bh.getHeaderInfo(this.fs);
	}
	
	//读入源文本文件，同时压缩一下
	public void setOriginalText(File origin) throws Exception  
	{
		File tmpInZip=new File("c:/tmpIn.zip");
		if (tmpInZip.exists()) tmpInZip.delete();
		//创建文件输入流对象 
		FileInputStream in = new FileInputStream(origin ); 
		//创建文件输出流对象
		FileOutputStream out = new FileOutputStream( "c:/tmpIn.zip" ); 
		//创建ZIP数据输出流对象 
		ZipOutputStream zipOut = new ZipOutputStream( out ); 
		//创建指向压缩原始文件的入口
		ZipEntry entry = new ZipEntry(origin.getName());
		zipOut.putNextEntry( entry );
		//向压缩文件中输出数据
		int nNumber; 
		byte[] buffer = new byte[4096]; 
		while ((nNumber=in.read(buffer)) != -1) 
		zipOut.write(buffer,0,nNumber); 
		//关闭创建的流对象
		zipOut.close(); 
		out.close(); 
		in.close(); 
		this.originalText=new File("c:/tmpIn.zip");
		this.fs2=new FileInputStream(this.originalText);   
	}
	
	//设定目标图片对象
	public void setTargetBMP(File target) throws FileNotFoundException 
	{
		this.targetBMP=target;
		this.fos=new FileOutputStream(this.targetBMP);
	}
	
    //读取16bit制式图像内容
	public void readMap16() throws IOException     
	{
		int len=(int) (this.originalBMP.length()-this.bh.ndataOffset);
		this.originalImageData= new byte[len];
		//System.out.println("this.originalImageData.length= "+this.originalImageData.length);
		//System.out.println("(file.length-54)= "+(this.originalBMP.length()-54));     
        this.fs.skip(this.bh.ndataOffset-54);  //jump to the data position
        //System.out.println("bh.ndataOffset-54＝ "+(bh.ndataOffset-54));
        this.fs.read(this.originalImageData,0,len);    
        fs.close();
	}
	
	
	
	//读取24bit制式图像内容
	public void readMap24() throws IOException
	{
		int len=(int) (this.originalBMP.length()-this.bh.ndataOffset);
		//System.out.println("this.originalBMP.length()-this.bh.ndataOffset= "+len);
		int ndata[] = new int[len];
        this.originalImageData= new byte[len];
        //System.out.println("this.originalImageData.length= "+this.originalImageData.length);
        //System.out.println("(file.length-54)= "+(this.originalBMP.length()-54));
        this.fs.skip(this.bh.ndataOffset-54);  //jump to the data position
        //System.out.println("bh.ndataOffset-54＝ "+(bh.ndataOffset-54));
        this.fs.read(this.originalImageData,0,len);
        fs.close();
	}
	
	//读取32bit制式图像内容
	public void readMap32() throws IOException
	{
		int len=(int) (this.originalBMP.length()-this.bh.ndataOffset);
		//System.out.println("this.originalBMP.length()-this.bh.ndataOffset= "+len);
		int ndata[] = new int[len];
        this.originalImageData= new byte[len];
        //System.out.println("this.originalImageData.length= "+this.originalImageData.length);
        //System.out.println("(file.length-54)= "+(this.originalBMP.length()-54));
        this.fs.skip(this.bh.ndataOffset-54);  //jump to the data position
        //System.out.println("bh.ndataOffset-54＝ "+(bh.ndataOffset-54));
        this.fs.read(this.originalImageData,0,len);
        fs.close();
             
	}
	
	/**
	 * 	read the 8bit bmp file into byte[] and then into Image object
	 * 	attention : since the 8bit bmp use the index in the main data region
	 *  			it is proper to read just the platte area and hide the info in the platte 
	 *  			unfortunately only a max number of 3*256*2/8=192B could be hide so the 8 bit solution will be neglect here             
	 */	 
	public void readMap8() throws IOException 
	{
		int nNumColors = 0;//determing the number of colors
        if (bh.nclrused > 0) nNumColors = bh.nclrused;
        else nNumColors = (1 & 0xff) << bh.nbitcount;

        System.out.println("The number of Colors is"+nNumColors);

        if (bh.nsizeimage == 0)
        {
            bh.nsizeimage = ((((bh.nwidth * bh.nbitcount) + 31) & ~31) >> 3);
            bh.nsizeimage *= bh.nheight;
            //          System.out.println("nsizeimage (backup) is"+nsizeimage);
        }
        
        //      Read the palatte colors.
        int npalette[] = new int[nNumColors];
        byte bpalette[] = new byte[nNumColors * 4];
        fs.read(bpalette, 0, nNumColors * 4);
        int nindex8 = 0;
        for (int n = 0; n < nNumColors; n++)
        {
            npalette[n] = Utility.constructInt3(bpalette, nindex8);
            nindex8 += 4;
        }
        //Read the image data (actually indices into the palette)
        // Scan lines are still padded out to even 4-byte boundaries.
        int npad8 = (bh.nsizeimage / bh.nheight) - bh.nwidth;
        //    System.out.println("nPad is:"+npad8);
        int ndata8[] = new int[bh.nwidth * bh.nheight];
        this.originalImageData = new byte[(bh.nwidth + npad8) * bh.nheight];
        
        fs.read(this.originalImageData, 0, (bh.nwidth + npad8) * bh.nheight);
        
        nindex8 = 0;
        for (int j8 = 0; j8 < bh.nheight; j8++)
        {
            for (int i8 = 0; i8 < bh.nwidth; i8++)
            {
                ndata8[bh.nwidth * (bh.nheight - j8 - 1) + i8] =npalette[((int) this.originalImageData[nindex8] & 0xff)];
                nindex8++;
            }
            nindex8 += npad8;
        }
        this.origBMPImage = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(bh.nwidth, bh.nheight,ndata8, 0, bh.nwidth));        
	}
	
///////////////////////////////////////////////////////////////////////////
	
	//把文件内容读入内存，到byte[]中
	public void getText() throws IOException  
	{
		//System.out.print("Lenth of text: "+this.originalText.length()+"\n");
		int len= (int) this.originalText.length();
		this.forReserved=len;
		this.originalTextData=new byte[len+512];
		this.fs2.read(this.originalTextData,0,len);
		String name=this.originalText.getName();
		byte[] bname=name.getBytes();
		Utility.extractInt(bname.length,this.originalTextData,len);
		for (int i=0;i<bname.length;i++) this.originalTextData[len+4+i]=bname[i];
		this.fs2.close();   	
	}
	
	//判断是否足够容下文本文件
	public boolean isAdequate16()   
	{
		long bitTextLen=this.originalText.length()*8;
		long bitSpace=this.originalImageData.length/4;
		return (bitSpace>bitTextLen+512);
	}
	
	//判断是否足够容下文本文件
	public boolean isAdequate24()
	{
		long bitTextLen=this.originalText.length()*8;
		long bitSpace=this.originalImageData.length*2;
		return (bitSpace>bitTextLen+512);
		
	}
	
	//判断是否足够容下文本文件
	public boolean isAdequate32()
	{
		long bitTextLen=this.originalText.length()*8;
		long bitSpace=this.originalImageData.length*2;
		return (bitSpace>bitTextLen+512);
		
	}
	
	
	/**
	 * To hide info into 16bit bmp file
	 * must do pre-work: setBMP and setTExt and isAdequate and getMap16 and setTargetBMP;
	 * hide 1bit in 2byte (1pixel)
	 *
	 */
	//原理： 就是读出一个象素，拆开文本文件为一个一个bit，藏到每个象素的最低bit，下面的都一样 
	public void hideInfo16() 
	{
		this.targetImageData=new byte[this.originalImageData.length];
		for (int i=0;i<this.originalImageData.length;i++) this.targetImageData[i]=this.originalImageData[i];
		
		int countPixel=0;
		int countText=0;
		while(countText<this.originalTextData.length)
		{
			byte textByte=this.originalTextData[countText];
			byte tmpText;
			byte tmpPixel;
			
			tmpPixel=(byte) (this.originalImageData[countPixel] & 0x7f);  
			tmpText=(byte) ((textByte & 0x01)<<7);
			this.targetImageData[countPixel]=(byte) (tmpPixel + tmpText);
			countPixel++;
			countPixel++;
			
			tmpPixel=(byte) (this.originalImageData[countPixel] & 0x7f);  
			tmpText=(byte) ((textByte & 0x02)<<6);
			this.targetImageData[countPixel]=(byte) (tmpPixel + tmpText);
			countPixel++;
			countPixel++;
			
			tmpPixel=(byte) (this.originalImageData[countPixel] & 0x7f);  
			tmpText=(byte) ((textByte & 0x04)<<5);
			this.targetImageData[countPixel]=(byte) (tmpPixel + tmpText);
			countPixel++;
			countPixel++;
			
			tmpPixel=(byte) (this.originalImageData[countPixel] & 0x7f);  
			tmpText=(byte) ((textByte & 0x08)<<4);
			this.targetImageData[countPixel]=(byte) (tmpPixel + tmpText);
			countPixel++;
			countPixel++;
			
			tmpPixel=(byte) (this.originalImageData[countPixel] & 0x7f);  
			tmpText=(byte) ((textByte & 0x10)<<3);
			this.targetImageData[countPixel]=(byte) (tmpPixel + tmpText);
			countPixel++;
			countPixel++;
			
			tmpPixel=(byte) (this.originalImageData[countPixel] & 0x7f);  
			tmpText=(byte) ((textByte & 0x20)<<2);
			this.targetImageData[countPixel]=(byte) (tmpPixel + tmpText);
			countPixel++;
			countPixel++;
			
			tmpPixel=(byte) (this.originalImageData[countPixel] & 0x7f);  
			tmpText=(byte) ((textByte & 0x40)<<1);
			this.targetImageData[countPixel]=(byte) (tmpPixel + tmpText);
			countPixel++;
			countPixel++;
			
			tmpPixel=(byte) (this.originalImageData[countPixel] & 0x7f);  
			tmpText=(byte) (textByte & 0x80);
			this.targetImageData[countPixel]=(byte) (tmpPixel + tmpText);
			countPixel++;
			countPixel++;
			
			countText++;
		}
		Utility.extractInt(this.forReserved,this.bh.bf,6);
	}
	

	
	
	/**
	 * To hide info into 24bit bmp file
	 * must do pre-work: setBMP and setTExt and isAdequate and getMap24 and setTargetBMP;
	 *
	 */
	public void hideInfo24()
	{
		this.targetImageData=new byte[this.originalImageData.length];
		for (int i=0;i<this.originalImageData.length;i++) this.targetImageData[i]=this.originalImageData[i];

		int countPixel=0;
		int countText=0;
		while(countText<this.originalTextData.length)
		{
			byte textByte=this.originalTextData[countText];
			byte tmpText;
			byte tmpPixel;
			
			//System.out.print("lenth="+this.originalImageData.length);
			tmpPixel=(byte) (this.originalImageData[countPixel] & 0xfc);  
			tmpText=(byte) (textByte & 0x03);
			this.targetImageData[countPixel]=(byte) (tmpPixel + tmpText);
			countPixel++;
			
			tmpPixel=(byte) (this.originalImageData[countPixel] & 0xfc);  
			tmpText=(byte) ((textByte & 0x0c)>>2);
			this.targetImageData[countPixel]=(byte) (tmpPixel + tmpText);
			countPixel++;
			
			tmpPixel=(byte) (this.originalImageData[countPixel] & 0xfc);   
			tmpText=(byte) ((textByte & 0x30)>>4);
			this.targetImageData[countPixel]=(byte) (tmpPixel + tmpText);
			countPixel++;
			
			tmpPixel=(byte) (this.originalImageData[countPixel] & 0xfc);    
			tmpText=(byte) ((textByte & 0xc0)>>6);
			this.targetImageData[countPixel]=(byte) (tmpPixel + tmpText);
			countPixel++;	
			countText++;
		}
		Utility.extractInt(this.forReserved,this.bh.bf,6);
	}
	
	
	/**
	 * To hide info into 32bit bmp file
	 * must do pre-work: setBMP and setTExt and isAdequate and getMap32 and setTargetBMP;
	 *
	 */
	public void hideInfo32()
	{
		this.targetImageData=new byte[this.originalImageData.length];
		for (int i=0;i<this.originalImageData.length;i++) this.targetImageData[i]=this.originalImageData[i];

		int countPixel=0;
		int countText=0;
		while(countText<this.originalTextData.length)
		{
			byte textByte=this.originalTextData[countText];
			byte tmpText;
			byte tmpPixel;
			
			tmpPixel=(byte) (this.originalImageData[countPixel] & 0xfc);  
			tmpText=(byte) (textByte & 0x03);
			this.targetImageData[countPixel]=(byte) (tmpPixel + tmpText);
			countPixel++;
			
			tmpPixel=(byte) (this.originalImageData[countPixel] & 0xfc);  
			tmpText=(byte) ((textByte & 0x0c)>>2);
			this.targetImageData[countPixel]=(byte) (tmpPixel + tmpText);
			countPixel++;
			
			tmpPixel=(byte) (this.originalImageData[countPixel] & 0xfc);   
			tmpText=(byte) ((textByte & 0x30)>>4);
			this.targetImageData[countPixel]=(byte) (tmpPixel + tmpText);
			countPixel++;
			
			tmpPixel=(byte) (this.originalImageData[countPixel] & 0xfc);    
			tmpText=(byte) ((textByte & 0xc0)>>6);
			this.targetImageData[countPixel]=(byte) (tmpPixel + tmpText);
			countPixel++;	
			countText++;
		}
		Utility.extractInt(this.forReserved,this.bh.bf,6);
	}
	
	//写目的图像文件
	public void writeTargetBMP() throws IOException
	{
		fos.write(this.bh.bf,0,14);
		fos.write(this.bh.bi,0,40);
		fos.write(this.targetImageData,0,this.targetImageData.length);	
		fos.flush();
		fos.close();

		//System.out.println("this.originalImageData.length="+this.originalImageData.length);
		//System.out.println("targetFile.length-54= "+(this.targetBMP.length()-54));
	}
	
	/**
	 * the operation method for outside
	 * Do no check to the File object transferred in
	 */
	public static int hide(File originalBMP , File originalTxt, File targetBMP )//这个函数被外界调用，前面的函数都不被外界用 
	{
		if (originalBMP.equals(targetBMP) || originalTxt.equals(targetBMP) || originalBMP.equals(originalTxt)) return 1;
		//不许源文件和目的文件重复
		InfoHiding ih=new InfoHiding();
		
		try{
			ih.setOriginalBMP(originalBMP);
		}catch (Exception e)
		{
			//e.printStackTrace();
			return 21;				//Fault 21:  Fail to create stream for original BMP // File not Found
		}
		try {
			ih.getBMPHeaderInfo();
		} catch (IOException e2) {
			//e2.printStackTrace();
			return 28;				//Fault 28:  Fail to get the header info
		}
		
		if(ih.bh.isBM==false) return 31;   // Fault 31: not BMP file 
		if(ih.bh.nreserved>0) return 32;	// Fault 32: not original
		if(ih.bh.ncompression!=0) return 33;  //Fault 33: Compressed BMP File
		if(ih.bh.nbitcount<16 ) return 34; //Fault35: color bit too low to hide info.
		
		try {
			ih.setOriginalText(originalTxt);
		} catch (Exception e1) {
			e1.printStackTrace();
			return 22 ;   				//Fault 22: Fail to create stream for original TXT		
		} 
		
		try {
			ih.getText();
		} catch (IOException e) {
		//	e.printStackTrace();
			return 42; 		//Fault 42: Fail to read Text from stream (IOException)
		}
		
		//对不同制式分别讨论
		switch (ih.bh.nbitcount)
		{	
		case 16:
			try {
				ih.readMap16();
			} catch (IOException e) {
				// TODO 自动生成 catch 块
			//	e.printStackTrace();
				return 41; 		//Fault 41: Fail to read BMP from stream (IOException)
			}
			
			if (ih.isAdequate16()==false) return 51;   //Fault 51 : BMP capacity is not adequate for TXT content
			try {ih.hideInfo16();}catch (Exception e ){return 51;}
			break;
			
		case 24:
			try {
				ih.readMap24();
			} catch (IOException e) {
				// TODO 自动生成 catch 块
			//	e.printStackTrace();
				return 41; 		//Fault 41: Fail to read BMP from stream (IOException)
			}
			
			if (ih.isAdequate24()==false) return 51;   //Fault 51 : BMP capacity is not adequate for TXT content
			try {ih.hideInfo24();}catch (Exception e ){return 51;}
			break;
			
		case 32:
			try {
				ih.readMap32();
			} catch (IOException e) {
				// TODO 自动生成 catch 块
			//	e.printStackTrace();
				return 41; 		//Fault 41: Fail to read BMP from stream (IOException)
			}
			
			if (ih.isAdequate32()==false) return 51;   //Fault 51 : BMP capacity is not adequate for TXT content
			try {ih.hideInfo32();}catch (Exception e ){return 51;}
			break;
			
		default :
			return 34;	//Fault 34: Invalid Color Bit number 			
		}	
		//Section Output
		try {
			ih.setTargetBMP(targetBMP);
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			return 23; //Fault 23: Fail to open and set stream for target BMP file
		}
		try {
			ih.writeTargetBMP();
		} catch (IOException e) {
			//e.printStackTrace();
			return 43;  //Fault 43: Fail to write to target BMP file : IOException
		}	
		return 0;
	}
	
	
}
