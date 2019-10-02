package com.kyle18003144.fitme;

public class AppUser {
    private String email;
    private String firstName;
    private String surname;
    private int height;
    private int weight;
    private int footstepsGoal;
    private int weightGoal;
    private String containerID;

    public AppUser() {
    }

    public AppUser(String email, String firstName, String surname, int height, int weight, int footstepsGoal, int weightGoal, String containerID) {
        this.email = email;
        this.firstName = firstName;
        this.surname = surname;
        this.height = height;
        this.weight = weight;
        this.footstepsGoal = footstepsGoal;
        this.weightGoal = weightGoal;
        this.containerID = containerID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getFootstepsGoal() {
        return footstepsGoal;
    }

    public void setFootstepsGoal(int footstepsGoal) {
        this.footstepsGoal = footstepsGoal;
    }

    public int getWeightGoal() {
        return weightGoal;
    }

    public void setWeightGoal(int weightGoal) {
        this.weightGoal = weightGoal;
    }

    public String getContainerID() {
        return containerID;
    }

    public void setContainerID(String containerID) {
        this.containerID = containerID;
    }
}
