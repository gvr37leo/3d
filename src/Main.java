import processing.core.PApplet;
import processing.event.KeyEvent;

import java.util.*;


public class Main extends PApplet{

    Mesh mesh;
    Mesh mesh2;
    Camera camera;
    float dt = 1.0f / 60;
    float rotationSpeed = 4;
    HashMap<Character, Boolean> keyMap = new HashMap<>();
    Vec3f rot = new Vec3f(0, 0, 0);
    Vec3f translation = new Vec3f(0,0,0);
    private Vec2i screensize;


    public void settings(){
        size(600,600);
    }

    public void setup(){
        screensize = new Vec2i(width, height);

    }

    public void draw(){
        clear();
        loadPixels();
        if(keyMap.get('w') != null && keyMap.get('w')) rot.add(new Vec3f(1,0,0).scale(rotationSpeed).scale(dt));
        if(keyMap.get('s') != null && keyMap.get('s')) rot.add(new Vec3f(-1,0,0).scale(rotationSpeed).scale(dt));
        if(keyMap.get('a') != null && keyMap.get('a')) rot.add(new Vec3f(0,-1,0).scale(rotationSpeed).scale(dt));
        if(keyMap.get('d') != null && keyMap.get('d')) rot.add(new Vec3f(0,1,0).scale(rotationSpeed).scale(dt));
        if(keyMap.get('q') != null && keyMap.get('q')) rot.add(new Vec3f(0,0,1).scale(rotationSpeed).scale(dt));
        if(keyMap.get('e') != null && keyMap.get('e')) rot.add(new Vec3f(0,0,-1).scale(rotationSpeed).scale(dt));
        if(keyMap.get('r') != null && keyMap.get('r'))translation.add(new Vec3f(0,0,-1).scale(dt));
        if(keyMap.get('f') != null && keyMap.get('f'))translation.add(new Vec3f(0,0,1).scale(dt));

        mesh = Mesh.generateCube();
        mesh2 = Mesh.generateCube();
        mesh.rot(rot);
        mesh.add(translation);
        mesh.add(new Vec3f(0,0,2));
        mesh2.add(new Vec3f(0,0,2));
        camera.draw(new Mesh[]{mesh, mesh2});
        updatePixels();
    }

    public void keyPressed(KeyEvent event) {
        keyMap.put(event.getKey(), true);
        if(event.getKey() == 't')rot = new Vec3f();
    }

    public void keyReleased(KeyEvent event) {
        keyMap.put(event.getKey(), false);
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.runSketch();
        main.camera = new Camera(main);

//        ZBufferDrawer zBufferDrawer = new ZBufferDrawer(main.camera.zbuffer);
//        runSketch(new String[]{""}, zBufferDrawer);
    }


}
