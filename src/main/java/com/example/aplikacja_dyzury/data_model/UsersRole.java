package com.example.aplikacja_dyzury.data_model;

import javax.persistence.*;
import java.util.List;

@Entity
public class UsersRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;
    private String role;
    private String description;

    @ManyToMany(mappedBy = "roles")
    private List<Users> usersList;

    public List<Users> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = usersList;
    }

    public Long getRoleId() {
        return roleId;
    }
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "UsersRole{" +
                "roleId=" + roleId +
                ", role='" + role + '\'' +
                ", description='" + description + '\'' +
                ", users=" + usersList +
                '}';
    }
}