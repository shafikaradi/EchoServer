/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author bsc
 */
public class NetworkLayer{
    
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private final ThreadPool pool;
    private int port;
    private final Set <Integer> portSet = new HashSet<>();
    private byte [] packets;
    
   
    
    public NetworkLayer(int port){
        
        tryToCheckPort(port);
        this.tryToOpenSelector();
        this.tryToOpenChannel();
        pool = new ThreadPool(3);
       
      
    }
    
    private void tryToCheckPort(int port){
        try {
            checkPort(port);
        } catch (java.lang.Exception ex) {
            Logger.getLogger(NetworkLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void checkPort(int port) throws Exception{
        
        if(portSet.contains(port)){
            
            System.err.println("Port "+port+" is already in use");
            throw new Exception(String.format("Port %d is already in use", port));
         
        }else{
            
            addPortToSet(port);
        }
            
    }
    
    private void addPortToSet(int port){
        portSet.add(port);
        setPort(port);
    }
    
    private void setPort(int port){
        this.port = port;
    }
    
    
 
    
    private void tryToOpenSelector(){
         try {
           openSelector();
        } catch (IOException ex) {
             System.out.println(ex.getCause());
        }
    }
    
    private void openSelector() throws IOException{
        selector = Selector.open();
    }
    
    private void tryToOpenChannel(){
           try {
            openChannel();
        } catch (IOException ex) {
            System.err.println(ex.getCause());
            System.err.println(ex.getMessage());
        }
    }
    
    private void openChannel() throws IOException{
        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("localhost",port));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }
    
    
    public void startToListen() throws  IOException{
        
        System.out.println("System is starting to listen on port "+serverChannel.getLocalAddress());
        
        
        
       
                
        while(true){
          
           selector.select();
           Set<SelectionKey> selectedKeys = selector.selectedKeys();
           Iterator<SelectionKey> keys = selectedKeys.iterator();
            
            
            while(keys.hasNext()){
              SelectionKey  key = (SelectionKey)keys.next();
                
                
                if(key.isAcceptable()){
                   tryToReceiveRequest();
                }
              
                if(key.isReadable()){
                  tryToListenToClient(key);
                }
                
//                if(key.isWritable()){
//                    
//                
//                 }
                
              
                keys.remove();
                
                
                
            }
            
        }
        
    }
     
     
     private void registerClient() throws IOException{
         
         SocketChannel client = serverChannel.accept();
         client.configureBlocking(false);
         client.register(selector, SelectionKey.OP_READ);
     }
     
    private void tryToReceiveRequest(){
         
        try {
            registerClient();
        } catch (IOException ex) {
           System.out.println(ex.getCause());
        }
         
     }
      
      private void listenToClient(SelectionKey key) throws IOException{
          
          Person person = new Person();
          SocketChannel client = (SocketChannel) key.channel();
          client.register(selector, SelectionKey.OP_READ);
          int total = -1;
          
         
          ByteBuffer buffer = ByteBuffer.allocate(1024);
          total = client.read(buffer);
          
          if(total == -1){
              
              packets = buffer.array();
              
             
//               byte [] ageBytes = new byte[4];
//               byte [] nameLengthBytes = new byte[4];
//               byte [] serailizedName; 
//               
//               System.arraycopy(packets, 0, ageBytes, 0, ageBytes.length);
//               person.deserializeAge(ageBytes);
//                 System.out.println(person.getAge());
//               System.arraycopy(packets, 0, nameLengthBytes, 4, nameLengthBytes.length);
//               person.deserializeNameLength(nameLengthBytes);
//        
//               serailizedName = new byte[person.getNameLength()];
//               
//               System.arraycopy(packets, 0, serailizedName, 8, serailizedName.length);
               key.cancel();
               client.close();
              
          }
          
      }
      
      private void terminateConnectionWithClient( SocketChannel client,String trminateComment) throws IOException{
          if(trminateComment.equals("Bye Bye")){
              
              client.close();
              System.out.println("Connection is terminated");
              
          }
      }
      
      private void tryToListenToClient(SelectionKey key){
          
        try {
            listenToClient(key);
        } catch (IOException ex) {
           System.out.println(ex.getCause());
        }
      }

    
    public byte [] packets() {
       return packets;
    }
    
    
      
   
 
}



