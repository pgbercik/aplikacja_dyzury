package com.example.aplikacja_dyzury.DataModelAndRepo;


import javax.persistence.*;

@Entity
@Table(name = "doctor_title")
public class DoctorTitle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String type;

    public DoctorTitle(String type) {
        this.type = type;
    }
    public DoctorTitle(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "DoctorType{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
