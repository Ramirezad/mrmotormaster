/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles.domain;

/**
 *
 * @author MC Ren
 */
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;


@Entity
@Table(name = "tasks")
public class Tasks {
    @Id    
    private String name;
    private int price;
    private int taskId;
    
//    @OneToMany (mappedBy = "task")
//    private Set<Order> orders = new HashSet<>();
}
