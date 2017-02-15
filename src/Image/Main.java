package Image;

import processing.core.PApplet;

import java.io.IOException;

public class Main extends PApplet{
    public void settings(){
        size(500, 500);
    }

    public void setup() {
        stroke(255);
        Image img;
        try {
            img = Image.fromImage("test.png");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void draw(){
        clear();
    }

    public static void main(String[] args) {
        main("Image.Main");

    }
}
