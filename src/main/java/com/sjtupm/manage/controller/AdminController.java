package com.sjtupm.manage.controller;

import com.sjtupm.manage.dto.MedicineParam;
import com.sjtupm.manage.dto.ScheduleParam;
import com.sjtupm.manage.entity.Medicine;
import com.sjtupm.manage.entity.Person;
import com.sjtupm.manage.entity.Schedule;
import com.sjtupm.manage.repository.MedicineRepository;
import com.sjtupm.manage.repository.PersonRepository;
import com.sjtupm.manage.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminController {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;

    //获取所有药品信息
    @GetMapping(path = "/admin/getMedicine")
    public List<Medicine> getMedicine(){
        System.out.println("admin in getMedicine()");
        return medicineRepository.findAll();
    }

    //调整药品价格及库存
    @PostMapping(path = "/admin/postMedicine")
    public void postMedicine(MedicineParam param) {
        System.out.println("admin in postMedicine()");
        System.out.println("medicineName: "+param.getMedicineName()+" medicineStock: "+param.getMedicineStock()+" medicinePrice: "+param.getMedicinePrice());
        List<Medicine> medicines = medicineRepository.findMedicineByName(param.getMedicineName());
        if(medicines.isEmpty()){
            Medicine medicine = new Medicine();
            medicine.setName(param.getMedicineName());
            medicine.setStock(param.getMedicineStock());
            medicine.setPrice(param.getMedicinePrice());
            medicineRepository.save(medicine);
        }
        else {
            Medicine medicine = medicines.get(0);
            medicine.setStock(param.getMedicineStock());
            medicine.setPrice(param.getMedicinePrice());
            medicineRepository.save(medicine);
        }
    }

    //返回所有的医生排班
    @GetMapping(path = "/admin/getSchedule")
    public List<Schedule> getSchedule(){
        System.out.println("admin in getSchedule()");
        return scheduleRepository.findAll();
    }

    //更新医生排班
    @PostMapping(path = "/admin/postSchedule")
    public void postSchedule(ScheduleParam param) {
        System.out.println("admin in postSchedule()");
        System.out.println("docterName: "+param.getDoctorName()+" schedule: "+param.getSchedule());
        List<Person> doctors = personRepository.findPersonByNameEqualsAndRoleEquals(param.getDoctorName(), 1);
        if(doctors.isEmpty()){
            System.out.println("docter: " + param.getDoctorName() +" not exists");
            return;
        }
        List<Schedule> schedules = scheduleRepository.findByDocterNameEquals(param.getDoctorName());
        if(schedules.isEmpty()){
            Schedule schedule = new Schedule();
            schedule.setDocterID(doctors.get(0).getId());
            schedule.setDocterName(param.getDoctorName());
            schedule.setDay(param.getSchedule());
            scheduleRepository.save(schedule);
        }
        else {
            Schedule schedule = schedules.get(0);
            schedule.setDay(param.getSchedule());
            scheduleRepository.save(schedule);
        }
    }
}
