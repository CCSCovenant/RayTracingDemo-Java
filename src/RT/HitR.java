package RT;


import RT.Materials.Material;

public class HitR {
    public Vec3 p;
    public double t;
    public Vec3 normal;
    public Material m;
    public boolean front;
    public HitR(){
        p = new Vec3();
        t = 0;
        normal = new Vec3();
        front = true;
    }
    public HitR(Vec3 p, Vec3 normal, double t){
        this.p = p;
        this.normal = normal;
        this.t = t;
        front = true;
    }
    public HitR(HitR R){
        this.p = R.p;
        this.normal = R.normal;
        this.t = R.t;
        this.front = R.front;
    }
    public void setFaceNormal(Ray r, Vec3 outWardNormal){
        front = r.direction.dot(outWardNormal)<0;
        if (front){
            normal = outWardNormal;
        }else {
            normal = outWardNormal.negative();
        }
    }
}