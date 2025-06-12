package shuba.practice.db.generation;

import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shuba.practice.db.config.Config;
import shuba.practice.db.dto.*;
import shuba.practice.db.validation.ValidateDTO;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class GenerateRandomDTO {

    private static final Faker faker = new Faker(new Locale("uk_UA"));

    private static final Logger logger = LoggerFactory.getLogger(GenerateRandomDTO.class);

    private final Config config;

    public GenerateRandomDTO(Config config) {
        this.config = config;
    }

    private final AtomicInteger categoryCounter = new AtomicInteger();
    private final AtomicInteger storeCounter = new AtomicInteger();
    private final AtomicInteger productCounter = new AtomicInteger();
    private final AtomicInteger storeProductCounter = new AtomicInteger();

    private CategoryDTO generateCategoryDTO() {
        long count = categoryCounter.incrementAndGet();
        if (count % 100_000 == 0 || count == config.getCountCategories()) {
            logger.info("Generated {} categories", count);
        }
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(faker.commerce().department());
        return categoryDTO;
    }

    private StoreDTO generateStoreDTO() {
        long count = storeCounter.incrementAndGet();
        if (count % 100_000 == 0 || count == config.getCountStores()) {
            logger.info("Generated {} stores", count);
        }
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setName(faker.company().name());
        storeDTO.setLocation(faker.address().city());
        return storeDTO;
    }

    private ProductDTO generateProductDTO() {
        long count = productCounter.incrementAndGet();
        if (count % 100_000 == 0 || count == config.getCountProducts()) {
            logger.info("Generated {} products", count);
        }
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(faker.commerce().productName());
        productDTO.setPrice(BigDecimal.valueOf(faker.number().randomDouble(2, 1, 1000))); // цена от 1 до 1000
        productDTO.setCategoryId(faker.number().numberBetween(1, config.getCountCategories() + 1)); // существующий categoryId
        return productDTO;
    }

    public StoreProductDTO generateStoreProductDTO() {
        long count = storeProductCounter.incrementAndGet();
        if (count % 100_000 == 0) {
            logger.info("Generated {} store_products", count);
        }
        if (count == config.getCountStoreProducts()) {
            logger.info("Generated total {} store_products", count);
        }
        StoreProductDTO storeProductDTO = new StoreProductDTO();
        storeProductDTO.setStoreId(faker.number().numberBetween(1, config.getCountStores() + 1)); // существующий storeId
        storeProductDTO.setProductId(faker.number().numberBetween(1, config.getCountProducts() + 1)); // существующий productId
        storeProductDTO.setQuantity(faker.number().numberBetween(1, 100)); // количество от 1 до 100
        return storeProductDTO;
    }

    public void generateAndValidateDTOs(BlockingQueue<ValidatableDTO> queue, int targetSize, CountDownLatch latch, String dtoType) {
        try {
            while (queue.size() < targetSize) {
                ValidatableDTO dto = switch (dtoType) {
                    case "category" -> generateCategoryDTO();
                    case "store" -> generateStoreDTO();
                    case "product" -> generateProductDTO();
                    case "storeProduct" -> generateStoreProductDTO();
                    default -> throw new IllegalArgumentException("Unknown DTO type: " + dtoType);
                };

                Map<String, String> violations = ValidateDTO.validateDTO(dto);
                if (violations.isEmpty()) {
                    queue.add(dto);
                }

            }
        } finally {
            latch.countDown();
        }
    }
}
