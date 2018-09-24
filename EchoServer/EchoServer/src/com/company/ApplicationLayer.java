package com.company;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ApplicationLayer extends Object{

    private Person person;
    private byte [] packets;
    private byte ops = 0;
    private Path path;


    public final static byte OP_READ = 1 << 0;
    public final static byte OP_WRITE= 1 << 1;


    public ApplicationLayer(){
        path = Paths.get("name.txt");

    }


    public void read(byte [] bytes){
        System.out.println("HI");
        person.setName(new String(bytes));
        System.out.println(String.format("%s", person.getName()));


    }

    private void writeToFile(String data){

        try {
            FileChannel channel = new FileOutputStream(path.toFile(),true).getChannel();

            channel.write(ByteBuffer.wrap(data.getBytes()));

            channel.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized  void write(Person person) throws IOException{




            log(String.format("Person's name is %s and he's %s years old", person.getName(), person.getAge()));
            writeToFile(String.format("Person's name is %s and he's %s years old \n", person.getName(), person.getAge()));





    }




    public void log(String statment){

        System.out.println(statment);
    }

    public void setOperations(byte ops){


        this.ops = ops;

    }

    protected byte getOps(){

        return this.ops;
    }

    public boolean isReadable(){

        return  (ops & OP_READ) > 0;

    }


}
