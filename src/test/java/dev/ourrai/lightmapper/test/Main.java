package dev.ourrai.lightmapper.test;

public class Main {
    public static void main(String[] args) {
        Person person = new Person("John Doe", 1990);
        PersonDto dto = PersonMapper.map(person);
        
        System.out.println("Name: " + dto.getName());
        System.out.println("Year of Birth: " + dto.getYearOfBirth());
    }
} 