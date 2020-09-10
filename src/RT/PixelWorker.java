package RT;


import RT.World.BVHnode;
import RT.World.HitableList;

public class PixelWorker extends Thread {
    Vec3[][] buffer;
    Camera cam;
    BVHnode world;
    int start;
    int end;
    int W;
    int H;
    int depth;
    int spp;
    int pid;
    public PixelWorker(Vec3[][] buffer, Camera cam, BVHnode world, int start, int end, int W, int H, int depth, int spp, int pid){
        this.buffer = buffer;
        this.cam = cam;
        this.world = world;
        this.start = start;
        this.end = end;
        this.W = W;
        this.H = H;
        this.depth = depth;
        this.spp = spp;
        this.pid = pid;
    }
    @Override
    public void run() {
        int diff = end-start;
        for (int y =start;y<end;y++){
           // System.out.println("Rendering Line:"+y);
            //long start = System.currentTimeMillis();
            /*if ((start-y)%(diff/10)==0){
                System.out.println("PixelWork:["+pid+"] Progress:"+100*((double)(y-start)/diff)+"%");
            }*/
            for (int x=0;x<W;x++){
               for (int i=0;i<spp;i++){
                   double u = (x + Math.random()) / W;
                   double v = (y + Math.random()) / H;
                   Vec3 color = rayColor(cam.getRay(u,v),world,depth);
                   buffer[x][y] = buffer[x][y].add(color);
               }
               buffer[x][y] = buffer[x][y].div(spp);
             //  System.out.println("Color in ["+x+"] ["+y+"] is "+buffer[x][y].x()+" "+buffer[x][y].y()+" "+buffer[x][y].z());
            }
            //long end = System.currentTimeMillis();
           // System.out.println("Line "+y+" finished in "+(end-start)+" ms");

        }
        System.out.println("PixelWork:["+pid+"] finished");
    }
    public Vec3 rayColor(Ray r, BVHnode world, int depth) {
        Vec3 BGcolor = new Vec3(0,0,0);
        HitR rec = new HitR();
        if (depth<=0){
            return new Vec3(0,0,0);
        }
        if (!world.hit(r,0.001,Double.POSITIVE_INFINITY,rec)) {
            return BGcolor;
        }
        Ray out = new Ray();
        Vec3 attenuation = new Vec3();
        Vec3 emitted = rec.m.emitted(rec.u,rec.v,rec.p);
        if (!rec.m.scatter(r,rec,attenuation,out)){
                return emitted;
        }
        return emitted.add(attenuation.mul(rayColor(out,world,depth-1)));
    }




}
