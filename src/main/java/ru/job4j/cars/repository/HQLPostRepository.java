package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Post;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Map;

@Repository
@AllArgsConstructor
public class HQLPostRepository {
    private final CrudRepository crudRepository;

    public Collection<Post> findByAllPostWithPhoto() {
        return crudRepository.query("SELECT FROM Post p WHERE p.file_id is null order by p.id", Post.class);
    }

    public Collection<Post> findAllPostWitchBrand(String brand) {
        return crudRepository.query("FROM Post p WHERE p.car.brand = :fBrand", Post.class,
                Map.of("fBrand", brand));
    }

    public Collection<Post> findAllPostToday() {
        LocalDateTime today = LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.DAYS);
        return crudRepository.query("FROM Post WHERE created > :today", Post.class,
                Map.of("today", today));
    }
}