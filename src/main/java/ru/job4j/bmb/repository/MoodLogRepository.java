package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface MoodLogRepository extends CrudRepository<MoodLog, Long> {
    List<MoodLog> findAll();

    List<MoodLog> findByUserId(Long userId);

    Stream<MoodLog> findByUserIdOrderByCreatedAtDesc(Long userId);

     default List<User> findUsersWhoDidNotVoteToday(long startOfDay, long endOfDay) {
       return List.of();
     }

    default List<MoodLog> findMoodLogsForWeek(Long userId, long weekStart) {
        return List.of();
    }

    default List<MoodLog> findMoodLogsForMonth(Long userId, long monthStart) {
        return List.of();
    }
}
