public class RayCaster {

//    https://www.youtube.com/watch?v=EZXz-uPyCyA&t=281s
//    http://blackpawn.com/texts/pointinpoly/
    static public Vector3 rayTriangle(Vector3 from, Vector3 dir, Vector3 a, Vector3 b, Vector3 c){
        Vector3 ab = (Vector3) b.c().sub(a);
        Vector3 ac = (Vector3) c.c().sub(a);

        Vector3 normal = ab.cross(ac);
        Vector3 i = rayPlane(from, dir, normal, a);

        Vector3 bary = barycenter(a,b,c,i);

        if((bary.x >= 0) && (bary.y >= 0) && (bary.z >= 0))return i;
        return null;
    }

    static public Vector3 barycenter(Vector3 a, Vector3 b, Vector3 c, Vector3 p){
        Vector ab = b.c().sub(a);
        Vector ac = c.c().sub(a);
        Vector ap = p.c().sub(a);

        float dot00 = ac.dot(ac);
        float dot01 = ac.dot(ab);
        float dot02 = ac.dot(ap);
        float dot11 = ab.dot(ab);
        float dot12 = ab.dot(ap);

        float invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
        float u = (dot11 * dot02 - dot01 * dot12) * invDenom;
        float v = (dot00 * dot12 - dot01 * dot02) * invDenom;

        return new Vector3(u,v,1-(u+v));

    }

    static public Vector3 barycenter2(Vector3 a, Vector3 b, Vector3 c, Vector3 p){
        Vector3 ab = (Vector3) b.c().sub(a);
        Vector3 ac = (Vector3) c.c().sub(a);
        Vector3 ap = (Vector3) p.c().sub(a);
        Vector3 cb = (Vector3) b.c().sub(c);

        Vector n = ac.cross(ab);
        float area = n.length();

        float areaABP = ap.cross(ab).length();
        float areaACP = ap.cross(ac).length();
        float areaCBP = ap.cross(cb).length();

        float w = areaABP / area;
        float v = areaACP / area;
        float u = areaCBP / area;

        return new Vector3(u,v,w);
    }

//    https://www.youtube.com/watch?v=fIu_8b2n8ZM&t=174s
    static public Vector3 rayPlane(Vector3 from, Vector dir, Vector normal, Vector pos){
        Vector w = pos.c().sub(from);
        float ratio = w.dot(normal) / dir.dot(normal);
        return (Vector3) from.c().add(dir.c().scale(ratio));
    }
}
