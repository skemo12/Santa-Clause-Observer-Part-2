package main;

import checker.Checker;
import common.Constants;
import data.Database;
import fileio.InputLoader;
import fileio.MyWriter;

import java.io.File;

/**
 * Class used to run the code
 */
public final class Main {

    private Main() {
        ///constructor for checkstyle
    }
    /**
     * This method is used to call the checker which calculates the score
     * @param args
     *          the arguments used to call the main method
     */
    public static void main(final String[] args) {


        File file = new File(Constants.INPUT_PATH);
        String[] paths = file.list();

        if (paths == null) {
            System.out.println("No input data");
            return;
        }
        File outputDir = new File(Constants.OUTPUT_DIR);
        //Creating a folder using mkdir() method
        if (!outputDir.exists()) {
            boolean bool = outputDir.mkdir();
            if (!bool) {
                System.out.println("Output directory make failed!");
            }
        }

        for (String path : paths) {

            // Check whether file is actually a test file
            if (!path.startsWith(Constants.TEST_NAME)) {
                continue;
            }
            // Read input file
            String inputFilename = Constants.INPUT_PATH + path;
            InputLoader inputLoader = new InputLoader(inputFilename);
            inputLoader.readData();

            // Prepare output file
            String index = path.replaceAll("[^0-9]", "");
            String outputFilename = Constants.OUTPUT_PATH + index
                    + Constants.JSON_EXTENSION;
            MyWriter writer = new MyWriter(outputFilename);

            // Running for number of years + round 0
            for (int i = 0; i <= Database.getInstance().
                    getNumberOfYears(); i++) {

                Database.getInstance().getSanta().giveGifts();
                Database.getInstance().saveYear();
                if (i != Database.getInstance().getNumberOfYears()) {
                    Database.getInstance().updateDatabaseByYear(i);
                }

            }
            writer.closeJSON();
            Database.renewDatabase();
        }
        Checker.calculateScore();
    }
}
