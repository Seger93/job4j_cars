package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.File;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class HQLFileRepository implements FileRepository {

    private final CrudRepository crudRepository;

    @Override
    public Optional<File> save(File file) {
        Optional<File> res = Optional.empty();
        try {
            crudRepository.run(session -> session.persist(file));
            res = Optional.of(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public Optional<File> findById(int id) {
        return crudRepository.optional("FROM File WHERE c.id = :fId",
                File.class,
                Map.of("fId", id));
    }

    @Override
    public boolean deleteById(int id) {
        return crudRepository.runBoolean("DELETE File WHERE id = :fId",
                Map.of("fId", id));
    }

    public Collection<File> findAll() {
        return crudRepository.query("FROM File c ORDER BY c.id",
                File.class);
    }
}