/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles.dao;

import com.copernic.manageVehicles.domain.User;
import com.copernic.manageVehicles.domain.Vehicle;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleDAO extends JpaRepository<Vehicle, String> {
    List<Vehicle> findByOwner(User Owner);
    Vehicle findVehicleByNumberPlate(String numberPlate);
}

