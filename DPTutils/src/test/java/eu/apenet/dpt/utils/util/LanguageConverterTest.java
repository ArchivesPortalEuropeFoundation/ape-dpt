/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dpt.utils.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author papp
 */
public class LanguageConverterTest {
    
    public LanguageConverterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of get639_1Code method, of class LanguageConverter.
     */
    @Test
    public void testGet639_1Code() {
        System.out.println("get639_1Code");
        String lang639_2Code = "deu";
        String expResult = "de";
        String result = LanguageConverter.get639_1Code(lang639_2Code);
        assertEquals(expResult, result);
        System.out.println("get639_1Code");
        lang639_2Code = "ger";
        expResult = "de";
        result = LanguageConverter.get639_1Code(lang639_2Code);
        assertEquals(expResult, result);
    }
}