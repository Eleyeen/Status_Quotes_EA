package com.techease.bestquotes.Model;


import java.util.ArrayList;

/**
 * Created by Mohamed on 07/12/2018.
 */

public class Wolf {

    private int ID;
    private String Title;
    private String ImageCircle;
    private String ImageRectangle;
    private int StepsNumber;
    private String Level;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    private String color;
    private ArrayList<StepImage> StepsImages;

    public Wolf(){


    }

    public Wolf(int ID, String title,  int stepsNumber, String level, ArrayList<StepImage> stepsImages, String color) {
        this.ID = ID;
        this.Title = title;
        this.StepsNumber    = stepsNumber;
        this.Level = level;
        this.StepsImages = stepsImages;
        this.color = color;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getImageCircle() {
        return ImageCircle;
    }

    public void setImageCircle(String imageCircle) {
        this.ImageCircle = imageCircle;
    }

    public String getImageRectangle() {
        return ImageRectangle;
    }

    public void setImageRectangle(String imageRectangle) {
        this.ImageRectangle = imageRectangle;
    }

    public int getStepsNumber() {
        return StepsNumber;
    }

    public void setStepsNumber(int stepsNumber) {
        this.StepsNumber = stepsNumber;
    }

    public String getLevel() {
            return Level;
    }

    public void setLevel(String level) {
            this.Level = level;
    }

    public ArrayList<StepImage> getStepsImages() {
        return StepsImages;
    }

    public void setStepsImages(ArrayList<StepImage> stepsImages) {
        this.StepsImages = stepsImages;
    }

}
