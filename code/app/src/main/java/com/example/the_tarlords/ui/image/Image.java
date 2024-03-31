package com.example.the_tarlords.ui.image;

public class Image {
    private String imageData;
    private String collection;
    private String name;
    private String docId;
    public Image(String imageData, String collection, String name, String docId) {
        this.imageData = imageData;
        this.collection = collection;
        this.name = name;
        this.docId = docId;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
}
