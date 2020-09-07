package RT.World;

import RT.HitR;
import RT.Objects.AABB;
import RT.Objects.Hitable;
import RT.RTutils;
import RT.Ray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BVHnode extends Hitable {
    AABB Boundingbox;
    Hitable leftNode;
    Hitable rightNode;

    public BVHnode(List<Hitable> Objects, double t0, double t1){
        this.Boundingbox = new AABB();
        if (Objects.size()>2){
            int axis = (int)Math.random()*3;
            Comparator<Hitable> AxisComparator = new Comparator<Hitable>() {
                @Override
                public int compare(Hitable o1, Hitable o2) {
                    if (o1.boundingBox(t0,t1).minP.e[axis]<o2.boundingBox(t0, t1).minP.e[axis]){
                        return 1;
                    }else if (o1.boundingBox(t0,t1).minP.e[axis]>o2.boundingBox(t0, t1).minP.e[axis]){
                        return -1;
                    }else {
                        return 0;
                    }
                }
            };
            Collections.sort(Objects,AxisComparator);
            int mid = Objects.size()/2;

            leftNode = new BVHnode(Objects.subList(0,mid),t0,t1);
            rightNode = new BVHnode(Objects.subList(mid,Objects.size()),t0,t1);

            AABB LeftBB = leftNode.boundingBox(t0, t1);
            AABB RightBB = rightNode.boundingBox(t0, t1);
            AABB TmpBB = RTutils.surroundingBox(LeftBB,RightBB);
            this.Boundingbox.copyValue(TmpBB);

        }else {
            if (Objects.size()==1){
                leftNode = Objects.get(0);
                this.Boundingbox.copyValue(leftNode.boundingBox(t0,t1));
            }else if (Objects.size()==2){
                leftNode = Objects.get(0);
                rightNode = Objects.get(1);
                this.Boundingbox.copyValue(RTutils.surroundingBox(leftNode.boundingBox(t0,t1),rightNode.boundingBox(t0,t1)));
            }
        }
    }
    @Override
    public boolean hit(Ray r, double minT, double maxT, HitR rec){
        if (!Boundingbox.hit(r,minT,maxT)){
            return false;
        }else {
            boolean hitLeft = false;
            boolean hitRight = false;
            if (leftNode!=null) {
                hitLeft = leftNode.hit(r, minT, maxT, rec);
            }
            if (rightNode!=null) {
                hitRight = rightNode.hit(r, minT, hitLeft ? rec.t : maxT, rec);
            }
            return hitLeft||hitRight;
        }
    }
    @Override
    public AABB boundingBox(double t0, double t1) {
        AABB tmp = new AABB();
        tmp.copyValue(this.Boundingbox);
        return tmp;
    }



}
