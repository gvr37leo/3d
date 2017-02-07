import processing.core.PApplet;

import java.awt.*;

enum ViewMode {orthogonal, perspective}

public class Camera {
    ViewMode viewMode = ViewMode.orthogonal;
    PApplet app;
    Color color = Color.white;

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
        for(int i = 0; i < mesh.edges.length; i+=2){
            Vector p1 = screenCoords[mesh.edges[i]];
            Vector p2 = screenCoords[mesh.edges[i + 1]];
            if(i == 0)app.stroke(255,0,0);
            else app.stroke(255);
            app.line(p1.x, p1.y, p2.x, p2.y);
        }
    }

    void draw(Vector v){

    }

    void draw(Vector from, Vector dir){

    }


}
