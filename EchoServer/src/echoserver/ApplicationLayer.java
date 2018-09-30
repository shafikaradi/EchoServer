/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bsc
 */
public class ApplicationLayer implements ProtocolDelegate{
    
   private NetworkLayer network;
   private Protocol protocol;
   private Person person;
   private NetworkLayer networkLayer;
   private byte [] packets;
   
   public ApplicationLayer(){
       initializeProtocol();
       protocol.setDelegate(this);
       networkLayer = new NetworkLayer(6000);
       
   }
   
   private void initializeProtocol(){
       if(protocol == null)
           protocol = new Protocol();
       
       startLisiting();
   }
   
   private void startLisiting(){
       
       network = new NetworkLayer(5000);
       
   }
   

 

    @Override
    public void setPerson(Person person) {
        this.person = person;
    }
    
    public Person getPerson() {
        return person;
    }
    
    public void listenToNetwork(){
        
       try {
           network.startToListen();
       } catch (IOException ex) {
           System.err.println(ex.getMessage());
       }
    }

    @Override
    public byte[] getPackets() {
        
        
        return networkLayer.packets();
    }
    
    public void publishReceivedData(){
        protocol.writeToAppiclationLayer();
    }
  
    
    
}
