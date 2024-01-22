package com.copernic.manageVehicles.services;

import com.copernic.manageVehicles.domain.Task;
import java.util.List;

/**
 *
 * @author rfernandez
 */

public interface TaskService {
    
    //TASKS
    Task saveTask(Task task);
    List<Task> getAllTasks();
    Task findTask(Task task);
    void deleteTaskById(Long id);
    
    
}