/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles.services;

/**
 *
 * @author rfernandez
 */
import com.copernic.manageVehicles.domain.Vehicle;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.copernic.manageVehicles.dao.VehicleDAO;
import com.copernic.manageVehicles.domain.User;
import jakarta.persistence.EntityNotFoundException;


@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleDAO vehicleDAO;

    @Override
    @Transactional
    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleDAO.save(vehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicle> getAllVehicles() {
        return vehicleDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Vehicle findVehicle(Vehicle vehicle) {
        return vehicleDAO.findById(vehicle.getNumberPlate()).orElse(null);

    }

    @Override
    public void deleteVehicleById(String numberPlate) {
        vehicleDAO.deleteById(numberPlate);
    }

    @Override
    public List<Vehicle> findByOwner(User owner) {
        return vehicleDAO.findByOwner(owner);
    }

    public boolean existsById(String numberPlate) {
        return vehicleDAO.existsById(numberPlate);
    }

    @Override
    public Vehicle findByNumberPlate(String numberPlate) {
        return vehicleDAO.findByNumberPlate(numberPlate);

    }

    @Override
    public void updateVehicle(Vehicle updatedVehicle) {
        if (updatedVehicle == null || updatedVehicle.getNumberPlate() == null) {
            throw new IllegalArgumentException("El vehículo es nulo o no tiene placa");
        }

        // Recuperar el vehículo existente
        Vehicle existingVehicle = vehicleDAO.findByNumberPlate(updatedVehicle.getNumberPlate());

        if (existingVehicle == null) {
            throw new EntityNotFoundException("No se encontró el vehículo con placa: " + updatedVehicle.getNumberPlate());
        }

        // Actualizar los campos necesarios
        existingVehicle.setBrand(updatedVehicle.getBrand());
        existingVehicle.setColor(updatedVehicle.getColor());
        existingVehicle.setFabricationYear(updatedVehicle.getFabricationYear());
        existingVehicle.setKm(updatedVehicle.getKm());
        existingVehicle.setModel(updatedVehicle.getModel());

        // Guardar los cambios en la base de datos
        vehicleDAO.save(existingVehicle);
    }
}
