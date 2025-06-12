package shuba.practice.generate;

import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shuba.practice.config.Config;
import shuba.practice.dto.CategoryDTO;
import shuba.practice.dto.ProductTypeByStoreDTO;
import shuba.practice.dto.StoreDTO;
import shuba.practice.dto.StoreProductDTO;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class FakeGenerate {
    private static final Faker faker = new Faker(new Locale("uk_UA"));

    private static final Logger logger = LoggerFactory.getLogger(FakeGenerate.class);

    private static final Config config;

    private static List<UUID> storeUUIDs;

    private AtomicInteger atomicEncounter;

    private final AtomicInteger categoryCounter = new AtomicInteger();
    private final AtomicInteger storeCounter = new AtomicInteger();
    private final AtomicInteger storeProductCounter = new AtomicInteger();
    private final AtomicInteger productTypeByStoreCounter = new AtomicInteger();

    static {
        config = new Config();
        generateUUIDsForStores(config.getCountStores());
    }

    public FakeGenerate() {
        atomicEncounter = new AtomicInteger(config.getCountStores());
    }

    private static void generateUUIDsForStores(int countStores) {
        logger.info("Start generation UUIDs for stores.");
        Set<UUID> uniqueUUIDs = new HashSet<>();
        while (uniqueUUIDs.size() != countStores) {
            uniqueUUIDs.add(UUID.randomUUID());
        }
        storeUUIDs = new CopyOnWriteArrayList<>(uniqueUUIDs);
        logger.info("Finish generation UUIDs for stores.");
    }

    public CategoryDTO getRandomCategory() {
        long count = categoryCounter.incrementAndGet();
        logOut(count, "Category", config.getCountCategories());
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(UUID.randomUUID()); // uuid
        categoryDTO.setName(faker.commerce().department());
        return categoryDTO;
    }


    public StoreDTO getRandomStore() {
        long count = storeCounter.incrementAndGet();
        logOut(count, "Stores", config.getCountStores());

        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setId(getStoreUUID()); // uuid STORE
        storeDTO.setName(faker.company().name());
        storeDTO.setAddress(faker.address().city());
        return storeDTO;
    }

    public StoreProductDTO getRandomStoreProduct() {
        long count = storeProductCounter.incrementAndGet();
        logOut(count, "StoreProduct", config.getCountStoreProducts());

        StoreProductDTO storeProductDTO = new StoreProductDTO();
        storeProductDTO.setProductId(UUID.randomUUID()); // тут є факт того, що касандра тупо перезапише вже наявний
        storeProductDTO.setStoreId(getStoreUUID()); // uuid STORE
        storeProductDTO.setProductName(faker.commerce().productName());
        storeProductDTO.setProductType(faker.commerce().department()); // это имя категории
        storeProductDTO.setQuantity((short) faker.number().numberBetween(-10, 100)); // not valid
        return storeProductDTO;
    }

    public ProductTypeByStoreDTO getRandomProductTypeByStore() {
        long count = productTypeByStoreCounter.incrementAndGet();
        logOut(count, "ProductTypeByStore", config.getProductTypeByStore());

        ProductTypeByStoreDTO productTypeByStoreDTO = new ProductTypeByStoreDTO();
        productTypeByStoreDTO.setProductType(faker.commerce().department()); // тут є факт того, що касандра тупо перезапише вже наявний
        productTypeByStoreDTO.setStoreId(getStoreUUID()); // uuid STORE
        productTypeByStoreDTO.setStoreAddress(faker.address().city());
        productTypeByStoreDTO.setTotalQuantity((short) faker.number().numberBetween(-10, 300));
        return productTypeByStoreDTO;
    }

    private synchronized UUID getStoreUUID() {
        if (atomicEncounter.get() == 0) {
            atomicEncounter = new AtomicInteger(config.getCountStores());
        }
        return storeUUIDs.get(atomicEncounter.decrementAndGet());
    }

    private void logOut(long currantC, String tableName, int totalC) {
        if (currantC % config.getLogNumber() == 0 || currantC == totalC) {
            logger.info("Generated {} in {}", currantC, tableName);
        }
    }
}
