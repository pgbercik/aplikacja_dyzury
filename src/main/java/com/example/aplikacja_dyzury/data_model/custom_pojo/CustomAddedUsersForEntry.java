package com.example.aplikacja_dyzury.data_model.custom_pojo;

public class CustomAddedUsersForEntry {
    private Long userId;
    private String profTitle;
    private String firstName;
    private String lastName;

    public CustomAddedUsersForEntry(Long userId, String profTitle, String firstName, String lastName) {
        this.userId = userId;
        this.profTitle = profTitle;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public String getProfTitle() { return profTitle; }

    public void setProfTitle(String profTitle) { this.profTitle = profTitle; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    @Override
    public String toString() {
        return "CustomAddedUsersForEntry{" +
                "userId=" + userId +
                ", profTitle='" + profTitle + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
