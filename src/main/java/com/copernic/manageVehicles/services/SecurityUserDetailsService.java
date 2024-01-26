/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles.services;

import com.copernic.manageVehicles.dao.UserDAO;
import com.copernic.manageVehicles.security.SecurityUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author vigob
 */

@Service
public class SecurityUserDetailsService implements UserDetailsService {

    private final UserDAO userDAO;

    public SecurityUserDetailsService(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String nif) throws UsernameNotFoundException {
        var optUser = this.userDAO.findByNif(nif);
        if(optUser.isPresent()){
            return new SecurityUser(optUser.get());
        }

        throw new UsernameNotFoundException("Usuario no encontrado: " + nif);
    }
}




