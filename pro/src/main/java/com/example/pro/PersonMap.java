package com.example.pro;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PersonMap {
    private String name;
    private int age;
    private List<PersonMap> children = new ArrayList<>();
    private PersonMap spouse;
    private List<PersonMap> parents = new ArrayList<>();

    public PersonMap() {}

    public PersonMap(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<PersonMap> getChildren() {
        return children;
    }


    public List<PersonMap> getParents() {
        return parents;
    }

    public void addParent(PersonMap parent) {
        if (!this.parents.contains(parent)) {
            this.parents.add(parent);
        }
    }

    public void addChild(PersonMap child) {
        this.children.add(child);
        child.addParent(this);
    }

    public PersonMap getSpouse() {
        return spouse;
    }

    public void setSpouse(PersonMap spouse) {
        this.spouse = spouse;
    }

    public void marry(PersonMap person) {
        if (this.spouse == null && person.spouse == null) {
            this.spouse = person;
            person.spouse = this;
        }
    }


    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", spouse=" + (spouse != null ? spouse.getName() : "none") +
                ", children=" + children.stream().map(PersonMap::getName).collect(Collectors.toList()) +
                ", parents=" + parents.stream().map(PersonMap::getName).collect(Collectors.toList()) +
                '}';
    }
}