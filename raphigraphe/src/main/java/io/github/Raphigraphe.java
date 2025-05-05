package io.github;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import imajine.Pixel;
import imajine.Imajine;
import imajine.Adjustments;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "Raphigraphe", mixinStandardHelpOptions = true, version = "raphigraphe 1.0.0", description = "A CLI tool for converting images to ASCII art.")
public class Raphigraphe implements Callable<Integer> {
    final static String ROOT_DIR = System.getProperty("user.dir");
    final static File BRAILLE_IMAGES_DIR = new File(ROOT_DIR + "/raphigraphe/src/main/resources/braille-characters");
    final static String BRAILLE_CHARACTERS = "⡀⡁⡂⡃⡄⡅⡆⡇⡈⡉⡊⡋⡌⡍⡎⡏⡐⡑⡒⡓⡔⡕⡖⡗⡘⡙⡚⡛⡜⡝⡞⡟⡠⡡⡢⡣⡤⡥⡦⡧⡨⡩⡪⡫⡬⡭⡮⡯⡰⡱⡲⡳⡴⡵⡶⡷⡸⡹⡺⡻⡼⡽⡾⡿⢀⢁⢂⢃⢄⢅⢆⢇⢈⢉⢊⢋⢌⢍⢎⢏⢐⢑⢒⢓⢔⢕⢖⢗⢘⢙⢚⢛⢜⢝⢞⢟⢠⢡⢢⢣⢤⢥⢦⢧⢨⢩⢪⢫⢬⢭⢮⢯⢰⢱⢲⢳⢴⢵⢶⢷⢸⢹⢺⢻⢼⢽⢾⢿⣀⣁⣂⣃⣄⣅⣆⣇⣈⣉⣊⣋⣌⣍⣎⣏⣐⣑⣒⣓⣔⣕⣖⣗⣘⣙⣚⣛⣜⣝⣞⣟⣠⣡⣢⣣⣤⣥⣦⣧⣨⣩⣪⣫⣬⣭⣮⣯⣰⣱⣲⣳⣴⣵⣶⣷⣸⣹⣺⣻⣼⣽⣾⣿";

    @Parameters(index = "0", description = "The input file to be converted")
    private String source;

    @Option(names = { "-t", "--threshold" }, description = "The threshold value to be used for the conversion")
    private int threshold = 100;

    public static Imajine crop(Imajine image, int xOffset, int yOffset, int width, int height) {
        Imajine croppedImage = new Imajine(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Pixel oPixel = image.getPixel(xOffset + x, yOffset + y);
                Pixel nPixel = new Pixel(x, y, oPixel.getRed(), oPixel.getGreen(), oPixel.getBlue());

                croppedImage.setPixel(nPixel);
            }
        }

        return croppedImage;
    }

    public static Imajine centerCrop(Imajine image) throws IOException {
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();

        if (image.getWidth() > image.getHeight() || image.getWidth() == image.getHeight()) {
            int newWidth = (int) (originalWidth * 0.961538461538);
            return crop(image, 0, 0, newWidth, originalHeight);
        } else {
            int newHeight = (int) (originalWidth / 0.961538461538);
            return crop(image, 0, 0, originalWidth, newHeight);
        }
    }

    public static Imajine resize(Imajine image, int newWidth, int newHeight) throws IOException {
        Imajine resizedImage = new Imajine(newWidth, newHeight);

        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                int originalX = (int) ((double) x / newWidth * image.getWidth());
                int originalY = (int) ((double) y / newHeight * image.getHeight());
                Pixel oPixel = image.getPixel(originalX, originalY);
                Pixel nPixel = new Pixel(x, y, oPixel.getRed(), oPixel.getGreen(), oPixel.getBlue());

                resizedImage.setPixel(nPixel);
            }
        }

        return resizedImage;
    }

    @Override
    public Integer call() throws Exception {
        final String SOURCE_IMAGE = ROOT_DIR + "/raphigraphe/src/main/resources/test-images/el-gatito.png";

        Imajine imajine = new Imajine(SOURCE_IMAGE);
        Adjustments.threshold(imajine, threshold);
        Imajine cropped = centerCrop(imajine);
        Imajine resized = resize(imajine, 50, 52);

        System.out.println(imajine);

        imajine.save(ROOT_DIR + "/raphigraphe/src/main/resources/output-results/el-gatito-thresholded.png");
        cropped.save(ROOT_DIR + "/raphigraphe/src/main/resources/output-results/el-gatito-cropped.png");
        resized.save(ROOT_DIR + "/raphigraphe/src/main/resources/output-results/el-gatito-resized.png");

        return 0;
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new Raphigraphe()).execute(args);
        System.exit(exitCode);
    }
}
