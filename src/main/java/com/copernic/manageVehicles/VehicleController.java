/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles;
import com.copernic.manageVehicles.dao.RepairDAO;
import com.copernic.manageVehicles.domain.Repair;
import com.copernic.manageVehicles.domain.User;
import com.copernic.manageVehicles.domain.Vehicle;
import com.copernic.manageVehicles.services.RepairServiceImpl;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import com.copernic.manageVehicles.dao.RepairDAO;
import com.copernic.manageVehicles.domain.Repair;
import com.copernic.manageVehicles.domain.User;
import com.copernic.manageVehicles.domain.Vehicle;
import com.copernic.manageVehicles.services.UserServiceImpl;
import com.copernic.manageVehicles.services.VehicleServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.RequestParam;

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
    @Autowired
    private RepairServiceImpl repairService;
    
    //UPDATE VEHICLE
    @GetMapping("/updateVehicle/{numberPlate}")
    public String showUpdateForm(@PathVariable String numberPlate, Model model) {
        // Earn Vehicle to add Model
        Vehicle vehicle = vehicleService.findByNumberPlate(numberPlate);
        model.addAttribute("vehicle", vehicle);
        return "vehicle-update";
    }

    @PostMapping("/updateVehicle")
    public String updateVehicle(@ModelAttribute("vehicle") @Valid Vehicle updatedVehicle, BindingResult result) {
        if (result.hasErrors()) {
            // If there are validation errors, return to the update form with the errors
            return "vehicle-update";
        }

        // Check if the vehicle with the given number plate exists
        Vehicle existingVehicle = vehicleService.findByNumberPlate(updatedVehicle.getNumberPlate());
        if (existingVehicle == null) {
            // Handle the case where the vehicle doesn't exist ( redirect to an error page)
            return "redirect:/error";
        }

        // Update the existing vehicle with the new information
        existingVehicle.setBrand(updatedVehicle.getBrand());
        existingVehicle.setModel(updatedVehicle.getModel());
        existingVehicle.setColor(updatedVehicle.getColor());
        existingVehicle.setFabricationYear(updatedVehicle.getFabricationYear());
        existingVehicle.setKm(updatedVehicle.getKm());

        // Update the repairs if needed
        existingVehicle.setRepairs(updatedVehicle.getRepairs());

        // Call the service to update the information of the vehicle
        vehicleService.updateVehicle(existingVehicle);

        // Redirect to the page showing the list of vehicles after the update
        return "redirect:/vehicles";
    }

    // SHOW FORM VEHICLE
    @GetMapping("/vehicle")
    public String showForm(Vehicle vehicle) {
        return "vehicle-form";
    }

    // ADD VEHICLE
    @PostMapping("/vehicle")
    public String submitForm(@Valid Vehicle vehicle, BindingResult result, Model model) {
        Optional<User> userOptional = userService.findByNif(vehicle.getOwner().getNif());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            vehicle.setOwner(user);
            vehicle.getRepairs();

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
        return "redirect:/error"; // Cambia a la página de error que desees
    }
    }

//List Vehicles
   @GetMapping("/vehicles")
public String listVehicle(@RequestParam(required = false) String query, Model model, Principal principal) {
    List<Vehicle> vehicles;
    if (query != null && !query.isEmpty()) {
        vehicles = vehicleService.searchVehicles(query);
    } else {
        vehicles = vehicleService.getAllVehicles();
    }
    model.addAttribute("vehicles", vehicles);

    // Obtener el nif del usuario actualmente autenticado
    String nif = principal.getName();
    model.addAttribute("nif", nif);

    // Añadir roles al modelo
    addRolesToModel(model, principal);

    return "vehicle-list";
}
    // DELETE VEHICLE
    @GetMapping("/deleteVehicle/{numberPlate}")
    public String delete(@PathVariable("numberPlate") String numberPlate) {
        vehicleService.deleteVehicleById(numberPlate);
        return "redirect:/vehicles";
    }
    
    
    
    
    @GetMapping("/viewVehicle/{numberPlate}")
    public String viewVehicle(@PathVariable("numberPlate") String numberPlate, Model model, Principal principal) {
    Vehicle vehicle = vehicleService.findByNumberPlate(numberPlate);
    List<Repair> repairs = vehicleService.findRepairsByNumberPlate(numberPlate);
    User user = userService.findByNif(vehicle.getOwner().getNif()).orElse(new User());

    model.addAttribute("vehicle", vehicle); // Add Vehicle info to the model
    model.addAttribute("user", user); // Add User info to the model
    model.addAttribute("repairs", repairs); // Add Repairs info to the model

    // Obtener el nif del usuario actualmente autenticado
    String nif = principal.getName();
    model.addAttribute("nif", nif);

    // Añadir roles al modelo
    addRolesToModel(model, principal);

    return "vehicle-details";
}

    
}
