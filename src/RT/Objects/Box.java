package RT.Objects;

import RT.HitR;
import RT.Materials.Material;
import RT.Ray;
import RT.Vec3;
import RT.World.HitableList;

public class Box extends Hitable{
    HitableList planes;
    Vec3 minP;
    Vec3 maxP;

    public Box(Vec3 minP, Vec3 maxP, Material[] materials){
        this.minP = minP;
        this.maxP = maxP;
        planes = new HitableList();
        // Y max min X max min Z max min;
        planes.add(new XzRect(minP.x(),maxP.x(),minP.z(),maxP.z(), minP.y(),materials[0]));
        planes.add(new XzRect(minP.x(),maxP.x(),minP.z(),maxP.z(), maxP.y(),materials[1]));
        // Xz平面, 方块的上下表面.
        planes.add(new YzRect(minP.y(),maxP.y(),minP.z(),maxP.z(), minP.x(),materials[2]));
        planes.add(new YzRect(minP.y(),maxP.y(),minP.z(),maxP.z(), maxP.x(),materials[3]));
        // Yz平面, 方块的左右表面.
        planes.add(new XyRect(minP.x(),maxP.x(),minP.y(),maxP.y(), minP.z(),materials[4]));
        planes.add(new XyRect(minP.x(),maxP.x(),minP.y(),maxP.y(), maxP.z(),materials[5]));
        // Xy平面, 方块的前后表面.
    }
    @Override
    public boolean hit(Ray r, double minT, double maxT, HitR rec) {
        return planes.hit(r,minT,maxT,rec);
    }
    @Override
    public AABB boundingBox(double t0, double t1) {
        return new AABB(minP,maxP);
    }
}
/**
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 * */
