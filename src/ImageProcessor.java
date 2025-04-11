import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageProcessor {
    public static Block imageToBlock(String path) throws IOException{
        BufferedImage image = ImageIO.read(new File(path));
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] red = new int[width][height];
        int[][] green = new int[width][height];
        int[][] blue = new int[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = image.getRGB(x, y);
                red[x][y] = (rgb >> 16) & 0xFF;
                green[x][y] = (rgb >> 8) & 0xFF;
                blue[x][y] = rgb & 0xFF;
            }
        }

        return new Block(red, green, blue);
    }

    private static void drawNode(BufferedImage image, QuadTreeNode node) {
        int x = node.getX();
        int y = node.getY();
        int width = node.getWidth();
        int height = node.getHeight();
        int rgb = (node.getAvgRed() << 16) | (node.getAvgGreen() << 8) | node.getAvgBlue();

        if(node.isLeaf()){
            for (int i = x; i < x + width; i++) {
                for (int j = y; j < y + height; j++) {
                    image.setRGB(i, j, rgb);
                }
            }
        }
        else {
            for (QuadTreeNode child : node.getChildren()) {
                drawNode(image, child);
            }
        }
    }

    public static BufferedImage renderImage(QuadTreeNode root, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        drawNode(image, root);
        return image;
    }

    public static void saveImage(BufferedImage image, String path) throws IOException {
        File outputfile = new File(path);
        ImageIO.write(image, "png", outputfile);
    }
}
