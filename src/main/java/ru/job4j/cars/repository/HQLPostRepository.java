package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Post;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HQLPostRepository {
    private final CrudRepository crudRepository;

    public Collection<Post> findByAllPostWithPhoto() {
        return crudRepository.query("FROM Post p JOIN FETCH p.participates WHERE SIZE(p.fileId) > 0 ORDER BY p.id", Post.class);
    }

    public Collection<Post> findAllPostWitchBrand(String brandName) {
        return crudRepository.query("SELECT p FROM Post p JOIN FETCH p.brand b WHERE b.name = :fBrand", Post.class,
                Map.of("fBrand", brandName));
    }

    public Collection<Post> findAllPostToday() {
        LocalDateTime today = LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.DAYS);
        return crudRepository.query("FROM Post p WHERE p.created > :today", Post.class,
                Map.of("today", today));
    }

    public Optional<Post> findById(int id) {
        return crudRepository.optional("FROM Post p WHERE p.id = :fId", Post.class,
                Map.of("fId", id));
    }

    public Collection<Post> findAll() {
        return crudRepository.query("FROM Post p ORDER BY p.id",
                Post.class);
    }

    public boolean deleteById(int id) {
        return crudRepository.runBoolean("DELETE Post WHERE id = :fId",
                Map.of("fId", id));
    }

    public Optional<Post> save(Post post) {
        Optional<Post> res = Optional.empty();
        try {
            crudRepository.run(session -> session.persist(post));
            res = Optional.of(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}