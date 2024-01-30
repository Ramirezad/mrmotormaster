/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles.dao;

import com.copernic.manageVehicles.domain.Repair;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.copernic.manageVehicles.domain.Vehicle;

@Repository
public interface RepairDAO extends JpaRepository<Repair, Long> {
        List<Repair> findByVehicle(Vehicle vehicle);
        
}
