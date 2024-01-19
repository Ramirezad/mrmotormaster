/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles.services;

import com.copernic.manageVehicles.domain.Repair;
import java.util.List;

/**
 *
 * @author enricledo
 */
public interface RepairService {
    Repair saveRepair(Repair repair);
    List<Repair> getAllRepairs();
    Repair findRepair(Repair repair);
    void deleteRepairById(Long id);
}
