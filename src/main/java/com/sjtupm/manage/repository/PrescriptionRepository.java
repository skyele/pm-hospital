package com.sjtupm.manage.repository;

import com.sjtupm.manage.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
    List<Prescription> findPrescriptionsByPatientID(Integer patientID);
    Optional<Prescription> findPrescriptionsById(Integer prescriptionID);
}
