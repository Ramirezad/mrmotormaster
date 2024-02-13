/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles.domain;

/**
 *
 * @author rfernandez
 */

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

import org.antlr.v4.runtime.misc.NotNull;



@Entity
@Table(name = "vehicle")
@Data
public class Vehicle {
   
    @Id 
    @Size(max = 20, message = "This field can't have more than 20 characters")
    @NotBlank(message = "Number plate can't be empty!")
    private String numberPlate;
    @Size(max = 30, message = "This field can't have more than 30 characters")
    @NotBlank(message = "Brand can't be empty!")
    private String brand;
    @Size(max = 30, message = "This field can't have more than 30 characters")
    @NotBlank(message = "Model can't be empty!")
    private String model;
    @Size(max = 30, message = "This field can't have more than 30 characters")
    @NotBlank(message = "Color can't be empty!")
    private String color;
    
    @NotNull
    @Min(value = 1900, message = "Fabrication year can't be previous to 1900!") 
    @Max(value = 2024, message =" We are not there yet!")
    private int fabricationYear;
    
    @NotNull
    @Min(value = 0, message = "Kilometers can't be negative!")  
    @Max(value=10000000, message="That's impossible, too many kilometers, max 10 million")
    private int km;
    
    //Relaciones amorosas
    @ManyToOne
    @JoinColumn(name = "nif", referencedColumnName = "nif")
    private User owner;
 
    @OneToMany(fetch = FetchType.EAGER ,mappedBy = "vehicle",cascade = CascadeType.REMOVE)
    private List<Repair> repairs;
    
    @Override
    public String toString() {
        return "Vehicle {" +
            "numberPlate='" + numberPlate + '\'' +
            ", brand='" + brand + '\'' +
            ", model='" + model + '\'' +
            ", color='" + color + '\'' +
            ", fabricationYear=" + fabricationYear +
            ", km=" + km +
            ", owner=" + (owner != null ? owner.getNif() : "null") +
            ", repairs=" + (repairs != null ? repairs.size() : "0") +
            '}';
    }   
}
