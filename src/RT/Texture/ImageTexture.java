package RT.Texture;

import RT.RTutils;
import RT.Vec3;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageTexture extends Texture {
    int w;
    int h;
    BufferedImage texture;
    public ImageTexture(String filepath) throws IOException {
        this.texture = ImageIO.read(new File(filepath));
        this.w = texture.getWidth();
        this.h = texture.getHeight();
    }
    @Override
    public Vec3 Color(double u, double v, Vec3 p){
        double u1 = RTutils.clamp(u,0.0,1.0);
        double v1 = 1.0 - RTutils.clamp(v,0.0,1.0);

        int i = (int)(u1*w);
        int j = (int)(v1*h);

        if (i>=w){
            i = w-1;
        }
        if (j>h){
            j = h-1;
        }
        double colorscale = 1.0/255.0;
        Color ReturnColor = new Color(texture.getRGB(i,j));
        return new Vec3(ReturnColor.getRed()*colorscale,ReturnColor.getGreen()*colorscale,ReturnColor.getBlue()*colorscale);

    }
}
