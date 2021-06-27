package com.example.aplikacja_dyzury.data_model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;


@Entity(name = "user_table")
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "password"),
        @UniqueConstraint(columnNames = {"firstName", "lastName", "email", "password"})
})
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;


    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<UserRole> roles = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER)
    private DoctorTitle doctorTitle;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public DoctorTitle getDoctorTitle() {
        return doctorTitle;
    }

    public void setDoctorTitle(DoctorTitle doctorTitle) {
        this.doctorTitle = doctorTitle;
    }

    public String getCustomUserNameAndSurname() {
        return getFirstName() + " " + getLastName();
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", doctorTitle=" + doctorTitle +
                '}';
    }
}