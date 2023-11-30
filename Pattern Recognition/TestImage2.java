import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

class Threshold {
    public static int[][] filter(int[][] matrix) {
        int[][] result = matrix.clone();
        for(int i = 0; i < 10; i++) {
            for (int j = 0; j <10; j++) {
                if(matrix[i][j] < 20)
                    matrix[i][j]=0;
                else
                    matrix[i][j]=1;
                }
            }
        for(int q= 0; q<10; q++) {
            for (int r=0; r<10; r++){
                result[q][r]=matrix[q][r];
            }
        }
        return result;
    }
}

class Image {
    int[][] pixels;

    public Image(int[][] pixels) {
        this.pixels = pixels;
    }

    public int[] histogram() {
        int[] histogram = new int[256]; // Assuming 8-bit grayscale image

        for (int[] row : pixels) {
            for (int pixel : row) {
                histogram[pixel]++;
            }
        }

        return histogram;
    }

    public void threshold(){
        int[][] result = Threshold.filter(pixels);
        pixels = result;
    }

    // print
    public void print() {
        for (int[] row : pixels) {
            for (int pixel : row) {
                System.out.print(pixel + " ");
            }
            System.out.println();
        }
    }
}

class ConnectedComponentLabeling {
    private static final int BACKGROUND = 0;
    private static final int OBJECT = 1;

    private int[][] image;
    private int[][] labeledImage;
    private int currentLabel;

    public ConnectedComponentLabeling(Image i) {
        this.image = i.pixels;
        this.labeledImage = new int[image.length][image[0].length];
        this.currentLabel = 1; // Start labeling from 1
    }

    public int[][] labelComponents() {
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                if (image[i][j] == OBJECT && labeledImage[i][j] == BACKGROUND) {
                    dfs(i, j);
                    currentLabel++;
                }
            }
        }
        return labeledImage;
    }

    private void dfs(int row, int col) {
        if (row < 0 || row >= image.length || col < 0 || col >= image[0].length)
            return;

        if (image[row][col] == OBJECT && labeledImage[row][col] == BACKGROUND) {
            labeledImage[row][col] = currentLabel;

            // Recursively label connected components
            dfs(row - 1, col); // Up
            dfs(row + 1, col); // Down
            dfs(row, col - 1); // Left
            dfs(row, col + 1); // Right
        }
    }

    public void printLabeledImage() {
        for (int[] row : labeledImage) {
            System.out.println(Arrays.toString(row));
        }
    }

    public void computeAttributesAndRecognize() {
        for (int label = 1; label < currentLabel; label++) {
            int area = computeArea(label);
            int perimeter = computePerimeter(label);

            Shape shape = new Shape(area, perimeter);
            String objectType = shape.recognizeObject();

            System.out.println("Object " + label + ": " + objectType);
        }
    }

    private int computeArea(int label) {
        int area = 0;
        for (int i = 0; i < labeledImage.length; i++) {
            for (int j = 0; j < labeledImage[0].length; j++) {
                if (labeledImage[i][j] == label) {
                    area++;
                }
            }
        }
        return area;
    }

    private int computePerimeter(int label) {
        int perimeter = 0;
        for (int i = 0; i < labeledImage.length; i++) {
            for (int j = 0; j < labeledImage[0].length; j++) {
                if (labeledImage[i][j] == label) {
                    // Check if the current pixel has a neighboring background pixel
                    if (i - 1 >= 0 && labeledImage[i - 1][j] == BACKGROUND) perimeter++;
                    if (i + 1 < labeledImage.length && labeledImage[i + 1][j] == BACKGROUND) perimeter++;
                    if (j - 1 >= 0 && labeledImage[i][j - 1] == BACKGROUND) perimeter++;
                    if (j + 1 < labeledImage[0].length && labeledImage[i][j + 1] == BACKGROUND) perimeter++;
                }
            }
        }
        return perimeter;
    }
}

class Shape {
    private static final double PI = Math.PI;
    private int area;
    private int perimeter;

    public Shape(int area, int perimeter) {
        this.area = area;
        this.perimeter = perimeter;
    }

    public double calculateR() {
        return (4 * PI * area) / (perimeter * perimeter);
    }

    public String recognizeObject() {
        double r = calculateR();

        if (Math.abs(r - (PI/4)) < 1e-6) {
            return "Square";
        } else {
            return "Circular";
        }
    }
}

public class TestImage2 {
    public static void main(String[] args) {
        int[][] pixelArray = {
            {1,  3,  5,  7,  9,  3,  4,   4,  5,  6},
            {1, 20, 25, 24,  3,  5,  6,   4,  2,  4},
            {1, 22, 35, 24,  3,  5,  6,   4,  5,  7},
            {1, 20, 28, 34,  2,  5,  6,   4,  8,  9},
            {1,  3,  5,  7,  9,  3,  4,   4,  5,  6},
            {1,  3,  5,  7,  9,  3, 67,   4,  5,  6},
            {1,  3,  5,  7,  9, 78, 54,  94,  5,  6},
            {1,  3,  5,  7,  9, 99, 98,  54,  5,  6},
            {1,  3,  5,  7,  9,  3, 64,   4,  5,  6},
            {1,  3,  5,  7,  9,  3,  4,   4,  5,  6},
        };

        Image image = new Image(pixelArray);
        int[] result = image.histogram();

        // Display the histogram
        for (int i = 0; i < result.length; i++) {
            if (result[i] != 0)
                System.out.println("Pixel Value " + i + ": " + result[i]);
        }

        image.threshold();
        // Display the image after applying the threshold filter
        image.print();

        ConnectedComponentLabeling ccl = new ConnectedComponentLabeling(image);
        ccl.labelComponents();
        ccl.printLabeledImage();
        ccl.computeAttributesAndRecognize();
    }
}
