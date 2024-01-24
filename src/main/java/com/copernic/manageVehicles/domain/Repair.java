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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.HashSet;
import java.util.Set;

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
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate repairDate;
    
    @ManyToOne
    @JoinColumn(name = "numberPlate", referencedColumnName = "numberPlate")
    private Vehicle vehicle;
    
    @ManyToMany
    @JoinTable(
        name = "repair_task",
        joinColumns = @JoinColumn(name = "repair_id"),
        inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private Set<Task> tasks = new HashSet<>();
}
