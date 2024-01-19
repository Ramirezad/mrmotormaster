/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles;

import com.copernic.manageVehicles.domain.User;
import com.copernic.manageVehicles.domain.Vehicle;
import com.copernic.manageVehicles.services.UserServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author rfernandez
 */
@Controller
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    //UPDATE User
    @GetMapping("/update/{nif}")
    public String update(User user, Model model) {
        user = userService.find(user);
        model.addAttribute("user", user);
        return "user-edit";
    }

    //SHOW FORM User
    @GetMapping("/user/{nif}")
    public String viewById(User user) {
        return "user-details";
    }
    
    //SHOW FORM User
    @GetMapping("/signin")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "sign-in";
    }

    //SUMBIT FORM User
    @PostMapping("/signin")
    public String submitUser(User user, Model model) {
        userService.save(user);
        return "redirect:/users";
    }

    //LIST Users
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.getAll();
        model.addAttribute("users", users);
        return "user-list";
    }

    //DELETE User
    @GetMapping("/delete/{nif}")
    public String delete(@PathVariable("numberPlate") String nif) {
        userService.deleteById(nif);
        return "redirect:/users";
    }

    //VIEW User
    @GetMapping("/users/{nif}")
    public String viewUser(@PathVariable("nif") String nif, Model model) {
        User user = new User();
        user.setNif(nif);
        user = userService.find(user);
        List<Vehicle> vehicles = user.getVehicles();        
        model.addAttribute("user", user);  
        model.addAttribute("vehicles", vehicles);
        return "user-details";

    }
}
