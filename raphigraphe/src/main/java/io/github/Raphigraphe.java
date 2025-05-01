package io.github;

import java.io.File;
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
    final static File BRAILLE_IMAGES_DIR = new File(
            System.getProperty("user.dir") + "/raphigraphe/src/main/resources/braille-characters");
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

    @Override
    public Integer call() throws Exception {
        final String ROOT_DIR = System.getProperty("user.dir");
        final String SOURCE_IMAGE = ROOT_DIR + "/raphigraphe/src/main/resources/test-images/el-gatito.png";

        Imajine imajine = new Imajine(SOURCE_IMAGE);
        Imajine cropped = crop(imajine, 200, 200, 200, 200);

        Adjustments.threshold(imajine, threshold);

        System.out.println(imajine);

        imajine.save(ROOT_DIR + "/raphigraphe/src/main/resources/output-results/el-gatito-thresholded.png");
        cropped.save(ROOT_DIR + "/raphigraphe/src/main/resources/output-results/el-gatito-cropped.png");

        return 0;
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new Raphigraphe()).execute(args);
        System.exit(exitCode);
    }
}
