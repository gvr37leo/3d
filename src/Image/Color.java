package Image;

import java.nio.ByteBuffer;

public class Color {
    byte r;
    byte g;
    byte b;
    byte a;

    Color(byte r, byte g, byte b, byte a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    Color(int rgb){
        byte[] buffer = ByteBuffer.allocate(4).putInt(rgb).array();
        this.r = buffer[1] ;
        this.g = buffer[2];
        this.b = buffer[3];
    }

    public java.awt.Color toAWTColor(){
        return new java.awt.Color(r & (0xff), g & (0xff), b & (0xff));
    }
}
