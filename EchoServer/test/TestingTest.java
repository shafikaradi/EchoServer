/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import echoserver.ApplicationLayer;
import echoserver.Person;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author bsc
 */
public class TestingTest {
    
    
    private ApplicationLayer app;
    private Person person;
    public TestingTest() {
  
    }
    
    @BeforeClass
    public void setUpClass() {
         app = new ApplicationLayer();
         
    }
    
    @AfterClass
    public void tearDownClass() {
    }
    
    @Before
    public void setUp() {
      
       app.publishReceivedData();
       person = app.getPerson();
    }
    
    @After
    public void tearDown() {
        app = null;
        person = null;
        System.gc();
    }
    
  
      
      @Test
      public void TestPersonName(){
          
          assertEquals("Shafiq",person.getName());
           
     }  
      
      @Test
      public void TestPersonAge(){
          
          assertEquals(25,person.getAge());
           
     } 
      


    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

   
}
