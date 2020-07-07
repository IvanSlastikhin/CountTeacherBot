package ru.jhonsy.telegram.bots.countteacherbot.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jhonsy.telegram.bots.countteacherbot.model.User;
import ru.jhonsy.telegram.bots.countteacherbot.repo.UserRepository;

import java.util.List;

/**
 * @Author: Ivan Slastikhin
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public User findByChatId(long id){
        return userRepository.findByChatId(id);
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    @Transactional
    public void addUser(User user){
        user.setAdmin(userRepository.count() == 0);
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(User user){
        userRepository.save(user);
    }
}
