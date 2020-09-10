package RT.Texture;

import RT.Utils.Vec3;

public class CheckerTexture extends Texture {
    Vec3 odd;
    Vec3 even;
    public CheckerTexture(Vec3 odd,Vec3 even){
        this.even = even;
        this.odd = odd;
    }
    @Override
    public Vec3 Color(double u, double v, Vec3 p){
        double sines = Math.sin(10*p.x())*Math.sin(10*p.y())*Math.sin(10*p.z());
        if (sines < 0){
            return odd;
        }else {
            return even;
        }
    }
}
