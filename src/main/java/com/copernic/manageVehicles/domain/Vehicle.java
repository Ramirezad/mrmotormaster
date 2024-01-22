/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles.domain;

/**
 *
 * @author rfernandez
 */

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;



@Entity
@Table(name = "vehicle")
@Data
public class Vehicle {
    @Id
    @NotBlank(message = "Number plate can't be empty!")
    private String numberPlate;
    
    @NotBlank(message = "Brand can't be empty!")
    private String brand;
    
    @NotBlank(message = "Model can't be empty!")
    private String model;
   
    @NotBlank(message = "Color can't be empty!")
    private String color;
    
    @NotNull
    @Min(value = 1900, message = "Fabrication year can't be previous to 1900!") 
    @Max(value = 2024, message =" We are not there yet!")
    private int fabricationYear;
    
    @NotNull
    @Min(value = 0, message = "Kilometers can't be negative!")   
    private int km;
    
    //Relaciones amorosas
    @ManyToOne
    private User owner;
    
    @OneToMany(mappedBy = "vehicle")
    private List<Repair> reparaciones;
}
