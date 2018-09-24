package com.company;

public class Person {

    private String name;
    private String age;

    public Person(String name, String age) {
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }




//    public void deserializeAge(byte [] bytes){
//
//
//        this.age = (int) ((bytes[0] & 0xFF) | ((bytes[1] & 0xFF) << 8)  | ((bytes[2] & 0xFF) << 16)  | ((bytes[3] & 0xFF) << 24));
//
//    }
}
