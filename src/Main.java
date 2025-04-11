import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        printHeader();

        try {
            System.out.print("Masukkan path gambar: ");
            String path = scanner.nextLine();

            System.out.println("\n┌─────────────────────────────┐");
            System.out.println("│     PILIH METODE ERROR      │");
            System.out.println("├─────────────────────────────┤");
            System.out.println("│ 1. Variance                 │");
            System.out.println("│ 2. MAD (Mean Abs Deviation) │");
            System.out.println("│ 3. Max Pixel Difference     │");
            System.out.println("│ 4. Entropy                  │");
            System.out.println("└─────────────────────────────┘");
            System.out.print("Pilihan (1-4): ");
            int methodChoice = scanner.nextInt();
            scanner.nextLine();

            ErrorMeasurement.ErrorMethod method = switch (methodChoice) {
                case 1 -> ErrorMeasurement.ErrorMethod.VARIANCE;
                case 2 -> ErrorMeasurement.ErrorMethod.MAD;
                case 3 -> ErrorMeasurement.ErrorMethod.MAX_DIFFERENCE;
                case 4 -> ErrorMeasurement.ErrorMethod.ENTROPY;
                default -> throw new IllegalArgumentException("Pilihan tidak valid.");
            };

            System.out.print("Masukkan threshold: ");
            double threshold = scanner.nextDouble();

            System.out.print("Masukkan ukuran minimum blok (berbasis luas area): ");
            int minBlockSize = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Masukkan path untuk menyimpan gambar hasil kompresi (PNG): ");
            String outputPath = scanner.nextLine();

            System.out.print("Apakah Anda ingin menghasilkan GIF juga? (y/n): ");
            String gifChoice = scanner.nextLine().trim().toLowerCase();

            String gifPath = null;
            String frameFolderPath = new File(System.getProperty("user.dir"), "frames").getAbsolutePath();

            if (gifChoice.equals("y")) {
                System.out.print("Masukkan output path GIF (contoh: hasil.gif): ");
                gifPath = scanner.nextLine();
            }

            long startRead = System.currentTimeMillis();
            Block image = ImageProcessor.imageToBlock(path);
            long endRead = System.currentTimeMillis();

            long start = System.currentTimeMillis();
            Compressor compressor = new Compressor(image, method, threshold, minBlockSize);
            QuadTreeNode root = compressor.compress();
            long end = System.currentTimeMillis();

            BufferedImage outputImage = ImageProcessor.renderImage(root, image.getWidth(), image.getHeight());
            ImageProcessor.saveImage(outputImage, outputPath);
            System.out.println("\nGambar hasil kompresi disimpan di: " + outputPath);

            if (gifChoice.equals("y")) {
                System.out.println("\nMembuat GIF dari beberapa tingkat kompresi...");
                GifGenerator.generateFramesByBlockSize(image, method, threshold, minBlockSize);
                ImageProcessor.saveImage(outputImage, frameFolderPath + "/frame_final.png");
                GifGenerator.generateGifFromFolder(frameFolderPath, gifPath, 500);
                System.out.println("GIF berhasil dibuat di: " + gifPath);
            }

            long originalSize = new File(path).length();
            long compressedSize = new File(outputPath).length();

            double compressionRatio = 100.0 * (1.0 - ((double) compressedSize / originalSize));
            int depth = root.getDepth();
            int nodeCount = root.countNodes();

            System.out.println("\n═════════════════════════════════════════════════");
            System.out.println("                 STATISTIK KOMPRESI              ");
            System.out.println("═════════════════════════════════════════════════");
            System.out.printf("Waktu load gambar     : %d ms\n", (endRead - startRead));
            System.out.printf("Waktu kompresi        : %d ms\n", (end - start));
            System.out.printf("Ukuran gambar awal    : %d bytes\n", originalSize);
            System.out.printf("Ukuran gambar hasil   : %d bytes\n", compressedSize);
            System.out.printf("Persentase kompresi   : %.2f%%\n", compressionRatio);
            System.out.printf("Kedalaman pohon       : %d\n", depth);
            System.out.printf("Jumlah simpul quadtree: %d\n", nodeCount);
            System.out.println("═════════════════════════════════════════════════");

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void printHeader() {
        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.println("║                IMAGE COMPRESSOR               ║");
        System.out.println("╠═══════════════════════════════════════════════╣");
        System.out.println("║              Kompresi gambar + GIF            ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");
    }
}
