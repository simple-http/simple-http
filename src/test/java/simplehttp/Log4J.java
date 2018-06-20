/*
 * Copyright (c) 2011-2013, bad robot (london) ltd
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package simplehttp;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;
import org.hamcrest.Matcher;

import java.io.StringWriter;
import java.util.UUID;

public class Log4J {

    private final StringWriter writer = new StringWriter();
    private final Logger logger;
    private final String uuid = UUID.randomUUID().toString();

    public static Log4J appendTo(Logger logger) {
        return new Log4J(logger, Level.ALL);
    }

    public static Log4J appendTo(Logger logger, Level level) {
        return new Log4J(logger, level);
    }

    Log4J(Logger logger, Level level) {
        this.logger = logger;
        WriterAppender appender = new WriterAppender(new SimpleLayout(), writer);
        appender.setName(uuid);
        logger.addAppender(appender);
        logger.setLevel(level);
    }

    public void clean() {
        logger.removeAppender(uuid);
    }

    public void assertThat(Matcher<String> matcher) {
        org.junit.Assert.assertThat(writer.toString(), matcher);
    }

    public boolean matches(Matcher<String> matcher) {
        return matcher.matches(writer.toString());
    }

    public String toString() {
        return writer.toString();
    }
}
