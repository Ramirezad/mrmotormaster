/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

    @GetMapping("/users")
    public String showUsersList() {
        return "user-list"; 
    }

    @GetMapping("/vehicles")
    public String showVehiclesList() {
        return "vehicle-list";
    }

    @GetMapping("/repairs")
    public String showRepairsList() {
        return "repair-list"; 
    }

    @GetMapping("/tasks")
    public String showTasksList() {
        return "task-list"; 
    }
}