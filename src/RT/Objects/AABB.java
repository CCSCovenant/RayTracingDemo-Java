package RT.Objects;

import RT.Ray;
import RT.Vec3;

public class AABB {
    public Vec3 minP; // 坐标较小的点
    public Vec3 maxP; // 坐标较大的点
              // 对角上两个点定义一个AABB 正方体
    public AABB(){
        this.minP = new Vec3();
        this.maxP = new Vec3();
    }
    public AABB(Vec3 minP, Vec3 maxP){
        this.minP = minP;
        this.maxP = maxP;
    }

    public boolean hit(Ray r, double minT, double maxT){ // 碰撞检测
        double Min = minT;
        double Max = maxT;
        for (int i = 0; i < 3; i++){
            double invD = 1.0/r.direction.e[i];
            double t0 = (minP.e[i] - r.origin.e[i])*invD;
            double t1 = (maxP.e[i] - r.origin.e[i])*invD;
            // 计算命中的时间 t0和t1
            if (invD < 0){
                double tmp = t0;
                t0 = t1;
                t1 = tmp;
            }
            Min = t0 > Min ? t0 : Min; // minT = Math.max(t0,minT)
            Max = t1 < Max ? t1 : Max; // maxT = Math.max(t0,maxT)

            if (maxT <= minT){
                return false;
            }
        }
        return true;
    }
    public void copyValue( AABB target){
        this.minP.copyValue(target.minP);
        this.maxP.copyValue(target.maxP);
    }

}
