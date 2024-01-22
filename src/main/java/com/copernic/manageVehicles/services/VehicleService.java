/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles.services;

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
    boolean existsById(String numberPlate);
    
    
}
