package com.example.asynctest;

public class Book {
    private String id,name,authors;

    public Book(String id, String name, String authors){
        this.id = id;
        this.name = name;
        this.authors = authors;
    }

    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getAuthors(){
        return authors;
    }
}
