package RT.World;

import RT.Utils.RTutils;
import RT.Utils.Vec3;
import RT.Utils.Ray;

public class Camera {
    Vec3 origin;
    Vec3 llcorner ;
    Vec3 horizontal;
    Vec3 vertical ;
    Vec3 w;
    Vec3 u;
    Vec3 v;
    double lensRadius;
    double time0;
    double time1;

    public Camera(double vfov, double ratio, double aperture, double focusDist, double t0, double t1,Vec3 origin, Vec3 lookat, Vec3 vup){
        double theta = Math.toRadians(vfov);
        double h = Math.tan(theta/2);
        double vh = 2.0*h;
        double vw = vh*ratio;

        w = origin.sub(lookat).unitVector();
        u = vup.cross(w).unitVector();
        v = w.cross(u);

        time0 = t0;
        time1 = t1;

        this.origin = origin;
        horizontal = u.mul(vw).mul(focusDist);
        vertical = v.mul(vh).mul(focusDist);

        llcorner = origin.sub(horizontal.div(2)).sub(vertical.div(2)).sub(w.mul(focusDist));

        lensRadius = aperture/2;


    }
    public Ray getRay(double s, double t ){
        Vec3 rd = RTutils.RandomInUnitDisk().mul(lensRadius);
        Vec3 offset = u.mul(rd.x()).add(v.mul(rd.y()));
        return new Ray(origin.add(offset),llcorner.add(horizontal.mul(s)).add(vertical.mul(t).sub(origin).sub(offset)),time0+Math.random()*(time1-time0));

    }
}
