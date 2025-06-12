package shuba.practice.db.utils.db.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shuba.practice.db.config.Config;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DbQueries extends DbConnection {

    private static final Logger logger = LoggerFactory.getLogger(DbQueries.class);

    public DbQueries(Config config) {
        super(config);
    }

    public String getStoreAddressWithMostProducts(String categoryName) {

        logger.info("Trying to get store address with the most Products.");

        String sql = """
                SELECT stores.location, SUM(store_products.quantity) AS total_quantity
                FROM stores
                JOIN store_products ON stores.id = store_products.store_id
                JOIN products ON store_products.product_id = products.id
                JOIN categories ON products.category_id = categories.id
                WHERE categories.name = ?
                GROUP BY stores.location
                ORDER BY total_quantity DESC
                LIMIT 1;
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, categoryName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String location = rs.getString("location");
                    int totalQuantity = rs.getInt("total_quantity");
                    return String.format("Адреса магазину: %s, кількість товарів: %d", location, totalQuantity);
                } else {
                    return "Магазин із товарами зазначеної категорії не знайдено.";
                }
            }
        } catch (Exception e) {
            logger.error("Failed due to execute query: ", e);
            return "Помилка під час виконання запиту.";
        }
    }
}
