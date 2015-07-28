package hideInfo;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.*;

public class InfoGetting {  //和InfoHiding类似 
	BmpHeader bh;
	File originalBMP;
	FileInputStream fs;
	Image origBMPImage;
	byte[] originalImageData;
	
	File targetText;
	FileOutputStream fos;
	byte[] targetTextData;
	String targetString;

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
	
	public void setTargetTxt(File target) throws FileNotFoundException
	{
		this.targetText=target;	
		this.fos=new FileOutputStream(this.targetText);
		
	}

	/**
	 * ////	 read the 24bit bmp file into byte[] and then into Image object
	 * Way1 : From the angle of pixel number
	 */
//	public void readMap24() throws IOException
//	{
//		int npad = (bh.nsizeimage / bh.nheight) - bh.nwidth * 3;
//		int ndata[] = new int[bh.nheight * bh.nwidth];
//        this.originalImageData= new byte[(bh.nwidth + npad) * 3 * bh.nheight];
//        
//        this.fs.read(this.originalImageData,0,(bh.nwidth + npad) * 3 * bh.nheight);
//        
//        int nindex = 0;
//        for (int j = 0; j < bh.nheight; j++)
//        {
//            for (int i = 0; i < bh.nwidth; i++)
//            {
//                ndata[bh.nwidth*(bh.nheight-j-1)+i]=Utility.constructInt3(this.originalImageData, nindex);
//                nindex += 3;
//            }
//            nindex += npad;
//        }
//        this.origBMPImage=Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(bh.nwidth,bh.nheight,ndata,0,bh.nwidth));
//        fs.close();
//	}
	
	public void readMap16() throws IOException
	{
		int len=(int) (this.originalBMP.length()-this.bh.ndataOffset);
		this.originalImageData= new byte[len];
 //       System.out.println("this.originalImageData.length= "+this.originalImageData.length);
  //      System.out.println("(file.length-54)= "+(this.originalBMP.length()-54));     
        this.fs.skip(this.bh.ndataOffset-54);  //jump to the data position
    //    System.out.println("bh.ndataOffset-54＝ "+(bh.ndataOffset-54));
        this.fs.read(this.originalImageData,0,len);    
        fs.close();
	}
	
	
	/**
	 * 	 read the 24bit bmp file into byte[] and then into Image object ==
	 * 	 Way2: From the angle of File length
	 */
	public void readMap24() throws IOException
	{
		int len=(int) (this.originalBMP.length()-this.bh.ndataOffset);
//		System.out.println("this.originalBMP.length()-this.bh.ndataOffset= "+len);
		int ndata[] = new int[len];
        this.originalImageData= new byte[len];
//        System.out.println("this.originalImageData.length= "+this.originalImageData.length);
//        System.out.println("(file.length-54)= "+(this.originalBMP.length()-54));
        
        this.fs.skip(this.bh.ndataOffset-54);
//        System.out.println("bh.ndataOffset-54＝ "+(bh.ndataOffset-54));
        this.fs.read(this.originalImageData,0,len);
        
        int nindex = 0;
        for (int j = 0; j < bh.nheight; j++)
        {
            for (int i = 0; i < bh.nwidth; i++)
            {
                ndata[bh.nwidth*(bh.nheight-j-1)+i]=Utility.constructInt3(this.originalImageData, nindex);
                nindex += 3;
            }
        }
        this.origBMPImage=Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(bh.nwidth,bh.nheight,ndata,0,bh.nwidth));
        fs.close();
	}
	
	/**
	 *  read the 32bit bmp file into byte[] and then into Image object
	 */	
	public void readMap32() throws IOException
	{
		int len=(int) (this.originalBMP.length()-this.bh.ndataOffset);
//		System.out.println("this.originalBMP.length()-this.bh.ndataOffset= "+len);
		int ndata[] = new int[len];
        this.originalImageData= new byte[len];
 //       System.out.println("this.originalImageData.length= "+this.originalImageData.length);
  //      System.out.println("(file.length-54)= "+(this.originalBMP.length()-54));
        
        this.fs.skip(this.bh.ndataOffset-54);  //jump to the data position
   //     System.out.println("bh.ndataOffset-54＝ "+(bh.ndataOffset-54));
        this.fs.read(this.originalImageData,0,len);
        
        int nindex = 0;
        for (int j = 0; j < bh.nheight; j++)
            for (int i = 0; i < bh.nwidth; i++)
            {
                ndata[bh.nwidth * (bh.nheight - j - 1) + i] = Utility.constructInt3(this.originalImageData, nindex);
                nindex += 4;
            }
        this.origBMPImage = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(bh.nwidth, bh.nheight,ndata, 0, bh.nwidth));
        fs.close();
             
	}
	
	public void getInfo16()
	{
		if(this.bh.nreserved>0) this.targetTextData=new byte[this.bh.nreserved]; //create buffer for targetText
		else System.out.println("Reserved is 0!!!");
		
		int countPixel=0;
		int countText=0;
		
		while(countText<this.bh.nreserved)
		{
			byte tmpTextByte=0;
			tmpTextByte=(byte) ((tmpTextByte<<1) + (this.originalImageData[countPixel+14] & 0x80));
			tmpTextByte=(byte) ((tmpTextByte<<1) + (this.originalImageData[countPixel+12] & 0x80));
			tmpTextByte=(byte) ((tmpTextByte<<1) + (this.originalImageData[countPixel+10] & 0x80));
			tmpTextByte=(byte) ((tmpTextByte<<1) + (this.originalImageData[countPixel+8] & 0x80));
			tmpTextByte=(byte) ((tmpTextByte<<1) + (this.originalImageData[countPixel+6] & 0x80));
			tmpTextByte=(byte) ((tmpTextByte<<1) + (this.originalImageData[countPixel+4] & 0x80));
			tmpTextByte=(byte) ((tmpTextByte<<1) + (this.originalImageData[countPixel+2] & 0x80));
			tmpTextByte=(byte) ((tmpTextByte<<1) + (this.originalImageData[countPixel+0] & 0x80));
			this.targetTextData[countText]=tmpTextByte;
			countPixel+=16;
			countText++;
		}	                 
		
	}
	
	//getting information from 24bit bmp image
	public void getInfo24()
	{
		if(this.bh.nreserved>0) this.targetTextData=new byte[this.bh.nreserved]; //create buffer for targetText
		else System.out.println("Reserved is 0!!!");
		
		int countPixel=0;
		int countText=0;
		
		while(countText<this.bh.nreserved)
		{
			byte tmpTextByte=0;
			
			tmpTextByte=(byte) ((tmpTextByte<<2) + (this.originalImageData[countPixel+3] & 0x03));
			tmpTextByte=(byte) ((tmpTextByte<<2) + (this.originalImageData[countPixel+2] & 0x03));
			tmpTextByte=(byte) ((tmpTextByte<<2) + (this.originalImageData[countPixel+1] & 0x03));
			tmpTextByte=(byte) ((tmpTextByte<<2) + (this.originalImageData[countPixel+0] & 0x03));
			this.targetTextData[countText]=tmpTextByte;
			countPixel+=4;
			countText++;
		}	                                                    
	}
	
	public void getInfo32()
	{
		if(this.bh.nreserved>0) this.targetTextData=new byte[this.bh.nreserved]; //create buffer for targetText
		else System.out.println("Reserved is 0!!!");
		
		int countPixel=0;
		int countText=0;
		
		while(countText<this.bh.nreserved)
		{
			byte tmpTextByte=0;
			
			tmpTextByte=(byte) ((tmpTextByte<<2) + (this.originalImageData[countPixel+3] & 0x03));
			tmpTextByte=(byte) ((tmpTextByte<<2) + (this.originalImageData[countPixel+2] & 0x03));
			tmpTextByte=(byte) ((tmpTextByte<<2) + (this.originalImageData[countPixel+1] & 0x03));
			tmpTextByte=(byte) ((tmpTextByte<<2) + (this.originalImageData[countPixel+0] & 0x03));
			this.targetTextData[countText]=tmpTextByte;
			countPixel+=4;
			countText++;
		}	                                                    
	}
	
	//write the infomation which has been got into the indicated target file
	public void writeTargetTxt() throws Exception
	{	
		File tmpOutZip=new File("c:\\tmpOut.zip");
		if (tmpOutZip.exists()) tmpOutZip.delete();
		
		FileOutputStream tmpHold=this.fos;
		this.fos=new FileOutputStream(new File("c:\\tmpOut.zip"));
		fos.write(this.targetTextData);	
		fos.flush();
		fos.close();  //output to the temp zip file
		
		FileInputStream fis = new FileInputStream("c:\\tmpOut.zip");
		ZipInputStream zin = new ZipInputStream(new BufferedInputStream(fis));
		ZipEntry entry = zin.getNextEntry();
		
		int BUFFER = 2048;
		BufferedOutputStream dest = new BufferedOutputStream(tmpHold,BUFFER);
		int count;
		byte data[] = new byte[BUFFER];
		while ((count = zin.read(data, 0, BUFFER)) != -1) {
			dest.write(data, 0, count);
		}
		dest.flush();
		dest.close();
		zin.close();	//end extract from tmp zip file to indicated file
		
//		boolean del=new File("c:\\tmpOut.zip").delete();
	//	System.out.println("delete c:\\tmpOut.zip? :"+del);
		
	}
	
	//Main operation method for getting information from image
	public static int get(File originalBMP,File targetTxt)
	{
		if (originalBMP.equals(targetTxt)) return 1;
		InfoGetting ig=new InfoGetting();
		
		try{
			ig.setOriginalBMP(originalBMP);
		}catch (Exception e)
		{
			//e.printStackTrace();
			return 21;				//Fault 21:  Fail to create stream for original BMP // File not Found
		}
		try {
			ig.getBMPHeaderInfo();
		} catch (IOException e2) {
			//e2.printStackTrace();
			return 28;				//Fault 28:  Fail to get the header info
		}
		
		if(ig.bh.isBM==false) return 31;   // Fault 31: not BMP file 
		if(ig.bh.nreserved==0) return 32;	// Fault 32:  no info contained
		if(ig.bh.ncompression!=0) return 33;  //Fault 33: Compressed BMP File , so must be plain bmp
		
		switch (ig.bh.nbitcount)
		{
		case 16:
			try {
				ig.readMap16();
			} catch (IOException e) {
				// TODO 自动生成 catch 块
			//	e.printStackTrace();
				return 41; 		//Fault 41: Fail to read BMP from stream (IOException)
			}			
			ig.getInfo16();
			break;
			
		case 24:
			try {
				ig.readMap24();
			} catch (IOException e) {
				// TODO 自动生成 catch 块
			//	e.printStackTrace();
				return 41; 		//Fault 41: Fail to read BMP from stream (IOException)
			}			
			ig.getInfo24();
			break;
			
			
		case 32:
			try {
				ig.readMap32();
			} catch (IOException e) {
				// TODO 自动生成 catch 块
			//	e.printStackTrace();
				return 41; 		//Fault 41: Fail to read BMP from stream (IOException)
			}			
			ig.getInfo32();
			break;
			
		default :
			return 34;	//Fault 34: Invalid Color Bit number 			
		}
		
//		Section Output
		try {
			ig.setTargetTxt(targetTxt);
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			return 23; //Fault 23: Fail to open and set stream for target Txt file
		}
		
		try {
			ig.writeTargetTxt();
		} catch (Exception e) {
			//e.printStackTrace();
			return 43;  //Fault 43: Fail to write to target TXT file : IOException
		}
		
		return 0;
	}

}
