package org.apache.commons.configuration;
/*
 * Copyright 2001-2004 The Apache Software Foundation.
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This Configuration class allows you to interface with a JNDI datasource.
 * 
 * @version $Id: JNDIConfiguration.java,v 1.8 2004/03/09 10:31:31 epugh Exp $
 */
public class JNDIConfiguration
    extends BaseConfiguration
    implements Configuration
{
    private static Log log = LogFactory.getLog(JNDIConfiguration.class);
    private String prefix;
    private Context envCtx;
    private List clearedProperties = new ArrayList();
    /**
     * Creates an empty JNDIConfiguration object which can then
     * be added some other Configuration files
     */
    public JNDIConfiguration()
    {
    }
    /**
     * JNDIConfigurations can not be added to
     *
     * @param key The Key to add the property to.
     * @param token The Value to add.
     */
    public void addProperty(String key, Object token)
    {
        throw new Error("This operation is not supported");
    }
    /**
     * This method recursive traverse the JNDI tree, looking for Context objects.
     * When it finds them, it traverses them as well.  Otherwise it just adds the
     * values to the list of keys found.
     * @param keys All the keys that have been found.
     * @param enum An enumeration of all the elements found at a specific context
     * @param key What key we are building on.
     * @throws NamingException If JNDI has an issue.
     */
    private void recursiveGetKeys(
        List keys,
        NamingEnumeration enum,
        String key)
        throws NamingException
    {
        while (enum.hasMoreElements())
        {
            Binding binding = (Binding) enum.next();
            StringBuffer newKey = new StringBuffer();
            newKey.append(key);
            if (newKey.length() > 0)
            {
                newKey.append(".");
            }
            newKey.append(binding.getName());
            if (binding.getObject() instanceof Context)
            {
                Context c = (Context) binding.getObject();
                NamingEnumeration enum2 = c.listBindings("");
                recursiveGetKeys(keys, enum2, newKey.toString());
            }
            else
            {
                if (!keys.contains(newKey.toString()))
                {
                    keys.add(newKey.toString());
                }
            }
        }
    }
    /**
     * Get the list of the keys contained in the configuration
     * repository.
     *
     * @return An Iterator.
     */
    public Iterator getKeys()
    {
        return getKeys("");
    }
    /**
     * Get the list of the keys contained in the configuration
     * repository that match a passed in beginning pattern.
     *
     * @param key the key pattern to match on.
     * @return An Iterator.
     */
    public Iterator getKeys(String key)
    {
        List keys = new ArrayList();
        try
        {
            String[] splitKeys = StringUtils.split(key, ".");
            for (int i = 0; i < splitKeys.length; i++)
            {
                keys.add(splitKeys[i]);
            }
            Context context = null;
            if (keys.size() == 0)
            {
                context = getContext();
            }
            else
            {
                context =
                    getStartingContextPoint(
                        keys,
                        getContext().listBindings(""));
            }
            if (context != null)
            {
                NamingEnumeration enum2 = context.list("");
                for(;enum2.hasMore();){
                    System.out.println(enum2.next());
                }
                NamingEnumeration enum = context.listBindings("");
                for(;enum.hasMore();){
                    System.out.println(enum.next());
                }
                recursiveGetKeys(keys, enum, key);
            }
        }
        catch (NamingException ne)
        {
            log.warn(ne);
        }
        return keys.iterator();
    }
    /**
     * Because JNDI is based on a tree configuration, we need to filter down the
     * tree, till we find the Context specified by the key to start from.  
     * Otherwise return null.
     * 
     * @param The key (or name) of the Context we are looking to start from.
     * @return The context at that key's location in the JNDI tree, or null if not found
     * @throws NamingException if JNDI has an issue
     */
    private Context getStartingContextPoint(List keys, NamingEnumeration enum)
        throws NamingException
    {
        String keyToSearchFor = (String) keys.get(0);
        log.debug("Key to search for is " + keyToSearchFor);
        while (enum.hasMoreElements())
        {
            Binding binding = (Binding) enum.next();
            log.debug(
                "Binding for name: "
                    + binding.getName()
                    + ", object:"
                    + binding.getObject()
                    + ", class:"
                    + binding.getClassName());
            if (binding.getObject() instanceof Context
                && binding.getName().equals(keyToSearchFor))
            {
                keys.remove(0);
                Context c = (Context) binding.getObject();
                if (keys.size() > 0)
                {
                    return getStartingContextPoint(keys, c.listBindings(""));
                }
                else
                {
                    return c;
                }
            }
        }
        return null;
    }
    /**
     * Get a list of properties associated with the given
     * configuration key.
     *
     * @param key The configuration key.
     * @return The associated properties if key is found.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a String/List.
     * @throws IllegalArgumentException if one of the tokens is
     * malformed (does not contain an equals sign).
     * @see #getProperties(String, Properties)
     */
    public Properties getProperties(String key)
    {
        throw new Error("This operation is not supported");
    }
    public boolean isEmpty()
    {
        try
        {
            NamingEnumeration enum = getContext().listBindings("");
            return !enum.hasMore();
        }
        catch (NamingException ne)
        {
            log.warn(ne);
            return true;
        }
    }
    /**
     *  Gets a property from the configuration.
     *
     *  @param key property to retrieve
     *  @return value as object. Will return user value if exists,
     *          if not then default value if exists, otherwise null
     */
    public Object getProperty(String key)
    {
        return getValueFromJNDI(key);
    }
    /**
     * Set a property, this will replace any previously
     * set values. Set values is implicitly a call
     * to clearProperty(key), addProperty(key,value).
     *
     * @param key
     * @param value
     */
    public void setProperty(String key, Object value)
    {
        throw new Error("This operation is not supported");
    }
    /**
     * Clear a property in the configuration.  Just marks it as cleared, 
     * doesn't change the underlying JNDI data source.
     *
     * @param key the key to remove along with corresponding value.
     */
    public void clearProperty(String key)
    {
        if (!clearedProperties.contains(key))
        {
            clearedProperties.add(key);
        }
    }
    /**
     * check if the configuration contains the key, or the key 
     * has been removed.
     */
    public boolean containsKey(String key)
    {
        if (clearedProperties.contains(key))
        {
            return false;
        }
        key = StringUtils.replace(key, ".", "/");
        try
        {
        	// throws a NamingException if JNDI doesn't contain the key.
            getContext().lookup(key);
            return true;
        }
        catch (javax.naming.NamingException ne)
        {
            return false;
        }
    }
    
    /**
     * Get a boolean associated with the given configuration key.
     *
     * @param key The configuration key.
     * @param defaultValue The default value.
     * @return The associated boolean if key is found and has valid
     * format, default value otherwise.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Boolean.
     */
    public Boolean getBoolean(String key, Boolean defaultValue)
    {
        Object value = getValueFromJNDI(key);
        if (value instanceof Boolean)
        {
            return (Boolean) value;
        }
        else if (value instanceof String)
        {
            return BooleanUtils.toBooleanObject((String) value);
        }
        else if (value == null)
        {
            return defaultValue;
        }
        else
        {
            throw new ClassCastException(
                '\'' + key + "' doesn't map to a Boolean object");
        }
    }
    
    /**
     * Get a byte associated with the given configuration key.
     *
     * @param key The configuration key.
     * @param defaultValue The default value.
     * @return The associated byte if key is found and has valid format, default
     *         value otherwise.
     * @throws ClassCastException is thrown if the key maps to an object that
     *            is not a Byte.
     * @throws NumberFormatException is thrown if the value mapped by the key
     *            has not a valid number format.
     */
    public Byte getByte(String key, Byte defaultValue)
    {
        Object value = getValueFromJNDI(key);
        if (value instanceof Byte)
        {
            return (Byte) value;
        }
        else if (value instanceof String)
        {
            Byte b = new Byte((String) value);
            return b;
        }
        else if (value == null)
        {
            return defaultValue;
        }
        else
        {
            throw new ClassCastException(
                '\'' + key + "' doesn't map to a Byte object");
        }
    }
    
    /**
     * Get a double associated with the given configuration key.
     *
     * @param key The configuration key.
     * @param defaultValue The default value.
     * @return The associated double if key is found and has valid
     * format, default value otherwise.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Double.
     * @throws NumberFormatException is thrown if the value mapped
     * by the key has not a valid number format.
     */
    public Double getDouble(String key, Double defaultValue)
    {
        Object value = this.getValueFromJNDI(key);
        if (value instanceof Double)
        {
            return (Double) value;
        }
        else if (value instanceof String)
        {
            Double d = new Double((String) value);
            return d;
        }
        else if (value == null)
        {
            return defaultValue;
        }
        else
        {
            throw new ClassCastException(
                '\'' + key + "' doesn't map to a Double object");
        }
    }
    
    /**
     * Get a float associated with the given configuration key.
     *
     * @param key The configuration key.
     * @param defaultValue The default value.
     * @return The associated float if key is found and has valid
     * format, default value otherwise.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Float.
     * @throws NumberFormatException is thrown if the value mapped
     * by the key has not a valid number format.
     */
    public Float getFloat(String key, Float defaultValue)
    {
        Object value = getValueFromJNDI(key);
        if (value instanceof Float)
        {
            return (Float) value;
        }
        else if (value instanceof String)
        {
            Float f = new Float((String) value);
            return f;
        }
        else if (value == null)
        {
            return defaultValue;
        }
        else
        {
            throw new ClassCastException(
                '\'' + key + "' doesn't map to a Float object");
        }
    }
    
    /**
     * Get a int associated with the given configuration key.
     *
     * @param key The configuration key.
     * @param defaultValue The default value.
     * @return The associated int if key is found and has valid format, default
     *         value otherwise.
     * @throws ClassCastException is thrown if the key maps to an object that
     *         is not a Integer.
     * @throws NumberFormatException is thrown if the value mapped by the key
     *         has not a valid number format.
     */
    public Integer getInteger(String key, Integer defaultValue)
    {
        Object value = getValueFromJNDI(key);
        if (value instanceof Integer)
        {
            return (Integer) value;
        }
        else if (value instanceof String)
        {
            Integer i = new Integer((String) value);
            return i;
        }
        else if (value == null)
        {
            return defaultValue;
        }
        else
        {
            throw new ClassCastException(
                '\'' + key + "' doesn't map to a Integer object");
        }
    }
    
    /**
     * Get a long associated with the given configuration key.
     *
     * @param key The configuration key.
     * @param defaultValue The default value.
     * @return The associated long if key is found and has valid
     * format, default value otherwise.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Long.
     * @throws NumberFormatException is thrown if the value mapped
     * by the key has not a valid number format.
     */
    public Long getLong(String key, Long defaultValue)
    {
        Object value = getValueFromJNDI(key);
        if (value instanceof Long)
        {
            return (Long) value;
        }
        else if (value instanceof String)
        {
            Long l = new Long((String) value);
            return l;
        }
        else if (value == null)
        {
            return defaultValue;
        }
        else
        {
            throw new ClassCastException(
                '\'' + key + "' doesn't map to a Long object");
        }
    }
    
    /**
     * Get a short associated with the given configuration key.
     *
     * @param key The configuration key.
     * @param defaultValue The default value.
     * @return The associated short if key is found and has valid
     * format, default value otherwise.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Short.
     * @throws NumberFormatException is thrown if the value mapped
     * by the key has not a valid number format.
     */
    public Short getShort(String key, Short defaultValue)
    {
        Object value = getValueFromJNDI(key);
        if (value instanceof Short)
        {
            return (Short) value;
        }
        else if (value instanceof String)
        {
            Short s = new Short((String) value);
            return s;
        }
        else if (value == null)
        {
            return defaultValue;
        }
        else
        {
            throw new ClassCastException(
                '\'' + key + "' doesn't map to a Short object");
        }
    }
    
    /**
     * Get a string associated with the given configuration key.
     *
     * @param key The configuration key.
     * @param defaultValue The default value.
     * @return The associated string if key is found, default value otherwise.
     * @throws ClassCastException is thrown if the key maps to an object that
     *            is not a String.
     */
    public String getString(String key, String defaultValue)
    {
        try
        {
            Object o = getValueFromJNDI(key);
            if (o == null)
            {
                return defaultValue;
            }
            else
            {
                return (String) o;
            }
        }
        catch (NoSuchElementException nsee)
        {
            return defaultValue;
        }
    }
    /**
     * Get an array of strings associated with the given configuration
     * key.
     *
     * @param key The configuration key.
     * @return The associated string array if key is found.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a String/List of Strings.
     */
    public String[] getStringArray(String key)
    {
        Object value = getValueFromJNDI(key);
        String[] tokens;
        if (value instanceof String)
        {
            tokens = new String[1];
            tokens[0] = interpolate((String) value);
        }
        else if (value instanceof Container)
        {
            tokens = new String[((Container) value).size()];
            for (int i = 0; i < tokens.length; i++)
            {
                tokens[i] = interpolate((String) ((Container) value).get(i));
            }
        }
        else if (value == null)
        {
            tokens = new String[0];
        }
        else
        {
            throw new ClassCastException(
                '\'' + key + "' doesn't map to a String/List object");
        }
        return tokens;
    }
    
    /**
     * Get a List of strings associated with the given configuration key.
     * Typically this will be just a single item, as you can't have multiple
     * properties with the same name.
     *
     * @param key The configuration key.
     * @param defaultValue The default value.
     * @return The associated List.
     */
    public List getList(String key, List defaultValue)
    {
        try
        {
            Object value = this.getValueFromJNDI(key);
            if (value != null)
            {
                List list = new ArrayList(1);
                list.add(value.toString());
                return list;
            }
            else
            {
                if (defaultValue == null)
                {
                    defaultValue = new ArrayList();
                }
                return defaultValue;
            }
        }
        catch (NoSuchElementException nsse)
        {
            return defaultValue;
        }
    }
    /**
     * @return String
     */
    public String getPrefix()
    {
        return prefix;
    }
    /**
     * Sets the prefix.
     * @param prefix The prefix to set
     */
    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }
    private Object getValueFromJNDI(String key)
    {
        if (clearedProperties.contains(key))
        {
            return null;
        }
        try
        {
            key = StringUtils.replace(key, ".", "/");
            return getContext().lookup(key);
        }
        catch (java.util.NoSuchElementException nsse)
        {
            return null;
        }
        catch (NamingException ne)
        {
            return null;
        }
    }
    private Context getContext() throws NamingException
    {
        if (envCtx == null)
        {
            Context initCtx = new InitialContext();
            envCtx = (Context) initCtx.lookup(getPrefix());
        }
        return envCtx;
    }
}
