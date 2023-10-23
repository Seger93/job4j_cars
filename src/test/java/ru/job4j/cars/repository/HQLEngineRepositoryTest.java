package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Engine;


import java.util.List;

import static org.assertj.core.api.Assertions.*;

class HQLEngineRepositoryTest {
    private static SessionFactory sf;
    private static HQLEngineRepository hqlEngineRepository;

    @BeforeAll
    public static void initRepositories() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        hqlEngineRepository = new HQLEngineRepository(new CrudRepository(sf));
    }

    @AfterEach
    public void clear() {
        List<Engine> engines = (List<Engine>) hqlEngineRepository.findAll();
        for (Engine e : engines) {
            hqlEngineRepository.deleteById(e.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        Engine engine = new Engine();
        engine.setName("test Engine");
        assertThat(hqlEngineRepository.save(engine).get().getName()).isEqualTo("test Engine");
    }

    @Test
    public void whenDeleteEngineIsTrue() {
        Engine engine = new Engine();
        engine.setName("test Engine");
        hqlEngineRepository.save(engine);
        boolean resDel = hqlEngineRepository.deleteById(engine.getId());
        assertThat(resDel).isTrue();
    }

    @Test
    public void whenFindAllEngine() {
        Engine engine = new Engine();
        Engine engine1 = new Engine();
        engine.setName("test Engine");
        engine1.setName("test1 Engine1");
        hqlEngineRepository.save(engine);
        hqlEngineRepository.save(engine1);
        assertThat(hqlEngineRepository.findAll()).isEqualTo(List.of(engine, engine1));
    }
}