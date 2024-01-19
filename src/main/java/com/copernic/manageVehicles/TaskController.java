package com.copernic.manageVehicles;

import com.copernic.manageVehicles.dao.TasksDAO;
import com.copernic.manageVehicles.domain.Task;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String findAll(Model model){
        model.addAttribute("tasks", taskService.findAll());
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
    public String create(@ModelAttribute Task task){
        if(task.getId() != null){
            //acctualizacion
            taskService.findById(task.getId()).ifPresent(t ->{
                t.setName(task.getName());
                t.setPrice(task.getPrice());
                taskService.save(t);
            });
        }else{
            //creacion
            taskService.save(task);
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
