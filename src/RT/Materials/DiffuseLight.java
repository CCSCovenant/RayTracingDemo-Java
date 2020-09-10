package RT.Materials;

import RT.HitR;
import RT.RTutils;
import RT.Ray;
import RT.Texture.Texture;
import RT.Vec3;

public class DiffuseLight extends Material {
    Texture texture;
    public DiffuseLight(Texture texture){
        this.texture = texture;
    }
    @Override
    public Vec3 emitted(double u, double v, Vec3 p){
        return texture.Color(u,v,p);
    }
}
