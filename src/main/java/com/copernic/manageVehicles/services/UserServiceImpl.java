/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles.services;

/**
 *
 * @author rfernandez
 */
import com.copernic.manageVehicles.dao.UserDAO;
import com.copernic.manageVehicles.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements ServiceInterface<User> {

    @Autowired
    private UserDAO userDAO;

    @Override
    public User save(User user) {
        if (user.getId() != 0) {
            User oldUser = userDAO.findByNif(user.getNif()).orElse(null);
            oldUser.setName(user.getName());
            oldUser.setPassword(user.getPassword());
            oldUser.setSurname(user.getSurname());
            oldUser.setPhone(user.getPhone());
            oldUser.setEmail(user.getEmail());            
            oldUser.setCargo(user.getCargo());
            return userDAO.save(oldUser);
        } else {
            return userDAO.save(user);
        }

    }

    @Override
    public List getAll() {
        return userDAO.findAll();
    }

    public Optional<User> findByNif(String nif) {
        return userDAO.findByNif(nif);
    }

    @Override
    public Optional<User> find(User user) {
        return userDAO.findByNif(user.getNif());
    }

    public List<User> search(String query) {
        return userDAO.findByNifContainingOrNameContainingOrSurnameContaining(query, query, query);
    }

    @Override
    @Transactional
    public void deleteById(String nif) {
        userDAO.deleteByNif(nif);
    }

}
