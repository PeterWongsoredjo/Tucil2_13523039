public class Block {
    private int [][] red;
    private int [][] green;
    private int [][] blue;
    private int width;
    private int height;

    public Block(int [][] red, int [][] green, int [][] blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.width = red.length;
        this.height = red[0].length;
    }

    public int[][] getRed() {
        return red;
    }

    public int[][] getGreen() {
        return green;
    }

    public int[][] getBlue() {
        return blue;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getPixelRed(int x, int y) {
        return red[x][y];
    }

    public int getPixelGreen(int x, int y) {
        return green[x][y];
    }

    public int getPixelBlue(int x, int y) {
        return blue[x][y];
    }

    public double[] getAverageRGB(){
        double[] average = new double[3];
        int totalPixels = width * height;
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                average[0] += red[i][j];
                average[1] += green[i][j];
                average[2] += blue[i][j];
            }
        }
        average[0] /= totalPixels;
        average[1] /= totalPixels;
        average[2] /= totalPixels;
        return average;
    }

    public Block getSubBlock(int startX, int startY, int subWidth, int subHeight){
        int[][] subRed = new int[subWidth][subHeight];
        int[][] subGreen = new int[subWidth][subHeight];
        int[][] subBlue = new int[subWidth][subHeight];

        for(int i = 0; i < subWidth; i++){
            for(int j = 0; j < subHeight; j++){
                subRed[i][j] = red[startX + i][startY + j];
                subGreen[i][j] = green[startX + i][startY + j];
                subBlue[i][j] = blue[startX + i][startY + j];
            }
        }
        return new Block(subRed, subGreen, subBlue);
    }
}