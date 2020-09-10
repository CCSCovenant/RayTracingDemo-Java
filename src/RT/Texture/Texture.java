package RT.Texture;

import RT.Utils.Vec3;

public abstract class Texture {

    public Texture() {

    }

    public Vec3 Color(double u, double v, Vec3 p) {
        return new Vec3(1, 1, 1);
    }
}
