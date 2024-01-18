/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Set;
import lombok.Data;

/**
 *
 * @author enricledo
 */
@Data
@Entity
@Table(name = "repair")
public class Repair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repairId;

    private String observation;
    private int km;
    private LocalDate repairDate;
    
    @ManyToOne
    @JoinColumn(name = "numberPlate", referencedColumnName = "numberPlate")
    private Vehicle vehicle;
    
//    @OneToMany (mappedBy = "Repair")
//    private Set<Order> orders = new HashSet<>();

}
