/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles;

import com.copernic.manageVehicles.dao.RepairDAO;
import com.copernic.manageVehicles.dao.TasksDAO;
import com.copernic.manageVehicles.domain.Repair;
import com.copernic.manageVehicles.domain.Task;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author enricledo
 */
@Controller
public class RepairController {
    
    @Autowired
    private RepairDAO repairService;
    
    @GetMapping("/repairs")
    public String findAll(Model model){
        model.addAttribute("repairs", repairService.findAll());
        return "repair-list";
    }
    @GetMapping("/repair-form")
    public String getEmptyForm(Model model){
        model.addAttribute("repair", new Repair());
        return "repair-form";
    }
    @PostMapping("/repairs")
    public String saveRepair(@ModelAttribute("repair") Repair repair) {
        repairService.save(repair);  
        return "redirect:/repairs";  
    }
    
    //ESTE
    @GetMapping("/repairs/view/{id}")
    public String findById(Model model, @PathVariable Long id){
        Optional<Repair> repairOptional = repairService.findById(id);

            if (repairOptional.isPresent()) {
                 Repair repair = repairOptional.get();
                 model.addAttribute("repair", repair);
                 return "repair-view";
        } else {

            return "redirect:/repairs";
        }
    }
}