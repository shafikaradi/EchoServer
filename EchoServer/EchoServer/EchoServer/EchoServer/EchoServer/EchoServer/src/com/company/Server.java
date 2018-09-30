package com.company;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Server {



    private Selector selector ;
    private ServerSocketChannel ssc;

    private ThreadPool pool;
    private Protocol protocol;

    private int ops;



    private Server() throws IOException {
        selector = Selector.open();
        ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress("localhost",6000));
        ssc.configureBlocking(false);
        pool = new ThreadPool(3);
        protocol = new Protocol();



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






                                    if (key.isAcceptable()) {


                                        registerRequest();
                                    }





                        pool.execute(() -> {


                            if(key.isReadable()){


                                answerClient(key);


                            }



                        });

                    }



                }





    }


    private void registerRequest(){

        try {

                SocketChannel client = ssc.accept();
                client.configureBlocking(false);
                client.register(selector, SelectionKey.OP_READ);

        }catch (IOException e){

            e.printStackTrace();

        }

    }

    private byte [] sendResponse(){


         return protocol.getSerializedBytes();
    }


    private synchronized  void answerClient(SelectionKey key){


        try{

            SocketChannel client = (SocketChannel) key.channel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(256);
            client.read(byteBuffer);
            byteBuffer.flip();


            byte [] bytes = byteBuffer.array();



              protocol.deserialize(bytes);

              closeConnection(client);





        }catch (IOException e){
           e.printStackTrace();
        }



    }

    public void closeConnection( SocketChannel channel) throws  IOException{

        if(protocol.hasFinished())
          channel.close();

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




}
