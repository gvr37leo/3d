public class Transformer {

    public static Vector2 wsToss(Vector3 v, Vector2 screenSize){
        float zInv = 1 / v.z;
        return new Vector2((v.x * zInv + 1) * (screenSize.x / 2), (-v.y * zInv + 1) * (screenSize.y / 2));
    }

    public static Vector3 ssTows(Vector2 v, Vector2 screensize){
        return new Vector3(v.x / screensize.x * 2 - 1, -(v.y / screensize.y * 2 - 1), 1);
    }
}
