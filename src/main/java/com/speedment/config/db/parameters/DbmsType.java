/**
 *
 * Copyright (c) 2006-2016, Speedment, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); You may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.speedment.config.db.parameters;

import com.speedment.Speedment;
import com.speedment.annotation.Api;
import com.speedment.config.db.Dbms;
import com.speedment.config.db.parameters.DbmsTypeBuilder.WithDbmsNameMeaning;
import com.speedment.config.db.parameters.DbmsTypeBuilder.WithName;
import com.speedment.db.ConnectionUrlGenerator;
import com.speedment.db.DatabaseNamingConvention;
import com.speedment.db.DbmsHandler;
import com.speedment.internal.core.config.dbms.DbmsTypeImpl;
import com.speedment.manager.SpeedmentPredicateView;
import com.speedment.util.sql.SqlTypeInfo;
import static com.speedment.stream.MapStream.comparing;
import java.util.Comparator;

import java.util.Optional;
import java.util.Set;

/**
 * The {@code DbmsType} interface defines unique properties for different Dbms
 * types. By implementing a new {@code DbmsType} and perhaps a new
 * {@code DbmsHandler}, one may easily implement support for new Dbms vendor
 * types.
 *
 * @author pemi
 * @since 2.0
 */
@Api(version = "2.2")
public interface DbmsType {

    final Comparator<DbmsType> COMPARATOR = comparing(DbmsType::getName);

    /**
     * Returns the non-null name for this {@code DbmsType}. For example MySQL or
     * Oracle
     *
     * @return the non-null name for this {@code DbmsType}
     */
    String getName();

    /**
     * Returns the non-null Driver Manager Name for this {@code DbmsType}. For
     * example "MySQL-AB JDBC Driver" or "Oracle JDBC Driver"
     *
     * @return the non-null Driver Manager Name
     */
    String getDriverManagerName();

    /**
     * Returns the default port for this {@code DbmsType}. For example 3306
     * (MySQL) or 1521 (Oracle)
     *
     * @return the default port
     */
    int getDefaultPort();

    /**
     * Returns the delimiter used between a Schema and a Table for this
     * {@code DbmsType}. Most {@code DbmsType} are using a "." as a separator.
     *
     * @return the delimiter used between a Schema and a Table
     */
    String getSchemaTableDelimiter();

    /**
     * Returns a textual representation of what the database name is used for.
     * Some databases (notably MySQL) does not use the database name for
     * anything whereas other (such as Oracle) are using the name as an address
     * (i.e. for Oracle the name is used as SID)
     *
     * @return a textual representation of what the database name is used for
     */
    String getDbmsNameMeaning();

    /**
     * Returns the default name for this {@code DbmsType}. For example ‘orcl'
     * (Oracle)
     *
     * @return the default dbms name
     */
    Optional<String> getDefaultDbmsName();

    /**
     * Returns if this {@code DbmsType} is supported by Speedment in the current
     * implementation.
     *
     * @return if this {@code DbmsType} is supported by Speedment in the current
     * implementation
     */
    boolean isSupported();

    // Implementation specifics
    /**
     * Returns the non-null fully qualified JDBC class name for this
     * {@code DbmsType}. For example "com.mysql.jdbc.Driver" or
     * "oracle.jdbc.OracleDriver"
     *
     * @return the non-null name for this {@code DbmsType}
     */
    String getDriverName();
    
    /**
     * Returns the naming convention used by this database.
     * 
     * @return  the naming convention
     */
    DatabaseNamingConvention getDatabaseNamingConvention();

    /**
     * Creates and returns a new {@code DbmsHandler} instance for the given
     * database.
     *
     * @param speedment instance to use
     * @param dbms the Dbms configuration to use
     * @return a new {@code DbmsHandler} instance for the given database
     */
    DbmsHandler makeDbmsHandler(Speedment speedment, Dbms dbms);

    // TODO: Improve javadoc in this file.
    
    String getResultSetTableSchema();

    ConnectionUrlGenerator getConnectionUrlGenerator();

    SpeedmentPredicateView getSpeedmentPredicateView();

    Set<SqlTypeInfo> getDataTypes();
    
    String getInitialQuery();

    public static WithName builder() {
        return DbmsTypeImpl.builder();
    }

    public static WithDbmsNameMeaning builder(String name, String driverManagerName, int defaultPort) {
        return builder().withName(name).withDriverManagerName(driverManagerName).withDefaultPort(defaultPort);
    }
}