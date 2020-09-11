package RT.Materials;

import RT.Utils.HitR;
import RT.Utils.RTutils;
import RT.Utils.Ray;
import RT.Utils.Vec3;

public class Dielectric extends Material {
    double refIndex;
    double fuzz;
    Vec3 color;

    public Dielectric() {

    }

    public Dielectric(double refIndex, double fuzz) {
        this.refIndex = refIndex;
        this.fuzz = fuzz;
        this.color = new Vec3(1.0,1.0,1.0);
    }
    public Dielectric(double refIndex, double fuzz,Vec3 color) {
        this.refIndex = refIndex;
        this.fuzz = fuzz;
        this.color = color;
    }

    @Override
    public boolean scatter(Ray in, HitR rec, Vec3 color, Ray out) {
        color.copyValue(this.color);
        double etaiOverEtat = rec.front ? (1.0 / refIndex) : refIndex;
        Vec3 unitDirection = in.direction.unitVector();
        double cosTheta = Math.min(unitDirection.negative().dot(rec.normal), 1.0);
        double sinTheta = Math.sqrt(1.0 - cosTheta * cosTheta);
        out.time = in.time;

        if (etaiOverEtat * sinTheta > 1.0) {
            Vec3 reflected = RTutils.reflect(unitDirection, rec.normal);
            out.origin.copyValue(rec.p);
            out.direction.copyValue(reflected.add(RTutils.RandomInUnitSphere().mul(fuzz)));
            return true;
        }
        double reflectProb = RTutils.schlick(cosTheta, etaiOverEtat);
        if (Math.random() < reflectProb) {
            Vec3 reflected = RTutils.reflect(unitDirection, rec.normal);
            out.origin.copyValue(rec.p);
            out.direction.copyValue(reflected.add(RTutils.RandomInUnitSphere().mul(fuzz)));
            return true;
        }
        Vec3 refracted = RTutils.refract(unitDirection, rec.normal, etaiOverEtat);
        out.origin.copyValue(rec.p);
        out.direction.copyValue(refracted);
        return true;
    }
}
