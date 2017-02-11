import processing.core.PApplet;
import processing.event.KeyEvent;

import java.awt.*;
import java.util.HashMap;

public class Main extends PApplet{

    Mesh mesh;
    Camera camera = new Camera(this);
    float rotationSpeed = 4;
    HashMap<Character, Boolean> keyMap = new HashMap<Character, Boolean>();
    float dt = 1.0f / 60;
    Vector rot = new Vector(0, 0, 0);
    Vector translation = new Vector();

    public void settings(){
        size(500, 500);
    }

    public void setup(){
        stroke(255);
    }

    public void draw(){
        clear();
        loadPixels();
        if(keyMap.get('w') != null && keyMap.get('w')) rot.add(new Vector(1,0,0).scale(rotationSpeed).scale(dt));
        if(keyMap.get('s') != null && keyMap.get('s')) rot.add(new Vector(-1,0,0).scale(rotationSpeed).scale(dt));
        if(keyMap.get('a') != null && keyMap.get('a')) rot.add(new Vector(0,-1,0).scale(rotationSpeed).scale(dt));
        if(keyMap.get('d') != null && keyMap.get('d')) rot.add(new Vector(0,1,0).scale(rotationSpeed).scale(dt));
        if(keyMap.get('q') != null && keyMap.get('q')) rot.add(new Vector(0,0,1).scale(rotationSpeed).scale(dt));
        if(keyMap.get('e') != null && keyMap.get('e')) rot.add(new Vector(0,0,-1).scale(rotationSpeed).scale(dt));
        if(keyMap.get('r') != null && keyMap.get('r'))translation.add(new Vector(0,0,-1).scale(dt));
        if(keyMap.get('f') != null && keyMap.get('f'))translation.add(new Vector(0,0,1).scale(dt));

        mesh = Mesh.generateCube();
        mesh.rot(rot);
        mesh.add(translation);
        mesh.add(new Vector(0,0,3));
        camera.draw(mesh);
        updatePixels();
    }

    public void keyPressed(KeyEvent event) {
        keyMap.put(event.getKey(), true);
    }

    public void keyReleased(KeyEvent event) {
        keyMap.put(event.getKey(), false);
    }

    public static void main(String[] args) {
        main("Main");
    }
}
