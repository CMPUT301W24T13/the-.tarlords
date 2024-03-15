package com.example.the_tarlords.data.app;

public class App {
    private String name;

    private String typeOfApp;

    public App(String name, String typeOfApp) {
        this.name = name;
        this.typeOfApp = typeOfApp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeOfApp() {
        return typeOfApp;
    }

    public void setTypeOfApp(String typeOfApp) {
        this.typeOfApp = typeOfApp;
    }
}