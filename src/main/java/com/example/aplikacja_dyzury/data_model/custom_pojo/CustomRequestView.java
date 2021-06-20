package com.example.aplikacja_dyzury.data_model.custom_pojo;

import com.example.aplikacja_dyzury.data_model.EntryDyzurDb;
import com.example.aplikacja_dyzury.data_model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomRequestView {

    private Long requestId;
    private String description;
    private String requestTime;
    private String status;
    private String initUserName;
    private String targetUserName;
    private String targetEntryName;
    private String targetEntryTime;
    private EntryDyzurDb initEntry;
    private EntryDyzurDb targetEntry;
    private User initUser;
    private User targetUser;



    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public CustomRequestView(Long requestId, String description, LocalDateTime requestTime, String status, String initUserName, String targetUserName,
                             String targetEntryName, LocalDateTime targetEntryTime, EntryDyzurDb initEntry, EntryDyzurDb targetEntry, User initUser, User targetUser) {
        this.requestId = requestId;
        this.description = description;
        this.requestTime = requestTime.format(formatter);
        this.status = status;
        this.initUserName = initUserName;
        this.targetUserName = targetUserName;
        this.targetEntryName = targetEntryName;
        this.targetEntryTime = targetEntryTime.format(formatter);
        this.initEntry = initEntry;
        this.targetEntry = targetEntry;
        this.initUser = initUser;
        this.targetUser = targetUser;
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

    public void setDescription(LocalDateTime description) { this.description = description.format(formatter); }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInitUserName() {
        return initUserName;
    }

    public void setInitUserName(String initUserName) {
        this.initUserName = initUserName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTargetUserName() {
        return targetUserName;
    }

    public void setTargetUserName(String targetUserName) {
        this.targetUserName = targetUserName;
    }

    public String getTargetEntryName() {
        return targetEntryName;
    }

    public void setTargetEntryName(String targetEntryName) {
        this.targetEntryName = targetEntryName;
    }

    public String getTargetEntryTime() { return targetEntryTime; }

    public void setTargetEntryTime(String targetEntryTime) { this.targetEntryTime = targetEntryTime; }

    public EntryDyzurDb getInitEntry() {
        return initEntry;
    }

    public void setInitEntry(EntryDyzurDb initEntry) {
        this.initEntry = initEntry;
    }

    public EntryDyzurDb getTargetEntry() {
        return targetEntry;
    }

    public void setTargetEntry(EntryDyzurDb targetEntry) {
        this.targetEntry = targetEntry;
    }

    public User getInitUser() {
        return initUser;
    }

    public void setInitUser(User initUser) {
        this.initUser = initUser;
    }

    public User getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }
}
