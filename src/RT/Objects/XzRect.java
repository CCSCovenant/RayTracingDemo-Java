package RT.Objects;

import RT.Utils.HitR;
import RT.Materials.Material;
import RT.Utils.Ray;
import RT.Utils.Vec3;

public class XzRect extends Hitable {
    double x0;
    double x1;
    double z0;
    double z1;
    double y;
    Material m;

    public XzRect(double x0, double x1, double z0, double z1, double y, Material m) {
        this.x0 = x0;
        this.x1 = x1;
        this.z0 = z0;
        this.z1 = z1;
        this.y = y;
        this.m = m;
    }

    @Override
    public boolean hit(Ray r, double minT, double maxT, HitR rec) {
        double t = (this.y - r.origin.y()) / r.direction.y();
        if (t < minT || t > maxT) {
            return false;
        }
        double x = r.Pt(t).x();
        double z = r.Pt(t).z();
        if (x < x0 || x > x1 || z < z0 || z > z1) {
            return false;
        }
        rec.u = (x - x0) / (x1 - x0);
        rec.v = (z - z0) / (z1 - z0);
        rec.t = t;
        rec.m = this.m;
        Vec3 outWardNormal = new Vec3(0, 1, 0);
        rec.setFaceNormal(r, outWardNormal);
        rec.p = r.Pt(t);
        return true;
    }

    @Override
    public AABB boundingBox(double t0, double t1) {
        return new AABB(new Vec3(x0, y - 0.0001, z0), new Vec3(x1, y + 0.0001, z1));
    }
}
