package shuba.practice.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shuba.practice.builder.builders.CarBuilder;
import shuba.practice.builder.builders.CarManualBuilder;
import shuba.practice.builder.cars.Car;
import shuba.practice.builder.cars.CarType;
import shuba.practice.builder.cars.Manual;
import shuba.practice.builder.components.Engine;
import shuba.practice.builder.components.Transmission;
import shuba.practice.builder.director.Director;

public class Demonstrate {
    private static final Logger LOGGER = LoggerFactory.getLogger(Demonstrate.class);

    public static void main(String[] args) {
        Director director = new Director();

        // create car
        CarBuilder carBuilder = new CarBuilder();
        director.constructSportCar(carBuilder);

        // ось тут
        carBuilder.setTransmission(Transmission.AUTOMATIC);

        Car sportCar = carBuilder.build();


        LOGGER.info("Created car: {}", sportCar.getCarType());

        Engine engine = sportCar.getEngine();
//        engine.on();
//        engine.go(10);

        LOGGER.info("Volume of engine: {}", engine.getVolume());

        // create manual car
        CarManualBuilder manualBuilder = new CarManualBuilder();
        director.constructSportCar(manualBuilder);
        Manual sportCarManual = manualBuilder.getResult();
        LOGGER.info("Manual how to use: \n {}", sportCarManual.getInfo());
    }
}
