package io.github;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

        if (originalWidth > originalHeight || originalWidth == originalHeight) {
            int newWidth = (int) (originalWidth * 0.961538461538);
            return crop(image, 0, 0, newWidth, originalHeight);
        } else {
            int newHeight = (int) (originalWidth / 0.961538461538);
            return crop(image, 0, 0, originalWidth, newHeight);
        }
    }

    public static Imajine resize(Imajine image, int newWidth, int newHeight) throws IOException {
        Imajine resizedImage = new Imajine(newWidth, newHeight);
        double widthRatio = (double) image.getWidth() / newWidth;
        double heightRatio = (double) image.getHeight() / newHeight;

        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                int originalX = (int) (x * widthRatio);
                int originalY = (int) (y * heightRatio);
                Pixel oPixel = image.getPixel(originalX, originalY);
                resizedImage.setPixel(new Pixel(x, y, oPixel.getRed(), oPixel.getGreen(), oPixel.getBlue()));
            }
        }

        return resizedImage;
    }

    public static Imajine extractSubimage(Imajine image, int x, int y) {
        int width = Math.min(2, image.getWidth() - x);
        int height = Math.min(4, image.getHeight() - y);
        return crop(image, x, y, width, height);
    }

    public static String getAsciiCharacter(Imajine subimage) throws IOException {
        Pixel[][] subimagePixels = new Pixel[2][4];

        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 4; y++) {
                subimagePixels[x][y] = subimage.getPixel(x, y);
            }
        }

        String closestMatch = "";

        File[] brailleImages = BRAILLE_IMAGES_DIR.listFiles();

        if (brailleImages == null) {
            throw new IOException("Braille images directory is empty or not found.");
        }

        for (File asciiImage : brailleImages) {
            if (asciiImage.getName().equals(".DS_Store")) {
                continue;
            }

            Imajine braille = new Imajine(asciiImage.getAbsolutePath());

            int matches = 0;

            for (int x = 0; x < 2; x++) {
                for (int y = 0; y < 4; y++) {
                    Pixel p1 = braille.getPixel(x, y);
                    Pixel p2 = subimagePixels[x][y];

                    if (p1.toString().equals(p2.toString())) {
                        matches++;
                    }
                }
            }

            if (matches == 8) {
                return BRAILLE_CHARACTERS.charAt(Integer.parseInt(asciiImage.getName().substring(8, 11))) + "";
            }

            if (matches == 7) {
                closestMatch = BRAILLE_CHARACTERS.charAt(Integer.parseInt(asciiImage.getName().substring(8, 11))) + "";
            }
        }

        return closestMatch;
    }

    public static List<List<String>> getAsciiImage(Imajine image) throws IOException {
        int width = image.getWidth();
        int height = image.getHeight();
        List<List<String>> asciiImage = new ArrayList<>();

        for (int y = 0; y < height; y += 4) {
            List<String> asciiRow = new ArrayList<>();

            for (int x = 0; x < width; x += 2) {
                Imajine subimage = extractSubimage(image, x, y);
                asciiRow.add(getAsciiCharacter(subimage));
            }

            asciiImage.add(asciiRow);
        }

        return asciiImage;
    }

    public static void printAsciiImage(List<List<String>> asciiImage) {
        for (List<String> row : asciiImage) {
            for (String character : row) {
                System.out.print(character);
            }
            System.out.println();
        }
    }

    @Override
    public Integer call() throws Exception {
        final String SOURCE_IMAGE = ROOT_DIR + source;

        System.out.println("Loading image...");
        Imajine imajine = new Imajine(SOURCE_IMAGE);
        System.out.println("Cropping image...");
        Imajine cropped = centerCrop(imajine);
        System.out.println("Resizing image...");
        Imajine resized = resize(cropped, 50, 52);
        System.out.println("Performing thresholding...");
        Adjustments.threshold(resized, threshold);

        System.out.println("Converting image to ASCII art...");
        List<List<String>> asciiImage = getAsciiImage(resized);
        printAsciiImage(asciiImage);
        System.out.println("ASCII art generated successfully!");

        return 0;
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new Raphigraphe()).execute(args);
        System.exit(exitCode);
    }
}
