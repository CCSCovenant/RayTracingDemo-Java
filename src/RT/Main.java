package RT;

import RT.Materials.Dielectric;
import RT.Materials.Lambertian;
import RT.Materials.Metal;
import RT.Objects.Hitable;
import RT.Objects.MovingSphere;
import RT.Objects.Sphere;
import RT.World.BVHnode;
import RT.World.HitableList;
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


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("RayTracing Test");
        Group root = new Group();
        javafx.scene.canvas.Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Render(gc);

        root.getChildren().add(canvas);
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void Render(GraphicsContext gc) throws IOException {
        int W = 400;
        int H = 300;
        int Thread = 8;
        Vec3[][] PixelBuffer = new Vec3[W][H];
        PixelWorker[] WorkerPool = new PixelWorker[Thread];

        Vec3 lookfrom = new Vec3(13,2,3);
        Vec3 lookat = new Vec3(0,0,0);
        Vec3 vup = new Vec3(0,1,0);
        double t0 = 0;
        double t1 = 1;
        Camera cam = new Camera(20,8.0/6.0,0.1,10,t0,t1,lookfrom,lookat,vup);
        ArrayList<Hitable> scene = randomeScene();
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
            WorkerPool[i] = new PixelWorker(PixelBuffer,cam,world,i*(H/Thread),Math.max(H,(i+1)*(H/Thread)),W,H,MaxDepth,spp,i);
            WorkerPool[i].start();
        }
        System.out.println("Writing PixelBuffer into image");
        for (int i=0;i<Thread;i++){
            try {
                WorkerPool[i].join();
            }catch (Exception e){

            }
        }
        long endTime = System.currentTimeMillis();

        System.out.println("All PixelWorkers Finished");
        System.out.println("Render finished in "+(endTime-startTime)+"ms");
        BufferedImage IOimage = new BufferedImage(W, H, BufferedImage.TYPE_4BYTE_ABGR);

        for (int y=H-1;y>=0;y--){
            for (int x=0;x<W;x++){
                int Red = clamp(0,255,(int)(PixelBuffer[x][y].x()*255));
                int Green = clamp(0,255,(int)(PixelBuffer[x][y].y()*255));
                int Blue = clamp(0,255,(int)(PixelBuffer[x][y].z()*255));
                pw.setArgb(x,(H-(y+1)),new Color(Red,Green,Blue,255).getRGB());
                IOimage.setRGB(x,(H-(y+1)),new Color(Red,Green,Blue,255).getRGB());
            }
        }
        ImageIO.write(IOimage,"png",new File("C:\\Users\\pzeug\\RT\\src\\output\\TestScene2.png"));
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
    public ArrayList<Hitable> randomeScene(){
        ArrayList<Hitable> world = new ArrayList<Hitable>();
        world.add(new Sphere(new Vec3(0, 1, 0), 1, new Dielectric(1.5,0)));
        world.add(new Sphere(new Vec3(-4, 1, 0), 1, new Lambertian(new Vec3(0.3, 1, 0.2))));
        world.add(new Sphere(new Vec3(4, 1, 0), 1, new Metal(new Vec3(1, 0.9, 1),0)));
        world.add(new Sphere(new Vec3(0, -1000, -1), 1000, new Lambertian(new Vec3(0.9, 0.9, 0.9))));
        for (int x = -11;x<11;x++){
            for (int z = -11;z<11;z++){
                double material = Math.random();
                Vec3 center = new Vec3(x+0.4*Math.random(),0.2,z+0.4*Math.random());
                if (material<0.5){
                    Vec3 color = new Vec3(Math.random(),Math.random(),Math.random());
                    world.add(new Sphere(center,0.2,new Lambertian(color)));
                }else if (material<0.8){
                    Vec3 color = new Vec3(Math.random(),Math.random(),Math.random());
                    world.add(new Sphere(center,0.2,new Metal(color,Math.random()*0.2)));
                }else {
                    world.add(new Sphere(center ,0.2, new Dielectric(1.5,0)));
                }
            }
        }
        return world;
    }


}