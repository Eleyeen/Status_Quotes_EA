package com.techease.bestquotes.Model;


/**
 * Created by Mohamed on 12/12/2018.
 */

public class Step {

    private int ID;
    private String ImageStep;
    private int ID_Wolf;

    public Step(int id, String imageStep, int idWolf) {
        this.ID = id;
        this.ImageStep = imageStep;
        this.ID_Wolf = idWolf;
    }

    public int getID() {
        return ID;
    }

    public void setID(int id) {
        this.ID = id;
    }

    public String getImageStep() {
        return ImageStep;
    }

    public void setImageStep(String imageStep) {
        this.ImageStep = imageStep;
    }

    public int getIdWolf() {
        return ID_Wolf;
    }

    public void setIdWolf(int idWolf) {
        this.ID_Wolf = idWolf;
    }
}

