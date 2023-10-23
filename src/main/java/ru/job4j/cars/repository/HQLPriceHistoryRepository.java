package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.PriceHistory;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class HQLPriceHistoryRepository {
    private final CrudRepository crudRepository;

    public Optional<PriceHistory> findById(int id) {
        return crudRepository.optional("FROM PriceHistory WHERE id = :fId", PriceHistory.class,
                Map.of("fId", id));
    }

    public boolean deleteById(int id) {
        return crudRepository.runBoolean("DELETE PriceHistory WHERE id = :fId",
                Map.of("fId", id));
    }

    public Collection<PriceHistory> findAll() {
        return crudRepository.query("FROM PriceHistory ORDER BY id",
                PriceHistory.class);
    }

    public Optional<PriceHistory> save(PriceHistory priceHistory) {
        Optional<PriceHistory> res = Optional.empty();
        try {
            crudRepository.run(session -> session.persist(priceHistory));
            res = Optional.of(priceHistory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}