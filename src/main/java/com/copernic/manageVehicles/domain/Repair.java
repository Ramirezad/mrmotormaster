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
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.HashSet;
import java.util.List;
import org.springframework.validation.annotation.Validated;

/**
 *
 * @author enricledo
 */
@Validated
@Data
@Entity
@Table(name = "repair")
public class Repair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long repairId;

    @Size(max = 255, message = "This field can't have more than 255 characters")
    @NotBlank(message = "Observation can't be empty!")
    private String observation;
    
    @Min(value = 0, message = "Cannot be less that zero!") 
    @Max(value = 3000000, message ="There is no way you did 3M km on that!")
    private int km;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate repairDate;
    
    @ManyToOne
    @JoinColumn(name = "numberPlate" , referencedColumnName = "numberPlate")
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
    
    @Override
    public String toString() {
        return "Repair{" +
                "repairId=" + repairId +
                ", observation='" + observation + '\'' +
                ", km=" + km +
                ", repairDate=" + repairDate +
                ", vehicle=" + (vehicle != null ? vehicle.getNumberPlate() : null) +
                // 'tasks' field is not included to avoid potential circular dependency
                '}';
    }

}
