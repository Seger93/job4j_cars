package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Owner;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class HQLOwnerRepository implements OwnerRepository {

    private final CrudRepository crudRepository;

    @Override
    public Optional<Owner> save(Owner owner) {
        Optional<Owner> res = Optional.empty();
        try {
            crudRepository.run(session -> session.persist(owner));
            res = Optional.of(owner);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public Optional<Owner> findById(int id) {
        return crudRepository.optional("from Owner WHERE id = :fId", Owner.class,
                Map.of("fId", id));
    }

    @Override
    public Collection<Owner> findAll() {
        return crudRepository.query("FROM Owner c ORDER BY c.id",
                Owner.class);
    }

    @Override
    public boolean deleteById(int id) {
        return crudRepository.runBoolean("DELETE Owner WHERE id = :fId",
                Map.of("fId", id));
    }
}