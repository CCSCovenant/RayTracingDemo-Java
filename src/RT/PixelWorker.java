package RT;


public class PixelWorker extends Thread {
    Vec3[][] buffer;
    Camera cam;
    World world;
    int start;
    int end;
    int W;
    int H;
    int depth;
    int spp;
    int pid;
    public PixelWorker(Vec3[][] buffer, Camera cam, World world, int start, int end, int W, int H, int depth,int spp,int pid){
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
            if ((start-y)%(diff/10)==0){
                System.out.println("PixelWork:["+pid+"] Progress:"+100*((double)(y-start)/diff)+"%");
            }
            for (int x=0;x<W;x++){
               for (int i=0;i<spp;i++){
                   double u = (x + Math.random()) / W;
                   double v = (y + Math.random()) / H;
                   buffer[x][y] = buffer[x][y].add(rayColor(cam.getRay(u,v),world,depth));
               }
               buffer[x][y] = buffer[x][y].div(spp);
             //  System.out.println("Color in ["+x+"] ["+y+"] is "+buffer[x][y].x()+" "+buffer[x][y].y()+" "+buffer[x][y].z());
            }
        }
        System.out.println("PixelWork:["+pid+"] finished");
    }
    public Vec3 rayColor(Ray r, World world, int depth) {
        HitR rec = new HitR();
        if (depth<=0){
            return new Vec3(0,0,0);
        }
        if (world.hit(r,0,Double.POSITIVE_INFINITY,rec)){
            Ray out = new Ray();
            Vec3 color = new Vec3();
            if (rec.m.scatter(r,rec,color,out)){
                return color.mul(rayColor(out,world,depth-1));
            }
            return new Vec3(0,0,0);
        }
        Vec3 unitDirection = r.direction.unitVector();
        double t = 0.5*(unitDirection.y()+1.0);
        Vec3 white = new Vec3(1.0,1.0,1.0);
        Vec3 blue = new Vec3(0.5,0.7,1.0);
        return white.mul(1.0-t).add(blue.mul(t));
    }



}
