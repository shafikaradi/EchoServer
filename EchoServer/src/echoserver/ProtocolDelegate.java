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
public interface ProtocolDelegate {
    
    public void setPerson(Person person);
    public byte[] getPackets();
    
}
