/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles.services;

import com.copernic.manageVehicles.dao.RepairDAO;
import com.copernic.manageVehicles.domain.Repair;
import com.copernic.manageVehicles.domain.Task;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author enricledo
 */
@Service
public class RepairServiceImpl implements RepairService {

    @Autowired
    private RepairDAO repairDAO;

    @Override
    @Transactional
    public Repair saveRepair(Repair repair) {
        return repairDAO.save(repair);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Repair> getAllRepairs() {
        return repairDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Repair findById(Long id) {
        return repairDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repairDAO.deleteById(id);
    }

    @Override
    @Transactional
    public double getTotalPrice(Long id) {
        Repair repair = findById(id);
        if (repair!=null) {
            List<Task> tasks = repair.getTasks();
            double totalPrice = tasks.stream()
                    .mapToDouble(Task::getPrice)
                    .sum();
            return totalPrice;
        } else {
            throw new RuntimeException("Repair with ID " + id + " not found.");
        }
    }
}
