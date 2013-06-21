/*
 * The MIT License
 *
 * Copyright 2013 Oleg Nenashev <o.v.nenashev@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.plugins.tcl;

import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Oleg Nenashev <o.v.nenashev@gmail.com>
 */
@Ignore
public class TclDriverTest {

    public TclDriverTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    /**
     * Test of Execute method, of class TclDriver.
     */
    @Test
    public void testExecute() throws Exception
    {
        System.out.println("Execute");
        String command = "";
        TclDriver instance = null;
        String expResult = "";
        String result = instance.Execute(command);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBuildListener method, of class TclDriver.
     */
    @Test
    public void testGetBuildListener()
    {
        System.out.println("getBuildListener");
        TclDriver instance = null;
        BuildListener expResult = null;
        BuildListener result = instance.getBuildListener();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBuildLauncher method, of class TclDriver.
     */
    @Test
    public void testGetBuildLauncher()
    {
        System.out.println("getBuildLauncher");
        TclDriver instance = null;
        Launcher expResult = null;
        Launcher result = instance.getBuildLauncher();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBuildInfo method, of class TclDriver.
     */
    @Test
    public void testGetBuildInfo()
    {
        System.out.println("getBuildInfo");
        TclDriver instance = null;
        AbstractBuild expResult = null;
        AbstractBuild result = instance.getBuildInfo();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

}