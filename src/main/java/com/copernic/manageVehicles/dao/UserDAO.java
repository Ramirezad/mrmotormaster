/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles.dao;

import com.copernic.manageVehicles.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
public interface UserDAO extends JpaRepository<User, Long> {
    Optional<User> findByNif(String nif);
    void deleteByNif(String nif);
    List<User> findByNifContainingOrNameContainingOrSurnameContaining(String nif, String name, String surname);
}

