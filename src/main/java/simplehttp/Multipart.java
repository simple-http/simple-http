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

import java.io.File;

import static java.lang.String.format;

public class Multipart implements HttpPost {
	
	private final String name;
	private final File file;

	public Multipart(String name, File file) {
		this.name = name;
		this.file = file;
	}

	public File getFile() {
		return file;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public void accept(HttpRequestVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public MultipartContent getContent() {
		return new MultipartContent(name, file);
	}

	@Override
	public Headers getHeaders() {
//		return headers(header("Content-Type", "multipart/form-data;boundary=\"boundary\""));
		return EmptyHeaders.emptyHeaders();
	}

	@Override
	public String toString() {
		return format("Multipart{content='name=%s, filename=%s', headers='%s'}", name, file, getHeaders());
	}
}
