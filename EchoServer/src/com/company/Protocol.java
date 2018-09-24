package com.company;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Protocol {




    private ApplicationLayer appLayer;
    private Person person;
    private byte [] serializedBytes;
    private boolean hasFinished = false;
    private ProtocolDelegate delegate;

    public Protocol(){


            this.appLayer = new ApplicationLayer();


    }

    public void serialize(String data) {

        serializedBytes = data.getBytes();

    }

    public byte [] getSerializedBytes(){

        return serializedBytes;
    }



    public Object deserialize(){

        byte [] arr = delegate.getPackets();
        Person person = new Person();
        person.deserializeAge(arr);
        person.deserializeNameLength(arr);

        byte [] personByteArray = new byte[person.getNameLength()];
        System.arraycopy(arr,8,personByteArray,0, person.getNameLength());

        try {

            if(person.getName().getBytes("UTF-16").length != person.getNameLength()){
                log("part of data is missing");
            }

            appLayer.write(person);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return person;
    }


    public void setDelegate(ProtocolDelegate delegate){ this.delegate = delegate;}

    public void log(String statment){
        System.out.println(statment);
    }


}
