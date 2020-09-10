package RT.Materials;

import RT.HitR;
import RT.Ray;
import RT.Vec3;

public abstract class Material {
    public Material(){

    }
    public boolean scatter(Ray in, HitR rec, Vec3 color, Ray out){
        return false;
    }

    public Vec3 emitted(double u, double v, Vec3 p){
        return new Vec3(0,0,0);
    }
}
