package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.model.PriceHistory;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class HQLPriceHistoryRepositoryTest {
    private static SessionFactory sf;
    private static HQLPriceHistoryRepository hqlPriceHistoryRepository;

    @BeforeAll
    public static void initRepositories() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        hqlPriceHistoryRepository = new HQLPriceHistoryRepository(new CrudRepository(sf));
    }

    @AfterEach
    public void clear() {
        List<PriceHistory> historyList = (List<PriceHistory>) hqlPriceHistoryRepository.findAll();
        for (PriceHistory e : historyList) {
            hqlPriceHistoryRepository.deleteById(e.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setBefore(1000);
        priceHistory.setAfter(1500);
        assertThat(hqlPriceHistoryRepository.save(priceHistory).get().getAfter()).isEqualTo(1500);
    }

    @Test
    public void whenDeleteEngineIsTrue() {
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setBefore(1000);
        priceHistory.setAfter(1500);
        hqlPriceHistoryRepository.save(priceHistory);
        boolean resDel = hqlPriceHistoryRepository.deleteById(priceHistory.getId());
        assertThat(resDel).isTrue();
    }

    @Test
    public void whenFindAllEngine() {
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setBefore(1000);
        priceHistory.setAfter(1500);
        PriceHistory priceHistory1 = new PriceHistory();
        priceHistory1.setBefore(9990);
        priceHistory1.setAfter(10000);
        hqlPriceHistoryRepository.save(priceHistory);
        hqlPriceHistoryRepository.save(priceHistory1);
        assertThat(hqlPriceHistoryRepository.findAll()).isEqualTo(List.of(priceHistory, priceHistory1));
    }
}