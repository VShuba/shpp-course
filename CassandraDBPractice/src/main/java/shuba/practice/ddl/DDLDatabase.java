package shuba.practice.ddl;

import com.datastax.oss.driver.api.core.CqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shuba.practice.config.Config;

public class DDLDatabase {
    private static final Logger logger = LoggerFactory.getLogger(DDLDatabase.class);

    private final Config config;
    private final CqlSession session;

    public DDLDatabase(Config config, CqlSession session) {
        this.config = config;
        this.session = session;
    }

    public void createTables() {
        executeScript(Config.loadScript(config.getPassToCreateTablesScript()), "Create Tables");
    }

    public void deleteTables() {
        executeScript(Config.loadScript(config.getPassToDeleteTablesScript()), "Delete Tables");
    }

    private void executeScript(String createScript, String action) {
        logger.info("Trying to execute CQL script: {}", action);

        String[] commands = createScript.split(";");
        for (String command : commands) {
            if (!command.isEmpty()) {
                session.execute(command);
            }
        }
        logger.info("Execute was successful.");
    }

}
