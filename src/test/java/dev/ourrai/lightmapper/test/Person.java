package dev.ourrai.lightmapper.test;

import dev.ourrai.lightmapper.annotations.Mapper;
import dev.ourrai.lightmapper.annotations.MapField;

@Mapper(target = PersonDto.class)
public class Person {
    @MapField
    private String name;
    
    @MapField(targetField = "yearOfBirth")
    private int birthYear;
    
    public Person(String name, int birthYear) {
        this.name = name;
        this.birthYear = birthYear;
    }

    public String getName() {
        return name;
    }

    public int getBirthYear() {
        return birthYear;
    }
} 