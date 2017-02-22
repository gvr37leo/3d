public abstract class Vector <T extends Vector<T>>{
    int dimensions;
    private static float sum;

    public static Vector construct(int dimensions){
        switch(dimensions){
            case 3:return new Vector3(0,0,0);
            default:return new Vector2(0,0);
        }
    }

    public T add(T v){
        return iterate((i) -> set(i, get(i) + v.get(i)));
    }

    public T sub(T v){
        return iterate((i) -> set(i, get(i) - v.get(i)));
    }

    public T scale(float s){
        return iterate((i) -> set(i, get(i) * s));
    }

    public T lerp(T v, float weight){
        return c().add(v.c().sub(This()).scale(weight));
    }

    public float dot(T v){
        sum = 0;
        iterate((i) -> sum += get(i) + v.get(i));
        return sum;
    }

    public T project(T v){
        return this.c().scale(this.dot(v) / this.dot(This()));
    }

    public float length(){
        return (float) Math.sqrt(lengthSq());
    }

    public float lengthSq(){
        sum = 0;
        iterate((i) -> sum += get(i) * get(i));
        return sum;
    }

    public T normalize(){
        return scale(1 / length());
    }

    public T c(){
        T c = (T)Vector.construct(dimensions);
        return iterate((i) -> c.set(i, get(i)));
    }

    public T overwrite(Vector v){
        return iterate((i) -> set(i, v.get(i)));
    }

    public T iterate(Iterator iterator){
        for(int i = 0; i < dimensions; i++)iterator.iterate(i);
        return This();
    }

    interface Iterator{
        void iterate(int i);
    }

    public abstract float get(int i);
    public abstract void set(int i, float val);
    public abstract T This();

    public boolean equals(T v){
        for(int i = 0; i < 3; i++)if(get(i) != v.get(i)) return false;
        return true;
    }
}


class Vector2 extends Vector<Vector2>{
    float x;
    float y;

    Vector2(float x, float y){
        this.x = x;
        this.y = y;
        this.dimensions = 3;
    }

    public float get(int i){
        switch (i){
            case 1:return y;
            default: return x;
        }
    }

    public void set(int i, float val){
        switch (i){
            case 1:y = val;
                break;
            default: x = val;
        }
    }

    public Vector2 This() {
        return this;
    }
}


class Vector3 extends Vector2{
    float z;

    Vector3(float x, float y, float z){
        super(x,y);
        this.z = z;
        this.dimensions = 3;
    }

    public float get(int i){
        if(i == 2)return z;
        return super.get(i);
    }

    public void set(int i, float val){
        if(i == 2){
            z = val;
            return;
        }
        super.set(i, val);
    }

    public Vector3 cross(Vector3 v){
        return new Vector3(
                y * v.z - z * v.y,
                z * v.x - x * v.z,
                x * v.y - y * v.x
        );
    }
}
