package RT.Objects;

import RT.Utils.HitR;
import RT.Utils.Ray;
import RT.Utils.Vec3;

public class Translate extends Hitable{
    Hitable obj;
    Vec3 offset;
    public Translate(Hitable obj, Vec3 offset){
        this.obj = obj;
        this.offset = offset;
    }
    @Override
    public AABB boundingBox(double t0, double t1) {
        if (obj.boundingBox(t0, t1)==null){
            return null;
        }
        return new AABB(obj.boundingBox(t0, t1).minP.add(offset),obj.boundingBox(t0, t1).maxP.add(offset));
    }
    @Override
    public boolean hit(Ray r, double minT, double maxT, HitR rec) {
        Ray deltaR = new Ray(r.origin.sub(offset),r.direction,r.time);
        if (!obj.hit(deltaR,minT,maxT,rec)){
            return false;
        }
        rec.p = rec.p.add(offset);
        rec.setFaceNormal(deltaR,rec.normal);
        return true;
    }
}
