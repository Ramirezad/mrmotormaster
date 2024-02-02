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
/**
 *
 * @author rfernandez
 */
@Controller
public class UserController {
    
    private void addRolesToModel(Model model, Principal principal) {
        Collection<? extends GrantedAuthority> authorities = ((UserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getAuthorities();
        boolean isAdmin = authorities.contains(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"));
        boolean isUser = authorities.contains(new SimpleGrantedAuthority("ROLE_USUARIO"));
        boolean isMecanico = authorities.contains(new SimpleGrantedAuthority("ROLE_MECANICO"));

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isUser", isUser);
        model.addAttribute("isMecanico", isMecanico);
    }

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private VehicleServiceImpl vehicleService;

    //UPDATE User
    @GetMapping("/updateUser/{nif}")
    public String update(@PathVariable("nif") String nif, Model model) {
        Optional<User> userOptional = userService.findByNif(nif);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);
            return "user-edit";
        } else {
            return "redirect:/error";
        }
    }

    //SHOW FORM User
    @GetMapping("/user")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "sign-in";
    }
    
    
    
    @PostMapping("/user")
    public String submitUser(User user, Model model) {
        Optional<User> existingUserOptional = userService.findByNif(user.getNif());

        if (existingUserOptional.isPresent()) {
            // Usuario existente, actualiza los detalles
            User existingUser = existingUserOptional.get();
            existingUser.setName(user.getName());
            existingUser.setSurname(user.getSurname());
            existingUser.setPhone(user.getPhone());
            existingUser.setEmail(user.getEmail());
            existingUser.setCargo(user.getCargo());
            existingUser.setVehicles(user.getVehicles());
            userService.save(existingUser);
        } else {
            // Nuevo usuario, guárdalo
            user.setCargo(User.Rol.USUARIO);
            userService.save(user);
        }

        return "redirect:/users";
    }

    //LIST Users
    @GetMapping("/users")
    public String listUsers(@RequestParam(required = false) String query, Model model, Principal principal) {
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



    //DELETE User solo para administradores
    @GetMapping("/deleteUser/{nif}")
    public String delete(@PathVariable("nif") String nif) {
        userService.deleteById(nif);
        return "redirect:/users";
    }

    //SHOW User
    @GetMapping("/users/{nif}")
    public String viewById(@PathVariable("nif") String nif, Model model, Principal principal) {
    Optional<User> userOptional = userService.findByNif(nif);

    if (userOptional.isPresent()) {
        User user = userOptional.get();
        List<Vehicle> vehicles = vehicleService.findByOwner(user);
        model.addAttribute("user", user);
        model.addAttribute("vehicles", vehicles);

        addRolesToModel(model, principal);

        return "user-details";
    } else {
        // Manejar el caso en que el usuario no existe
        // Puedes redirigir a una página de error o hacer algo apropiado
        return "redirect:/error"; // Cambia a la página de error que desees
    }
}

    


   @GetMapping("/session")
public ResponseEntity<?> getDetailsSession(Authentication authentication) {
    String sessionId = "";
    Object sessionUser = null;

    if (authentication != null) {
        if (authentication.getDetails() instanceof WebAuthenticationDetails) {
            WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
            sessionId = details.getSessionId();
        }
        if (authentication.getPrincipal() instanceof com.copernic.manageVehicles.security.SecurityUser) {
            com.copernic.manageVehicles.security.SecurityUser securityUser = (com.copernic.manageVehicles.security.SecurityUser) authentication.getPrincipal();
            sessionUser = securityUser;
        }
    }

    @GetMapping("/session")
    public ResponseEntity<?> getDetailsSession(Authentication authentication) {
        String sessionId = "";
        Object sessionUser = null;

        if (authentication != null) {
            if (authentication.getDetails() instanceof WebAuthenticationDetails) {
                WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
                sessionId = details.getSessionId();
            }
            if (authentication.getPrincipal() instanceof com.copernic.manageVehicles.security.SecurityUser) {
                com.copernic.manageVehicles.security.SecurityUser securityUser = (com.copernic.manageVehicles.security.SecurityUser) authentication.getPrincipal();
                sessionUser = securityUser;
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("response", "Hello World");
        response.put("sessionId", sessionId);
        response.put("sessionUser", sessionUser);

        System.out.println(authentication);
        return ResponseEntity.ok(response);
    }

}
