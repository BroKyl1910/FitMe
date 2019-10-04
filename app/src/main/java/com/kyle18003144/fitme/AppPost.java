package com.kyle18003144.fitme;

import android.net.Uri;

public class AppPost {
    private String containerID;
    private String email;
    private String title;
    private PostType postType;
    private String postBody;
    private double postValue; // Just called value so I can use the same field for footsteps and weight
    private Uri postImageURI;

    public AppPost() {
    }

    public AppPost(String containerID, String email, String title, PostType postType, String postBody, double postValue, Uri postImageURI) {
        this.containerID = containerID;
        this.email = email;
        this.title = title;
        this.postType = postType;
        this.postBody = postBody;
        this.postValue = postValue;
        this.postImageURI = postImageURI;
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

    public Uri getPostImageURI() {
        return postImageURI;
    }

    public void setPostImageURI(Uri postImageURI) {
        this.postImageURI = postImageURI;
    }
}
