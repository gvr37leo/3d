public class RayCaster {

//    https://www.youtube.com/watch?v=EZXz-uPyCyA&t=281s
    public Vector rayTriangle(Vector from, Vector dir, Vector a, Vector b, Vector c){
        Vector ab = b.c().sub(a);
        Vector cb = c.c().sub(b);
        Vector ac = c.c().sub(a);
        Vector normal = ab.cross(ac);
        Vector i = rayPlane(from, dir, normal, a);
        Vector ai = i.c().sub(a);


        Vector v = ab.c().sub(cb.project(ab));
        float baryA = 1 - (v.dot(ai) / v.dot(ab));

        return i;
    }

//    https://www.youtube.com/watch?v=fIu_8b2n8ZM&t=174s
    public Vector rayPlane(Vector from, Vector dir, Vector normal, Vector pos){
        Vector w = pos.c().sub(from);
        float ratio = w.dot(normal) / dir.dot(normal);
        return from.c().add(dir.c().scale(ratio));
    }
}
