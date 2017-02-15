package Image;

public class Vector2i {
    int x;
    int y;

    Vector2i(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Vector2i c(){
        return new Vector2i(x, y);
    }
}
