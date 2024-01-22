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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author rfernandez
 */
@Controller
public class VehicleController {

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
    
    // SUMBIT FORM VEHICLE
    @PostMapping("/vehicle")

    public String submitForm(Vehicle vehicle, Model model) {
        User user = userService.findByNif(vehicle.getOwner().getNif());
        vehicle.setOwner(user);
        vehicleService.saveVehicle(vehicle);
        return "redirect:/vehicles";    
    }

    public String submitForm(@Valid Vehicle vehicle, BindingResult result, Model model) {
        // Antes de guardar, verifica si el vehículo ya existe
         if (vehicleService.existsById(vehicle.getNumberPlate())) {
            model.addAttribute("alertMessage", "La matrícula ya existe. No se pudo agregar el vehículo.");
        } else {
            // Manejar la lógica de vehículo existente
            // Agregar mensaje de alerta flash
            vehicleService.saveVehicle(vehicle);
            model.addAttribute("alertMessage", "La matrícula ya existe. No se pudo agregar el vehículo.");
        }
        return "redirect:/vehicles?successMessage=Vehículo agregado exitosamente.\"" ;

    }

    
    
    //SHOW USER'S VEHICLES
    @GetMapping("/vehicle/{nif}")
    public String showForm(@PathVariable("nif") String nif, Model model) {
        Vehicle vehicle = new Vehicle();
        User user = userService.findByNif(nif);
        vehicle.setOwner(user);
        model.addAttribute("vehicle", vehicle);
        return "vehicle-form";
    }
    


    

    //LIST VEHICLES
    @GetMapping("/vehicles")
    public String listVehicle(Model model) {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        model.addAttribute("vehicles", vehicles);
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