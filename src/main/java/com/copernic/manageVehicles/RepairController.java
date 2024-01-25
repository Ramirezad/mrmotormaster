/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles;

import com.copernic.manageVehicles.dao.RepairDAO;
import com.copernic.manageVehicles.dao.TasksDAO;
import com.copernic.manageVehicles.domain.Repair;
import com.copernic.manageVehicles.domain.Task;
import com.copernic.manageVehicles.domain.Vehicle;
import com.copernic.manageVehicles.services.RepairServiceImpl;
import com.copernic.manageVehicles.services.VehicleServiceImpl;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author enricledo
 */
@Controller
public class RepairController {

    @Autowired
    private RepairServiceImpl repairService;
    @Autowired
    private VehicleServiceImpl vehicleService;

    //List of repairs
    @GetMapping("/repairs")
    public String findAll(Model model) {
        model.addAttribute("repairs", repairService.getAllRepairs());
        return "repair-list";
    }

    //Repair form
    @GetMapping("/repair-form")
    public String getEmptyForm(Model model) {
        model.addAttribute("repair", new Repair());
        return "repair-form";
    }

    @PostMapping("/repairs")
    public String saveRepair(@Valid Repair repair, BindingResult result, Model model) {
        Vehicle vehicle = vehicleService.findVehicleByNumberPlate(repair.getVehicle().getNumberPlate());
        if (vehicle != null) {
            repair.setVehicle(vehicle);
            if (repairService.existsById(repair.getRepairId())) {
                model.addAttribute("alertMessage", "La matrícula ya existe. No se pudo agregar el vehículo.");
            } else {
                repairService.saveRepair(repair);
                model.addAttribute("successMessage", "Vehículo agregado exitosamente.");
            }
        } else {
            model.addAttribute("alertMessage", "El usuario no existe. No se pudo agregar el vehículo.");
        }
        return "redirect:/repairs";
    }

    //ESTE
    @GetMapping("/repairs/view/{id}")
    public String findById(Model model, @PathVariable Long id) {
        Optional<Repair> repairOptional = repairService.findRepairById(id);

        if (repairOptional.isPresent()) {
            Repair repair = repairOptional.get();
            model.addAttribute("repair", repair);
            return "repair-view";
        } else {

            return "redirect:/repairs";
        }
    }

    //SHOW USER'S VEHICLES
    @GetMapping("/repairs/view/{numberPlate}")
    public String showForm(@PathVariable("numberPlate") Vehicle vehicle, Model model) {
        Repair repair = new Repair();
        Vehicle vehicles = vehicleService.findVehicleByNumberPlate(vehicle.getNumberPlate());
        repair.setVehicle(vehicles);
        model.addAttribute("repair", repair);
        return "repair-form";
    }

    //Update a repair
    @GetMapping("/repairs/edit/{id}")
    public String editRepair(Model model, @PathVariable Long id) {
        Optional<Repair> repairOptional = repairService.findRepairById(id);
        if (repairOptional.isPresent()) {
            Repair repair = repairOptional.get();
            model.addAttribute("repair", repair);
            return "repair-edit";
        } else {
            return "redirect:/repairs";
        }
    }

    // Delete a repair
    @GetMapping("/repairs/delete/{id}")
    public String deleteRepair(@PathVariable Long id) {
        repairService.deleteRepairById(id);
        return "redirect:/repairs";

    }
}
