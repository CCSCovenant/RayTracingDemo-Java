package RT.Objects;

import RT.Utils.HitR;
import RT.Materials.Material;
import RT.Utils.Ray;
import RT.Utils.Vec3;

public class YzRect extends Hitable {
    double y0;
    double y1;
    double z0;
    double z1;
    double x;
    boolean positive;
    Material m;

    public YzRect(double y0, double y1, double z0, double z1, double x,boolean positive, Material m) {
        this.y0 = y0;
        this.y1 = y1;
        this.z0 = z0;
        this.z1 = z1;
        this.x = x;
        this.positive = positive;
        this.m = m;
    }

    @Override
    public boolean hit(Ray r, double minT, double maxT, HitR rec) {
        double t = (this.x - r.origin.x()) / r.direction.x();
        if (t < minT || t > maxT) {
            return false;
        }
        double y = r.Pt(t).y();
        double z = r.Pt(t).z();
        if (y < y0 || y > y1 || z < z0 || z > z1) {
            return false;
        }
        rec.u = (x - y0) / (y1 - y0);
        rec.v = (z - z0) / (z1 - z0);
        rec.t = t;
        rec.m = this.m;
        Vec3 outWardNormal;
        if (positive) {
            outWardNormal = new Vec3(1, 0, 0);
        }else {
            outWardNormal = new Vec3(-1, 0, 0);
        }
        rec.setFaceNormal(r, outWardNormal);
        rec.p = r.Pt(t);
        return true;
    }

    @Override
    public AABB boundingBox(double t0, double t1) {
        return new AABB(new Vec3(x - 0.0001, y0, z0), new Vec3(x + 0.0001, y1, z1));
    }
}