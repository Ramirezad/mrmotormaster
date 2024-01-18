/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles.services;

/**
 *
 * @author rfernandez
 */
import com.copernic.manageVehicles.domain.Vehicle;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.copernic.manageVehicles.dao.TasksDAO;
import com.copernic.manageVehicles.domain.Task;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TasksDAO taskDAO;

    @Override
    @Transactional
    public Task saveTask(Task task) {
        return taskDAO.save(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllTasks() {
        return taskDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Task findTask(Task task) {
        return taskDAO.findById(task.getId()).orElse(null);

    }
    @Override
    public void deleteTaskById(Long id) {
        taskDAO.deleteById(id);
    }
}