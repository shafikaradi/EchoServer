/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bsc
 */
public class Person {

  
    private String name;
    private int age;
    private int lengthOfName;
    byte [] bytes;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
        bytes = new byte[4];
    }

    Person() {
    
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
    
    public int getNameLength(){
        return this.lengthOfName;
    }
     
    public int deserializeAge(byte [] bytes){
        
        this.age = (int) (bytes[0] | (bytes[1] << 8) | (bytes[2] << 16) | (bytes[3] << 24));
        return this.age; 
    }
    
     public int deserializeNameLength(byte [] bytes){
        
        this.lengthOfName = (int) (bytes[0] | (bytes[1] << 8) | (bytes[2] << 16) | (bytes[3] << 24));
        return this.lengthOfName; 
    }
    
    public String deserializeName(byte [] bytes){
        
        try {
            this.name = new String(bytes,"UTF-16");
            return this.name;
        } catch (UnsupportedEncodingException ex) {
           System.err.println(ex.getMessage());
        }
        
        return "NOt known";
    }
    
    @Override
    public String toString(){
        return String.format("Person's name is %s and his age is %d", name,age);
    }

}
