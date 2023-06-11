package com.example.demo;

public class Families {
    int id;
    String name;
    int areaId;
    String areaName;
    int asdEmployee;
    String asdName;

    public Families(int id, String name, int areaId, int asdEmployee,String asdName, String areaName) {
        this.id = id;
        this.name = name;
        this.areaId = areaId;
        this.asdEmployee = asdEmployee;
        this.asdName = asdName;
        this.areaName = areaName;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAreaId() {
        return this.areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public int getAsdEmployee() {
        return this.asdEmployee;
    }

    public void setAsdEmployee(int asdEmployee) {
        this.asdEmployee = asdEmployee;
    }

    public String getAsdName() {
        return this.asdName;
    }

    public void setAsdName(String asdName) {
        this.asdName = asdName;
    }

    public String getAreaName() {
        return this.areaName;
    }

    public void setAreaName(String areaName) {
        this.asdName = areaName;
    }

}
