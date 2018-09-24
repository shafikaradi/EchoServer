package com.company;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Server implements ProtocolDelegate {



    private Selector selector ;
    private ServerSocketChannel ssc;
    private ThreadPool pool;
    private Protocol protocol;
    private int ops;
    private byte [] agePackets;
    private byte [] nameLengthPackets;
    private byte [] namePackets;
    private  byte [] buffHolder;
    private int counter = 0;

    private Server() throws IOException {
        selector = Selector.open();
        ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress("localhost",6000));
        ssc.configureBlocking(false);
        pool = new ThreadPool(3);
        protocol = new Protocol();

        protocol.setDelegate(this);



    }


    public  void processRequest() throws IOException {



             ops = ssc.validOps();

              ssc.register(selector,SelectionKey.OP_ACCEPT);
                while (true) {

                    selector.select();
                    Iterator<SelectionKey> keys =  selector.selectedKeys().iterator();


                    while (keys.hasNext()){

                        SelectionKey key = keys.next();
                        keys.remove();






                                    if (key.isValid() && key.isAcceptable()) {


                                        registerRequest();
                                    }





                        pool.execute(() -> {


                            if(key.isValid() && key.isReadable()){


                                readRequest(key);


                            }



                        });


                        pool.execute(

                                () -> {
                                    if(key.isValid() && key.isWritable()){
                                        sendResponse(key);
                                    }
                                }
                        );

                    }



                }





    }


    private void registerRequest(){

        try {

                SocketChannel client = ssc.accept();
                client.configureBlocking(false);
                client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);


        }catch (IOException e){

            e.printStackTrace();

        }

    }

    private byte [] sendResponse(){


         return protocol.getSerializedBytes();
    }


    private synchronized  void readRequest(SelectionKey key){


        try{

            if(key.channel().isOpen()){

                SocketChannel client = (SocketChannel) key.channel();
                ByteBuffer byteBuffer = ByteBuffer.allocate(512);
                int length = client.read(byteBuffer);




                if (length == -1){
                    log("Nothing more is received");

                    key.cancel();
                    client.close();
                    protocol.deserialize();
                    return;
                }



                fillBufferHolder(byteBuffer);
                byteBuffer.clear();







            }


        }catch (IOException e){
           e.printStackTrace();
        }



    }


    public void sendResponse(SelectionKey key)  {

        SocketChannel client = (SocketChannel) key.channel();

        ByteBuffer buffer = ByteBuffer.allocate(256);
        String response = "Your Request is in progress";
        buffer.put(response.getBytes());


        try {
            buffer.flip();
            client.write(buffer);
            key.cancel();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    private void log(String statment){

     System.out.println(statment);


    }

    public boolean isAcceptable(){

        return  (ops & SelectionKey.OP_ACCEPT) > 0;

    }

    public boolean isReadable(){

        return  (ops & SelectionKey.OP_READ) > 0;

    }

    public boolean isWritable(){

        return  (ops & SelectionKey.OP_WRITE) > 0;

    }

    public static Server getInstance() throws IOException{

        return new Server();
    }

    public void fillBufferHolder(ByteBuffer byteBuffer) {
        byteBuffer.get(buffHolder,counter,byteBuffer.position());
        counter = byteBuffer.position();
        byteBuffer.flip();
    }

    @Override
    public byte[] getPackets() {
        return buffHolder;
    }
}
