package ru.jhonsy.telegram.bots.countteacherbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jhonsy.telegram.bots.countteacherbot.model.User;

/**
 * @Author: Ivan Slastikhin
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User findByChatId(long id);
}
