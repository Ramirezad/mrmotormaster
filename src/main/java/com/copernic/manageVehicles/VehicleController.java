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
import com.copernic.manageVehicles.services.VehicleServiceImpl;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.Collection;
import java.util.Optional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author rfernandez
 */
@Controller
public class VehicleController {
    
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
    private VehicleServiceImpl vehicleService;
    @Autowired
    private UserServiceImpl userService;

    //UPDATE VEHICLE
    @GetMapping("/updateVehicle/{numberPlate}")
    public String update(Vehicle vehicle, Model model) {
        vehicle = vehicleService.findVehicle(vehicle);
        model.addAttribute("vehicle", vehicle);
        return "vehicle-form";
    }

    //SHOW FORM VEHICLE
    @GetMapping("/vehicle")
    public String showForm(Vehicle vehicle) {
        return "vehicle-form";
    }

    @PostMapping("/vehicle")
    public String submitForm(@Valid Vehicle vehicle, BindingResult result, Model model) {
    Optional<User> userOptional = userService.findByNif(vehicle.getOwner().getNif());

    if (userOptional.isPresent()) {
        User user = userOptional.get();
        vehicle.setOwner(user);

        if (vehicleService.existsById(vehicle.getNumberPlate())) {
            model.addAttribute("alertMessage", "La matrícula ya existe. No se pudo agregar el vehículo.");
        } else {
            vehicleService.saveVehicle(vehicle);
            model.addAttribute("successMessage", "Vehículo agregado exitosamente.");
        }
    } else {
        model.addAttribute("alertMessage", "El usuario no existe. No se pudo agregar el vehículo.");
    }

    return "redirect:/vehicles";    
}


    @GetMapping("/vehicle/{nif}")
public String showForm(@PathVariable("nif") String nif, Model model) {
    Optional<User> userOptional = userService.findByNif(nif);

    if (userOptional.isPresent()) {
        User user = userOptional.get();
        Vehicle vehicle = new Vehicle();
        vehicle.setOwner(user);
        model.addAttribute("vehicle", vehicle);
        return "vehicle-form";
    } else {
        // Manejar el caso en que el usuario no existe
        // Puedes redirigir a una página de error o hacer algo apropiado
        return "redirect:/error"; // Cambia a la página de error que desees
    }
}


    //LIST VEHICLES
   @GetMapping("/vehicles")
    public String listVehicle(Model model, Principal principal) {
    List<Vehicle> vehicles = vehicleService.getAllVehicles();
    model.addAttribute("vehicles", vehicles);

    // Obtener el nif del usuario actualmente autenticado
    String nif = principal.getName();
    model.addAttribute("nif", nif);

    // Añadir roles al modelo
    addRolesToModel(model, principal);

    return "vehicle-list";
}



    //DELETE VEHICLE
    @GetMapping("/deleteVehicle/{numberPlate}")
    public String delete(@PathVariable("numberPlate") String numberPlate) {
        vehicleService.deleteVehicleById(numberPlate);
        return "redirect:/vehicles";
    }

    //VIEW VEHICLE
    @GetMapping("/viewVehicle/{numberPlate}")
    public String viewVehicle(@PathVariable("numberPlate") String numberPlate, Model model) {
        Vehicle vehicle = new Vehicle();
        vehicle.setNumberPlate(numberPlate);
        vehicle = vehicleService.findVehicle(vehicle);
        model.addAttribute("vehicle", vehicle);
        return "vehicle-details";

    }
}
