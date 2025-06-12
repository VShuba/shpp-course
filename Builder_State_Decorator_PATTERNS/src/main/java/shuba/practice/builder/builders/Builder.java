package shuba.practice.builder.builders;


import shuba.practice.builder.cars.CarType;
import shuba.practice.builder.components.Engine;
import shuba.practice.builder.components.GPSNavigator;
import shuba.practice.builder.components.Transmission;
import shuba.practice.builder.components.TripComputer;

public interface Builder {
    void setCarType(CarType type);
    void setSeats(int seats);
    void setEngine(Engine engine);
    void setTransmission(Transmission transmission);
    void setTripComputer(TripComputer tripComputer);
    void setGPSNavigator(GPSNavigator gpsNavigator);
}