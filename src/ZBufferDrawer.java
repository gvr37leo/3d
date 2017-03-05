import processing.core.PApplet;

public class ZBufferDrawer extends PApplet{

    int[][] zbuffer;

    ZBufferDrawer(int[][] zbuffer){
        this.zbuffer = zbuffer;
    }

    public void settings() {
        size(600,600);
    }

    public void setup() {
        loadPixels();
    }

    public void draw() {
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                putPixel(x,y,zbuffer[x][y]);
            }
        }
        updatePixels();
    }

    public void putPixel(int x, int y, int color){
        int index = y * (width) + x;
        pixels[index] = color(color);
    }
}
