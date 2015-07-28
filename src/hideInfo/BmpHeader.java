package hideInfo;

//用于表达bmp文件头,里面包含很多字段有用
import java.io.FileInputStream;
import java.io.IOException;

//Utility class to represent and get the information of BMP file header
public class BmpHeader {
		public boolean isBM;
        public int nsize;
        public int nreserved;
        public int ndataOffset;

        public int nbisize;
        public int nwidth;
        public int nheight;
        public int nplanes;
        public int nbitcount;
        public int ncompression;
        public int nsizeimage;
        public int nxpm;
        public int nypm;
        public int nclrused;
        public int nclrimp;

        public byte[] bf;
        public byte[] bi;

        // read in the bitmap header
        public void getHeaderInfo(FileInputStream fs) throws IOException
        {
            final int bflen = 14; // 14 byte BITMAPFILEHEADER
            bf = new byte[bflen];
            fs.read(bf, 0, bflen);
            final int bilen = 40; // 40-byte BITMAPINFOHEADER
            bi = new byte[bilen];
            fs.read(bi, 0, bilen);
            // IntERPeret data.
            nsize = Utility.constructInt(bf, 2);

            StringBuffer type=new StringBuffer();
            type.append((char)bf[0]).append((char)bf[1]);
            if (type.toString().equalsIgnoreCase("BM")) this.isBM=true;
            else this.isBM=false;

//            System.out.println("File type is :"+(char)bf[0]+(char)bf[1]+"\nisBM: "+this.isBM);
            //System.out.println("Size of file is :"+nsize);

            nreserved=Utility.constructInt(bf,6);
//            System.out.println("Reserved Content is :"+nreserved);

            ndataOffset=Utility.constructInt(bf,10);
  //          System.out.println("Data Offset Value is:"+ndataOffset);

            ////////////////////////////////////////////



            nbisize = Utility.constructInt(bi, 2);

        //       System.out.println("Size of bitmapinfoheader is :"+nbisize);

            nwidth = Utility.constructInt(bi, 4);

            //   System.out.println("Width is :"+nwidth);

            nheight = Utility.constructInt(bi, 8);

            //  System.out.println("Height is :"+nheight);

            nplanes = Utility.constructShort(bi, 12); //(((int)bi[13]&0xff)<<8) |
            // (int)bi[12]&0xff;

            //  System.out.println("Planes is :"+nplanes);

            nbitcount = Utility.constructShort(bi, 14); //(((int)bi[15]&0xff)<<8) |
            // (int)bi[14]&0xff;

            //  System.out.println("BitCount is :"+nbitcount);

            // Look for non-zero values to indicate compression
            ncompression = Utility.constructInt(bi, 16);

     //         System.out.println("Compression is :"+ncompression);

            nsizeimage = Utility.constructInt(bi, 20);

            //  System.out.println("SizeImage is :"+nsizeimage);

            nxpm = Utility.constructInt(bi, 24);

            // System.out.println("X-Pixels per meter is :"+nxpm);

            nypm = Utility.constructInt(bi, 28);

            // System.out.println("Y-Pixels per meter is :"+nypm);

            nclrused = Utility.constructInt(bi, 32);

            //  System.out.println("Colors used are :"+nclrused);

            nclrimp = Utility.constructInt(bi, 36);

            //  System.out.println("Colors important are :"+nclrimp);

        }

    }
