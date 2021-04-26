package com.example.aplikacja_dyzury.DataModelAndRepo;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "dyzur")
public class EntryDyzurDb {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;


    private String title;


    @Column(name="start_time")
    private LocalDateTime startTime;

    @Column(name="end_name")
    private  LocalDateTime endTime;

    @Column(name="all_day")
    private boolean allDay;

//    private String color;

    private String description;

    private boolean editable;

    @OneToOne(fetch = FetchType.EAGER)
//    @Column(name="hospital_name")
    private Hospital hospital;

    @OneToOne(fetch = FetchType.EAGER)
//    @Column(name="hospital_department")
    private HospitalDepartment hospitalDepartment;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<User> users;



    public EntryDyzurDb() {}



    public EntryDyzurDb(String id, String title, LocalDateTime startTime, LocalDateTime endTime, boolean allDay,/* String color, */String description, boolean editable, Hospital hospital, HospitalDepartment hospitalDepartment, Set<User> users) {
        this.id = id;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.allDay = allDay;

        this.description = description;
        this.editable = editable;
        this.hospital = hospital;
        this.hospitalDepartment = hospitalDepartment;
        this.users = users;
    }


    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public LocalDateTime getStartTime() { return startTime; }

    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }

    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public boolean isAllDay() { return allDay; }

    public void setAllDay(boolean allDay) { this.allDay = allDay; }



    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public boolean isEditable() { return editable; }

    public void setEditable(boolean editable) { this.editable = editable; }

    public Hospital getHospital() { return hospital; }

    public void setHospital(Hospital hospital) { this.hospital = hospital; }

    public HospitalDepartment getHospitalDepartment() { return hospitalDepartment; }

    public void setHospitalDepartment(HospitalDepartment hospitalDepartment) { this.hospitalDepartment = hospitalDepartment; }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "EntryDyzurDb{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", allDay=" + allDay +
                ", description='" + description + '\'' +
                ", editable=" + editable +
                ", hospital=" + hospital +
                ", hospitalDepartment=" + hospitalDepartment +
                ", users=" + users +
                '}';
    }
}
