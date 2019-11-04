package com.kyle18003144.fitme;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AppUser {
    private String email;
    private String firstName;
    private String surname;
    private double height;
    private double weight;
    private int footstepsGoal;
    private double weightGoal;
    private String containerID;
    private ArrayList<String> following;
    private ArrayList<String> followers;

    public AppUser() {
    }

    public AppUser(String email, String firstName, String surname, double height, double weight, int footstepsGoal, double weightGoal, String containerID, @Nullable ArrayList<String> following, @Nullable ArrayList<String> followers) {
        this.email = email;
        this.firstName = firstName;
        this.surname = surname;
        this.height = height;
        this.weight = weight;
        this.footstepsGoal = footstepsGoal;
        this.weightGoal = weightGoal;
        this.containerID = containerID;
        this.followers = (followers == null) ? new ArrayList<String>() : followers;
        this.following = (following == null) ? new ArrayList<String>() : following;
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

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getFootstepsGoal() {
        return footstepsGoal;
    }

    public void setFootstepsGoal(int footstepsGoal) {
        this.footstepsGoal = footstepsGoal;
    }

    public double getWeightGoal() {
        return weightGoal;
    }

    public void setWeightGoal(double weightGoal) {
        this.weightGoal = weightGoal;
    }

    public String getContainerID() {
        return containerID;
    }

    public void setContainerID(String containerID) {
        this.containerID = containerID;
    }

    public ArrayList<String> getFollowing() {
        if(following == null){
            return new ArrayList<>();
        }
        return following;
    }

    public void setFollowing(ArrayList<String> following) {
        this.following = following;
    }

    public ArrayList<String> getFollowers() {
        if(followers == null){
            return new ArrayList<>();
        }
        return followers;
    }

    public void setFollowers(ArrayList<String> followers) {
        this.followers = followers;
    }

    public void addFollower(String follower) {
        if (followers == null) followers = new ArrayList<>();
        this.followers.add(follower);
    }

    public void followUser(String user) {
        if (following == null) following = new ArrayList<>();
        this.following.add(user);
    }

    public void removeFollower(String user) {
        this.followers.remove(user);
    }

    public void unfollowUser(String user) {
        this.following.remove(user);
    }

    public boolean isFollowing(String user) {
        if(following == null) return false;
        return following.contains(user);
    }

    public boolean isFollowedBy(String user) {
        return followers.contains(user);
    }
}
