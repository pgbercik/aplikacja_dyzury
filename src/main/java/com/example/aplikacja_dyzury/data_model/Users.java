package com.example.aplikacja_dyzury.data_model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;


@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "password"),
        @UniqueConstraint(columnNames = {"firstName", "lastName", "email", "password"})
})
public class Users {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    private Long usersId;
    private String firstName;
    private String lastName;


    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String password;

    @ManyToMany
    @JoinTable(
            name = "users_table_roles",
            joinColumns = { @JoinColumn(name = "users_id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id") }
    )
    private Set<UsersRole> roles = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER)
    private DoctorTitle doctorTitle;


    public Long getUsersId() {
        return usersId;
    }

    public void setUsersId(Long usersId) {
        this.usersId = usersId;
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

    public Set<UsersRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UsersRole> roles) {
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
        return "Users{" +
                "usersId=" + usersId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", doctorTitle=" + doctorTitle +
                '}';
    }
}