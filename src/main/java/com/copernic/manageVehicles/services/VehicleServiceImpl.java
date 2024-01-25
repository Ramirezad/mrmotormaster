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
import java.util.Optional;

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
    public List<Vehicle> findByOwner(User owner){
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
    public void updateVehicle(Vehicle vehicle) {
        if (vehicle != null && vehicle.getNumberPlate() != null) {
            // Verificar si el vehículo existe antes de intentar actualizar
            Optional<Vehicle> existingVehicleOptional = vehicleDAO.findById(vehicle.getNumberPlate());

            if (existingVehicleOptional.isPresent()) {
                Vehicle existingVehicle = existingVehicleOptional.get();

                // Actualizar solo los campos necesarios
                existingVehicle.setBrand(vehicle.getBrand());
                existingVehicle.setColor(vehicle.getColor());
                existingVehicle.setFabricationYear(vehicle.getFabricationYear());
                existingVehicle.setKm(vehicle.getKm());
                existingVehicle.setModel(vehicle.getModel());

                // Puedes agregar más campos según sea necesario

                vehicleDAO.save(existingVehicle); // Esto actualizará el vehículo en la base de datos
            } else {
                // Manejar el caso en que el vehículo no existe
                // Puedes lanzar una excepción, loggear un mensaje, etc.
            }
        } else {
            // Manejar el caso de un vehículo nulo o sin placa
            // Puedes lanzar una excepción, loggear un mensaje, etc.
        }
    }
}
    
