/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto.util.hash.test;

import crypto.util.hash.Hash;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jlimachi
 */
public class HashTest {

    public HashTest() {
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void hashTest() throws IOException {

        Hash h = Hash.getInstance();
        String versionH = h.generateHash("config.eva", false);
        System.out.println(versionH);

    }
    
    public static void main(String[] args) {
        Hash h = Hash.getInstance();
        String versionH = h.generateHash("123", false);
        System.out.println(versionH);
    }
}
