import java.security.PublicKey;

public class Vector {
    float x;
    float y;
    float z;
    float u;
    float v;

    Vector(){
        this.x = 0;
        this.y = 0;
        this.z = 0;
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
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }

    public Vector sub(Vector v){
        x -= v.x;
        y -= v.y;
        z -= v.z;
        return this;
    }

    public Vector scale(float s){
        x *= s;
        y *= s;
        z *= s;
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

    public Vector set(Vector v){
        x = v.x;
        y = v.y;
        z = v.z;
        return this;
    }

    public boolean equals(Vector v){
        return x == v.x && y == v.y && z == v.z;
    }
}
