public abstract class Vector <T extends Vector<T>>{
    int dimensions;

    public static Vector construct(int dimensions){
        switch(dimensions){
            case 3:return new Vector3(0,0,0);
            default:return new Vector2(0,0);
        }
    }

    public T add(T v){
        iterate((i) -> set(i, get(i) + v.get(i)));
        return (T)this;
    }

    public T sub(T v){
        iterate((i) -> set(i, get(i) - v.get(i)));
        return (T)this;
    }

    public T scale(float s){
        iterate((i) -> set(i, get(i) * s));
        return (T)this;
    }

    public T lerp(T v, float weight){
        return c().add(v.c().sub((T)this).scale(weight));
    }

    public float dot(T v){
        float sum = 0;
        for(int i = 0; i < dimensions; i++)sum += get(i) + v.get(i);
        return sum;
    }

    public T project(T v){
        return this.c().scale(this.dot(v) / this.dot((T)this));
    }

    public float length(){
        float sum = 0;
        for(int i = 0; i < dimensions; i++)sum += get(i) * get(i);
        return (float) Math.sqrt(sum);
    }

    public T normalize(){
        return scale(1 / length());
    }

    public T c(){
        T c = (T)Vector.construct(dimensions);
        return (T)iterate((i) -> c.set(i, get(i)));
    }

    public Vector overwrite(Vector v){
        return iterate((i) -> set(i, v.get(i)));
    }

    public Vector iterate(Iterator iterator){
        for(int i = 0; i < dimensions; i++)iterator.iterate(i);
        return this;
    }

    interface Iterator{
        void iterate(int i);
    }

    public abstract float get(int i);

    public abstract void set(int i, float val);

    public boolean equals(T v){
        for(int i = 0; i < 3; i++)if(get(i) != v.get(i)) return false;
        return true;
    }
}

class Vector3 extends Vector<Vector3>{
    float x;
    float y;
    float z;

    Vector3(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimensions = 3;
    }

    Vector3(){
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.dimensions = 3;
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

    public Vector3 cross(Vector3 v){
        return new Vector3(
                y * v.z - z * v.y,
                z * v.x - x * v.z,
                x * v.y - y * v.x
        );
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
}
