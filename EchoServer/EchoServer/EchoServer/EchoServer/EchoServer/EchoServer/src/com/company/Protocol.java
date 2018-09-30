package com.company;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

public class Protocol {



    private ArrayList<String> list;
    private ApplicationLayer appLayer;
    private Person person;
    private byte [] serializedBytes;
    private boolean hasFinished = false;

    public Protocol(){


            this.appLayer = new ApplicationLayer();
            list = new ArrayList<>();

    }

    public void serialize(String data) {

        serializedBytes = data.getBytes();

    }

    public byte [] getSerializedBytes(){

        return serializedBytes;
    }



    public void deserialize( byte [] bytes)throws IOException{
        String data = new String(bytes).trim();
        list.add(data);


        if(list.size() == 2){
            appLayer.write(list);
            list.clear();
        }

        if(data.equals("finish")){


            hasFinished = true;
            log("Connection is closed");

        }
    }

    public boolean hasFinished(){

        if(hasFinished)
            return true;
         else
             return false;
    }

    public void log(String statment){
        System.out.println(statment);
    }
}
