/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles.services;

import java.util.List;

/**
 *
 * @author rfernandez
 */

public interface ServiceInterface<T> {
    T save(T entidad);
    List<T> getAll();
    T find(T entidad);
    void deleteById(String entidad);
    
    
}
