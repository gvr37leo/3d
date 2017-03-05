import processing.core.PApplet;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;

enum ViewMode {orthogonal, perspective}
enum RenderMode {solid, wireframe}

public class Camera {

    public int[][] zbuffer;
    Vec3f lightDir = new Vec3f(0,0,1).normalize();
    Vec3f dir = new Vec3f(0,0,1);
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
        zbuffer = new int[app.width][app.height];
    }

    void draw(Mesh[] meshes){
        for(int x = 0; x < app.width; x++){
            for(int y = 0; y < app.height; y++){
                zbuffer[x][y] = 255;
            }
        }
        for(Mesh mesh: meshes)draw(mesh, zbuffer);
    }

    void draw(Mesh mesh, int[][] zbuffer){
        Vec2i[] screenCoords = new Vec2i[mesh.vertices.length];
        Vec2i screenSize = new Vec2i(app.width, app.height);
        for(int i = 0; i < mesh.vertices.length; i++){
            Vec3f v = mesh.vertices[i];
            screenCoords[i] = Transformer.wsToss(v.c(), screenSize);
        }
        for (int i = 0; i < mesh.faces.length; i += 3) {
            Vec2i p1 = screenCoords[mesh.faces[i]];
            Vec2i p2 = screenCoords[mesh.faces[i + 1]];
            Vec2i p3 = screenCoords[mesh.faces[i + 2]];

            Vec3f pws1 = mesh.vertices[mesh.faces[i]];
            Vec3f pws2 = mesh.vertices[mesh.faces[i + 1]];
            Vec3f pws3 = mesh.vertices[mesh.faces[i + 2]];

            Vec2f uv1 = mesh.uvs[mesh.faces[i]];
            Vec2f uv2 = mesh.uvs[mesh.faces[i + 1]];
            Vec2f uv3 = mesh.uvs[mesh.faces[i + 2]];

            Vec3f normalws = (pws2.c().sub(pws1)).cross(pws3.c().sub(pws1)).normalize();

            int _i = i;
            if(normalws.dot(pws1.c().sub(new Vec3f())) < 0){//back face culling. positive means facing the same way as camera thus not facing it
                triangle(p1, p2, p3, zbuffer,(Vec2i pos) -> {
                    float bary[] = RayCaster.barycenter(p1.toFloat(),p2.toFloat(),p3.toFloat(),pos.toFloat());
//                    Vec2f uv = uv1.c().scale(bary[0]).add(uv2.c().scale(bary[1])).add(uv3.c().scale(bary[2]));
//                    color = mesh.texture.getPixel(uv.x, uv.y).toAWTColor();
                    float z = 0;
                    for(int off = 0; off < 3; off++)z += mesh.vertices[mesh.faces[_i + off]].z * bary[off];
                    int clampedz = clamp((int) PApplet.map(z,0,5,0,255),0,255);

                    if(clampedz < zbuffer[pos.x][pos.y]){
//                        color = normalColor(normalws,lightDir);
                        zbuffer[pos.x][pos.y] = clampedz;
                        color = clownColor(_i / 3);
                        putPixel(pos.x, pos.y);
                    }

                });
            }
        }


    }

    public void triangle(Vec2i t0, Vec2i t1, Vec2i t2, int[][] zbuffer, ILocationGiver locationGiver){
        Vec2i[] vs = {t0,t1,t2};
        Arrays.sort(vs, Comparator.comparingInt(a -> a.y));

        int total_height = vs[2].y-vs[0].y;

        IFromToer fromToer = (Vec2i low, Vec2i heigh) -> {
            for (int y = low.y; y <= heigh.y; y++) {
                int segment_height = heigh.y-low.y;//+1
                float alpha = (float)(y-vs[0].y) / total_height;
                float beta  = (float)(y-low.y) / segment_height; // be careful with divisions by zero
                Vec2i A = vs[0].lerp(vs[2],alpha);
                Vec2i B = low.lerp(heigh,beta);
                if (A.x>B.x){
                    Vec2i temp = A;
                    A = B;
                    B = temp;
                }
                for(int x = A.x; x < B.x; x++){
                    locationGiver.giveLocation((new Vec2i(x, y)));
                }
            }
        };

        fromToer.fromTo(vs[0],vs[1]);
        fromToer.fromTo(vs[1],vs[2]);

    }
    interface IFromToer{
        void fromTo(Vec2i low, Vec2i heigh);
    }
    
    interface ILocationGiver{
        void giveLocation(Vec2i pos);
    }

    public void putPixel(int x, int y){
        if(x >= app.width || x < 0 || y >= app.height || y < 0)return;
        int index = y * (app.width) + x;
        app.pixels[index] = app.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public Color baryColor(Vec2f a, Vec2f b, Vec2f c, Vec2f p){
        float bary[] = RayCaster.barycenter(a,b,c,p);
        int r = clamp((int)PApplet.map(bary[0],0,1,0,255),0,255);
        int g = clamp((int)PApplet.map(bary[1],0,1,0,255),0,255);
        int _b = clamp((int)PApplet.map(bary[2],0,1,0,255),0,255);
        return new Color(r,g,_b);
    }

    public Color normalColor(Vec3f normal, Vec3f lightDir){
        float lightIntensity = normal.dot(lightDir);
        return new Color(
                Math.abs(lightIntensity),
                Math.abs(lightIntensity),
                Math.abs(lightIntensity));
    }

    public Color clownColor(int i){
        return colors[i % colors.length];
    }

    public int clamp(int val,int min, int max){
        return PApplet.max(PApplet.min(val, max), min);
    }
}
