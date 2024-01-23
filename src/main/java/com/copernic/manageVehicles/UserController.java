/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles;

import com.copernic.manageVehicles.domain.User;
import com.copernic.manageVehicles.domain.Vehicle;
import com.copernic.manageVehicles.services.UserServiceImpl;
import com.copernic.manageVehicles.services.VehicleServiceImpl;
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
    @Autowired
    private VehicleServiceImpl vehicleService;

    //UPDATE User
    @GetMapping("/updateUser/{nif}")
    public String update(@PathVariable("nif") String nif, Model model) {
        User user = userService.findByNif(nif);
        model.addAttribute("user", user);
        return "user-edit";
    }

    //SHOW FORM User
    @GetMapping("/user")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "sign-in";
    }

    @PostMapping("/user")
    public String submitUser(User user, Model model) {
        User existingUser = userService.findByNif(user.getNif());
        if (existingUser != null) {
            // Actualización sin modificar el nif
            existingUser.setName(user.getName());
            existingUser.setSurname(user.getSurname());
            existingUser.setPhone(user.getPhone());
            existingUser.setEmail(user.getEmail());
            existingUser.setCargo(user.getCargo());
            existingUser.setVehicles(user.getVehicles());
            userService.save(existingUser);
        } else {
            // Guardar nuevo usuario
            userService.save(user);
        }
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
    @GetMapping("/deleteUser/{nif}")
    public String delete(@PathVariable("nif") String nif) {
        userService.deleteById(nif);
        return "redirect:/users";
    }

    //SHOW  User
    @GetMapping("/users/{nif}")
    public String viewById(@PathVariable("nif") String nif, Model model) {
        User user = userService.findByNif(nif);
        List<Vehicle> vehicles = vehicleService.findByOwner(user);
        model.addAttribute("user", user);
        model.addAttribute("vehicles", vehicles);
        return "user-details";
    }

}
