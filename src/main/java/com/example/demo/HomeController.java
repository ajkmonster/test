package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
public class HomeController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/")
    public String index(Model model){
        model.addAttribute("messages", messageRepository.findAll());
        return "index";
    }

    @RequestMapping("/addMessage")
    public String add(Model model){
        model.addAttribute("message", new Message());
        return "messageform";
    }
    @RequestMapping("/addUser")
    public String newuser(Model model){
        model.addAttribute("user", new User());
        return "userform";
    }
    @PostMapping("/processuser")
    public String processuser(@Valid @ModelAttribute User user){
        userService.saveUser(user);
        return "redirect:/";
    }

    @PostMapping("/processmessage")
    public String processmessage(@Valid @ModelAttribute Message message, Principal principal){
        User user = userRepository.findByUsername(principal.getName());
        message.setUser(user);
        Set<Message> messages;
        if(user.getMessages().size() > 0){
            messages = new HashSet<>(user.getMessages());
        }
        else{
            messages = new HashSet<>();
        }
        messages.add(message);
        messageRepository.save(message);
        user.setMessages(messages);
        userRepository.save(user);
        return "redirect:/";
    }
}
