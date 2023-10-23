package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Owner;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class HQLOwnerRepositoryTest {
    private static HQLOwnerRepository hqlOwnerRepository;

    private static SessionFactory sf;

    @BeforeAll
    public static void initRepositories() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        hqlOwnerRepository = new HQLOwnerRepository(new CrudRepository(sf));
    }

    @AfterEach
    public void clear() {
        List<Owner> owners = (List<Owner>) hqlOwnerRepository.findAll();
        for (Owner e : owners) {
            hqlOwnerRepository.deleteById(e.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        Owner owner = new Owner();
        owner.setName("test Owner");
        assertThat(hqlOwnerRepository.save(owner).get().getName()).isEqualTo("test Owner");
    }

    @Test
    public void whenDeleteOwnerIsTrue() {
        Owner owner = new Owner();
        owner.setName("test Owner");
        hqlOwnerRepository.save(owner);
        boolean resDel = hqlOwnerRepository.deleteById(owner.getId());
        assertThat(resDel).isTrue();
    }

    @Test
    public void whenFindAllOwner() {
        Owner owner = new Owner();
        owner.setName("test Owner");
        Owner owner1 = new Owner();
        owner1.setName("test Owner1");
        hqlOwnerRepository.save(owner);
        hqlOwnerRepository.save(owner1);
        assertThat(hqlOwnerRepository.findAll()).isEqualTo(List.of(owner, owner1));
    }
}