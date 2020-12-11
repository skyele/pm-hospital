package com.sjtupm.manage.repository;

import com.sjtupm.manage.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine,Integer> {
    List<Medicine> findMedicineByName(String medicineName);
}
