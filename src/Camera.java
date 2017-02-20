import processing.core.PApplet;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;

enum ViewMode {orthogonal, perspective}
enum RenderMode {solid, wireframe}

public class Camera {
    Vector lightDir = new Vector3(0,0,1).normalize();
    Vector dir = new Vector3(0,0,-1);
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
            Color.gray,
            new Color(0x6E2B02)
    };

    Camera(PApplet app){
        this.app = app;
    }

    void draw(Mesh mesh){
        int[][] zbuffer = new int[app.width][app.height];

        app.fill(color.getRed(), color.getGreen(), color.getBlue());
        Vector2 screenSize = new Vector2(app.width, app.height);
        Vector2[] screenCoords = new Vector2[mesh.vertices.length];
        for(int i = 0; i < mesh.vertices.length; i++){
            Vector v = mesh.vertices[i];
            screenCoords[i] = Transformer.wsToss((Vector3) v.c(), screenSize);
        }
        if(renderMode == RenderMode.wireframe) {
            for (int i = 0; i < mesh.edges.length; i += 2) {
                Vector2 p1 = screenCoords[mesh.edges[i]];
                Vector2 p2 = screenCoords[mesh.edges[i + 1]];
                app.line(p1.x, p1.y, p2.x, p2.y);
            }
        }else {
            for (int i = 0; i < mesh.faces.length; i += 3) {
                Vector2 p1 = screenCoords[mesh.faces[i]];
                Vector2 p2 = screenCoords[mesh.faces[i + 1]];
                Vector2 p3 = screenCoords[mesh.faces[i + 2]];

                Vector3 pws1 = mesh.vertices[mesh.faces[i]];
                Vector3 pws2 = mesh.vertices[mesh.faces[i + 1]];
                Vector3 pws3 = mesh.vertices[mesh.faces[i + 2]];

                Vector normal = p2.c().sub(p1).cross(p3.c().sub(p1)).normalize();
                Vector normalws = ((Vector3) pws2.c().sub(pws1)).cross((Vector3) pws3.c().sub(pws1)).normalize();

                if(normal.dot(dir) < 0){//back face culling. positive means facing the same way as camera thus not facing it

                    float lightIntensity = normalws.dot(lightDir);
                    color = new Color(
                            Math.abs(lightIntensity),
                            Math.abs(lightIntensity),
                            Math.abs(lightIntensity));
                    if(i == 6)color = Color.red;
                    triangle(p1, p2, p3, zbuffer, mesh);
                }
            }
        }

    }

    void fill(Color c){
        color = c;
    }

    void draw(Vector3 v){
        Vector2 v2 = Transformer.wsToss((Vector3) v.c(), new Vector2(app.width, app.height));
        app.fill(color.getRed(), color.getGreen(), color.getBlue());
        app.rect(v2.x - 5,v2.y - 5,10,10);;
    }

    void draw(Vector from, Vector dir){
        Vector2 screensize = new Vector2(app.width, app.height);
        Vector2 from2 = Transformer.wsToss((Vector3) from.c(), screensize);
        Vector2 dir2 = Transformer.wsToss((Vector3) dir.c(), screensize);
        app.line(from2.x, from2.y, dir2.x, dir2.y);
    }

    void triangle(Vector2 a, Vector2 b, Vector2 c, int[][] zbuffer, Mesh mesh){
        Vector2[] vers = {a,b,c};
        Arrays.sort(vers, (a1, b1) -> (int) a1.y - (int) b1.y);


        if(vers[0].y == vers[1].y){
            if(vers[1].x < vers[0].x){//swap
                Vector2 temp = vers[0];
                vers[0] = vers[1];
                vers[1] = temp;
            }
            flatTop(vers[0], vers[1], vers[2], zbuffer, mesh);
        }else if(vers[1].y == vers[2].y){
            if(vers[2].x < vers[1].x){//swap
                Vector2 temp = vers[2];
                vers[2] = vers[1];
                vers[1] = temp;
            }
            flatBot(vers[0], vers[1], vers[2], zbuffer, mesh);
        }else{
            float weight = (vers[1].y - vers[0].y) / (vers[2].y - vers[0].y);
            Vector split = vers[0].lerp(vers[2], weight);
            Vector uvlerped = new Vector(vers[0].u,vers[0].v,0).lerp(new Vector(vers[2].u, vers[2].v, 0), weight);
            split.u = uvlerped.x;
            split.v = uvlerped.y;
            if(vers[1].x < split.x){
                flatBot(vers[0], vers[1], split, zbuffer, mesh);
                flatTop(vers[1], split, vers[2], zbuffer, mesh);
            }else{
                flatBot(vers[0], split, vers[1], zbuffer, mesh);
                flatTop(split, vers[1], vers[2], zbuffer, mesh);
            }
        }

    }

    //0 and 1 are tops and 0 is the left of those
    void flatTop(Vector a, Vector b, Vector c, int[][] zbuffer, Mesh mesh){
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
                drawPixel(a,b,c,zbuffer,mesh,x,y);
            }
        }
    }

    private void drawPixel(Vector a, Vector b, Vector c, int[][] zbuffer, Mesh mesh, int x, int y){
        Vector bary = RayCaster.barycenter2(a,b,c,new Vector(x,y,2.5f));
        Vector auv = new Vector(a.u, a.v, 0);
        Vector buv = new Vector(b.u, b.v, 0);
        Vector cuv = new Vector(c.u, c.v, 0);
        Vector uv = auv.scale(bary.x).add(buv.scale(bary.y)).add(cuv.scale(bary.z));

        color = mesh.texture.getPixel(uv.x, uv.y).toAWTColor();
//        color = Color.blue;
        setPixel(x,y);
    }


    // b and c are bottom and and b is the left of those
    void flatBot(Vector3 a, Vector3 b, Vector3 c, int[][] zbuffer, Mesh mesh){
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
                drawPixel(a,b,c,zbuffer,mesh,x,y);
            }
        }
    }

    private void setPixel(int x, int y){
        if((y * app.width + x) >= (app.width * app.height) || (y * app.width + x) < 0)return;
        app.pixels[y * app.width + x] = app.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
}
