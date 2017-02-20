public class Matrix {
    float[][] els;

    Matrix(){
        els = new float[3][3];
    }
    Matrix(float[][] els){
        this.els = els;
    }

    static Matrix rotZ(float theta){
        float cosTheta = (float)Math.cos((double) theta);
        float sinTheta = (float)Math.sin((double) theta);
        return new Matrix(new float[][]{
                {cosTheta,  sinTheta,    0},
                {-sinTheta, cosTheta,   0},
                {0,         0,          1},
        });
    }
    static Matrix rotY(float theta){
        float cosTheta = (float)Math.cos((double) theta);
        float sinTheta = (float)Math.sin((double) theta);
        return new Matrix(new float[][]{
                {cosTheta,  0 ,     -sinTheta},
                {0,         1,      0},
                {sinTheta, 0,      cosTheta},
        });
    }
    static Matrix rotX(float theta){
        float cosTheta = (float)Math.cos((double) theta);
        float sinTheta = (float)Math.sin((double) theta);
        return new Matrix(new float[][]{
                {1,         0,                  0},
                {0,         cosTheta,           sinTheta},
                {0,         -sinTheta,          cosTheta},
        });
    }

    Vector mult(Vector3 lhs){
        float xp = lhs.x * els[0][0] + lhs.y * els[1][0] + lhs.z * els[2][0];
        float yp = lhs.x * els[0][1] + lhs.y * els[1][1] + lhs.z * els[2][1];
        float zp = lhs.x * els[0][2] + lhs.y * els[1][2] + lhs.z * els[2][2];
        lhs.x = xp;
        lhs.y = yp;
        lhs.z = zp;
        return lhs;
    }

    final Matrix mult(Matrix rhs){
        Matrix result = new Matrix();
        for(int row = 0; row < 3; row++){
            for(int col = 0; col < 3; col++){
                result.els[row][col] = els[row][0] * rhs.els[0][col] + els[row][1] * rhs.els[1][col] + els[row][2] * rhs.els[2][col];
            }
        }
        return result;
    }
}
