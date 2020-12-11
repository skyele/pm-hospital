package com.sjtupm.manage.repository;

import com.sjtupm.manage.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment,Integer> {
    List<Appointment> findAppointmentByPatientNameEquals(String patientName);
    Optional<Appointment> findById(Integer id);
}
