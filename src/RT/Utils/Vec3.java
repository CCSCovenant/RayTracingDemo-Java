package RT.Utils;

public class Vec3 {
    public double[] e = new double[3];

    public Vec3() {
    }

    public Vec3(double x, double y, double z) {
        e[0] = x;
        e[1] = y;
        e[2] = z;
    }

    public double x() {
        return e[0];
    }

    public double y() {
        return e[1];
    }

    public double z() {
        return e[2];
    }

    public double length() {
        return Math.sqrt(this.lengthSq());
    }

    public double lengthSq() {
        return e[0] * e[0] + e[1] * e[1] + e[2] * e[2];
    }

    public double dot(Vec3 u) {
        return e[0] * u.e[0] + e[1] * u.e[1] + e[2] * u.e[2];
    }

    public Vec3 cross(Vec3 u) {
        return new Vec3(e[1] * u.e[2] - e[2] * u.e[1], e[2] * u.e[0] - e[0] * u.e[2], e[0] * u.e[1] - e[1] * u.e[0]);
    }

    public Vec3 add(Vec3 u) {
        return new Vec3(e[0] + u.e[0], e[1] + u.e[1], e[2] + u.e[2]);
    }

    public Vec3 sub(Vec3 u) {
        return new Vec3(e[0] - u.e[0], e[1] - u.e[1], e[2] - u.e[2]);
    }

    public Vec3 negative() {
        return new Vec3(-e[0], -e[1], -e[2]);
    }

    public Vec3 mul(Vec3 u) {
        return new Vec3(e[0] * u.e[0], e[1] * u.e[1], e[2] * u.e[2]);
    }

    public Vec3 mul(double t) {
        return new Vec3(e[0] * t, e[1] * t, e[2] * t);
    }

    public Vec3 div(double t) {
        return this.mul(1 / t);
    }

    public Vec3 unitVector() {
        return this.div(this.length());
    }

    public void copyValue(Vec3 u) {
        this.e[0] = u.e[0];
        this.e[1] = u.e[1];
        this.e[2] = u.e[2];
    }

}
