package RT;

import RT.Materials.Dielectric;
import RT.Materials.Lambertian;
import RT.Materials.Metal;
import RT.Objects.MovingSphere;
import RT.Objects.Sphere;
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
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;


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
        int W = 800;
        int H = 600;
        int Thread = 8;
        Vec3[][] PixelBuffer = new Vec3[W][H];
        PixelWorker[] WorkerPool = new PixelWorker[Thread];

        Vec3 lookfrom = new Vec3(0,3,5);
        Vec3 lookat = new Vec3(0,0,-1);
        Vec3 vup = new Vec3(0,1,0);
        Camera cam = new Camera(20,8.0/6.0,0.1,8,0,1.0,lookfrom,lookat,vup);

        World world = new World();
        world.add(new Sphere(new Vec3(0, 0, -1), 0.3, new Dielectric(1.5,0)));
        world.add(new Sphere(new Vec3(-1, 0, -1), 0.3, new Lambertian(new Vec3(0.3, 1, 0.2))));
        world.add(new MovingSphere(new Vec3(0, 0, -2),new Vec3(0, 0.4, -2), 0.3,0,0.5, new Lambertian(new Vec3(0.8, 0.8, 0.8))));
        world.add(new Sphere(new Vec3(1, 0, -1), 0.3, new Metal(new Vec3(1, 0.9, 1),0)));
        world.add(new Sphere(new Vec3(0, -1000.5, -1), 1000, new Lambertian(new Vec3(0.9, 0.9, 0.9))));

        int spp = 50;
        int MaxDepth = 50;

        WritableImage image = new WritableImage(W, H);
        PixelWriter pw = image.getPixelWriter();
        for (int y=0;y<H;y++){
            for (int x=0;x<W;x++){
                PixelBuffer[x][y] = new Vec3();
            }
        }
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
        System.out.println("All PixelWorkers Finished");
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
        ImageIO.write(IOimage,"png",new File("C:\\Users\\pzeug\\RT\\src\\output\\MotionBlur.png"));
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


}