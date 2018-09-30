/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver;

/**
 *
 * @author bsc
 */
public class Protocol {
    
    
    private ProtocolDelegate delegate;
    public Protocol(){
        
    }
    
    public void writeToAppiclationLayer(){
        
        byte [] packets = delegate.getPackets();
        Person person = new Person();
        
        byte [] ageBytes = new byte[4];
        byte [] nameLengthBytes = new byte[4];
        byte [] serailizedName; 
        System.arraycopy(packets, 0, ageBytes, 0, ageBytes.length);
        person.deserializeAge(ageBytes);
        
        System.arraycopy(packets, 0, packets, 4, nameLengthBytes.length);
        person.deserializeNameLength(ageBytes);
        
        serailizedName = new byte[person.getNameLength()];
        System.arraycopy(packets, 0, serailizedName, 8, serailizedName.length);
        
        
        
        
        delegate.setPerson(person);
        
    }
 
    
    public void setDelegate(ProtocolDelegate delegate){ this.delegate = delegate;}
    
}
