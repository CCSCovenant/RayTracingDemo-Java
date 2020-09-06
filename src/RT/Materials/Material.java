package RT.Materials;

import RT.HitR;
import RT.Ray;
import RT.Vec3;

public abstract class Material {
    Vec3 color;
    public Material(){

    }
    public boolean scatter(Ray in, HitR rec, Vec3 color, Ray out){
        return false;
    }
}
