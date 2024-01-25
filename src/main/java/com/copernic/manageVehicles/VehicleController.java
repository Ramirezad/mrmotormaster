    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles;

import com.copernic.manageVehicles.dao.RepairDAO;
import com.copernic.manageVehicles.domain.Repair;
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
import java.util.Optional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    @Autowired
    private RepairDAO repairService;

    // Método para mostrar el formulario de actualización
    @GetMapping("/updateVehicle/{numberPlate}")
    public String updateForm(@PathVariable("numberPlate") String numberPlate, Model model) {
        Vehicle vehicle = vehicleService.findByNumberPlate(numberPlate);
        model.addAttribute("vehicle", vehicle);
        return "vehicle-update";
    }

    // Método para procesar el formulario de actualización
    @PostMapping("/updateVehicle")
    public String updateVehicle(@ModelAttribute("vehicle") Vehicle vehicle, Model model) {
        vehicleService.updateVehicle(vehicle);
        return "redirect:/vehicles"; // Redirige a la lista de vehículos después de la actualización
    }
    //SHOW FORM VEHICLE
    @GetMapping("/vehicle")
    public String showForm(Vehicle vehicle) {
        return "vehicle-form";
    }

    //ADD VEHICLE
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
        // Manejar el caso en que el usuario no existe
        // Puedes redirigir a una página de error o hacer algo apropiado
        return "redirect:/error"; // Cambia a la página de error que desees
    }
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
        List<Repair> repair = repairService.findByVehicle(vehicle);
        User user = userService.findByNif(vehicle.getOwner().getNif()).orElse(new User());
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("user", user);
        model.addAttribute("repair", repair);
        return "vehicle-details";

    }
}
