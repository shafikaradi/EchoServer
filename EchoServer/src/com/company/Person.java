package com.company;

public class Person {

    private String name;
    private int age;
    private int nameLength;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Person(){}



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

    public int getNameLength() {return  this.nameLength;}




    public int deserializeAge(byte [] bytes){


        this.age = ((bytes[0] & 0xFF) | ((bytes[1] & 0xFF) << 8)  | ((bytes[2] & 0xFF) << 16)  | ((bytes[3] & 0xFF) << 24));
        return this.age;


    }

    public int deserializeNameLength(byte [] bytes){


        nameLength = ((bytes[4] & 0xFF) | ((bytes[5] & 0xFF) << 8)  | ((bytes[6] & 0xFF) << 16)  | ((bytes[7] & 0xFF) << 24));
        return nameLength;


    }
}
