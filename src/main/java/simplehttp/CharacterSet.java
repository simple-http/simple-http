/*
 * Copyright (c) 2011-2019, simple-http committers
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

public enum CharacterSet {

    // character encodings
    UTF_8("UTF-8"),
    UTF_16("UTF-16"),
    // character sets
    US_ASCII("US-ASCII"),
    ASCII("ASCII"),
    ISO_8859_1("ISO-8859-1"),
    defaultCharacterSet(ISO_8859_1.characterSet);

    private final String characterSet;

    CharacterSet(String characterSet) {
        this.characterSet = characterSet;
    }
    
    public String asString() {
        return characterSet;
    }

}
