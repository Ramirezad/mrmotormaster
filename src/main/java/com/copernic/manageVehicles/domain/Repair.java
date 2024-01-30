/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
import java.util.List;

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
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "numberPlate" )
    private Vehicle vehicle;
    
    //With lazy it does not work
    //Thats an inconvenient because if we try to get a repair with many tasks
    //we will be charging all that information to memory.
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "repair_task",
        joinColumns = @JoinColumn(name = "repair_id"),
        inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    public List<Task> tasks;
}
