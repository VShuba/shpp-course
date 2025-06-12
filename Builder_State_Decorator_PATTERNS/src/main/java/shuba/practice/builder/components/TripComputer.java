package shuba.practice.builder.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shuba.practice.builder.cars.Car;

public class TripComputer {

    private static final Logger LOGGER = LoggerFactory.getLogger(TripComputer.class);

    private Car car;

    public void setCar(Car car) {

        this.car = car;
    }

    public void showFuelLevel() {
        LOGGER.info("Fuel level: {}", car.getFuel());
    }

    public void showStatus() {
        if (this.car.getEngine().isStarted()) {
            LOGGER.info("Car is started");
        } else {
            LOGGER.info("Car isn't started");
        }
    }
}
