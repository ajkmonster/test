package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserService userService;
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... strings) throws Exception {
        roleRepository.save(new Role("USER"));
        roleRepository.save(new Role("ADMIN"));

        User user = new User("yusuf@gmail.com", "password","Yusuf","Reyazuddin","yusuf",true);
        userService.saveUser(user);

        Set<Message> messages = new HashSet<>();

        Message message = new Message("I am so tired","12/09/2019");
        message.setUser(user);
        messageRepository.save(message);
        messages.add(message);

        message = new Message("Coding is hard and fun", "12/06/2019");
        message.setUser(user);
        messageRepository.save(message);
        messages.add(message);

        user.setMessages(messages);
        userService.saveUser(user);
    }
}
