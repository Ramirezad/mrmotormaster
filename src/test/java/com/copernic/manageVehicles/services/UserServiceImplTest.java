/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles.services;

import com.copernic.manageVehicles.dao.UserDAO;
import com.copernic.manageVehicles.domain.User;
import com.copernic.manageVehicles.services.UserServiceImpl;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class UserServiceImplTest {

    @Mock
    private UserDAO userDAO;
    
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testSave() {
        User user = new User();
        user.setId(1);
        user.setNif("12345678Z");
        user.setName("Test");
        user.setPassword("password");
        user.setSurname("Surname");
        user.setPhone(123456789);
        user.setEmail("test@test.com");
        user.setCargo(User.Rol.ADMINISTRADOR);

        when(userDAO.findByNif(user.getNif())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userDAO.save(any(User.class))).thenReturn(user);
        
        userService.save(user);

        verify(userDAO, times(1)).findByNif(user.getNif());
        verify(userDAO, times(1)).save(user);
    }

    @Test
    public void testFindByNif() {
        User user = new User();
        user.setId(1);
        user.setNif("12345678Z");
        user.setName("Test");
        user.setPassword("password");
        user.setSurname("Surname");
        user.setPhone(123456789);
        user.setEmail("test@test.com");
        user.setCargo(User.Rol.ADMINISTRADOR);

        when(userDAO.findByNif(user.getNif())).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByNif(user.getNif());

        assertTrue(foundUser.isPresent());
        assertEquals(user.getNif(), foundUser.get().getNif());

        verify(userDAO, times(1)).findByNif(user.getNif());
    }

    @Test
    public void testGetAll() {
        User user1 = new User();
        user1.setId(1);
        user1.setNif("12345678Z");
        user1.setName("Test1");
        user1.setPassword("password1");
        user1.setSurname("Surname1");
        user1.setPhone(123456789);
        user1.setEmail("test1@test.com");
        user1.setCargo(User.Rol.ADMINISTRADOR);

        User user2 = new User();
        user2.setId(2);
        user2.setNif("87654321Z");
        user2.setName("Test2");
        user2.setPassword("password2");
        user2.setSurname("Surname2");
        user2.setPhone(987654321);
        user2.setEmail("test2@test.com");
        user2.setCargo(User.Rol.USUARIO);

        List<User> users = Arrays.asList(user1, user2);

        when(userDAO.findAll()).thenReturn(users);

        List<User> foundUsers = userService.getAll();

        assertEquals(2, foundUsers.size());
        verify(userDAO, times(1)).findAll();
    }

    @Test
    public void testDeleteById() {
        String nif = "12345678Z";

        doNothing().when(userDAO).deleteByNif(nif);

        userService.deleteById(nif);

        verify(userDAO, times(1)).deleteByNif(nif);
    }

}
