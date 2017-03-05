package Image;

import processing.core.PApplet;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Image {
    public Vector2i size;//x0...23(4 bytes 0...3)  y24...47(4 bytes 4...7)
    public boolean grayscale = false;//48 in byte 8
    public boolean alphaChannel = true;//49 in byte 8
    //50,51,52,53,54,55 is filler set at 0 because java cant access more precise than bytes
    public byte bitDepth = 8;//56...63 byte 9
    public byte[] data;//64...n byte 10...n
    private int channels;
    private final byte headerSize = 10;

    public Image(Vector2i size){
        this.size = size.c();
        channels = 3;
        if(grayscale)channels = 1;
        if (alphaChannel)channels++;
        data = new byte[size.x * size.y * channels];
    }

    public Image(String filename){
        byte[] file = new byte[0];
        try {
            file = Files.readAllBytes(Paths.get(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        size = new Vector2i(ByteBuffer.wrap(file,0,4).getInt(), ByteBuffer.wrap(file,4,4).getInt());
        byte flags = file[8];
        grayscale = (flags & ((byte) 1 << 7)) != 0;
        alphaChannel = (flags & ((byte) 1 << 6)) != 0;

        bitDepth = file[9];

        data = new byte[file.length - 10];
        System.arraycopy(file, headerSize, data,0,file.length - headerSize);
    }

    public static Image fromImage(String filename){

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image myimage = new Image(new Vector2i(img.getWidth(), img.getHeight()));
        for(int y = 0; y < myimage.size.y; y++){
            for(int x = 0; x < myimage.size.x; x++){
                myimage.setPixel(new Vector2i(x,y), new Color(img.getRGB(x, y)));
            }
        }
        return myimage;
    }

    public void setPixel(Vector2i pos, Color c){
        int index = (pos.y * size.x + pos.x) * channels;
        data[index] = c.r;
        data[index + 1] = c.g;
        data[index + 2] = c.b;
        data[index + 3] = c.a;
    }

    public Color getPixel(Vector2i pos){
        int index = (pos.y * size.x + pos.x) * channels;
        return new Color(data[index], data[index + 1], data[index + 2], data[index + 3]);
    }

    public Color getPixel(float u, float v){
        if(u > 1 || u < 0 || v > 1 || v < 0)return new Color((byte) 0, (byte) 0, (byte) 0, (byte) 255);
        int x = (int) ((size.x - 1 ) * u);
        int y = (int) ((size.y - 1 ) * v);
        int index = (y * size.x + x) * channels;
        index = index % (data.length - 1);
        return new Color(data[index], data[index + 1], data[index + 2], data[index + 3]);
    }


    public void save(String filename){
        byte[] dataToWrite = new byte[(size.x * size.y) * channels + headerSize];

        System.arraycopy(ByteBuffer.allocate(Integer.BYTES).putInt(size.x).array(), 0, dataToWrite,0, Integer.BYTES);
        System.arraycopy(ByteBuffer.allocate(Integer.BYTES).putInt(size.y).array(), 0, dataToWrite,4, Integer.BYTES);
        if(grayscale) dataToWrite[8] |= 1 << 7;
        if(alphaChannel) dataToWrite[8] |= 1 << 6;
        dataToWrite[9] = bitDepth;

        System.arraycopy(data, 0, dataToWrite,headerSize, size.x * size.y * channels);
        try {
            Files.write(new File(filename).toPath(), dataToWrite);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render(PApplet g){
        g.loadPixels();
        for(int y = 0; y < size.y; y++){
            for(int x = 0; x < size.x; x++){
                Color col = getPixel(new Vector2i(x, y));
                g.pixels[y * g.width + x] = g.color(col.r & 0xff, col.g & 0xff, col.b & 0xff);
            }
        }
        g.updatePixels();
    }
}
