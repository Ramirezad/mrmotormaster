/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles.services;

import com.copernic.manageVehicles.domain.Repair;
import com.copernic.manageVehicles.domain.User;
import com.copernic.manageVehicles.domain.Vehicle;
import java.util.List;

/**
 *
 * @author rfernandez
 */

public interface VehicleService {
    Vehicle saveVehicle(Vehicle vehicle);
    List<Vehicle> getAllVehicles();
    Vehicle findVehicle(Vehicle vehicle);
    void deleteVehicleById(String numberPlate);    
    List<Vehicle> findByOwner(User owner);
    boolean existsById(String numberPlate);
    Vehicle findByNumberPlate(String numberPlate);
    void updateVehicle(Vehicle vehicle);
    List<Repair> findRepairsByNumberPlate(String numberPlate);
    List<Vehicle> searchVehicles(String query);
}


    
