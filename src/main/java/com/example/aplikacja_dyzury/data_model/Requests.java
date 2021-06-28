package com.example.aplikacja_dyzury.data_model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Requests {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;
    private String description;
    private LocalDateTime requestTime;
    private Integer status;
    private boolean isActive;

    @OneToOne(fetch = FetchType.EAGER)
    private EntryDyzurDb entryInitial;

    @OneToOne(fetch = FetchType.EAGER)
    private EntryDyzurDb entryTarget;

    @OneToOne(fetch = FetchType.EAGER)
    private Users usersInit;

    @OneToOne(fetch = FetchType.EAGER)
    private Users usersTarget;


    @ManyToMany(fetch = FetchType.EAGER)
    private List<RequestStatus> requestStatusList;

    public Requests() {}

    public Requests(String description, LocalDateTime requestTime, Integer status, boolean isActive, EntryDyzurDb entryInitial, EntryDyzurDb entryTarget, Users usersInit, Users usersTarget) {
        this.description = description;
        this.requestTime = requestTime;
        this.status = status;
        this.isActive = isActive;
        this.entryInitial = entryInitial;
        this.entryTarget = entryTarget;
        this.usersInit = usersInit;
        this.usersTarget = usersTarget;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public EntryDyzurDb getEntryInitial() {
        return entryInitial;
    }

    public void setEntryInitial(EntryDyzurDb entryInitial) {
        this.entryInitial = entryInitial;
    }

    public EntryDyzurDb getEntryTarget() {
        return entryTarget;
    }

    public void setEntryTarget(EntryDyzurDb entryTarget) {
        this.entryTarget = entryTarget;
    }

    public Users getUserInit() {
        return usersInit;
    }

    public void setUserInit(Users usersInit) {
        this.usersInit = usersInit;
    }

    public Users getUserTarget() {
        return usersTarget;
    }

    public void setUserTarget(Users usersTarget) {
        this.usersTarget = usersTarget;
    }

    public List<RequestStatus> getRequestStatusList() {
        return requestStatusList;
    }

    public void setRequestStatusList(List<RequestStatus> requestStatusList) {
        this.requestStatusList = requestStatusList;
    }

    @Override
    public String toString() {
        return "Requests{" +
                "requestId=" + requestId +
                ", description='" + description + '\'' +
                ", requestTime=" + requestTime +
                ", status=" + status +
                ", isActive=" + isActive +
                ", entryInitial=" + entryInitial +
                ", entryTarget=" + entryTarget +
                ", usersInit=" + usersInit +
                ", usersTarget=" + usersTarget +
                ", requestStatusList=" + requestStatusList +
                '}';
    }
}
