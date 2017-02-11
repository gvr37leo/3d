public class Mesh {
    Vector[] vertices;
    int[] edges;
    int[] faces;

    static Mesh generateTriangle(){
        Mesh triangle = new Mesh();
        float f = 0.5f;
        triangle.vertices = new Vector[]{
                new Vector(0,1,0),
                new Vector(1,-1,0),
                new Vector(-1,-1,0),
        };
        triangle.edges = new int[]{
            0,1, 1,2, 2,0
        };
        triangle.faces = new int[]{
            0,1,2
        };
        return triangle;
    }

    static Mesh generateCube(){
        float f = 0.5f;
        Mesh cube = new Mesh();
        cube.vertices = new Vector[]{
                new Vector(-f,f,f),
                new Vector(f,f,f),
                new Vector(f,f,-f),
                new Vector(-f,f,-f),

                new Vector(-f,-f,f),
                new Vector(f,-f,f),
                new Vector(f,-f,-f),
                new Vector(-f,-f,-f),
        };
        cube.edges = new int[]{
                0,1,  1,2,  2,3,  3,0,
                0,4,  1,5,  2,6,  3,7,
                4,5,  5,6,  6,7,  7,4,
        };
        cube.faces = new int[]{
                0,1,2, 0,3,2,

                3,7,6, 3,6,2,
                2,6,5, 2,5,1,
                0,1,4, 1,4,5,
                0,3,4, 3,4,7,

                4,7,6, 4,6,5
        };
        return cube;
    }

    Mesh rot(Vector v){
        Matrix m = Matrix.rotX(v.x).mult(Matrix.rotY(v.y)).mult(Matrix.rotZ(v.z));
        for(Vector vertex : vertices){
            m.mult(vertex);
        }
        return this;
    }

    Mesh add(Vector v){
        for(Vector vertex : vertices)vertex.add(v);
        return this;
    }
}
