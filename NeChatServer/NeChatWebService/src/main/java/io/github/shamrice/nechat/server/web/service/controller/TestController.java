package io.github.shamrice.nechat.server.web.service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Erik on 10/21/2017.
 */

@Controller
@RequestMapping(path = "/test")
public class TestController {
/*
    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/add")
    public @ResponseBody String addNewUser(@RequestParam String login, @RequestParam String password, @RequestParam String email) {
        User n = new User();
        n.setLogin(login);
        n.setPassword(password);
        n.setEmail(email);
        userRepository.save(n);
        return "Saved";
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping(path = "/find", params = "login")
    public Iterable<User> findByLogin(
            @Spec(path = "login", spec = Like.class) Specifications<User> spec) {

        return null;
        //return userRepository.findAll(spec);

    }
    */

}
