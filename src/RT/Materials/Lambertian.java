package RT.Materials;

import RT.*;

public class Lambertian extends Material {
    Vec3 color;
    public Lambertian(Vec3 color){
        this.color = color;
    }
    @Override
    public boolean scatter(Ray in, HitR rec, Vec3 color, Ray out){
        Vec3 direction = rec.normal.add(RTutils.RandomUnitVector());
        out.origin = rec.p;
        out.direction = direction;
        out.time = in.time;
        color.copyValue(this.color);

        return true;
    }
}
