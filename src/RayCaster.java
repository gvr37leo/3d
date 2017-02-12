public class RayCaster {

//    https://www.youtube.com/watch?v=EZXz-uPyCyA&t=281s
//    http://blackpawn.com/texts/pointinpoly/
    static public Vector rayTriangle(Vector from, Vector dir, Vector a, Vector b, Vector c){
        Vector ab = b.c().sub(a);
        Vector ac = c.c().sub(a);

        Vector normal = ab.cross(ac);
        Vector i = rayPlane(from, dir, normal, a);
        Vector ai = i.c().sub(a);

        float dot00 = ac.dot(ac);
        float dot01 = ac.dot(ab);
        float dot02 = ac.dot(ai);
        float dot11 = ab.dot(ab);
        float dot12 = ab.dot(ai);

        float invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
        float u = (dot11 * dot02 - dot01 * dot12) * invDenom;
        float v = (dot00 * dot12 - dot01 * dot02) * invDenom;

        if((u >= 0) && (v >= 0) && (u + v < 1))return i;
        return null;
    }

//    https://www.youtube.com/watch?v=fIu_8b2n8ZM&t=174s
    static public Vector rayPlane(Vector from, Vector dir, Vector normal, Vector pos){
        Vector w = pos.c().sub(from);
        float ratio = w.dot(normal) / dir.dot(normal);
        return from.c().add(dir.c().scale(ratio));
    }
}
