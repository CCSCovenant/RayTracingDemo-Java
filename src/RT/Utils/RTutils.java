package RT.Utils;

import RT.Objects.AABB;
import RT.Utils.Vec3;

public final class RTutils {
    public static Vec3 RandomInUnitSphere(){
        while (true){
            Vec3 p = new Vec3(Math.random()*2-1,Math.random()*2-1,Math.random()*2-1);
            if (p.lengthSq()< 1){
                return p;
            }
        }
    }
    public static Vec3 RandomUnitVector(){
        double a = Math.random()*Math.PI*2;
        double z = Math.random()*2-1;
        double r = Math.sqrt(1-z*z);
        return new Vec3(r*Math.cos(a),r*Math.sin(a),z);
    }
    public static Vec3 reflect(Vec3 v, Vec3 n){
        return v.sub(n.mul(v.dot(n)*2));
    }
    public static Vec3 refract(Vec3 uv, Vec3 n, double etaRadio){
        double cosTheta = uv.negative().dot(n);
        Vec3 rayOutPerp = uv.add(n.mul(cosTheta)).mul(etaRadio);
        Vec3 rayOutParallel = n.mul(-Math.sqrt(Math.abs(1.0-rayOutPerp.lengthSq())));
        return rayOutPerp.add(rayOutParallel);
    }
    public static double schlick(double cosine,double refindex){
        double r0 = (1-refindex)/(1+refindex);
        r0 = r0*r0;
        return  r0+(1-r0)*Math.pow((1-cosine),5);
    }
    public static Vec3 RandomInUnitDisk() {
        while (true) {
            Vec3 p = new Vec3(Math.random() * 2 - 1, Math.random() * 2 - 1, 0);
            if (p.lengthSq() < 1) {
                return p;
            }
        }
    }
    public static AABB surroundingBox(AABB box0, AABB box1){
        if (box0 == null){
            if (box1 == null){
                return null;
            }
            AABB tmp = new AABB();
            tmp.copyValue(box1);
            return tmp;
        }
        if (box1 == null){
            if (box0 == null){
                return null;
            }
            AABB tmp = new AABB();
            tmp.copyValue(box0);
            return tmp;
        }
        Vec3 min = new Vec3(Math.min(box0.minP.x(),box1.minP.x()),Math.min(box0.minP.y(),box1.minP.y()),Math.min(box0.minP.z(),box1.minP.z()));
        Vec3 max = new Vec3(Math.max(box0.maxP.x(),box1.maxP.x()),Math.max(box0.maxP.x(),box1.maxP.x()),Math.max(box0.maxP.x(),box1.maxP.x()));
        return new AABB(min,max);
    }
    public static double clamp(double x,double min,double max){
        if (x<min){
            return min;
        }
        if (x>max){
            return max;
        }
        return x;
    }
    // 输入两个AABB 输出一个将其包围的AABB;
}
