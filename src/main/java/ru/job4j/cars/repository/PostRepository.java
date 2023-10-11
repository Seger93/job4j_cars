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
public class PostRepository {
    private final CrudRepository crudRepository;

    public Collection<Post> findByAllPostWithPhoto() {
        return crudRepository.query("Select from auto_post p where p.file_id is null order by p.id", Post.class);
    }

    public Collection<Post> findAllPostWitchBrand(int carId) {
        return crudRepository.query("from auto_post WHERE car_id = :fcar_id", Post.class,
                Map.of("fcar_id", carId));
    }

    public Collection<Post> findAllPostToday() {
        LocalDateTime today = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime tomorrow = today.plusDays(1).truncatedTo(ChronoUnit.DAYS);
        return crudRepository.query("from auto_post WHERE created >= :today AND created < :tomorrow", Post.class,
                Map.of("today", today, "tomorrow", tomorrow));
    }
}