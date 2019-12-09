package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/addUser")
    public String newuser(Model model){
        model.addAttribute("user", new User());
        return "userform";
    }
    @PostMapping("/process")
    public String process(@Valid @ModelAttribute User user, BindingResult result, Model model){
        model.addAttribute("user", user);
        if (result.hasErrors()){
            return "userform";
        }
        else{
            userService.saveUser(user);
        }
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

    @RequestMapping("/updateMessage/{id}")
    public String updateEmp(Model model, @PathVariable("id") long id){
        model.addAttribute("message", messageRepository.findById(id).get());
        return "messageform";
    }
    @RequestMapping("/deleteMessage/{id}")
    public String delEmp(@PathVariable("id") long id){
        messageRepository.deleteById(id);
        return "redirect:/";
    }

}
