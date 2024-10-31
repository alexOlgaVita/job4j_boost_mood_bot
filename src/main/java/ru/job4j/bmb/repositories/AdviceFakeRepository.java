package ru.job4j.bmb.repositories;

import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.bmb.model.Advice;
import ru.job4j.bmb.repository.AdviceRepository;

import java.util.ArrayList;
import java.util.List;

public class AdviceFakeRepository extends CrudRepositoryFake<Advice, Long> implements AdviceRepository {
    public List<Advice> findAll() {
        return new ArrayList<>(memory.values());
    }
}