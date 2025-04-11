public class QuadTreeNode {
    private int x, y, width, height;
    private int avgRed, avgGreen, avgBlue;
    private QuadTreeNode[] children;   // ada 4 children kalo punya

    public QuadTreeNode(int x, int y, int width, int height, int avgRed, int avgGreen, int avgBlue) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.avgRed = avgRed;
        this.avgGreen = avgGreen;
        this.avgBlue = avgBlue;
        this.children = null; // Awal awal ngga ada children
    }

    public void setChildren(QuadTreeNode[] children) {
        this.children = children;
    }

    public boolean isLeaf(){
        return children == null;
    }

    public QuadTreeNode[] getChildren() {
        return children;
    }

    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getAvgRed() {
        return avgRed;
    }

    public int getAvgGreen() {
        return avgGreen;
    }

    public int getAvgBlue() {
        return avgBlue;
    }

    public int getDepth(){
        if(isLeaf()){
            return 0;
        }
        int maxDepth = 0;
        for(QuadTreeNode child : children){
            maxDepth = Math.max(maxDepth, child.getDepth());
        }
        return maxDepth + 1;
    }

    public int countNodes(){
        if(isLeaf()){
            return 1;
        }
        int count = 1; // hitung diri sendiri
        for(QuadTreeNode child : children){
            count += child.countNodes();
        }
        return count;
    }
}

