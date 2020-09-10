package RT.Objects;

import RT.Utils.HitR;
import RT.Utils.Ray;
import RT.Utils.Vec3;

public class Rotate extends Hitable {
    Hitable obj;
    AABB bbox;
    int axis;
    double angle;
    double sinTheta;
    double cosTheta;

    public Rotate(Hitable obj, int axis, double angle) {
        // x axis = 0 , y axis = 1 , z axis = 2
        this.obj = obj;
        this.axis = axis;
        this.angle = angle;
        this.bbox = obj.boundingBox(0, 0);

        double radians = Math.toRadians(angle);
        sinTheta = Math.sin(radians);
        cosTheta = Math.cos(radians);

        Vec3 min = new Vec3(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        Vec3 max = new Vec3(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);

        //构建旋转后的新碰撞箱
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    double x = i * bbox.maxP.x() + (1.0 - i) * bbox.maxP.x();
                    double y = j * bbox.maxP.y() + (1.0 - j) * bbox.maxP.y();
                    double z = k * bbox.maxP.z() + (1.0 - k) * bbox.maxP.z();

                    double newX = x;
                    double newY = y;
                    double newZ = z;
                    if (axis == 0) {
                        newY = cosTheta * y + sinTheta * z;
                        newZ = -sinTheta * y + cosTheta * z;
                    } else if (axis == 1) {
                        newX = cosTheta * x + sinTheta * z;
                        newZ = -sinTheta * x + cosTheta * z;
                    } else {
                        newX = cosTheta * x + sinTheta * y;
                        newY = -sinTheta * x + cosTheta * y;
                    }
                    Vec3 test = new Vec3(newX, newY, newZ);
                    for (int c = 0; c < 3; c++) {
                        min.e[c] = Math.min(test.e[c], min.e[c]);
                        max.e[c] = Math.max(test.e[c], max.e[c]);
                    }
                }
            }
        }
        this.bbox = new AABB(min, max);
    }

    @Override
    public boolean hit(Ray r, double minT, double maxT, HitR rec) {
        Vec3 o = new Vec3();
        o.copyValue(r.origin);
        Vec3 d = new Vec3();
        d.copyValue(r.direction);
        //旋转后的光线原点
        if (axis == 0) {
            o.e[1] = cosTheta * r.origin.e[1] - sinTheta * r.origin.e[2];
            o.e[2] = sinTheta * r.origin.e[1] + cosTheta * r.origin.e[2];
        } else if (axis == 1) {
            o.e[0] = cosTheta * r.origin.e[0] - sinTheta * r.origin.e[2];
            o.e[2] = sinTheta * r.origin.e[0] + cosTheta * r.origin.e[2];
        } else {
            o.e[0] = cosTheta * r.origin.e[0] - sinTheta * r.origin.e[1];
            o.e[1] = sinTheta * r.origin.e[0] + cosTheta * r.origin.e[1];
        }
        //旋转后的光线方向
        if (axis == 0) {
            d.e[1] = cosTheta * r.direction.e[1] - sinTheta * r.direction.e[2];
            d.e[2] = sinTheta * r.direction.e[1] + cosTheta * r.direction.e[2];
        } else if (axis == 1) {
            d.e[0] = cosTheta * r.direction.e[0] - sinTheta * r.direction.e[2];
            d.e[2] = sinTheta * r.direction.e[0] + cosTheta * r.direction.e[2];
        } else {
            d.e[0] = cosTheta * r.direction.e[0] - sinTheta * r.direction.e[1];
            d.e[1] = sinTheta * r.direction.e[0] + cosTheta * r.direction.e[1];
        }
        Ray rotatedRay = new Ray(o, d, r.time);
        if (!obj.hit(rotatedRay, minT, maxT, rec)) {
            return false;
        }
        Vec3 p = new Vec3();
        p.copyValue(rec.p);
        Vec3 normal = new Vec3();
        normal.copyValue(rec.normal);

        // 旋转后的命中点
        if (axis == 0) {
            p.e[1] = cosTheta * rec.p.e[1] + sinTheta * rec.p.e[2];
            p.e[2] = -sinTheta * rec.p.e[1] + cosTheta * rec.p.e[2];
        } else if (axis == 1) {
            p.e[0] = cosTheta * rec.p.e[0] + sinTheta * rec.p.e[2];
            p.e[2] = -sinTheta * rec.p.e[0] + cosTheta * rec.p.e[2];
        } else {
            p.e[0] = cosTheta * rec.p.e[0] + sinTheta * rec.p.e[1];
            p.e[1] = -sinTheta * rec.p.e[0] + cosTheta * rec.p.e[1];
        }

        //旋转后的法线
        if (axis == 0) {
            normal.e[1] = cosTheta * rec.normal.e[1] + sinTheta * rec.normal.e[2];
            normal.e[2] = -sinTheta * rec.normal.e[1] + cosTheta * rec.normal.e[2];
        } else if (axis == 1) {
            normal.e[0] = cosTheta * rec.normal.e[0] + sinTheta * rec.normal.e[2];
            normal.e[2] = -sinTheta * rec.normal.e[0] + cosTheta * rec.normal.e[2];
        } else {
            normal.e[0] = cosTheta * rec.normal.e[0] + sinTheta * rec.normal.e[1];
            normal.e[1] = -sinTheta * rec.normal.e[0] + cosTheta * rec.normal.e[1];
        }
        rec.p = p;
        rec.setFaceNormal(rotatedRay, normal);
        return true;
    }

    @Override
    public AABB boundingBox(double t0, double t1) {
        return new AABB(this.bbox.minP, this.bbox.maxP);
    }
}
