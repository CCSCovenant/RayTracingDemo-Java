package RT.Texture;

import RT.Utils.Vec3;

public class PureColorTexture extends Texture {
    Vec3 color;

    public PureColorTexture(Vec3 color) {
        this.color = color;
    }

    @Override
    public Vec3 Color(double u, double v, Vec3 p) {
        return this.color;
    }
}
