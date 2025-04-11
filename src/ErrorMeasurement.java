import java.util.Arrays;

public class ErrorMeasurement {
    // Variance
    private static double varianceChannel(int[][] channel){
        int height = channel[0].length;
        int width = channel.length;
        int N = height * width;
        double mean = 0;
        double sum = 0;
        double sumSquared = 0;
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                sum += channel[i][j];
            }
        }
        mean = sum / N;
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                sumSquared += Math.pow(channel[i][j] - mean, 2);
            }
        }
        double variance = sumSquared / N;
        return variance;
    }

    private static double variance(Block block){
        return (varianceChannel(block.getRed())
                + varianceChannel(block.getGreen())
                + varianceChannel(block.getBlue())) / 3;
    }

    // MAD
    private static double madChannel(int[][] channel){
        int height = channel.length;
        int width = channel[0].length;
        double sum = 0;
        int N = height * width;
        double mean = Arrays.stream(channel)
                        .flatMapToInt(Arrays::stream)
                        .average().orElse(0);

        for (int[] row : channel) {
            for (int value : row) {
                sum += Math.abs(value - mean);
            }
        }

        return sum / N;
    }

    private static double mad(Block block){
        return (madChannel(block.getRed())
                + madChannel(block.getGreen())
                + madChannel(block.getBlue())) / 3;
    }

    // Max Pixel Difference
    private static double maxPixelDifferenceChannel(int[][] channel){
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;

        for (int[] row : channel) {
            for (int value : row) {
                max = Math.max(max, value);
                min = Math.min(min, value);
            }
        }

        return max - min;
    }

    private static double maxPixelDifference(Block block){
        return (maxPixelDifferenceChannel(block.getRed())
                + maxPixelDifferenceChannel(block.getGreen())
                + maxPixelDifferenceChannel(block.getBlue())) / 3;
    }

    // Entropy
    private static double entropyChannel(int[][] channel){
        int[] histogram = new int[256];
        int total = 0;

        for (int[] row : channel) {
            for (int value : row) {
                histogram[value]++;
                total++;
            }
        }

        double entropy = 0.0;
        for (int i = 0; i < histogram.length; i++) {
            if (histogram[i] > 0) {
                double probability = (double) histogram[i] / total;
                entropy -= probability * Math.log(probability) / Math.log(2);
            }
        }
        return entropy;
    }

    private static double entropy(Block block){
        return (entropyChannel(block.getRed())
                + entropyChannel(block.getGreen())
                + entropyChannel(block.getBlue())) / 3;
    }

    // Helper
    public enum ErrorMethod {
        VARIANCE,
        MAD,
        MAX_DIFFERENCE,
        ENTROPY
    }

    public static double calculateError(Block block, ErrorMethod method) {
        switch (method) {
            case VARIANCE:
                return variance(block);
            case MAD:
                return mad(block);
            case MAX_DIFFERENCE:
                return maxPixelDifference(block);
            case ENTROPY:
                return entropy(block);
            default:
                throw new IllegalArgumentException("Metode apa ini Kapten?");
        }
    }
}
