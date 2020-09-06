package RT;

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
}
