import java.security.PublicKey;

public class Vector {
    float x;
    float y;
    float z;
    float u;
    float v;

    Vector(){
        for(int i = 0; i < 3; i++)set(i, 0);
    }

    Vector(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Vector(float x, float y, float z, float u, float v){
        this.x = x;
        this.y = y;
        this.z = z;
        this.u = u;
        this.v = v;
    }

    public Vector add(Vector v){
        for(int i = 0; i < 3; i++)set(i, get(i) + v.get(i));
        return this;
    }

    public Vector sub(Vector v){
        for(int i = 0; i < 3; i++)set(i, get(i) - v.get(i));
        return this;
    }

    public Vector scale(float s){
        for(int i = 0; i < 3; i++)set(i, get(i) * s);
        return this;
    }

    public Vector lerp(Vector v, float weight){
        return c().add(v.c().sub(this).scale(weight));
    }

    public float dot(Vector v){
        return x * v.x + y * v.y + z * v.z;
    }

    public Vector cross(Vector v){
        return new Vector(
                y * v.z - z * v.y,
                z * v.x - x * v.z,
                x * v.y - y * v.x
        );
    }

    public Vector project(Vector v){
        return this.c().scale(this.dot(v) / this.dot(this));
    }

    public float length(){
        return (float)Math.pow(x*x + y*y + z*z, 0.5);
    }

    public Vector normalize(){
        return scale(1 / length());
    }

    public Vector c(){
        return new Vector(x, y, z);
    }

    public Vector write(Vector v){
        for(int i = 0; i < 3; i++)set(i, v.get(i));
        return this;
    }

    public float get(int i){
        switch (i){
            case 1:return y;
            case 2:return z;
            default: return x;
        }
    }

    public void set(int i, float val){
        switch (i){
            case 1:y = val;
                break;
            case 2:z = val;
                break;
            default: x = val;
        }
    }

    public boolean equals(Vector v){
        return x == v.x && y == v.y && z == v.z;
    }
}
