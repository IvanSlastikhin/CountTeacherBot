package ru.slastikhin.crud.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.slastikhin.crud.DAO.User;
import ru.slastikhin.crud.Repository.UserRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @Author: Ivan Slastikhin
 */
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = new LinkedList<>(userRepository.findAll());

            if (users.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id){
        Optional<User> userData = userRepository.findById(id);

        if (userData.isPresent()){
            return new ResponseEntity<>(userData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try{
            User _user = userRepository.save(new User(user.getName(), user.getAge()));
            return new ResponseEntity<>(_user, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user){
       Optional<User> userData = userRepository.findById(id);

       if (userData.isPresent()){
           User _user = userData.get();
           _user.setName(user.getName());
           _user.setAge(user.getAge());
           return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK);
       } else {
           return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
       }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") long id){
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
