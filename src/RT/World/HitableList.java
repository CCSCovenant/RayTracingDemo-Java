package RT.World;

import RT.HitR;
import RT.Objects.Hitable;
import RT.Ray;

import java.util.ArrayList;

public class HitableList {
    ArrayList<Hitable> elements;

    public HitableList(){
        elements = new ArrayList<>();
    }
    public void add(Hitable object){
        elements.add(object);
    }
    public void clear(){
        elements = new ArrayList<>();
    }
    public boolean hit(Ray r, double minT, double maxT, HitR rec){
        boolean hit = false;
        double closestT = maxT;
        for (int i=0;i<elements.size();i++){
            if (elements.get(i).hit(r,minT,closestT,rec)){
                hit = true;
                closestT = rec.t;
            }
        }
        return hit;
    }
}
