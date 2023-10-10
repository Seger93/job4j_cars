package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class HQLCarRepository implements CarRepository {

    CrudRepository crudRepository;

    @Override
    public Optional<Car> save(Car car) {
        Optional<Car> res = Optional.empty();
        try {
            crudRepository.run(session -> session.persist(car));
            res = Optional.of(car);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public Optional<Car> findById(int id) {
        return crudRepository.optional("from Car WHERE id = :fId", Car.class,
                Map.of("fId", id));
    }

    @Override
    public Collection<Car> findAll() {
        return crudRepository.query("FROM Car c ORDER BY c.id",
                Car.class);
    }

    @Override
    public boolean deleteById(int id) {
        return crudRepository.runBoolean("DELETE Car WHERE id = :fId",
                Map.of("fId", id));
    }
}