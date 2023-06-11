package com.example.demo;

public class AsdEmployees {
    int id;
    String name;

    public AsdEmployees(String name, int id) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }
    @Override
    public String toString() {
        return this.name;
    }
}

