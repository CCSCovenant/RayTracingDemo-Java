package RT;

public class Ray {
    public Vec3 origin;
    public Vec3 direction;
    public double time;
    public Ray(){
        this.direction = new Vec3();
        this.origin = new Vec3();
        this.time = 0;
    }
    public Ray(Vec3 o, Vec3 d,double time){
        this.direction = d;
        this.origin = o;
        this.time = time;
    }
    public Vec3 Pt(double t){
        return origin.add(direction.mul(t));
    }
    public double getTime(){
        return time;
    }
}