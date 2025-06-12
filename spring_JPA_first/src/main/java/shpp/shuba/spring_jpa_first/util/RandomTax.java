package shpp.shuba.spring_jpa_first.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomTax {

    private final Random random = new Random();

    public String generateRandomTaxIdNumber() {
        int[] digits = new int[10];

        for (int i = 1; i < 10; i++) {
            digits[i] = random.nextInt(10);
        }

        int checksum = (digits[1] * -1 + digits[2] * 5 + digits[3] * 7 + digits[4] * 9 +
                digits[5] * 4 + digits[6] * 6 + digits[7] * 10 + digits[8] * 5 + digits[9] * 7) % 11;

        digits[0] = (checksum == 10) ? 0 : checksum;

        StringBuilder taxId = new StringBuilder();
        for (int digit : digits) {
            taxId.append(digit);
        }


        return taxId.toString();
    }
}
