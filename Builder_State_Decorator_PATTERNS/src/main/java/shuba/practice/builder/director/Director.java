package shuba.practice.builder.director;

import shuba.practice.builder.builders.Builder;
import shuba.practice.builder.cars.CarType;
import shuba.practice.builder.components.Engine;
import shuba.practice.builder.components.GPSNavigator;
import shuba.practice.builder.components.Transmission;
import shuba.practice.builder.components.TripComputer;

public class Director { // return this!!! for all setters
    public void constructSportCar(Builder builder){
        builder.setCarType(CarType.SPORT_CAR);
        builder.setSeats(2);
        builder.setEngine(new Engine(3.0,0));
        builder.setTransmission(Transmission.SEMI_AUTOMATIC);
        builder.setTripComputer(new TripComputer());
        builder.setGPSNavigator(new GPSNavigator());
    }
    public void constructCityCar(Builder builder) {
        builder.setCarType(CarType.CITY_CAR);
        builder.setSeats(2);
        builder.setEngine(new Engine(1.2, 100));
        builder.setTransmission(Transmission.AUTOMATIC);
        builder.setTripComputer(new TripComputer());
        builder.setGPSNavigator(new GPSNavigator());
    }

    public void constructFamilyCar(Builder builder) {
        builder.setCarType(CarType.FAMILY_CAR);
        builder.setSeats(5);
        builder.setEngine(new Engine(1.8, 1000));
        builder.setTransmission(Transmission.MANUAL);
        builder.setGPSNavigator(new GPSNavigator());
    }
}
