/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.configuration;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.StringTokenizer;

import junit.framework.TestCase;
import junitx.framework.ListAssert;
import junitx.framework.ObjectAssert;

/**
 * Tests some basic functions of the BaseConfiguration class. Missing keys will
 * throw Exceptions
 *
 * @version $Id$
 */
public class TestBaseConfiguration extends TestCase
{
    protected BaseConfiguration config = null;

    protected static Class missingElementException = NoSuchElementException.class;
    protected static Class incompatibleElementException = ConversionException.class;

    protected void setUp() throws Exception
    {
        config = new BaseConfiguration();
        config.setThrowExceptionOnMissing(true);
    }

    public void testThrowExceptionOnMissing()
    {
        assertTrue("Throw Exception Property is not set!", config.isThrowExceptionOnMissing());
    }

    public void testGetProperty()
    {
        /* should be empty and return null */
        assertEquals("This returns null", config.getProperty("foo"), null);

        /* add a real value, and get it two different ways */
        config.setProperty("number", "1");
        assertEquals("This returns '1'", config.getProperty("number"), "1");
        assertEquals("This returns '1'", config.getString("number"), "1");
    }

    public void testGetByte()
    {
        config.setProperty("number", "1");
        byte oneB = 1;
        byte twoB = 2;
        assertEquals("This returns 1(byte)", oneB, config.getByte("number"));
        assertEquals("This returns 1(byte)", oneB, config.getByte("number", twoB));
        assertEquals("This returns 2(default byte)", twoB, config.getByte("numberNotInConfig", twoB));
        assertEquals("This returns 1(Byte)", new Byte(oneB), config.getByte("number", new Byte("2")));

        // missing key without default value
        Throwable t = null;
        try
        {
            config.getByte("numberNotInConfig");
        }
        catch (Throwable T)
        {
            t = T;
        }
        assertNotNull("No exception thrown for missing keys", t);
        ObjectAssert.assertInstanceOf("Exception thrown for missing keys", missingElementException, t);

        // existing key with an incompatible value
        config.setProperty("test.empty", "");
        t = null;
        try
        {
            config.getByte("test.empty");
        }
        catch (Throwable T)
        {
            t = T;
        }
        assertNotNull("No exception thrown for incompatible values", t);
        ObjectAssert.assertInstanceOf("Exception thrown for incompatible values", incompatibleElementException, t);
    }

    public void testGetShort()
    {
        config.setProperty("numberS", "1");
        short oneS = 1;
        short twoS = 2;
        assertEquals("This returns 1(short)", oneS, config.getShort("numberS"));
        assertEquals("This returns 1(short)", oneS, config.getShort("numberS", twoS));
        assertEquals("This returns 2(default short)", twoS, config.getShort("numberNotInConfig", twoS));
        assertEquals("This returns 1(Short)", new Short(oneS), config.getShort("numberS", new Short("2")));

        // missing key without default value
        Throwable t = null;
        try
        {
            config.getShort("numberNotInConfig");
        }
        catch (Throwable T)
        {
            t = T;
        }
        assertNotNull("No exception thrown for missing keys", t);
        ObjectAssert.assertInstanceOf("Exception thrown for missing keys", missingElementException, t);

        // existing key with an incompatible value
        config.setProperty("test.empty", "");
        t = null;
        try
        {
            config.getShort("test.empty");
        }
        catch (Throwable T)
        {
            t = T;
        }
        assertNotNull("No exception thrown for incompatible values", t);
        ObjectAssert.assertInstanceOf("Exception thrown for incompatible values", incompatibleElementException, t);
    }

    public void testGetLong()
    {
        config.setProperty("numberL", "1");
        long oneL = 1;
        long twoL = 2;
        assertEquals("This returns 1(long)", oneL, config.getLong("numberL"));
        assertEquals("This returns 1(long)", oneL, config.getLong("numberL", twoL));
        assertEquals("This returns 2(default long)", twoL, config.getLong("numberNotInConfig", twoL));
        assertEquals("This returns 1(Long)", new Long(oneL), config.getLong("numberL", new Long("2")));

        // missing key without default value
        Throwable t = null;
        try
        {
            config.getLong("numberNotInConfig");
        }
        catch (Throwable T)
        {
            t = T;
        }
        assertNotNull("No exception thrown for missing keys", t);
        ObjectAssert.assertInstanceOf("Exception thrown for missing keys", missingElementException, t);

        // existing key with an incompatible value
        config.setProperty("test.empty", "");
        t = null;
        try
        {
            config.getLong("test.empty");
        }
        catch (Throwable T)
        {
            t = T;
        }
        assertNotNull("No exception thrown for incompatible values", t);
        ObjectAssert.assertInstanceOf("Exception thrown for incompatible values", incompatibleElementException, t);
    }

    public void testGetFloat()
    {
        config.setProperty("numberF", "1.0");
        float oneF = 1;
        float twoF = 2;
        assertEquals("This returns 1(float)", oneF, config.getFloat("numberF"), 0);
        assertEquals("This returns 1(float)", oneF, config.getFloat("numberF", twoF), 0);
        assertEquals("This returns 2(default float)", twoF, config.getFloat("numberNotInConfig", twoF), 0);
        assertEquals("This returns 1(Float)", new Float(oneF), config.getFloat("numberF", new Float("2")));

        // missing key without default value
        Throwable t = null;
        try
        {
            config.getFloat("numberNotInConfig");
        }
        catch (Throwable T)
        {
            t = T;
        }
        assertNotNull("No exception thrown for missing keys", t);
        ObjectAssert.assertInstanceOf("Exception thrown for missing keys", missingElementException, t);

        // existing key with an incompatible value
        config.setProperty("test.empty", "");
        t = null;
        try
        {
            config.getFloat("test.empty");
        }
        catch (Throwable T)
        {
            t = T;
        }
        assertNotNull("No exception thrown for incompatible values", t);
        ObjectAssert.assertInstanceOf("Exception thrown for incompatible values", incompatibleElementException, t);
    }

    public void testGetDouble()
    {
        config.setProperty("numberD", "1.0");
        double oneD = 1;
        double twoD = 2;
        assertEquals("This returns 1(double)", oneD, config.getDouble("numberD"), 0);
        assertEquals("This returns 1(double)", oneD, config.getDouble("numberD", twoD), 0);
        assertEquals("This returns 2(default double)", twoD, config.getDouble("numberNotInConfig", twoD), 0);
        assertEquals("This returns 1(Double)", new Double(oneD), config.getDouble("numberD", new Double("2")));

        // missing key without default value
        Throwable t = null;
        try
        {
            config.getDouble("numberNotInConfig");
        }
        catch (Throwable T)
        {
            t = T;
        }
        assertNotNull("No exception thrown for missing keys", t);
        ObjectAssert.assertInstanceOf("Exception thrown for missing keys", missingElementException, t);

        // existing key with an incompatible value
        config.setProperty("test.empty", "");
        t = null;
        try
        {
            config.getDouble("test.empty");
        }
        catch (Throwable T)
        {
            t = T;
        }
        assertNotNull("No exception thrown for incompatible values", t);
        ObjectAssert.assertInstanceOf("Exception thrown for incompatible values", incompatibleElementException, t);
    }

    public void testGetBigDecimal()
    {
        config.setProperty("numberBigD", "123.456");
        BigDecimal number = new BigDecimal("123.456");
        BigDecimal defaultValue = new BigDecimal("654.321");

        assertEquals("Existing key", number, config.getBigDecimal("numberBigD"));
        assertEquals("Existing key with default value", number, config.getBigDecimal("numberBigD", defaultValue));
        assertEquals("Missing key with default value", defaultValue, config.getBigDecimal("numberNotInConfig", defaultValue));

        // missing key without default value
        Throwable t = null;
        try
        {
            config.getBigDecimal("numberNotInConfig");
        }
        catch (Throwable T)
        {
            t = T;
        }
        assertNotNull("No exception thrown for missing keys", t);
        ObjectAssert.assertInstanceOf("Exception thrown for missing keys", missingElementException, t);

        // existing key with an incompatible value
        config.setProperty("test.empty", "");
        t = null;
        try
        {
            config.getBigDecimal("test.empty");
        }
        catch (Throwable T)
        {
            t = T;
        }
        assertNotNull("No exception thrown for incompatible values", t);
        ObjectAssert.assertInstanceOf("Exception thrown for incompatible values", incompatibleElementException, t);
    }

    public void testGetBigInteger()
    {
        config.setProperty("numberBigI", "1234567890");
        BigInteger number = new BigInteger("1234567890");
        BigInteger defaultValue = new BigInteger("654321");

        assertEquals("Existing key", number, config.getBigInteger("numberBigI"));
        assertEquals("Existing key with default value", number, config.getBigInteger("numberBigI", defaultValue));
        assertEquals("Missing key with default value", defaultValue, config.getBigInteger("numberNotInConfig", defaultValue));

        // missing key without default value
        Throwable t = null;
        try
        {
            config.getBigInteger("numberNotInConfig");
        }
        catch (Throwable T)
        {
            t = T;
        }
        assertNotNull("No exception thrown for missing keys", t);
        ObjectAssert.assertInstanceOf("Exception thrown for missing keys", missingElementException, t);

        // existing key with an incompatible value
        config.setProperty("test.empty", "");
        t = null;
        try
        {
            config.getBigInteger("test.empty");
        }
        catch (Throwable T)
        {
            t = T;
        }
        assertNotNull("No exception thrown for incompatible values", t);
        ObjectAssert.assertInstanceOf("Exception thrown for incompatible values", incompatibleElementException, t);
    }

    public void testGetString()
    {
        config.setProperty("testString", "The quick brown fox");
        String string = "The quick brown fox";
        String defaultValue = "jumps over the lazy dog";

        assertEquals("Existing key", string, config.getString("testString"));
        assertEquals("Existing key with default value", string, config.getString("testString", defaultValue));
        assertEquals("Missing key with default value", defaultValue, config.getString("stringNotInConfig", defaultValue));

        // missing key without default value
        Throwable t = null;
        try
        {
            config.getString("stringNotInConfig");
        }
        catch (Throwable T)
        {
            t = T;
        }
        assertNotNull("No exception thrown for missing keys", t);
        ObjectAssert.assertInstanceOf("Exception thrown for missing keys", missingElementException, t);
    }

    public void testGetBoolean()
    {
        config.setProperty("boolA", Boolean.TRUE);
        boolean boolT = true, boolF = false;
        assertEquals("This returns true", boolT, config.getBoolean("boolA"));
        assertEquals("This returns true, not the default", boolT, config.getBoolean("boolA", boolF));
        assertEquals("This returns false(default)", boolF, config.getBoolean("boolNotInConfig", boolF));
        assertEquals("This returns true(Boolean)", new Boolean(boolT), config.getBoolean("boolA", new Boolean(boolF)));

        // missing key without default value
        Throwable t = null;
        try
        {
            config.getBoolean("numberNotInConfig");
        }
        catch (Throwable T)
        {
            t = T;
        }
        assertNotNull("No exception thrown for missing keys", t);
        ObjectAssert.assertInstanceOf("Exception thrown for missing keys", missingElementException, t);

        // existing key with an incompatible value
        config.setProperty("test.empty", "");
        t = null;
        try
        {
            config.getBoolean("test.empty");
        }
        catch (Throwable T)
        {
            t = T;
        }
        assertNotNull("No exception thrown for incompatible values", t);
        ObjectAssert.assertInstanceOf("Exception thrown for incompatible values", incompatibleElementException, t);
    }

    public void testGetList()
    {
        config.addProperty("number", "1");
        config.addProperty("number", "2");
        List list = config.getList("number");
        assertNotNull("The list is null", list);
        assertEquals("List size", 2, list.size());
        assertTrue("The number 1 is missing from the list", list.contains("1"));
        assertTrue("The number 2 is missing from the list", list.contains("2"));

        /*
         *  now test dan's new fix where we get the first scalar
         *  when we access a list valued property
         */
        try
        {
            config.getString("number");
        }
        catch (NoSuchElementException nsse)
        {
            fail("Should return a string");
        }
    }

    public void testGetInterpolatedList()
    {
        config.addProperty("number", "1");
        config.addProperty("array", "${number}");
        config.addProperty("array", "${number}");

        List list = new ArrayList();
        list.add("1");
        list.add("1");

        ListAssert.assertEquals("'array' property", list, config.getList("array"));
    }

    public void testCommaSeparatedString()
    {
        String prop = "hey, that's a test";
        config.setProperty("prop.string", prop);
        try
        {
            config.getList("prop.string");
        }
        catch (NoSuchElementException nsse)
        {
            fail("Should return a list");
        }

        String prop2 = "hey\\, that's a test";
        config.clearProperty("prop.string");
        config.setProperty("prop.string", prop2);
        try
        {
            config.getString("prop.string");
        }
        catch (NoSuchElementException nsse)
        {
            fail("Should return a list");
        }

    }

    public void testAddProperty() throws Exception
    {
        Collection props = new ArrayList();
        props.add("one");
        props.add("two,three,four");
        props.add(new String[] { "5.1", "5.2", "5.3,5.4", "5.5" });
        props.add("six");
        config.addProperty("complex.property", props);

        Object val = config.getProperty("complex.property");
        assertTrue(val instanceof Collection);
        Collection col = (Collection) val;
        assertEquals(10, col.size());

        props = new ArrayList();
        props.add("quick");
        props.add("brown");
        props.add("fox,jumps");
        Object[] data = new Object[] {
                "The", props, "over,the", "lazy", "dog."
        };
        config.setProperty("complex.property", data);
        val = config.getProperty("complex.property");
        assertTrue(val instanceof Collection);
        col = (Collection) val;
        Iterator it = col.iterator();
        StringTokenizer tok = new StringTokenizer("The quick brown fox jumps over the lazy dog.", " ");
        while(tok.hasMoreTokens())
        {
            assertTrue(it.hasNext());
            assertEquals(tok.nextToken(), it.next());
        }
        assertFalse(it.hasNext());

        config.setProperty("complex.property", null);
        assertFalse(config.containsKey("complex.property"));
    }

    public void testPropertyAccess()
    {
        config.clearProperty("prop.properties");
        config.setProperty("prop.properties", "");
        assertEquals(
                "This returns an empty Properties object",
                config.getProperties("prop.properties"),
                new Properties());
        config.clearProperty("prop.properties");
        config.setProperty("prop.properties", "foo=bar, baz=moo, seal=clubber");

        Properties p = new Properties();
        p.setProperty("foo", "bar");
        p.setProperty("baz", "moo");
        p.setProperty("seal", "clubber");
        assertEquals(
                "This returns a filled in Properties object",
                config.getProperties("prop.properties"),
                p);
    }

    public void testSubset()
    {
        /*
         * test subset : assure we don't reprocess the data elements
         * when generating the subset
         */

        String prop = "hey, that's a test";
        String prop2 = "hey\\, that's a test";
        config.setProperty("prop.string", prop2);
        config.setProperty("property.string", "hello");

        Configuration subEprop = config.subset("prop");

        assertEquals(
                "Returns the full string",
                prop,
                subEprop.getString("string"));
        try
        {
            subEprop.getString("string");
        }
        catch (NoSuchElementException nsse)
        {
            fail("Should return a string");
        }
        try
        {
            subEprop.getList("string");
        }
        catch (NoSuchElementException nsse)
        {
            fail("Should return a list");
        }

        Iterator it = subEprop.getKeys();
        it.next();
        assertFalse(it.hasNext());

        subEprop = config.subset("prop.");
        it = subEprop.getKeys();
        assertFalse(it.hasNext());
    }

    public void testInterpolation() throws Exception
    {
        config.setProperty("applicationRoot", "/home/applicationRoot");
        config.setProperty("db", "${applicationRoot}/db/hypersonic");
        String unInterpolatedValue = "${applicationRoot2}/db/hypersonic";
        config.setProperty("dbFailedInterpolate", unInterpolatedValue);
        String dbProp = "/home/applicationRoot/db/hypersonic";

        //construct a new config, using config as the defaults config for it.
        BaseConfiguration superProp = config;

        assertEquals(
                "Checking interpolated variable", dbProp,
                superProp.getString("db"));
        assertEquals(
                "lookup fails, leave variable as is",
                superProp.getString("dbFailedInterpolate"),
                unInterpolatedValue);

        superProp.setProperty("arrayInt", "${applicationRoot}/1");
        String[] arrayInt = superProp.getStringArray("arrayInt");
        assertEquals(
                "check first entry was interpolated",
                "/home/applicationRoot/1",
                arrayInt[0]);

        config.addProperty("path", "/temp,C:\\Temp,/usr/local/tmp");
        config.setProperty("path.current", "${path}");
        assertEquals("Interpolation with multi-valued property", "/temp", superProp.getString("path.current"));
    }

    public void testMultipleInterpolation() throws Exception
    {
        config.setProperty("test.base-level", "/base-level");
        config.setProperty("test.first-level", "${test.base-level}/first-level");
        config.setProperty(
                "test.second-level",
                "${test.first-level}/second-level");
        config.setProperty(
                "test.third-level",
                "${test.second-level}/third-level");

        String expectedValue =
                "/base-level/first-level/second-level/third-level";

        assertEquals(config.getString("test.third-level"), expectedValue);
    }

    public void testInterpolationLoop() throws Exception
    {
        config.setProperty("test.a", "${test.b}");
        config.setProperty("test.b", "${test.a}");

        try
        {
            config.getString("test.a");
        }
        catch (IllegalStateException e)
        {
            return;
        }

        fail("IllegalStateException should have been thrown for looped property references");
    }

    public void testGetHexadecimalValue()
    {
        config.setProperty("number", "0xFF");
        assertEquals("byte value", (byte) 0xFF, config.getByte("number"));

        config.setProperty("number", "0xFFFF");
        assertEquals("short value", (short) 0xFFFF, config.getShort("number"));

        config.setProperty("number", "0xFFFFFFFF");
        assertEquals("int value", 0xFFFFFFFF, config.getInt("number"));

        config.setProperty("number", "0xFFFFFFFFFFFFFFFF");
        assertEquals("long value", 0xFFFFFFFFFFFFFFFFL, config.getLong("number"));

        assertEquals("long value", 0xFFFFFFFFFFFFFFFFL, config.getBigInteger("number").longValue());
    }

    public void testResolveContainerStore()
    {
        AbstractConfiguration config = new BaseConfiguration();

        // array of objects
        config.addPropertyDirect("array", new String[] { "foo", "bar" });

        assertEquals("first element of the 'array' property", "foo", config.resolveContainerStore("array"));

        // list of objects
        List list = new ArrayList();
        list.add("foo");
        list.add("bar");
        config.addPropertyDirect("list", list);

        assertEquals("first element of the 'list' property", "foo", config.resolveContainerStore("list"));

        // arrays of primitives
        config.addPropertyDirect("array.boolean", new boolean[] { true, false });
        assertEquals("first element of the 'array.boolean' property", true, config.getBoolean("array.boolean"));

        config.addPropertyDirect("array.byte", new byte[] { 1, 2 });
        assertEquals("first element of the 'array.byte' property", 1, config.getByte("array.byte"));

        config.addPropertyDirect("array.short", new short[] { 1, 2 });
        assertEquals("first element of the 'array.short' property", 1, config.getShort("array.short"));

        config.addPropertyDirect("array.int", new int[] { 1, 2 });
        assertEquals("first element of the 'array.int' property", 1, config.getInt("array.int"));

        config.addPropertyDirect("array.long", new long[] { 1, 2 });
        assertEquals("first element of the 'array.long' property", 1, config.getLong("array.long"));

        config.addPropertyDirect("array.float", new float[] { 1, 2 });
        assertEquals("first element of the 'array.float' property", 1, config.getFloat("array.float"), 0);

        config.addPropertyDirect("array.double", new double[] { 1, 2 });
        assertEquals("first element of the 'array.double' property", 1, config.getDouble("array.double"), 0);
    }
}
