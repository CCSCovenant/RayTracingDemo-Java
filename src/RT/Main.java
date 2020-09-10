package RT;

import RT.Materials.*;
import RT.Objects.*;
import RT.Materials.Lambertian;
import RT.Texture.ImageTexture;
import RT.Texture.PureColorTexture;
import RT.World.BVHnode;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("RayTracing Test");
        Group root = new Group();
        javafx.scene.canvas.Canvas canvas = new Canvas(600, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Render(gc);

        root.getChildren().add(canvas);
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void Render(GraphicsContext gc) throws IOException {
        int W = 600;
        int H = 600;
        int Thread = 8;
        Vec3[][] PixelBuffer = new Vec3[W][H];
        PixelWorker[] WorkerPool = new PixelWorker[Thread];


        double t0 = 0;
        double t1 = 0;
          ArrayList<Hitable> scene = CornellBox();
          Camera cam = CornellBoxCam();
        //HitableList world = TestScene();
        BVHnode world = new BVHnode(scene,t0,t1);


        int spp = 50;
        int MaxDepth = 50;
        WritableImage image = new WritableImage(W, H);
        PixelWriter pw = image.getPixelWriter();
        for (int y=0;y<H;y++){
            for (int x=0;x<W;x++){
                PixelBuffer[x][y] = new Vec3();
            }
        }
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < Thread; i++) {
            WorkerPool[i] = new PixelWorker(PixelBuffer,cam,world,i*(H/Thread),Math.min(H,(i+1)*(H/Thread)),W,H,MaxDepth,spp,i);
            WorkerPool[i].start();
        }
        System.out.println("Writing PixelBuffer into image");
        for (int i=0;i<Thread;i++){
            try {
                WorkerPool[i].join();
            }catch (Exception e){

            }
        }
        /*
        for (int y =0;y<H;y++){
            System.out.println("Rendering Line:"+y);
            for (int x=0;x<W;x++){
                for (int i=0;i<spp;i++){
                    double u = (x + Math.random()) / W;
                    double v = (y + Math.random()) / H;
                    PixelBuffer[x][y] = PixelBuffer[x][y].add(rayColor(cam.getRay(u,v),world,MaxDepth));
                }
                PixelBuffer[x][y] = PixelBuffer[x][y].div(spp);
                //  System.out.println("Color in ["+x+"] ["+y+"] is "+buffer[x][y].x()+" "+buffer[x][y].y()+" "+buffer[x][y].z());
            }
        }
        */
        long endTime = System.currentTimeMillis();

        System.out.println("All PixelWorkers Finished");
        System.out.println("Render finished in "+(endTime-startTime)+"ms");
        BufferedImage IOimage = new BufferedImage(W, H, BufferedImage.TYPE_4BYTE_ABGR);

        for (int y=H-1;y>=0;y--){
            for (int x=0;x<W;x++){
                double Ar = Math.sqrt(PixelBuffer[x][y].x());
                double Ag = Math.sqrt(PixelBuffer[x][y].y());
                double Ab = Math.sqrt(PixelBuffer[x][y].z());



                int Red = clamp(0,255,(int)(Ar*255));
                int Green = clamp(0,255,(int)(Ag*255));
                int Blue = clamp(0,255,(int)(Ab*255));
                pw.setArgb(x,(H-(y+1)),new Color(Red,Green,Blue,255).getRGB());
                IOimage.setRGB(x,(H-(y+1)),new Color(Red,Green,Blue,255).getRGB());
            }
        }
        ImageIO.write(IOimage,"png",new File("C:\\Users\\pzeug\\RT\\src\\output\\CornellBox-WithBoxes.png"));
        gc.drawImage(image, 0, 0);
    }

    public int clamp(int min,int max ,int n){
        if (n<min){
            return min;
        }else if (n>max){
            return max;
        }else {
            return n;
        }
    }
    public Camera CornellBoxCam(){
        Vec3 lookfrom = new Vec3(278,278,-800);
        Vec3 lookat = new Vec3(278 ,278,0);
        Vec3 vup = new Vec3(0,1,0);
        Camera cam = new Camera(40,1,0,10,0,0,lookfrom,lookat,vup);
        return cam;

    }
    public ArrayList<Hitable> CornellBox() throws IOException {
        ArrayList<Hitable> world = new ArrayList<Hitable>();

        Material red = new Lambertian(new PureColorTexture(new Vec3(0.65,0.05,0.05)));
        Material white = new Lambertian(new PureColorTexture(new Vec3(0.73,0.73,0.73)));
        Material green = new Lambertian(new PureColorTexture(new Vec3(0.12,0.45,0.05)));
        Material light = new DiffuseLight(new PureColorTexture(new Vec3(15,15,15)));
        //ImageTexture Earth = new ImageTexture("C:\\Users\\pzeug\\RT\\src\\TextureFiles\\earth.jpg");
        //world.add(new Sphere(new Vec3(278, 278, 350), 100, new Metal(Earth,0.8)));

        world.add(new YzRect(0,555,0,555,555,green));
        world.add(new YzRect(0,555,0,555,0,red));
        world.add(new XzRect(213,343,227,332,554,light));
        world.add(new XzRect(0,555,0,555,0,white));
        world.add(new XzRect(0,555,0,555,555,white));
        world.add(new XyRect(0,555,0,555,555,white));
        Material whiteMetal = new Metal(new PureColorTexture(new Vec3(0.73,0.73,0.73)),0.2);

        Material[] whitebox = new Material[6];
        for(int i=0;i<6;i++){
            whitebox[i] = whiteMetal;
        }
        world.add(new Box(new Vec3(130,0,65),new Vec3(295,165,230),whitebox));
        world.add(new Box(new Vec3(265,0,295),new Vec3(430,330,460),whitebox));


        return world;
    }

    /*
    public ArrayList<Hitable> TestScene() throws IOException {
        ArrayList<Hitable> world = new ArrayList<Hitable>();
        ImageTexture Earth = new ImageTexture("C:\\Users\\pzeug\\RT\\src\\TextureFiles\\earth.jpg");
        world.add(new Sphere(new Vec3(0, 1, 0), 1, new Lambertian(Earth)));
        return world;
    }
    /*
    public ArrayList<Hitable> TestScene(){
        ArrayList<Hitable> world = new ArrayList<Hitable>();
        RandomGrayTexture GroundTexture = new RandomGrayTexture();
        world.add(new Sphere(new Vec3(0, 1, 0), 1, new Dielectric(1.5,0)));
        world.add(new Sphere(new Vec3(0, -1000, -1), 1000, new Lambertian(GroundTexture)));
        return world;
    }

    /*

    public ArrayList<Hitable> TestScene(){
        ArrayList<Hitable> world = new ArrayList<Hitable>();
        CheckerTexture GroundTexture = new CheckerTexture(new Vec3(0.3,0.5,0.9),new Vec3(1.0,1.0,1.0));
        world.add(new Sphere(new Vec3(0, 1, 0), 1, new Dielectric(1.5,0)));
        world.add(new Sphere(new Vec3(-4, 1, 0), 1, new Lambertian(new PureColorTexture(new Vec3(0.3, 1, 0.2)))));
        world.add(new Sphere(new Vec3(4, 1, 0), 1, new Metal(new PureColorTexture(new Vec3(0.9,0.4,0.6)),0)));
        world.add(new Sphere(new Vec3(0, -1000, -1), 1000, new Lambertian(GroundTexture)));
        for (int x = -11;x<11;x++){
            for (int z = -11;z<11;z++){

                Vec3 center = new Vec3(x,0.2,z);
                Vec3 color = new Vec3(Math.random(),Math.random(),Math.random());
                PureColorTexture smallBallColor = new PureColorTexture(color);
                world.add(new Sphere(center,0.2,new Lambertian(smallBallColor)));

            }
        }
        return world;
    }
    /*
    public HitableList TestScene(){
        HitableList world = new HitableList();
        world.add(new Sphere(new Vec3(0, 1, 0), 1, new Dielectric(1.5,0)));
        world.add(new Sphere(new Vec3(-4, 1, 0), 1, new Lambertian(new Vec3(0.3, 1, 0.2))));
        world.add(new Sphere(new Vec3(4, 1, 0), 1, new Metal(new Vec3(1, 0.9, 1),0)));
        world.add(new Sphere(new Vec3(0, -1000, -1), 1000, new Lambertian(new Vec3(0.9, 0.9, 0.9))));
        for (int x = -11;x<11;x++){
            for (int z = -11;z<11;z++){

                Vec3 center = new Vec3(x,0.2,z);
                Vec3 color = new Vec3(Math.random(),Math.random(),Math.random());
                world.add(new Sphere(center,0.2,new Lambertian(color)));

            }
        }
        return world;
    }*/

    /*
    public Vec3 rayColor(Ray r, BVHnode world, int depth) {
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
    }*/



}