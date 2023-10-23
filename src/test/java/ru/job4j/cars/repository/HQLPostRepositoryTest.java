package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.*;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

class HQLPostRepositoryTest {

    private static SessionFactory sf;
    private static HQLPostRepository hqlPostRepository;
    private static UserRepository userRepository;
    private static HQLFileRepository hqlFileRepository;
    private static HQLBrandRepository hqlBrandRepository;
    private static HQLPriceHistoryRepository hqlPriceHistoryRepository;

    @BeforeAll
    public static void initRepositories() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        hqlPostRepository = new HQLPostRepository(new CrudRepository(sf));
        userRepository = new UserRepository(new CrudRepository(sf));
        hqlBrandRepository = new HQLBrandRepository(new CrudRepository(sf));
        hqlFileRepository = new HQLFileRepository(new CrudRepository(sf));
        hqlPriceHistoryRepository = new HQLPriceHistoryRepository(new CrudRepository(sf));
    }

    @AfterEach
    public void clearAll() {
        hqlPriceHistoryRepository.findAll().forEach(car -> hqlPriceHistoryRepository.deleteById(car.getId()));
        hqlFileRepository.findAll().forEach(car -> hqlFileRepository.deleteById(car.getId()));
        hqlPostRepository.findAll().forEach(post -> hqlPostRepository.deleteById(post.getId()));
        userRepository.findAllOrderById().forEach(user -> userRepository.delete(user.getId()));
    }

    private User getUser() {
        User user = new User();
        user.setPassword("Sergei");
        user.setLogin("Sergei@mail.ru");
        userRepository.create(user);
        return user;
    }

    private User getUser1() {
        User user1 = new User();
        user1.setPassword("Test1");
        user1.setLogin("Test");
        userRepository.create(user1);
        return user1;
    }

    private User getUser2() {
        User user2 = new User();
        user2.setPassword("Test2");
        user2.setLogin("Test2");
        userRepository.create(user2);
        return user2;
    }

    private User getUser3() {
        User user2 = new User();
        user2.setPassword("Test3");
        user2.setLogin("Test3");
        userRepository.create(user2);
        return user2;
    }

    private Brand getBrand() {
        Brand brand = new Brand();
        brand.setName("opel");
        hqlBrandRepository.save(brand);
        return brand;
    }

    private File getFile() {
        File file = new File();
        file.setName("001");
        file.setPath("/file");
        hqlFileRepository.save(file);
        return file;
    }

    private File getFile1() {
        File file1 = new File();
        file1.setName("002");
        file1.setPath("/file2");
        hqlFileRepository.save(file1);
        return file1;
    }

    private PriceHistory getPriceHistory() {
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setAfter(111);
        priceHistory.setBefore(1000);
        hqlPriceHistoryRepository.save(priceHistory);
        return priceHistory;
    }

    @Test
    public void whenGetOnlyFilePost() {
        Post post = new Post();
        post.setDescription("Test post");
        post.setBrand(getBrand());
        post.setPriceHistories(List.of(getPriceHistory()));
        post.setFileId(Set.of(getFile()));
        post.setAutoUserId(getUser());
        post.setParticipates(List.of(getUser1()));
        hqlPostRepository.save(post);
        Post post1 = new Post();
        post1.setDescription("Test post1");
        post1.setBrand(getBrand());
        post1.setPriceHistories(List.of(getPriceHistory()));
        post1.setAutoUserId(getUser2());
        post1.setParticipates(List.of(getUser3()));
        hqlPostRepository.save(post1);
        Collection<Post> resList = hqlPostRepository.findByAllPostWithPhoto();
        assertThat(resList).isEqualTo(List.of(post));
    }

    @Test
    public void whenGetOnlyBrand() {
        Post post = new Post();
        post.setDescription("Test post");
        post.setBrand(getBrand());
        post.setPriceHistories(List.of(getPriceHistory()));
        post.setFileId(Set.of(getFile()));
        post.setAutoUserId(getUser());
        post.setParticipates(List.of(getUser1()));
        hqlPostRepository.save(post);
        Post post1 = new Post();
        post1.setDescription("Test post1");
        post1.setBrand(getBrand());
        post1.setPriceHistories(List.of(getPriceHistory()));
        post1.setFileId(Set.of(getFile1()));
        post1.setAutoUserId(getUser2());
        post1.setParticipates(List.of(getUser3()));
        hqlPostRepository.save(post1);
        Collection<Post> resList = hqlPostRepository.findAllPostWitchBrand("opel");
        assertThat(resList).isEqualTo(List.of(post, post1));
    }

    @Test
    public void whenFindByPostToday() {
        Post post = new Post();
        post.setDescription("Test post");
        post.setBrand(getBrand());
        post.setPriceHistories(List.of(getPriceHistory()));
        post.setCreated(LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.DAYS));
        post.setFileId(Set.of(getFile()));
        post.setAutoUserId(getUser());
        post.setParticipates(List.of(getUser1()));
        hqlPostRepository.save(post);
        Post post1 = new Post();
        post1.setDescription("Test post1");
        post1.setBrand(getBrand());
        post1.setPriceHistories(List.of(getPriceHistory()));
        post1.setAutoUserId(getUser2());
        post.setFileId(Set.of(getFile()));
        post1.setParticipates(List.of(getUser3()));
        hqlPostRepository.save(post1);
        Collection<Post> resList = hqlPostRepository.findAllPostToday();
        assertThat(resList).isEqualTo(List.of(post1));
    }

    @Test
    public void whenFindByIdAndCompareName() {
        Post post = new Post();
        post.setDescription("Test post");
        post.setBrand(getBrand());
        post.setPriceHistories(List.of(getPriceHistory()));
        post.setFileId(Set.of(getFile()));
        post.setAutoUserId(getUser());
        hqlPostRepository.save(post);
        Optional<Post> res = hqlPostRepository.findById(post.getId());
        assertThat(res.get().getId()).isEqualTo(post.getId());
        assertThat(res.get().getDescription()).isEqualTo("Test post");
    }

    @Test
    public void whenFindAll() {
        Post post = new Post();
        post.setDescription("Test post");
        post.setBrand(getBrand());
        post.setPriceHistories(List.of(getPriceHistory()));
        post.setFileId(Set.of(getFile()));
        post.setAutoUserId(getUser());
        post.setParticipates(List.of(getUser1()));
        hqlPostRepository.save(post);
        Post post1 = new Post();
        post1.setDescription("Test post1");
        post1.setBrand(getBrand());
        post1.setPriceHistories(List.of(getPriceHistory()));
        post1.setAutoUserId(getUser2());
        post1.setParticipates(List.of(getUser3()));
        hqlPostRepository.save(post1);
        Collection<Post> resList = hqlPostRepository.findAll();
        assertThat(resList).isEqualTo(List.of(post, post1));
    }
}