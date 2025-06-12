package simplecalculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import static simplecalculator.MultiplicationTable.printTable;
import static simplecalculator.PropertiesModifier.*;

// Temporary comment
// Temporary comment
// Temporary comment
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting the program.");

        Properties properties = new Properties();
        try {
            load(properties);

            String numberType = System.getProperty("number.type", "int").toLowerCase();

            Number min = getMin(properties, numberType);
            Number max = getMax(properties, numberType);
            Number increment = getIncrement(properties, numberType);

            validateProperties(numberType, min, max, increment);

            printTable(numberType, min, max, increment);

        } catch (Exception e) {
            logger.error("An error occurred: ", e);
        }

        logger.info("Program finished.");
    }
}
