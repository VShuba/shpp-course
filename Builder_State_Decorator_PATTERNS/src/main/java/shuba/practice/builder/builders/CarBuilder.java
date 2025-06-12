package shuba.practice.builder.builders;

import shuba.practice.builder.cars.Car;
import shuba.practice.builder.cars.CarType;
import shuba.practice.builder.components.Engine;
import shuba.practice.builder.components.GPSNavigator;
import shuba.practice.builder.components.Transmission;
import shuba.practice.builder.components.TripComputer;

public class CarBuilder implements Builder {

    private CarType type;
    private int seats;
    private Engine engine;
    private Transmission transmission;
    private TripComputer tripComputer;
    private GPSNavigator gpsNavigator;

    @Override
    public void setCarType(CarType type) {
        this.type = type;
    }

    @Override
    public void setSeats(int seats) {
        this.seats = seats;
    }

    @Override
    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void setTransmission(Transmission transmission) {
        this.transmission = transmission;
    }

    @Override
    public void setTripComputer(TripComputer tripComputer) {
        this.tripComputer = tripComputer;
    }

    @Override
    public void setGPSNavigator(GPSNavigator gpsNavigator) {
        this.gpsNavigator = gpsNavigator;
    }

    public Car build() {
        return new Car(this.type, this.seats, this.engine, this.transmission, this.tripComputer, this.gpsNavigator);
    }
}
