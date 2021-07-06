package ru.slastikhin.crud.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.slastikhin.crud.DAO.User;

/**
 * @Author: Ivan Slastikhin
 */
public interface UserRepository extends JpaRepository<User, Long> {
    
}
