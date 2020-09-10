package RT.Objects;

import RT.Utils.HitR;
import RT.Materials.Material;
import RT.Utils.RTutils;
import RT.Utils.Ray;
import RT.Utils.Vec3;

public class MovingSphere extends Hitable {
    Vec3 center0;
    Vec3 center1;
    double radius;
    double time0;
    double time1;
    Material material;

    public MovingSphere() {

    }

    public MovingSphere(Vec3 center0, Vec3 center1, double radius, double t0, double t1, Material material) {
        this.center0 = center0;
        this.center1 = center1;
        this.radius = radius;
        this.material = material;
        this.time0 = t0;
        this.time1 = t1;
    }

    public Vec3 getCenter(double time) {
        return center0.add(center1.sub(center0).mul((time - time0) / (time1 - time0)));
    }

    @Override
    public boolean hit(Ray r, double minT, double maxT, HitR rec) {
        Vec3 oc = r.origin.sub(getCenter(r.getTime()));
        double a = r.direction.lengthSq();
        double halfB = oc.dot(r.direction);
        double c = oc.lengthSq() - (radius * radius);
        double discriminant = halfB * halfB - a * c;

        if (discriminant > 0) {
            double root = Math.sqrt(discriminant);
            double tmp = (-halfB - root) / a;
            if (tmp < maxT && tmp > minT) {
                rec.t = tmp;
                rec.p = r.Pt(rec.t);
                Vec3 outWardNormal = rec.p.sub(getCenter(r.getTime()).div(radius));
                rec.setFaceNormal(r, outWardNormal);
                rec.m = this.material;
                return true;
            }
            tmp = (-halfB + root) / a;
            if (tmp < maxT && tmp > minT) {
                rec.t = tmp;
                rec.p = r.Pt(rec.t);
                Vec3 outWardNormal = rec.p.sub(getCenter(r.getTime()).div(radius));
                rec.setFaceNormal(r, outWardNormal);
                rec.m = this.material;
                return true;
            }
        }
        return false;
    }

    @Override
    public AABB boundingBox(double t0, double t1) {
        AABB AABBt0 = new AABB(getCenter(t0).sub(new Vec3(radius, radius, radius)), getCenter(t0).add(new Vec3(radius, radius, radius)));
        AABB AABBt1 = new AABB(getCenter(t1).sub(new Vec3(radius, radius, radius)), getCenter(t1).add(new Vec3(radius, radius, radius)));
        return RTutils.surroundingBox(AABBt0, AABBt1);
    }
}
