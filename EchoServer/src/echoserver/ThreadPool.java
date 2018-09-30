/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver;

import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author bsc
 */
public class ThreadPool{
    
    private int numOfWorkers = 0;
    private Worker [] workers;
    private LinkedBlockingQueue <Runnable>queue;
    private Worker worker;
    
    public ThreadPool(int numOfWorkers){
        
        this.numOfWorkers = numOfWorkers;
        queue = new LinkedBlockingQueue<>();
        workers = new Worker[4];
        this.runAllThreads();
        
        
    }
    
   private void runAllThreads(){
       
       for(int counter = 0; counter < this.numOfWorkers; counter++){
           
          workers[counter] =  new Worker();
          workers[counter].start();
          workers[counter].setName("Thread " + (counter+1));
           
       }
       
   }
    
    public void execute(Runnable task){
        
        synchronized(queue){
            queue.add(task);
            
        }
        
    }
    
    
    
    private class Worker extends Thread{

        private Runnable runnable;
        @Override
        public void run() {
            
           while(true){
               
              runnable = queue.poll();
              
              if(runnable != null){
                  runnable.run();
                  System.out.println(this.getName());
              }
             
              
           }
        }
        
    }
    
}
