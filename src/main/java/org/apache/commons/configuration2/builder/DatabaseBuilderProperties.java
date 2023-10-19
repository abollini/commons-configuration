/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.configuration2.builder;

import javax.sql.DataSource;

/**
 * <p>
 * Definition of a properties interface for parameters of a database configuration.
 * </p>
 * <p>
 * The properties defined by this interface are used to configure a {@code DatabaseConfiguration} instance. They mainly
 * specify the database tables containing configuration properties. Note that many properties are mandatory; they must
 * be provided, otherwise the builder for database configurations throws an exception.
 * </p>
 * <p>
 * <strong>Important note:</strong> This interface is not intended to be implemented by client code! It defines a set of
 * available properties and may be extended even in minor releases.
 * </p>
 *
 * @since 2.0
 * @param <T> the type of the result of all set methods for method chaining
 */
public interface DatabaseBuilderProperties<T> {
    /**
     * Sets the data source for the database configuration. All database connections are obtained from this data source.
     * This is a mandatory property.
     *
     * @param src the data source for the database configuration
     * @return a reference to this object for method chaining
     */
    T setDataSource(DataSource src);

    /**
     * Sets the name of the table containing configuration data. Database configuration will access this database table.
     * This is a mandatory property.
     *
     * @param name the name of the table with configuration data
     * @return a reference to this object for method chaining
     */
    T setTable(String name);

    /**
     * Sets the name of the table column containing configuration keys. This is a mandatory property.
     *
     * @param name the column name
     * @return a reference to this object for method chaining
     */
    T setKeyColumn(String name);

    /**
     * Sets the name of the table column containing the configuration property value. This is a mandatory property.
     *
     * @param name the column name
     * @return a reference to this object for method chaining
     */
    T setValueColumn(String name);

    /**
     * Sets the name of the table column containing the configuration name. This property is needed if a single database
     * table contains the data of multiple configuration instances. Then this column is used as discriminator to select a
     * specific configuration instance.
     *
     * @param name the column name
     * @return a reference to this method for method chaining
     */
    T setConfigurationNameColumn(String name);

    /**
     * Sets the name of this configuration instance. This property is needed if a single database table contains the data of
     * multiple configuration instances. Then SQL statements generated by the configuration contain an additional constraint
     * filtering the configuration name column for this name.
     *
     * @param name the name of this configuration instance
     * @return a reference to this object for method chaining
     */
    T setConfigurationName(String name);

    /**
     * Enables or disable auto commit mode. If enabled, the database configuration instance performs a commit after each
     * database update.
     *
     * @param f the value of the auto commit flag
     * @return a reference to this object for method chaining
     */
    T setAutoCommit(boolean f);
}
