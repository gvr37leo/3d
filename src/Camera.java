import processing.core.PApplet;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;

enum ViewMode {orthogonal, perspective}
enum RenderMode {solid, wireframe}

public class Camera {
    ViewMode viewMode = ViewMode.orthogonal;
    RenderMode renderMode = RenderMode.solid;
    PApplet app;
    Color color = Color.white;
    Color[] colors = new Color[]{
            Color.yellow,
            Color.blue,
            Color.cyan,
            Color.magenta,
            Color.red,
            Color.pink,
            Color.green,
    };

    Camera(PApplet app){
        this.app = app;
    }

    void draw(Mesh mesh){

        app.fill(color.getRed(), color.getGreen(), color.getBlue());
        Vector screenSize = new Vector(app.width, app.height, 0);
        Vector[] screenCoords = new Vector[mesh.vertices.length];
        for(int i = 0; i < mesh.vertices.length; i++){
            Vector v = mesh.vertices[i];
            screenCoords[i] = Transformer.wsToss(v.c(), screenSize);
        }
        if(renderMode == RenderMode.wireframe) {
            for (int i = 0; i < mesh.edges.length; i += 2) {
                Vector p1 = screenCoords[mesh.edges[i]];
                Vector p2 = screenCoords[mesh.edges[i + 1]];
                if (i == 0) app.stroke(255, 0, 0);
                else app.stroke(255);
                app.line(p1.x, p1.y, p2.x, p2.y);
            }
        }else {
            for (int i = 0; i < mesh.faces.length; i += 3) {
                Vector p1 = screenCoords[mesh.faces[i]];
                Vector p2 = screenCoords[mesh.faces[i + 1]];
                Vector p3 = screenCoords[mesh.faces[i + 2]];
                color = colors[(i / 3) % colors.length];
                triangle(p1, p2, p3);
            }
        }

    }

    void fill(Color c){
        color = c;
    }

    void draw(Vector v){

    }

    void draw(Vector from, Vector dir){

    }

    void triangle(Vector a, Vector b, Vector c){
        Vector[] vers = {a,b,c};
        Arrays.sort(vers, new Comparator<Vector>() {
            public int compare(Vector a, Vector b) {
                return (int)a.y - (int)b.y;
            }
        });

        if(vers[0].y == vers[1].y){
            if(vers[1].x < vers[0].x){//swap
                Vector temp = vers[0];
                vers[0] = vers[1];
                vers[1] = temp;
            }
            flatTop(vers[0], vers[1], vers[2]);
        }else if(vers[1].y == vers[2].y){
            if(vers[2].x < vers[1].x){//swap
                Vector temp = vers[2];
                vers[2] = vers[1];
                vers[1] = temp;
            }
            flatBot(vers[0], vers[1], vers[2]);
        }else{
            Vector split = vers[0].lerp(vers[2], (vers[1].y - vers[0].y) / (vers[2].y - vers[0].y));
            if(vers[1].x < split.x){
                flatBot(vers[0], vers[1], split);
                flatTop(vers[1], split, vers[2]);
            }else{
                flatBot(vers[0], split, vers[1]);
                flatTop(split, vers[1], vers[2]);
            }
        }

    }

    //0 and 1 are tops and 0 is the left of those
    void flatTop(Vector a, Vector b, Vector c){
        float m0 = (c.x - a.x) / (c.y - a.y);
        float m1 = (c.x - b.x) / (c.y - b.y);

        int yStart = (int)Math.ceil(a.y - 0.5f);
        int yEnd = (int)Math.ceil(c.y - 0.5f);

        for(int y = yStart; y < yEnd; y++){
            float px0 = m0 * ((float)y + 0.5f - a.y) + a.x;
            float px1 = m1 * ((float)y + 0.5f - b.y) + b.x;

            int xStart = (int)Math.ceil(px0 - 0.5f);
            int xEnd = (int)Math.ceil(px1 - 0.5f);
            for(int x = xStart; x < xEnd; x++){
                Color col = new Color((int)app.random(255),(int)app.random(255),(int)app.random(255));
                app.pixels[y * app.width + x] = app.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            }
        }
    }


    // b and c are bottom and and b is the left of those
    void flatBot(Vector a, Vector b, Vector c){
        float m0 = (b.x - a.x) / (b.y - a.y);
        float m1 = (c.x - a.x) / (c.y - a.y);

        int yStart = (int)Math.ceil(a.y - 0.5f);
        int yEnd = (int)Math.ceil(c.y - 0.5f);

        for(int y = yStart; y < yEnd; y++){
            float px0 = m0 * ((float)y + 0.5f - a.y) + a.x;
            float px1 = m1 * ((float)y + 0.5f - a.y) + a.x;

            int xStart = (int)Math.ceil(px0 - 0.5f);
            int xEnd = (int)Math.ceil(px1 - 0.5f);
            for(int x = xStart; x < xEnd; x++){
                app.pixels[y * app.width + x] = app.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            }
        }
    }
}
