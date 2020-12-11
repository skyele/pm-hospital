package com.sjtupm.manage.controller;

import com.sjtupm.manage.dto.PatientAppointmentParam;
import com.sjtupm.manage.dto.PatientPrescriptionParam;
import com.sjtupm.manage.entity.Appointment;
import com.sjtupm.manage.entity.Person;
import com.sjtupm.manage.entity.Prescription;
import com.sjtupm.manage.repository.AppointmentRepository;
import com.sjtupm.manage.repository.PersonRepository;
import com.sjtupm.manage.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class PatientController {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PrescriptionRepository prescriptionRepository;

    //挂号
    @PostMapping(path = "/patient/postAppointment")
    public void postAppointment(PatientAppointmentParam param){
        System.out.println("patient in postAppointment()");
        String patientName = param.getPatientName();
        System.out.println("patient name " + patientName);
        List<Person> patients = personRepository.findPersonByNameEqualsAndRoleEquals(patientName, 2);
        for(Person person:patients){
            System.out.println("person name " + person.getName());
        }
        if(patients.isEmpty()){
            Person tmpPatient = new Person();
            tmpPatient.setName(patientName);
            tmpPatient.setRole(2);
            patients.add(tmpPatient);
            personRepository.save(tmpPatient);
        }

        Person patient = patients.get(0);
        Appointment appointment = new Appointment();
        appointment.setPatientID(patient.getId());
        appointment.setPatientName(patient.getName());
        appointment.setCompletion(Boolean.FALSE);
        appointment.setContent(new String("hello"));

        appointmentRepository.save(appointment);
    }

    //获取所有挂号历史信息
    @GetMapping(path = "/patient/getAppointment")
    public List<Appointment> getAppointment(PatientAppointmentParam param){
        System.out.println("patient in getAppointment()");
        String patientName = param.getPatientName();
        System.out.println("patient name " + patientName);
        List<Person> patients = personRepository.findPersonByNameEqualsAndRoleEquals(patientName, 2);
        for(Person person:patients){
            System.out.println("person name " + person.getName());
        }
        if(patients.isEmpty()){
            return null;
        }

        Person patient = patients.get(0);
        return appointmentRepository.findAppointmentByPatientNameEquals(patient.getName());
    }

    //获取所有处方的历史信息
    @GetMapping(path = "/patient/getPrescription")
    public List<Prescription> getPrescription(PatientPrescriptionParam param){
        System.out.println("patient in getPrescription()");

        List<Person> patients = personRepository.findPersonByNameEqualsAndRoleEquals(param.getPatientName(), 2);
        if(patients.isEmpty()){
            System.out.println("There is no prescription for patient " + param.getPatientName());
            return null;
        }
        else {
            return prescriptionRepository.findPrescriptionsByPatientID(patients.get(0).getId());
        }
    }

    //支付处方
    @PostMapping(path = "/patient/postPrescription")
    public void postPrescription(PatientPrescriptionParam param){
        System.out.println("patient in postPrescription()");
        Optional<Prescription> prescription = prescriptionRepository.findPrescriptionsById(param.getPrescriptionID());
        if(prescription.isPresent()){
            prescription.get().setPayed(true);
            prescriptionRepository.save(prescription.get());
        }
        else {
            System.out.println("prescription: " + param.getPrescriptionID() + " not exist");
        }
    }
}
