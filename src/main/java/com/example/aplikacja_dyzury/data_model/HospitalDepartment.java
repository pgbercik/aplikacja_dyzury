package com.example.aplikacja_dyzury.data_model;

import javax.persistence.*;

@Entity
@Table(name = "hospital_department")
public class HospitalDepartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_dept_id")
    private Long id;

    @Column(nullable = false)
    private  String department;

    @Column
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "referenced_hospital_id")
    private Hospital hospital;

    public HospitalDepartment(String department, boolean isActive) {
        this.department = department;
        this.isActive = isActive;
    }

    public HospitalDepartment() {
    }

    public String getDepartment() { return department; }

    public void setDepartment(String department) { this.department = department; }

    public boolean isActive() { return isActive; }

    public void setActive(boolean active) { isActive = active; }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    @Override
    public String toString() {
        return "HospitalDepartment{" +
                "id=" + id +
                ", department='" + department + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
