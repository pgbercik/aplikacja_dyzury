package com.example.aplikacja_dyzury.DataModelAndRepo;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "hospital", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "address", "city"})
})

public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_id")
    private Long id;

    private String name;
    private String address;
    private String city;
    private boolean isActive;

    @OneToMany()
    @LazyCollection(LazyCollectionOption.FALSE) //obchodzimy MultipleBagFetchException
    @JoinColumn(name = "referenced_hospital_id", referencedColumnName = "hospital_id")
    private List<HospitalDepartment> hospitalDepartments;


    public Hospital(String name, String address, String city, boolean isActive) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.isActive = isActive;
    }

    public Hospital(String name, String address, String city, boolean isActive, List<HospitalDepartment> hospitalDepartments) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.isActive = isActive;
        this.hospitalDepartments = hospitalDepartments;
    }

    public Hospital() {

    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public boolean isActive() { return isActive; }

    public void setActive(boolean active) { isActive = active; }

    public List<HospitalDepartment> getHospitalDepartments() { return hospitalDepartments; }

    public void setHospitalDepartments(List<HospitalDepartment> hospitalDepartments) { this.hospitalDepartments = hospitalDepartments; }

    @Override
    public String toString() {
        return "Hospital{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", isActive=" + isActive +
                ", hospitalDepartments=" + hospitalDepartments +
                '}';
    }
}
