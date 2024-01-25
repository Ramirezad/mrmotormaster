package com.copernic.manageVehicles;

import com.copernic.manageVehicles.dao.TasksDAO;
import com.copernic.manageVehicles.domain.Task;
import java.security.Principal;
import java.util.Collection;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TaskController {

    @Autowired
    private TasksDAO taskService;
   
    
    @GetMapping("/tasks")
public String findAll(Model model, Principal principal){
    Collection<? extends GrantedAuthority> authorities = ((UserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getAuthorities();
    boolean isAdmin = authorities.contains(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"));
    model.addAttribute("tasks", taskService.findAll());
    model.addAttribute("isAdmin", isAdmin);
    return "task-list";
}


    @GetMapping("/tasks/view/{id}")
    public String findById(Model model, @PathVariable Long id){
    Optional<Task> taskOptional = taskService.findById(id);
    
        if (taskOptional.isPresent()) {
             Task task = taskOptional.get();
             model.addAttribute("task", task);
             return "task-view";
    } else {
      
        return "redirect:/tasks";
    }
}


    @GetMapping("/task-form")
    public String getEmptyForm(Model model){
         model.addAttribute("task", new Task());
         return "task-form";
}

    
    @GetMapping("/tasks/edit/{id}")
        public String getFormWithTask(Model model, @PathVariable Long id) {
        Optional<Task> taskOptional = taskService.findById(id);

         if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            model.addAttribute("task", task);
            return "task-form";
        } else {
            return "redirect:/task/form";
    }
}


    @PostMapping("/tasks")
public String create(@ModelAttribute Task task, Model model, Authentication authentication){
    if(task.getId() != null){
        //actualizacion
        taskService.findById(task.getId()).ifPresent(t ->{
            t.setName(task.getName());
            t.setPrice(task.getPrice());
            taskService.save(t);
        });
    }else{
        //creacion
        taskService.save(task);
    }

    if (authentication != null) {
        if (authentication.getPrincipal() instanceof com.copernic.manageVehicles.security.SecurityUser) {
            com.copernic.manageVehicles.security.SecurityUser securityUser = (com.copernic.manageVehicles.security.SecurityUser) authentication.getPrincipal();
            model.addAttribute("userRole", securityUser.getUser().getCargo().name());
        }
    }

    return "redirect:/tasks";
}


    @GetMapping("/tasks/delete/{id}")
    public String deleteById(@PathVariable Long id){
        taskService.findById(id).ifPresent(t ->{
            taskService.deleteById(t.getId());
        } );

        return "redirect:/tasks";
    }



}
