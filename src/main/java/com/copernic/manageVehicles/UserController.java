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

    @GetMapping("/updateUser/{nif}")
    public String update(@PathVariable("nif") String nif, Model model, Principal principal) {
        // Obtén el nombre del usuario logueado
        String loggedInNif = principal.getName();

        // Comprueba si el usuario logueado tiene el rol 'isUser', 'isAdmin' o 'isMecanico'
        Collection<? extends GrantedAuthority> authorities = ((UserDetails) ((Authentication) principal).getPrincipal()).getAuthorities();
        boolean isUser = authorities.contains(new SimpleGrantedAuthority("ROLE_USUARIO"));
        boolean isAdmin = authorities.contains(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"));
        boolean isMecanico = authorities.contains(new SimpleGrantedAuthority("ROLE_MECANICO"));

        if ((isUser && loggedInNif.equals(nif)) || isAdmin || isMecanico) {
            // Si el usuario logueado es 'isUser' y el nif coincide, o si es 'isAdmin' o 'isMecanico', procede como antes
            Optional<User> userOptional = userService.findByNif(nif);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                model.addAttribute("user", user);

                // Agregar roles al modelo
                addRolesToModel(model, principal);

                return "user-edit";
            } else {
                System.out.println("pito");
                return "redirect:/error";
            }
        } else {
            // Si el usuario logueado no es 'isUser' o el nif no coincide, y tampoco es 'isAdmin' ni 'isMecanico', redirige a la página de error
            return "redirect:/error";
        }
    }

    @PostMapping("/updateUser/update")
    public String userUpdate(@ModelAttribute User user, Model model, Principal principal) {
        userService.save(user);
        List<Vehicle> vehicles = vehicleService.findByOwner(user);
            model.addAttribute("user", user);
            model.addAttribute("vehicles", vehicles);

            addRolesToModel(model, principal);
            userService.save(user);
            return "user-details";        
    }

    //SHOW FORM User
    @GetMapping("/signin")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "sign-in";
    }

    @PostMapping("/signin")
    public String submitUser(User user, Model model) {
        Optional<User> existingUserOptional = userService.findByNif(user.getNif());

        if (existingUserOptional.isPresent()) {
            // Usuario existente, actualiza los detalles
            user.setCargo(User.Rol.USUARIO);
        } else {
            // Nuevo usuario, guárdalo           
            user.setCargo(User.Rol.ADMINISTRADOR);

        }
        userService.save(user);

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

    @PostMapping("/users")
    public String listarUsers(@RequestParam(required = false) String query, Model model, Principal principal) {
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
    @PostMapping("/users/{nif}")
    public String viewUser(@PathVariable("nif") String nif, Model model, Principal principal) {
        Optional<User> userOptional = userService.findByNif(nif);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Vehicle> vehicles = vehicleService.findByOwner(user);
            model.addAttribute("user", user);
            model.addAttribute("vehicles", vehicles);

            addRolesToModel(model, principal);
            userService.save(user);
            return "user-details";
        } else {
            // Manejar el caso en que el usuario no existe
            // Puedes redirigir a una página de error o hacer algo apropiado
            return "redirect:/error"; // Cambia a la página de error que desees
        }
    }

    //SHOW User
    @GetMapping("/users/{nif}")
    public String viewById(@PathVariable("nif") String nif, Model model, Principal principal) {
        // Obtén el nombre del usuario logueado
        String loggedInNif = principal.getName();

        // Comprueba si el usuario logueado tiene el rol 'isUser', 'isAdmin' o 'isMecanico'
        Collection<? extends GrantedAuthority> authorities = ((UserDetails) ((Authentication) principal).getPrincipal()).getAuthorities();
        boolean isUser = authorities.contains(new SimpleGrantedAuthority("ROLE_USUARIO"));
        boolean isAdmin = authorities.contains(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"));
        boolean isMecanico = authorities.contains(new SimpleGrantedAuthority("ROLE_MECANICO"));

        if ((isUser && loggedInNif.equals(nif)) || isAdmin || isMecanico) {
            // Si el usuario logueado es 'isUser' y el nif coincide, o si es 'isAdmin' o 'isMecanico', procede como antes
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

    /* NO SE USA
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
     */
}
