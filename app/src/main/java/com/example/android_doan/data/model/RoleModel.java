package com.example.android_doan.data.model;

import java.io.Serializable;
import java.util.Objects;

public class RoleModel implements Serializable {
    private int id;
    private String code;
    private String name;

    public RoleModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RoleModel roleModel = (RoleModel) o;
        return id == roleModel.id && Objects.equals(name, roleModel.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
