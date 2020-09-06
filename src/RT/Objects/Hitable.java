package RT.Objects;

import RT.HitR;
import RT.Ray;

public abstract class Hitable {

    public Hitable(){

    }
    public boolean hit(Ray r, double minT, double maxT, HitR rec){
        return false;
    }
}