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
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "vehicle")
@Data
public class Vehicle {
    @Id
    
    private String numberPlate;
    private String brand;
    private String model;
    private String color;
    private int fabricationYear;
    private int km;
    @ManyToOne
    private User owner;
}
