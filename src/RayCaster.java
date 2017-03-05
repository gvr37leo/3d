public class RayCaster {

    public static float[] barycenter(Vecf a, Vecf b, Vecf c, Vecf p){
        Vecf ap = p.c().sub(a);
        Vecf bp = p.c().sub(b);
        Vecf cp = p.c().sub(c);

        float area = Math.abs(c.c().sub(a).det(b.c().sub(a)));
        float ara = Math.abs(bp.det(cp)) / area;
        float arb = Math.abs(cp.det(ap)) / area;
        float arc = Math.abs(ap.det(bp)) / area;

        return new float[]{ara, arb, 1 - ara - arb};
    }
}
