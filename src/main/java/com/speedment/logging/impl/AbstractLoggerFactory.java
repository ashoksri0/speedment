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
package com.speedment.logging.impl;

import com.speedment.logging.Logger;
import com.speedment.logging.LoggerEventListener;
import com.speedment.logging.LoggerFactory;
import com.speedment.logging.LoggerFormatter;
import com.speedment.logging.impl.formatter.StandardFormatters;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 *
 * @author pemi
 */
public abstract class AbstractLoggerFactory implements LoggerFactory {

    private final Map<String, Logger> loggers;
    private final Set<LoggerEventListener> listeners;
    private LoggerFormatter formatter;

    public AbstractLoggerFactory() {
        loggers = new ConcurrentHashMap<>();
        formatter = StandardFormatters.PLAIN_FORMATTER;
        listeners = Collections.newSetFromMap(new ConcurrentHashMap<LoggerEventListener, Boolean>());
    }

    @Override
    public Logger create(Class<?> binding) {
        final String[] tokens = binding.getName().split("\\.");
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tokens.length; i++) {
            if (i == tokens.length - 1) {
                sb.append(tokens[i]);
            } else {
                sb.append(tokens[i].charAt(0)).append('.');
            }
        }
        return acquireLogger(sb.toString());
    }

    @Override
    public Logger create(String binding) {
        return acquireLogger(binding);
    }

    @Override
    public synchronized void setFormatter(LoggerFormatter formatter) {
        this.formatter = Objects.requireNonNull(formatter);
        forEachLogger(l -> l.setFormatter(formatter));
    }

    @Override
    public LoggerFormatter getFormatter() {
        return formatter;
    }

    protected Logger acquireLogger(String binding) {
        return loggers.computeIfAbsent(binding, b -> make(b, formatter));
    }

    public abstract Logger make(String binding, LoggerFormatter formatter);

    @Override
    public void addListener(LoggerEventListener listener) {
        if (listeners.add(listener)) {
            forEachLogger(l -> l.addListener(listener));
        }
    }

    @Override
    public void removeListener(LoggerEventListener listener) {
        if (listeners.remove(listener)) {
            forEachLogger(l -> l.removeListener(listener));
        }
    }

    private void forEachLogger(Consumer<Logger> consumer) {
        loggers().map(Entry::getValue).forEach(consumer);
    }

    @Override
    public Stream<LoggerEventListener> listeners() {
        return listeners.stream();
    }

    @Override
    public Stream<Entry<String, Logger>> loggers() {
        return loggers.entrySet().stream();
    }

}
