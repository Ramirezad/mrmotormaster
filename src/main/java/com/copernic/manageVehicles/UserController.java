/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles;

import com.copernic.manageVehicles.domain.User;
import com.copernic.manageVehicles.domain.Vehicle;
import com.copernic.manageVehicles.services.UserServiceImpl;
import com.copernic.manageVehicles.services.VehicleServiceImpl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.security.Principal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 *
 * @author rfernandez
 */
@Controller
public class UserController {
    // This method adds role attributes to a User based on the roles of the authenticated user.
    private void addRolesToModel(Model model, Principal principal) {
        Collection<? extends GrantedAuthority> authorities = ((UserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getAuthorities();
        // Check if the authorities contain the roles "ROLE_ADMINISTRADOR", "ROLE_USUARIO", and "ROLE_MECANICO".
        // If an authority is present, the corresponding boolean variable (isAdmin, isUser, or isMecanico) is set to true.
        boolean isAdmin = authorities.contains(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"));
        boolean isUser = authorities.contains(new SimpleGrantedAuthority("ROLE_USUARIO"));
        boolean isMecanico = authorities.contains(new SimpleGrantedAuthority("ROLE_MECANICO"));
        // Add these boolean variables as attributes to the Model object with the attribute names "isAdmin", "isUser", and "isMecanico".
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isUser", isUser);
        model.addAttribute("isMecanico", isMecanico);
    }

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private VehicleServiceImpl vehicleService;

    @GetMapping("/updateUser/{nif}")
    public String update(@PathVariable("nif") String nif, Model model, Principal principal) {
        // Obtains the name of a logged user
        String loggedInNif = principal.getName();

        // Check the role of the user
        Collection<? extends GrantedAuthority> authorities = ((UserDetails) ((Authentication) principal).getPrincipal()).getAuthorities();
        boolean isUser = authorities.contains(new SimpleGrantedAuthority("ROLE_USUARIO"));
        boolean isAdmin = authorities.contains(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"));
        boolean isMecanico = authorities.contains(new SimpleGrantedAuthority("ROLE_MECANICO"));
        
        // Check if the user exists and have any role then add him to model after update him in service method
        if ((isUser && loggedInNif.equals(nif)) || isAdmin || isMecanico) {
            
            Optional<User> userOptional = userService.findByNif(nif);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                model.addAttribute("user", user);

                // Add roles to the model
                addRolesToModel(model, principal);

                return "user-edit";
            } else {
                return "redirect:/error";
            }
        } else {
            // If the user doesn't have role redirect to an error window
            return "redirect:/error";
        }
    }
    
    @PostMapping("/updateUser/update")
    public String userUpdate(@ModelAttribute User user, Model model, Principal principal) {
        //Creates a list of vehicles to add them in the model 
        userService.save(user);
        List<Vehicle> vehicles = vehicleService.findByOwner(user);
        model.addAttribute("user", user);
        model.addAttribute("vehicles", vehicles);
        
        if (user.getCargo() == null) {
            user.setCargo(User.Rol.USUARIO); // Establece el cargo al cargo actual
        }

        addRolesToModel(model, principal);
        userService.save(user);
        return "user-details";
    }

    //SHOW FORM User
    @GetMapping("/signin")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "signin";
    }

    @PostMapping("/signin")
    public String submitUser(User user, Model model) {
        //Create an object user in optional form taking the nif 
        Optional<User> existingUserOptional = userService.findByNif(user.getNif());
        //If there is no user it will be created with the ADMINISTRADOR role but if is the second user will be creates with the USUARIO role
        if (existingUserOptional != null) {
                       
            user.setCargo(User.Rol.USUARIO);
        } else {
            user.setCargo(User.Rol.ADMINISTRADOR);
        }
        userService.save(user);

        return "redirect:/login";
    }

    //LIST Users
    @GetMapping("/users")
    public String listUsers(@RequestParam(required = false) String query, Model model, Principal principal) {
        //Makes a list to show all users 
        List<User> users;
        if (query != null && !query.isEmpty()) {
            users = userService.search(query);
        } else {
            users = userService.getAll();
        }

        addRolesToModel(model, principal);

        model.addAttribute("users", users);
        return "user-list";
    }

    @PostMapping("/users")
    public String listarUsers(@RequestParam(required = false) String query, Model model, Principal principal) {
        //Makes a list to show all users 
        List<User> users;
        if (query != null && !query.isEmpty()) {
            users = userService.search(query);
        } else {
            users = userService.getAll();
        }

        addRolesToModel(model, principal);

        model.addAttribute("users", users);
        return "user-list";
    }

    //DELETE User only for admin
    @GetMapping("/deleteUser/{nif}")
    public String delete(@PathVariable("nif") String nif) {
        userService.deleteById(nif);
        return "redirect:/users";
    }

    //SHOW User
    @PostMapping("/users/{nif}")
    public String viewUser(@PathVariable("nif") String nif, Model model, Principal principal) {
        Optional<User> userOptional = userService.findByNif(nif);
        //Get user details and their vehicles to show them in a window
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Vehicle> vehicles = vehicleService.findByOwner(user);
            model.addAttribute("user", user);
            model.addAttribute("vehicles", vehicles);

            addRolesToModel(model, principal);
            userService.save(user);
            return "user-details";
        } else {
            
            return "redirect:/error"; // change de window to an error 
        }
    }

    //SHOW User
    @GetMapping("/users/{nif}")
    public String viewById(@PathVariable("nif") String nif, Model model, Principal principal) {
        // Obtains the logged user's name
        String loggedInNif = principal.getName();

        // Check the role 'isUser', 'isAdmin' o 'isMecanico'
        Collection<? extends GrantedAuthority> authorities = ((UserDetails) ((Authentication) principal).getPrincipal()).getAuthorities();
        boolean isUser = authorities.contains(new SimpleGrantedAuthority("ROLE_USUARIO"));
        boolean isAdmin = authorities.contains(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"));
        boolean isMecanico = authorities.contains(new SimpleGrantedAuthority("ROLE_MECANICO"));

        if ((isUser && loggedInNif.equals(nif)) || isAdmin || isMecanico) {
            // If the user is logged 'isUser' and the nif is correct, or if its 'isAdmin' o 'isMecanico', show the details of its
            Optional<User> userOptional = userService.findByNif(nif);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                List<Vehicle> vehicles = vehicleService.findByOwner(user);
                model.addAttribute("user", user);
                model.addAttribute("vehicles", vehicles);

                addRolesToModel(model, principal);

                return "user-details";
            } else {
                return "redirect:/error";
            }
        } else {
            // Si el usuario logueado no es 'isUser' o el nif no coincide, y tampoco es 'isAdmin' ni 'isMecanico', redirige a la página de error
            return "redirect:/error";
        }
    }

    @GetMapping("/session")
    public ResponseEntity<?> getDetailsSession(Authentication authentication) {
        // Initialize session ID and user as empty/null
        String sessionId = "";
        Object sessionUser = null;
        // Check if authentication is not null
        if (authentication != null) {
            // Check if authentication details are of type WebAuthenticationDetails
            if (authentication.getDetails() instanceof WebAuthenticationDetails) {
                WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
                // Get the session ID from the details
                sessionId = details.getSessionId();
            }
            // Check if the authenticated principal is of type SecurityUser
            if (authentication.getPrincipal() instanceof com.copernic.manageVehicles.security.SecurityUser) {
                com.copernic.manageVehicles.security.SecurityUser securityUser = (com.copernic.manageVehicles.security.SecurityUser) authentication.getPrincipal();
                // Get the user details from the principal
                sessionUser = securityUser;
            }
        }
        //Completes the response map
        Map<String, Object> response = new HashMap<>();
        response.put("response", "Hello World");
        response.put("sessionId", sessionId);
        response.put("sessionUser", sessionUser);

        System.out.println(authentication);
        return ResponseEntity.ok(response);
    }

}
