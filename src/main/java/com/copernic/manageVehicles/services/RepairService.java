/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles.services;

import com.copernic.manageVehicles.domain.Repair;
import com.copernic.manageVehicles.domain.Task;
import com.copernic.manageVehicles.domain.Vehicle;
import java.util.List;
import java.util.Optional;


/**
 *
 * @author enricledo
 */
public interface RepairService {
    Repair saveRepair(Repair repair);
    List<Repair> getAllRepairs();
    Optional<Repair> findRepairById(Long id);
    void deleteRepairById(Long id);
    double getTotalPrice(Long id);
    List <Repair> findByVehicle(Vehicle vehicle);
}
