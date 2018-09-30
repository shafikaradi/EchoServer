package com.company;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class NetworkLayer{


    private Selector selector;
    private Person person;
    private ThreadPool threads;
    private SocketChannel socketChannel;
    private byte ops;
    private ByteBuffer buffer;
    private int bufferLength;
    private byte[] packets;

    private NetworkLayer(){

        threads = new ThreadPool(3);

    }

    public void processRequest(){


        try{

            this.selector = Selector.open();

            ServerSocketChannel  serverSocketChannel = ServerSocketChannel.open();

            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress("localhost",4050));
            serverSocketChannel.register(this.selector,SelectionKey.OP_ACCEPT);

          //  person = new PersonBuilder().createPerson();

            while(true){

                selector.select();
                Iterator keys = selector.selectedKeys().iterator();

                while(keys.hasNext()) {

                    SelectionKey key = (SelectionKey) keys.next();
                    keys.remove();


                    ops = (byte) key.readyOps();

                    threads.execute(()-> {
                        if (this.isAcceptable()) {

                            accept(key);
                        }
                    });

                    threads.execute(()-> {
                                if (this.isReadable()) {
                                    read(key);
                                }
                    });


                    threads.execute(()->{

                        if (this.isWritable()) {
                          write(key, String.format("%s 's data is received",person.getName()).getBytes());
                          }
                    });
                }




            }

        }catch (IOException e){

        }


    }


    public byte[] getPackets(){


       return packets;

    }

    public int getLengthOfBuffer(){


        return bufferLength;
    }

    private synchronized void read(SelectionKey key){


        try {

            socketChannel = (SocketChannel) key.channel();

            buffer = ByteBuffer.allocate(1024);
            bufferLength = socketChannel.read(buffer);

            buffer.flip();
            packets = new byte[bufferLength];

            while (buffer.hasRemaining()) {

                buffer.get(packets);

            }

            socketChannel.register(selector,SelectionKey.OP_WRITE);
            key.interestOps(key.interestOps() | SelectionKey.OP_READ | SelectionKey.OP_WRITE);

        }catch (IOException io){
            io.getCause();
        }


    }



    private synchronized void accept(SelectionKey key) {

        try{
            ServerSocketChannel channel = (ServerSocketChannel) key.channel();
            SocketChannel sc = channel.accept();
            sc.configureBlocking(false);



           key = sc.register(this.selector,SelectionKey.OP_READ);
           key.interestOps(key.interestOps()| key.interestOps() | SelectionKey.OP_READ);


        }catch (IOException io){
            io.getCause();
        }

    }

    private void write(SelectionKey key, byte [] strBytes)  {

        try {
            SocketChannel socketChannel = (SocketChannel) key.channel();

            byte [] data = strBytes;
            socketChannel.write(ByteBuffer.wrap(data));



        }catch (IOException io){

        }




    }

    public boolean isWritable(){

        return  (ops & SelectionKey.OP_WRITE) > 0;
    }

    public boolean isReadable(){

        return (byte) (ops & SelectionKey.OP_READ) > 0;
    }

    public boolean isAcceptable(){

        return (byte) (ops & SelectionKey.OP_ACCEPT ) > 0;
    }

    public boolean isConnectable(){

        return (byte) (ops  & SelectionKey.OP_CONNECT) > 0;
    }

    public static NetworkLayer getInstance(){


        return new NetworkLayer();
    }

}
