public class Transformer {

    public static Vector wsToss(Vector v, Vector screenSize){
        float zInv = 1 / v.z;
        v.x = (v.x * zInv + 1) * (screenSize.x / 2);
        v.y = (-v.y * zInv + 1) * (screenSize.y / 2);
        return v;
    }

    public static Vector ssTows(Vector v, Vector screensize){
        return new Vector(v.x / screensize.x * 2 - 1, -(v.y / screensize.y * 2 - 1), 1);
    }
}
