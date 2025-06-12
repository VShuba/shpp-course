package shuba.practice.repository;

import shuba.practice.setters.Setter;

public interface Repository {
    void insertAll(Setter setter);
    void insertAllInSeveralThreads(Setter setter);
}
