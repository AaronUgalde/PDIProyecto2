package tests;

import Model.ImageMatrix;
import Model.MaskType;
import Operations.NonLinearFilters.*;
import Operations.Result;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

public class TestNonLinearFilters {
    public static void main(String[] args) throws Exception {
        String base = "/home/huron/Pictures/NoisyImages";
        // Load original
        BufferedImage origBuf = ImageIO.read(new File(base, "original.png"));
        ImageMatrix orig = new ImageMatrix(origBuf);
        int width = orig.getWidth(), height = orig.getHeight();
        int totalPixels = width * height;

        List<String> noisyNames = List.of(
                "image_sinusoidal.png",
                "image_gaussian.png",
                "image_exponential.png",
                "image_rayleigh.png",
                "image_uniform.png",
                "image_gamma.png",
                "image_salt_pepper.png"
        );

        List<FilterConfig> filters = List.of(
                new FilterConfig("ArithmeticMean", new ArithmeticMeanFilter(), Map.of()),
                new FilterConfig("Median", new MedianFilter(), Map.of()),
                new FilterConfig("Max", new MaxFilter(), Map.of()),
                new FilterConfig("Min", new MinFilter(), Map.of()),
                new FilterConfig("MidPoint", new MidPointFilter(), Map.of()),
                new FilterConfig("AlphaTrimmed", new AlphaTrimmedFilter(), Map.of("p", 2)),
                new FilterConfig("HarmonicMean", new HarmonicMeanFilter(), Map.of()),
                new FilterConfig("ContraHarmonic", new ContraHarmonicFilter(), Map.of("q", 1.0)),
                new FilterConfig("GeometricMean", new GeometricMeanFilter(), Map.of()),
                new FilterConfig("MaxMin", new MaxMinFilter(), Map.of())
        );

        for (String name : noisyNames) {
            File f = new File(base, name);
            BufferedImage noisyBuf = ImageIO.read(f);
            ImageMatrix noisy = new ImageMatrix(noisyBuf);

            int diffNoisy = countDifferent(origBuf, noisyBuf);
            double pctNoisy = diffNoisy * 100.0 / totalPixels;
            System.out.printf("Image %s: noisy diff = %d pixels (%.2f%%)%n", name, diffNoisy, pctNoisy);

            // apply each filter
            for (FilterConfig fc : filters) {
                // prepare params
                LinkedHashMap<String,Object> params = new LinkedHashMap<>();
                params.put("maskType", MaskType.SQUARE);
                params.put("maskSize", 3);
                params.putAll(fc.extraParams);

                // apply filter
                Result res = fc.filter.apply(new ImageMatrix(noisyBuf), params);
                BufferedImage outBuf = res.getImageMatrix().toBufferedImage();

                int diffOut = countDifferent(origBuf, outBuf);
                double pctOut = diffOut * 100.0 / totalPixels;
                System.out.printf("  Filter %s: diff = %d (%.2f%%)%n", fc.name, diffOut, pctOut);
            }
            System.out.println();
        }
    }

    private static int countDifferent(BufferedImage a, BufferedImage b) {
        int w = a.getWidth(), h = a.getHeight();
        int count = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (a.getRGB(x,y) != b.getRGB(x,y)) count++;
            }
        }
        return count;
    }

    static class FilterConfig {
        String name;
        Operations.OperationFunction filter;
        Map<String,Object> extraParams;
        FilterConfig(String name, Operations.OperationFunction filter, Map<String,Object> extraParams) {
            this.name = name;
            this.filter = filter;
            this.extraParams = extraParams;
        }
    }
}
