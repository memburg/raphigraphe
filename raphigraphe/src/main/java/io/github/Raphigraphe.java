package io.github;

import java.io.File;
import java.util.concurrent.Callable;

import imajine.Imajine;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "Raphigraphe", mixinStandardHelpOptions = true, version = "raphigraphe 1.0.0", description = "A CLI tool for converting images to ASCII art.")
public class Raphigraphe implements Callable<Integer> {
    final static File BRAILLE_IMAGES_DIR = new File(System.getProperty("user.dir") + "/raphigraphe/src/main/resources/braille-characters");
    final static String BRAILLE_CHARACTERS = "⡀⡁⡂⡃⡄⡅⡆⡇⡈⡉⡊⡋⡌⡍⡎⡏⡐⡑⡒⡓⡔⡕⡖⡗⡘⡙⡚⡛⡜⡝⡞⡟⡠⡡⡢⡣⡤⡥⡦⡧⡨⡩⡪⡫⡬⡭⡮⡯⡰⡱⡲⡳⡴⡵⡶⡷⡸⡹⡺⡻⡼⡽⡾⡿⢀⢁⢂⢃⢄⢅⢆⢇⢈⢉⢊⢋⢌⢍⢎⢏⢐⢑⢒⢓⢔⢕⢖⢗⢘⢙⢚⢛⢜⢝⢞⢟⢠⢡⢢⢣⢤⢥⢦⢧⢨⢩⢪⢫⢬⢭⢮⢯⢰⢱⢲⢳⢴⢵⢶⢷⢸⢹⢺⢻⢼⢽⢾⢿⣀⣁⣂⣃⣄⣅⣆⣇⣈⣉⣊⣋⣌⣍⣎⣏⣐⣑⣒⣓⣔⣕⣖⣗⣘⣙⣚⣛⣜⣝⣞⣟⣠⣡⣢⣣⣤⣥⣦⣧⣨⣩⣪⣫⣬⣭⣮⣯⣰⣱⣲⣳⣴⣵⣶⣷⣸⣹⣺⣻⣼⣽⣾⣿";

    @Parameters(index = "0", description = "The input file to be converted")
    private String source;

    @Option(names = { "-t", "--threshold" }, description = "The threshold value to be used for the conversion")
    private int threshold = 125;

    @Override
    public Integer call() throws Exception {
        Imajine imajine = new Imajine(System.getProperty("user.dir") + "/raphigraphe/src/main/resources/test-images/el-gatito.png");
        System.out.println(imajine);
        return 0;
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new Raphigraphe()).execute(args);
        System.exit(exitCode);
    }
}
