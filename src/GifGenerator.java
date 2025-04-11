import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.IIOImage;
import javax.imageio.ImageTypeSpecifier;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class GifGenerator {

    public static void generateGifFromFolder(String inputFolderPath, String outputGifPath, int delayMs) throws IOException {
        File folder = new File(inputFolderPath);
        File[] imageFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg"));

        if (imageFiles == null || imageFiles.length == 0) {
            throw new IOException("No image files found in the folder.");
        }

        ImageWriter gifWriter = ImageIO.getImageWritersByFormatName("gif").next();
        ImageOutputStream output = ImageIO.createImageOutputStream(new File(outputGifPath));
        gifWriter.setOutput(output);
        gifWriter.prepareWriteSequence(null);

        for (int i = 0; i < imageFiles.length; i++) {
            BufferedImage frame = ImageIO.read(imageFiles[i]);
            IIOMetadata metadata = getFrameMetadata(gifWriter, frame, delayMs, i == 0);
            gifWriter.writeToSequence(new IIOImage(frame, null, metadata), null);
        }

        gifWriter.endWriteSequence();
        output.close();
    }

    private static IIOMetadata getFrameMetadata(ImageWriter writer, BufferedImage image, int delayTimeMs, boolean isFirst) throws IOException {
        ImageTypeSpecifier type = ImageTypeSpecifier.createFromRenderedImage(image);
        IIOMetadata metadata = writer.getDefaultImageMetadata(type, null);
        String metaFormat = metadata.getNativeMetadataFormatName();
        IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(metaFormat);

        IIOMetadataNode gce = getOrCreateNode(root, "GraphicControlExtension");
        gce.setAttribute("disposalMethod", "none");
        gce.setAttribute("userInputFlag", "FALSE");
        gce.setAttribute("transparentColorFlag", "FALSE");
        gce.setAttribute("delayTime", Integer.toString(delayTimeMs / 10));
        gce.setAttribute("transparentColorIndex", "0");

        if (isFirst) {
            IIOMetadataNode appExtensions = getOrCreateNode(root, "ApplicationExtensions");
            IIOMetadataNode appNode = new IIOMetadataNode("ApplicationExtension");
            appNode.setAttribute("applicationID", "NETSCAPE");
            appNode.setAttribute("authenticationCode", "2.0");
            appNode.setUserObject(new byte[]{1, 0, 0});
            appExtensions.appendChild(appNode);
        }

        metadata.setFromTree(metaFormat, root);
        return metadata;
    }

    private static IIOMetadataNode getOrCreateNode(IIOMetadataNode root, String name) {
        for (int i = 0; i < root.getLength(); i++) {
            if (root.item(i).getNodeName().equalsIgnoreCase(name)) {
                return (IIOMetadataNode) root.item(i);
            }
        }
        IIOMetadataNode node = new IIOMetadataNode(name);
        root.appendChild(node);
        return node;
    }

    public static void generateFramesByBlockSize(Block image, ErrorMeasurement.ErrorMethod method, double threshold, int minBlockSize) throws IOException {
        int width = image.getWidth();
        int height = image.getHeight();
        int maxBlockArea = (height/2) * ((width+2)/2); // min area untuk gambar 1 pixel 

        //inputan
        Compressor baseCompressor = new Compressor(image, method, threshold, minBlockSize);
        QuadTreeNode fullTree = baseCompressor.compress();
        int maxDepth = fullTree.getDepth();

        String folderPath = new File(System.getProperty("user.dir"), "frames").getAbsolutePath();
        File folder = new File(folderPath);
        // Gif ngga nyampur
        if (folder.exists()) {
            for (File file : folder.listFiles()) {
                if (file.isFile()) file.delete();
            }
        } else {
            folder.mkdirs();
        }        

        double logStart = Math.log(minBlockSize);
        double logEnd = Math.log(maxBlockArea);
        double logStep = (logStart - logEnd) / (maxDepth - 1);

        for (int i = 1; i < maxDepth; i++) {
            double logValue = logStart - (maxDepth - i) * logStep;
            int currentMinBlockArea = (int) Math.exp(logValue);
            //System.out.println("Current min block area: " + currentMinBlockArea);
            Compressor compressor = new Compressor(image, method, threshold, currentMinBlockArea);
            QuadTreeNode tree = compressor.compress();
            BufferedImage frame = ImageProcessor.renderImage(tree, width, height);
            String filename = String.format(Locale.US, "%s/frame_%02d.png", folderPath, i);
            ImageProcessor.saveImage(frame, filename);
        }
    }
}
