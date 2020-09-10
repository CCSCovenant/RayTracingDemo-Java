package RT.Objects;

import RT.Utils.HitR;
import RT.Materials.Material;
import RT.Utils.Ray;
import RT.Utils.Vec3;

public class XyRect extends Hitable {
    double x0;
    double x1;
    double y0;
    double y1;
    double z;
    Material m;

    public XyRect(double x0, double x1, double y0, double y1, double z, Material m) {
        this.x0 = x0;
        this.x1 = x1;
        this.y0 = y0;
        this.y1 = y1;
        this.z = z;
        this.m = m;
    }

    @Override
    public boolean hit(Ray r, double minT, double maxT, HitR rec) {
        double t = (this.z - r.origin.z()) / r.direction.z();
        if (t < minT || t > maxT) {
            return false;
        }
        double x = r.Pt(t).x();
        double y = r.Pt(t).y();
        if (x < x0 || x > x1 || y < y0 || y > y1) {
            return false;
        }
        rec.u = (x - x0) / (x1 - x0);
        rec.v = (y - y0) / (y1 - y0);
        rec.t = t;
        rec.m = this.m;
        Vec3 outWardNormal = new Vec3(0, 0, 1);
        rec.setFaceNormal(r, outWardNormal);
        rec.p = r.Pt(t);
        return true;
    }

    @Override
    public AABB boundingBox(double t0, double t1) {
        return new AABB(new Vec3(x0, y0, z - 0.0001), new Vec3(x1, y1, z - 0.0001));
    }
}