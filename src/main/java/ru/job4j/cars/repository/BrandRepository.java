package ru.job4j.cars.repository;

import ru.job4j.cars.model.Brand;
import ru.job4j.cars.model.Car;

import java.util.Collection;
import java.util.Optional;

public interface BrandRepository {
    Optional<Brand> save(Brand brand);

    Optional<Brand> findById(int id);

    Collection<Brand> findAll();

    boolean deleteById(int id);
}
