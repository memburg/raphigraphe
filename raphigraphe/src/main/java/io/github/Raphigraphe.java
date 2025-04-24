package io.github;

import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "Raphigraphe", mixinStandardHelpOptions = true, version = "raphigraphe 1.1", description = "A CLI tool for converting images to ASCII art.")
public class Raphigraphe implements Callable<Integer> {
    @Parameters(index = "0", description = "The input file to be converted")
    private String source;

    @Option(names = {"-t", "--threshold"}, description = "The threshold value to be used for the conversion")
    private int threshold = 125;

    @Option(names = {"-o", "--output"}, description = "The output file to be generated")
    private String output;

    @Option(names = {"-c", "--console"}, description = "Display the result in the console")
    private boolean console = true;

    @Override
    public Integer call() throws Exception {
        System.out.println(source);
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Raphigraphe()).execute(args);
        System.exit(exitCode);
    }
}