package RT.Objects;

import RT.Utils.HitR;
import RT.Materials.Material;
import RT.Utils.Ray;
import RT.Utils.Vec3;

public class Sphere extends Hitable {
    Vec3 center;
    double radius;
    Material material;
    public Sphere(){

    }
    public Sphere(Vec3 center,double radius,Material material){
        this.center = center;
        this.radius = radius;
        this.material = material;
    }
    @Override
    public boolean hit(Ray r, double minT, double maxT, HitR rec) {
        Vec3 oc = r.origin.sub(center);
        double a = r.direction.lengthSq();
        double halfB = oc.dot(r.direction);
        double c = oc.lengthSq()-(radius*radius);
        double discriminant = halfB*halfB - a*c;

        if (discriminant>0){
            double root = Math.sqrt(discriminant);
            double tmp = (-halfB - root)/a;
            if (tmp < maxT && tmp > minT){
                rec.t = tmp;
                rec.p = r.Pt(rec.t);
                Vec3 outWardNormal = rec.p.sub(center).div(radius);
                rec.setFaceNormal(r,outWardNormal);
                //获取材质的坐标
                rec.u = getShpereU((rec.p.sub(center).div(radius)));
                rec.v = getShpereV((rec.p.sub(center).div(radius)));
                //获取材料
                rec.m = this.material;
                return true;
            }
             tmp = (-halfB + root)/a;
            if (tmp < maxT && tmp > minT){
                rec.t = tmp;
                rec.p = r.Pt(rec.t);
                Vec3 outWardNormal = rec.p.sub(center).div(radius);
                rec.setFaceNormal(r,outWardNormal);

                //获取材质的坐标
                rec.u = getShpereU((rec.p.sub(center).div(radius)));
                rec.v = getShpereV((rec.p.sub(center).div(radius)));
                //获取材料
                rec.m = this.material;
                return true;
            }
        }
        return false;
    }

    @Override
    public AABB boundingBox(double t0, double t1) {
        Vec3 minP = center.sub(new Vec3(radius,radius,radius));
        Vec3 maxP = center.add(new Vec3(radius,radius,radius));
        return new AABB(minP,maxP);
    }
    public double getShpereU(Vec3 p){
        double phi = Math.atan2(p.z(),p.x());
        return 1-(phi+Math.PI)/(2*Math.PI);
    }
    public double getShpereV(Vec3 p){
        double theta = Math.asin(p.y());
        return (theta+Math.PI/2)/Math.PI;
    }
}
