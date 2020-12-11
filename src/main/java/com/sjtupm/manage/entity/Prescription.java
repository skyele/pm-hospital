package com.sjtupm.manage.entity;

import javax.persistence.*;

@Entity
@Table(name = "prescription")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name="appointmentID")
    private Integer appointmentID;

    @Column(name="patientID")
    private Integer patientID;

    @Column(name="medicineID")
    private Integer medicineID;

    @Column(name="medicineName")
    private String medicineName;

    @Column(name="medicinePrice")
    private Double medicinePrice;

    @Column(name="payed")
    private Boolean payed;

    public Integer getId() {
        return id;
    }

    public Integer getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(Integer appointmentID) {
        this.appointmentID = appointmentID;
    }

    public Integer getPatientID() {
        return patientID;
    }

    public void setPatientID(Integer patientID) {
        this.patientID = patientID;
    }

    public Integer getMedicineID() {
        return medicineID;
    }

    public void setMedicineID(Integer medicineID) {
        this.medicineID = medicineID;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public Double getMedicinePrice() {
        return medicinePrice;
    }

    public void setMedicinePrice(Double medicinePrice) {
        this.medicinePrice = medicinePrice;
    }

    public Boolean getPayed() {
        return payed;
    }

    public void setPayed(Boolean payed) {
        this.payed = payed;
    }
}
