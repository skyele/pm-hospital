package com.sjtupm.manage.controller;

import com.sjtupm.manage.dto.DoctorAppointmentParam;
import com.sjtupm.manage.dto.DoctorPrescriptionParam;
import com.sjtupm.manage.entity.Appointment;
import com.sjtupm.manage.entity.Medicine;
import com.sjtupm.manage.entity.Person;
import com.sjtupm.manage.entity.Prescription;
import com.sjtupm.manage.repository.AppointmentRepository;
import com.sjtupm.manage.repository.MedicineRepository;
import com.sjtupm.manage.repository.PersonRepository;
import com.sjtupm.manage.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Column;
import java.util.List;
import java.util.Optional;

@RestController
public class DoctorController {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private PrescriptionRepository prescriptionRepository;

    //获取所有的病人挂号单
    @GetMapping(path = "/doctor/getAppointment")
    public List<Appointment> getAppointment(){
        System.out.println("doctor in getAppointment()");
        return appointmentRepository.findAll();
    }

    //更新检查单
    @PostMapping(path = "/doctor/postAppointment")
    public void postAppointment(DoctorAppointmentParam param){
        System.out.println("doctor in postAppointment()");

        Optional<Appointment> appointment = appointmentRepository.findById(param.getAppointmentId());
        if(appointment.isPresent()){
            appointment.get().setContent(param.getContent());
            appointment.get().setCompletion(true);
            appointmentRepository.save(appointment.get());
        }
        else{
            System.out.println("appointment not exist");
        }
    }

    //开处方
    @PostMapping(path = "/doctor/postPrescription")
    public void postPrescription(DoctorPrescriptionParam param){
        System.out.println("doctor in postPrescription()");
        Optional<Appointment> appointment = appointmentRepository.findById(param.getAppointmentID());
        if(!appointment.isPresent()){
            System.out.println("appointment not present");
            return;
        }
        Optional<Person> patient = personRepository.findPersonById(appointment.get().getPatientID());
        if(!patient.isPresent()){
            System.out.println("patient not present");
            return;
        }

        List<Medicine> medicines = medicineRepository.findMedicineByName(param.getMedicineName());
        if(medicines.isEmpty()){
            System.out.println("medicine: "+param.getMedicineName()+" not present");
            return;
        }

        Prescription prescription = new Prescription();
        prescription.setAppointmentID(appointment.get().getId());
        prescription.setPatientID(patient.get().getId());
        prescription.setMedicineID(medicines.get(0).getId());
        prescription.setMedicineName(medicines.get(0).getName());
        prescription.setMedicinePrice(medicines.get(0).getPrice());
        prescription.setPayed(false);
        prescriptionRepository.save(prescription);
    }
}
