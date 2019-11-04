package com.kyle18003144.fitme;

import android.net.Uri;

import java.time.LocalDateTime;
import java.util.Date;

public class AppPost  implements Comparable<AppPost> {
    private String containerID;
    private String email;
    private String title;
    private PostType postType;
    private String postBody;
    private double postValue; // Just called value so I can use the same field for footsteps and weight
    private String postImageURI;
    private Date date;

    public AppPost() {
    }

    public AppPost(String containerID, String email, String title, PostType postType, String postBody, double postValue, String postImageURI, Date date) {
        this.containerID = containerID;
        this.email = email;
        this.title = title;
        this.postType = postType;
        this.postBody = postBody;
        this.postValue = postValue;
        this.postImageURI = postImageURI;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContainerID() {
        return containerID;
    }

    public void setContainerID(String containerID) {
        this.containerID = containerID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PostType getPostType() {
        return postType;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public double getPostValue() {
        return postValue;
    }

    public void setPostValue(double postValue) {
        this.postValue = postValue;
    }

    public String getPostImageURI() {
        return postImageURI;
    }

    public void setPostImageURI(String postImageURI) {
        this.postImageURI = postImageURI;
    }

    //When sorting, I want posts ordered by their date in descending order
    public int compareTo(AppPost appPost){
        return appPost.getDate().compareTo(date);
    }
}
