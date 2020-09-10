package RT.Materials;

import RT.Utils.HitR;
import RT.Utils.RTutils;
import RT.Utils.Ray;
import RT.Texture.Texture;
import RT.Utils.Vec3;

public class Metal extends Material{
    Texture texture;
    double fuzz;
    public Metal(){

    }
    public Metal(Texture texture,double fuzz){
        this.texture = texture;
        this.fuzz = fuzz;
    }

    @Override
    public boolean scatter(Ray in, HitR rec, Vec3 color, Ray out) {
        Vec3 reflected = RTutils.reflect(in.direction.unitVector(),rec.normal);
        out.origin.copyValue(rec.p);
        out.direction.copyValue(reflected.add(RTutils.RandomInUnitSphere().mul(fuzz)));
        out.time = in.time;
        color.copyValue(texture.Color(rec.u,rec.v,rec.p));
        return out.direction.dot(rec.normal)>0;
    }
}
