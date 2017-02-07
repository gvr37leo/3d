public class Vector {
    float x;
    float y;
    float z;

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