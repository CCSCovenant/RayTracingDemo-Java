package RT.Texture;

import RT.Utils.Vec3;

public class RandomGrayTexture extends Texture {
    public RandomGrayTexture() {

    }

    @Override
    public Vec3 Color(double u, double v, Vec3 p) {
        double R = Math.random();
        return new Vec3(R, R, R);
    }
}
