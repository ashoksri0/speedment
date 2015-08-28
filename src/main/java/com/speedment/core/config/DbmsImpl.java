/**
 *
 * Copyright (c) 2006-2015, Speedment, Inc. All Rights Reserved.
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
package com.speedment.core.config;

import com.speedment.api.Speedment;
import com.speedment.core.config.aspects.ParentHelper;
import com.speedment.api.config.Dbms;
import com.speedment.api.config.Project;
import com.speedment.api.config.Schema;
import com.speedment.api.config.aspects.Parent;
import com.speedment.api.config.parameters.DbmsType;
import com.speedment.core.config.dbms.StandardDbmsType;
import com.speedment.core.config.aspects.DbmsTypeableHelper;
import com.speedment.core.config.utils.ConfigUtil;
import com.speedment.util.Cast;
import groovy.lang.Closure;
import java.util.Optional;

/**
 *
 * @author pemi
 */
public class DbmsImpl extends AbstractNamedConfigEntity implements Dbms, DbmsTypeableHelper, ParentHelper<Schema> {

    private final Speedment speedment;
    private Project parent;
    private final ChildHolder children;
    private DbmsType type;
    private String ipAddress;
    private Integer port;
    private String username, password;

    public DbmsImpl(Speedment speedment) {
        this.speedment = speedment;
        this.children = new ChildHolder();
    }

    @Override
    protected void setDefaults() {
        setType(StandardDbmsType.defaultType());
        setIpAddress("localhost");
        setPort(getType().getDefaultPort());
        setUsername("root");
        setPassword("");
    }

    @Override
    public DbmsType getType() {
        return type;
    }

    @Override
    public void setType(DbmsType dbmsType) {
        this.type = dbmsType;
    }

    @Override
    public Optional<String> getIpAddress() {
        return Optional.ofNullable(ipAddress);
    }

    @Override
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public Optional<Integer> getPort() {
        return Optional.of(port);
    }

    @Override
    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public Optional<String> getUsername() {
        return Optional.of(username);
    }

    @Override
    public void setUsername(String name) {
        this.username = name;
    }

    @Override
    public Optional<String> getPassword() {
        return Optional.ofNullable(password);
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setParent(Parent<?> parent) {
        this.parent = Cast.orFail(parent, Project.class);
    }

    @Override
    public Optional<Project> getParent() {
        return Optional.ofNullable(parent);
    }

    @Override
    public ChildHolder getChildren() {
        return children;
    }

    @Override
    public Schema addNewSchema() {
        final Schema e = Schema.newSchema();
        add(e);
        return e;
    }

    @Override
    public Speedment getSpeedment() {
        return speedment;
    }
    
    @Override
    public Schema schema(Closure<?> c) {
        return ConfigUtil.groovyDelegatorHelper(c, this::addNewSchema);
    }
}