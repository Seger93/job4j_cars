package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.model.Owner;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class HQLCarRepositoryTest {

    private static SessionFactory sf;
    private static HQLCarRepository hqlCarRepository;
    private static HQLOwnerRepository hqlOwnerRepository;

    @BeforeAll
    public static void initRepositories() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        hqlCarRepository = new HQLCarRepository(new CrudRepository(sf));
        hqlOwnerRepository = new HQLOwnerRepository(new CrudRepository(sf));
    }

    @AfterEach
    public void clear() {
        hqlCarRepository.findAll().forEach(car -> hqlCarRepository.deleteById(car.getId()));
        hqlOwnerRepository.findAll().forEach(car -> hqlOwnerRepository.deleteById(car.getId()));
    }

    @Test
    public void whenSaveIsPresetAndGetName() {
        Owner owner = new Owner();
        owner.setName("testOwner");
        hqlOwnerRepository.save(owner);
        Set<Owner> setOwners = Set.of(owner);
        Engine engine = new Engine();
        engine.setName("W8");
        Car car = new Car();
        car.setName("Car1");
        car.setEngine(engine);
        car.setOwners(setOwners);
        hqlCarRepository.save(car);
        Optional<Car> res = hqlCarRepository.findById(car.getId());
        assertThat(res.get().getName()).isEqualTo("Car1");
    }

    @Test
    public void whenGetAllCar() {
        Owner owner = new Owner();
        owner.setName("testOwner");
        hqlOwnerRepository.save(owner);
        Engine engine = new Engine();
        engine.setName("W8");
        Car car = new Car();
        car.setName("Car1");
        car.setEngine(engine);
        car.setOwners(Set.of(owner));
        hqlCarRepository.save(car);
        List<Car> res = (List<Car>) hqlCarRepository.findAll();
        assertThat(res).isEqualTo(List.of(car));
    }

    @Test
    public void whenDeleteCarIsTrue() {
        Owner owner = new Owner();
        owner.setName("testOwner");
        hqlOwnerRepository.save(owner);
        Engine engine = new Engine();
        engine.setName("W8");
        Car car = new Car();
        car.setName("Car1");
        car.setEngine(engine);
        car.setOwners(Set.of(owner));
        hqlCarRepository.save(car);
        assertThat(hqlCarRepository.deleteById(car.getId())).isTrue();
    }
}