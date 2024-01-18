/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles.services;

import com.copernic.manageVehicles.dao.RepairDAO;
import com.copernic.manageVehicles.domain.Repair;
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
    public Repair findRepair(Repair repair) {
        return repairDAO.findById(repair.getRepairId()).orElse(null);
    }

    @Override
    public void deleteRepairById(Long id) {
        repairDAO.deleteById(id);
    }

    
}

