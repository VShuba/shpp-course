//package shuba.practice.repository;
//
//import com.datastax.oss.driver.api.core.CqlSession;
//import com.datastax.oss.driver.api.core.cql.*;
//import shuba.practice.dto.CategoryDTO;
//import shuba.practice.dto.ProductTypeByStoreDTO;
//import shuba.practice.dto.StoreDTO;
//import shuba.practice.dto.StoreProductDTO;
//
//import java.util.Collections;
//
//public class Rep {
//    private final CqlSession session;
//
//    public Rep(CqlSession session) {
//        this.session = session;
//    }
//
//    public void insertCategory(CategoryDTO category) {
//        String query = "INSERT INTO epicenter.categories (id, name) VALUES (?, ?)";
//        PreparedStatement preparedStatement = session.prepare(query);
//        BoundStatement boundStatement = preparedStatement.bind(category.getId(), category.getName());
//        session.execute(boundStatement);
//    }
//
//    public void insertStore(StoreDTO store) {
//        String query = "INSERT INTO epicenter.stores (id, name, address) VALUES (?, ?, ?)";
//        PreparedStatement preparedStatement = session.prepare(query);
//        BoundStatement boundStatement = preparedStatement.bind(store.getId(), store.getName(), store.getAddress());
//        session.execute(boundStatement);
//    }
//
//    public void insertStoreProduct(StoreProductDTO product) {
//        String query = "INSERT INTO epicenter.store_products (store_id, product_id, product_name, product_type, quantity) VALUES (?, ?, ?, ?, ?)";
//        PreparedStatement preparedStatement = session.prepare(query);
//        BoundStatement boundStatement = preparedStatement.bind(
//                product.getStoreId(),
//                product.getProductId(),
//                product.getProductName(),
//                product.getProductType(),
//                product.getQuantity()
//        );
//        session.execute(boundStatement);
//    }
//
//    public void insertProductTypeByStore(ProductTypeByStoreDTO productType) {
//        String query = "INSERT INTO epicenter.product_types_by_store (product_type, store_id, store_address, total_quantity) VALUES (?, ?, ?, ?)";
//        PreparedStatement preparedStatement = session.prepare(query);
//        BoundStatement boundStatement = preparedStatement.bind(
//                productType.getProductType(),
//                productType.getStoreId(),
//                productType.getStoreAddress(),
//                productType.getTotalQuantity()
//        );
//        session.execute(boundStatement);
//    }
//    public class CategorySetter {
//
//        public BoundStatement getBoundStatement(PreparedStatement ps) {
//            CategoryDTO categoryDTO = new CategoryDTO();
//            return ps.bind(
//                    categoryDTO.getId(),
//                    categoryDTO.getName()
//            );
//        }
//
//        public String[] getColumns() {
//            return new String[]{"name"};
//        }
//    }
//
//    private String createSQLInsert(String tableName, String[] columns) {
//        String columnNames = String.join(", ", columns);
//        String placeHolders = String.join(", ", Collections.nCopies(columns.length, "?"));
//
//        return "INSERT INTO " + tableName + " ( " + columnNames + " ) VALUES ( " + placeHolders + " )";
//    }
//}
////            insertData(repository, session, new CategorySetter(config), "Categories");
////            insertData(repository, session, new StoreSetter(config), "Stores");
////            insertData(repository, session, new StoreProductSetter(config), "StoreProducts");
////            insertData(repository, session, new ProductTypeByStoreSetter(config), "ProductTypeByStore");