package com.example.aplikacja_dyzury.data_model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RequestStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String stateName;

    public RequestStatus(String stateName) {
        this.stateName = stateName;
    }
    public RequestStatus() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public String toString() {
        return "RequestStatus{" +
                "id=" + id +
                ", stateName='" + stateName + '\'' +
                '}';
    }
}
