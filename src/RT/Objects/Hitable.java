package RT.Objects;

import RT.Utils.HitR;
import RT.Utils.Ray;

public abstract class Hitable {

    public Hitable() {

    }

    public boolean hit(Ray r, double minT, double maxT, HitR rec) {
        return false;
    }
    // 判断光线 r 在时间 [minT,maxT] 是否和物体相交

    public AABB boundingBox(double t0, double t1) {
        return null;
    }


    // 返回在给定时间
    // t0, t1: 对于一个移动的物体来说 需要知道t0-t1占用的AABB
    // output: 作为参数传入, 被修改后使用
    // 不是所有的object都有碰撞箱, e.g: 无限平面
}