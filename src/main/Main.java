package main;

import checker.Checker;
import common.Constants;
import data.Database;
import enums.CityStrategyEnum;
import fileio.InputLoader;
import fileio.MyWriter;
import utils.Utils;
import yearsimulation.YearSimulation;

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


        final File file = new File(Constants.INPUT_PATH);
        final String[] paths = file.list();

        if (paths == null) {
            System.out.println("No input data");
            return;
        }

        final File outputDir = new File(Constants.OUTPUT_DIR);
        //Creating a folder using mkdir() method
        if (!outputDir.exists()) {
            final boolean bool = outputDir.mkdir();
            if (!bool) {
                System.out.println("Output directory make failed!");
            }
        }

        for (final String path : paths) {
            simulateAllYears(path);
        }
        Checker.calculateScore();
    }

    private static void simulateAllYears(final String path) {
        // Check whether file is actually a test file
        if (!path.startsWith(Constants.TEST_NAME)) {
            return;
        }
        // Read input file
        final String inputFilename = Constants.INPUT_PATH + path;
        final InputLoader inputLoader = new InputLoader(inputFilename);
        inputLoader.readData();

        // Prepare output file
        final String index = path.replaceAll("[^0-9]", "");
        final String outputFilename = Constants.OUTPUT_PATH + index
                + Constants.JSON_EXTENSION;
        final MyWriter writer = new MyWriter(outputFilename);

        // Running for number of years + round 0
        for (int i = 0; i <= Database.getInstance().
                getNumberOfYears(); i++) {
            CityStrategyEnum strategyEnum = CityStrategyEnum.ID;
            if (i != 0) {
                strategyEnum = Database.getInstance()
                        .getAnnualChanges().get(i - 1).getStrategy();
            }

            YearSimulation yearSimulation = new YearSimulation(
                    Utils.convertStrategyEnumToStrategy(strategyEnum));
            yearSimulation.simulateYear();
            Database.getInstance().saveYear();
            if (i != Database.getInstance().getNumberOfYears()) {
                Database.getInstance().updateDatabaseByYear(i);
            }
        }
        writer.closeJSON();
        Database.renewDatabase();
    }
}
