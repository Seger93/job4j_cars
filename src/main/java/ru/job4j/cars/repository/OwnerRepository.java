package ru.job4j.cars.repository;

import ru.job4j.cars.model.Owner;

import java.util.Collection;
import java.util.Optional;

public interface OwnerRepository {

    Optional<Owner> save(Owner owner);

    Optional<Owner> findById(int id);

    Collection<Owner> findAll();

    boolean deleteById(int id);
}