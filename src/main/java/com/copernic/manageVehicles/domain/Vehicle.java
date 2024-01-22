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
import org.antlr.v4.runtime.misc.NotNull;


@Entity
@Table(name = "vehicle")
@Data
public class Vehicle {
    @Id
    @NotNull
    private String numberPlate;
    @NotNull
    private String brand;
    @NotNull
    private String model;
    @NotNull
    private String color;
    @NotNull
    private int fabricationYear;
    @NotNull
    private int km;
    @ManyToOne
    private User owner;
}
