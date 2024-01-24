/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles;

import com.copernic.manageVehicles.dao.RepairDAO;
import com.copernic.manageVehicles.dao.TasksDAO;
import com.copernic.manageVehicles.domain.Repair;
import com.copernic.manageVehicles.domain.Task;
import com.copernic.manageVehicles.services.RepairService;
import com.copernic.manageVehicles.services.TaskService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

/**
 *
 * @author enricledo
 */
@Controller
public class RepairController {
    
    @Autowired
    private RepairService repairService;
    
    @Autowired
    private TaskService taskService;
    
    //List of repairs
    @GetMapping("/repairs")
    public String findAll(Model model){
        model.addAttribute("repairs", repairService.getAllRepairs());
        return "repair-list";
    }

    //Create repair
    @GetMapping("/repair-form")
    public String getEmptyForm(Model model){
        model.addAttribute("repair", new Repair());
        List<Task> tasks = taskService.getAllTasks();
        System.out.println(tasks.size());
        model.addAttribute("tasks", tasks);
        return "repair-form";
    }
    
    //Save a repair
    @PostMapping("/repairs")
    public String saveRepair(@ModelAttribute("repair") Repair repair) {
        repairService.saveRepair(repair);  
        return "redirect:/repairs";  
    }
    
    //Visualize individual repair
    @GetMapping("/repairs/view/{id}")
    public String findById(Model model, @PathVariable Long id){
        Optional<Repair> repairOptional = repairService.findRepairById(id);
        if (repairOptional.isPresent()) {
            Repair repair = repairOptional.get();
            model.addAttribute("repair", repair);
            model.addAttribute("total", repairService.getTotalPrice(id));
            
            return "repair-view";
        } else {

            return "redirect:/repairs";
        }
    }

   
    //Update a repair
    @GetMapping("/repairs/edit/{id}")
    public String editRepair(Model model, @PathVariable Long id){
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