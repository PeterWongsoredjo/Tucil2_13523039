public class Compressor {
    private Block Image;
    private ErrorMeasurement.ErrorMethod method;
    private double threshold;
    private int minBlockSize;

    public Compressor(Block image, ErrorMeasurement.ErrorMethod method, double threshold, int minBlockSize) {
        this.Image = image;
        this.method = method;
        this.threshold = threshold;
        this.minBlockSize = minBlockSize;
    }

    public QuadTreeNode compress(){
        int width = Image.getWidth();
        int height = Image.getHeight();
        return recursiveCompress(0, 0, width, height, Image);
    }

    public QuadTreeNode recursiveCompress(int x, int y, int width, int height, Block block){
        double error = ErrorMeasurement.calculateError(block, method);

        // <= threshold brarti seragam
        boolean seragam = error <= threshold;
        double[] avgRGB = block.getAverageRGB();
        int avgRed =  (int) Math.round(avgRGB[0]);
        int avgGreen =  (int) Math.round(avgRGB[1]);
        int avgBlue =  (int) Math.round(avgRGB[2]);

        // Kalo ngga seragam, ya baju bebas (bagi empat)
        int newWidth = width / 2;
        int newHeight = height  / 2;

        int area = width * height;
        int newArea = newWidth * newHeight;

        if(area <= minBlockSize || newArea < minBlockSize || seragam) {
            //System.out.println("Block (" + x + ", " + y + ", " + width + ", " + height + ") error: " + error);
            return new QuadTreeNode(x, y, width, height, avgRed, avgGreen, avgBlue);
        }
        
        Block topLeft = block.getSubBlock(0, 0, newWidth, newHeight);
        Block topRight = block.getSubBlock(newWidth, 0, width - newWidth, newHeight);
        Block bottomLeft = block.getSubBlock(0, newHeight, newWidth, height - newHeight);
        Block bottomRight = block.getSubBlock(newWidth, newHeight, width - newWidth, height - newHeight);
        
        QuadTreeNode topLeftNode = recursiveCompress(x, y, newWidth, newHeight, topLeft);
        QuadTreeNode topRightNode = recursiveCompress(x + newWidth, y, width - newWidth, newHeight, topRight);
        QuadTreeNode bottomLeftNode = recursiveCompress(x, y + newHeight, newWidth, height - newHeight, bottomLeft);
        QuadTreeNode bottomRightNode = recursiveCompress(x + newWidth, y + newHeight, width - newWidth, height - newHeight, bottomRight);

        QuadTreeNode parentNode = new QuadTreeNode(x, y, width, height, avgRed, avgGreen, avgBlue);
        parentNode.setChildren(new QuadTreeNode[]{topLeftNode, topRightNode, bottomLeftNode, bottomRightNode});
        return parentNode;
    }

}
