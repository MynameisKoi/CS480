import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class RecogImage {
    public static void main(String args[]) throws IOException {
        BufferedImage img = null;
        File f = null;

        // read image
        try {
            f = new File("e.bmp");
            img = ImageIO.read(f);
        } catch (IOException e) {
            System.out.println(e);
        }

        // get width and height
        int width = img.getWidth();
        int height = img.getHeight();
        int[][] arr = new int[height][width];
        // convert to red image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Here (x,y)denotes the coordinate of image
                // for modifying the pixel value.
                int p = img.getRGB(x, y);
                // System.out.println(p);

                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                // calculate average
                int avg = (r + g + b) / 3;
                // System.out.println("x: " + x + "y: " + y);

                arr[x][y] = avg;

                // replace RGB value with avg
                p = (a << 24) | (avg << 16) | (avg << 8) | avg;

                img.setRGB(x, y, p);
            }
        }

        System.out.println("height: " + height);
        System.out.println("width: " + width);

        // for (int i = 0; i < height; i++) {
        //     for (int j = 0; j < width; j++) {
        //         System.out.print(arr[i][j] + " ");
        //     }
        //     System.out.println();
        // }

        try {
            f = new File("out1.bmp");
            ImageIO.write(img, "jpg", f);
        } catch (IOException e) {
            System.out.println(e);
        }

        // output the array arr into a new txt file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    writer.write(Integer.toString(arr[j][i]) + " ");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}