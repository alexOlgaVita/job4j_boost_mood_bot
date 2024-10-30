package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findAll();

    default List<User> findByClientId(Long clientId) {
        return findAll().stream()
                .filter(user -> user.getClientId() == clientId)
                .collect(Collectors.toList());
    }
}
