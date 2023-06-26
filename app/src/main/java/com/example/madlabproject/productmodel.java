package com.example.madlabproject;

public class productmodel {
    private String title;
    private String description;
    private String cost;
    private String count;
    private String imagename;

    public productmodel() {
        // Required empty public constructor for Firebase
    }

    public productmodel(String title, String description, String cost, String count, String imagename) {
        this.title = title;
        this.description = description;
        this.cost = cost;
        this.count = count;
        this.imagename= imagename;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCost() {
        return cost;
    }

    public String getCount() {
        return count;
    }

    public String getimagename() {
        return imagename;
    }
}
