package shuba.practice.dml;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shuba.practice.config.Config;


public class DMLDatabase {

    private static final Logger logger = LoggerFactory.getLogger(DMLDatabase.class);

    private final Config config;
    private final CqlSession session;

    public DMLDatabase(Config config, CqlSession session) {
        this.config = config;
        this.session = session;
    }

    public void countAllRecords() {
        executeQueryScript(Config.loadScript(config.getPassToCountRecordsScript()), "Count Records");
    }


    public void executeHardQuery() {
        executeQueryScript(Config.loadScript(config.getPassToQueryScript()), "Complex Queries");
    }

    private void executeQueryScript(String queryScript, String action) {
        logger.info("Executing CQL script: {}", action);

        String[] commands = queryScript.split(";");
        for (String command : commands) {
            if (!command.trim().isEmpty()) {
                ResultSet resultSet = session.execute(command.trim());
                logResults(command, resultSet);
            }
        }

        logger.info("Execution of {} was successful.", action);
    }


    private void logResults(String command, ResultSet resultSet) {
        logger.info("Results for query: {}", command);

        for (Row row : resultSet) {
            StringBuilder rowLog = new StringBuilder();
            row.getColumnDefinitions().forEach(definition -> {
                String columnName = definition.getName().asInternal();
                Object value = row.getObject(columnName);
                rowLog.append(columnName).append(": ").append(value).append(", ");
            });

            logger.info("Row: {}", rowLog);
        }
    }

}
