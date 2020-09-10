package RT.Materials;

import RT.Texture.Texture;
import RT.Utils.RTutils;
import RT.Utils.Vec3;
import RT.Utils.HitR;
import RT.Utils.Ray;

public class Lambertian extends Material {
    Texture texture;

    public Lambertian(Texture texture) {
        this.texture = texture;
    }

    @Override
    public boolean scatter(Ray in, HitR rec, Vec3 color, Ray out) {
        Vec3 direction = rec.normal.add(RTutils.RandomUnitVector());
        out.origin = rec.p;
        out.direction = direction;
        out.time = in.time;
        color.copyValue(texture.Color(rec.u, rec.v, rec.p));
        return true;
    }
}
