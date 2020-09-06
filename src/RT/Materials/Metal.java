package RT.Materials;

import RT.HitR;
import RT.RTutils;
import RT.Ray;
import RT.Vec3;

public class Metal extends Material{
    Vec3 color;
    double fuzz;
    public Metal(){

    }
    public Metal(Vec3 color,double fuzz){
        this.color = color;
        this.fuzz = fuzz;
    }

    @Override
    public boolean scatter(Ray in, HitR rec, Vec3 color, Ray out) {
        Vec3 reflected = RTutils.reflect(in.direction.unitVector(),rec.normal);
        out.origin.copyValue(rec.p);
        out.direction.copyValue(reflected.add(RTutils.RandomInUnitSphere().mul(fuzz)));
        out.time = in.time;
        color.copyValue(this.color);
        return out.direction.dot(rec.normal)>0;
    }
}
