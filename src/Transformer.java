public class Transformer {

    public static Vec2i wsToss(Vec3f v, Vec2i screenSize){
        float zInv = 1 / v.z;
        return new Vec2i((int)((v.x * zInv + 1) * (screenSize.x / 2)), (int)((-v.y * zInv + 1) * (screenSize.y / 2)));
    }

    public static Vec3f ssTows(Vec2i v, Vec2i screensize){
        return new Vec3f(v.x / screensize.x * 2 - 1, -(v.y / screensize.y * 2 - 1), 1);
    }
}