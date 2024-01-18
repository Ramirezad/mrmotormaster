package com.copernic.manageVehicles.dao;

import com.copernic.manageVehicles.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TasksDAO extends JpaRepository<Task, Long> {


}
