package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Brand;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class HQLBrandRepository implements BrandRepository {

    private final CrudRepository crudRepository;

    @Override
    public Optional<Brand> save(Brand brand) {
        Optional<Brand> res = Optional.empty();
        try {
            crudRepository.run(session -> session.persist(brand));
            res = Optional.of(brand);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public Optional<Brand> findById(int id) {
        return crudRepository.optional("from Brand WHERE id = :fId", Brand.class,
                Map.of("fId", id));
    }

    @Override
    public Collection<Brand> findAll() {
        return crudRepository.query("FROM Brand c ORDER BY c.id",
                Brand.class);
    }

    @Override
    public boolean deleteById(int id) {
        return crudRepository.runBoolean("DELETE Brand WHERE id = :fId",
                Map.of("fId", id));
    }
}
