package hideInfo;
//工具函数 
public class Utility {
    // build an positive int from a byte array - convert little to big endian
	// index0 - lowest
	// index3 - highest
    public static int constructInt(byte[] in, int offset) {

        int ret = ((int) in[offset + 3] & 0xff);

        ret = (ret << 8) | ((int) in[offset + 2] & 0xff);

        ret = (ret << 8) | ((int) in[offset + 1] & 0xff);

        ret = (ret << 8) | ((int) in[offset + 0] & 0xff);

        return (ret);

    }
    
    // int -> byte[4]  int拆开为4个byte
    public static void extractInt(int num,byte[]out,int offset)
    {
    	out[offset]= (byte) (num-((num>>8)<<8));
    	num=num>>8;
    	out[offset+1]= (byte) (num-((num>>8)<<8));
    	num=num>>8;
    	out[offset+2]= (byte) (num-((num>>8)<<8));
    	num=num>>8;
    	out[offset+3]= (byte) (num-((num>>8)<<8));
    	num=num>>8;
    	return;
    }

    // build an negative int from a byte array - convert little to big endian
    // biggest : -1     
    public static int constructInt3(byte[] in, int offset) {

        int ret = 0xff;

        ret = (ret << 8) | ((int) in[offset + 2] & 0xff);

        ret = (ret << 8) | ((int) in[offset + 1] & 0xff);

        ret = (ret << 8) | ((int) in[offset + 0] & 0xff);

        return (ret);

    }

    // build an long from a byte array - convert little to big endian
    public static long constructLong(byte[] in, int offset) {

        long ret = ((long) in[offset + 7] & 0xff);

        ret |= (ret << 8) | ((long) in[offset + 6] & 0xff);

        ret |= (ret << 8) | ((long) in[offset + 5] & 0xff);

        ret |= (ret << 8) | ((long) in[offset + 4] & 0xff);

        ret |= (ret << 8) | ((long) in[offset + 3] & 0xff);

        ret |= (ret << 8) | ((long) in[offset + 2] & 0xff);

        ret |= (ret << 8) | ((long) in[offset + 1] & 0xff);

        ret |= (ret << 8) | ((long) in[offset + 0] & 0xff);

        return (ret);

    }

    // build an double from a byte array - convert little to big endian
    public static double constructDouble(byte[] in, int offset) {

        long ret = constructLong(in, offset);

        return (Double.longBitsToDouble(ret));

    }

    // build an short from a byte array - convert little to big endian
    public static short constructShort(byte[] in, int offset) {

        short ret = (short) ((short) in[offset + 1] & 0xff);

        ret = (short) ((ret << 8) | (short) ((short) in[offset + 0] & 0xff));

        return (ret);

    }

}
