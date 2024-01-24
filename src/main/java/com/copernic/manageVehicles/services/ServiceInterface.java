/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles.services;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author rfernandez
 */

public interface ServiceInterface<T> {
    T save(T entidad);
    List<T> getAll();
    Optional<T> find(T entity);
    void deleteById(String entidad);
    
    
}
