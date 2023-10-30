/*
 * Copyright (c) 2012, the Last.fm Java Project and Committers
 * All rights reserved.
 *
 * Redistribution and use of this software in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above
 *   copyright notice, this list of conditions and the
 *   following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above
 *   copyright notice, this list of conditions and the
 *   following disclaimer in the documentation and/or other
 *   materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.spotifyxp.deps.de.umass.lastfm.cache;

import com.spotifyxp.logging.ConsoleLoggingModules;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is just for testing. You probably don't want to use it in production.
 *
 * @author Janni Kovacs
 */
public class MemoryCache extends Cache {

	private final Map<String, String> data = new HashMap<>();
	private final Map<String, Long> expirations = new HashMap<>();

	public boolean contains(String cacheEntryName) {
		boolean contains = data.containsKey(cacheEntryName);
		ConsoleLoggingModules.debug("MemoryCache.contains: " + cacheEntryName + " ? " + contains);
		return contains;
	}

	public InputStream load(String cacheEntryName) {
		ConsoleLoggingModules.debug("MemoryCache.load: " + cacheEntryName);
        return new ByteArrayInputStream(data.get(cacheEntryName).getBytes(StandardCharsets.UTF_8));
    }

	public void remove(String cacheEntryName) {
		ConsoleLoggingModules.debug("MemoryCache.remove: " + cacheEntryName);
		data.remove(cacheEntryName);
		expirations.remove(cacheEntryName);
	}

	public void store(String cacheEntryName, InputStream inputStream, long expirationDate) {
		ConsoleLoggingModules.debug("MemoryCache.store: " + cacheEntryName + " Expires at: " + new Date(expirationDate));
		StringBuilder b = new StringBuilder();
		try {
			BufferedReader r = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			String l;
			while ((l = r.readLine()) != null) {
				b.append(l);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		data.put(cacheEntryName, b.toString());
		expirations.put(cacheEntryName, expirationDate);
	}

	public boolean isExpired(String cacheEntryName) {
		boolean exp = expirations.get(cacheEntryName) < System.currentTimeMillis();
		ConsoleLoggingModules.debug("MemoryCache.isExpired: " + cacheEntryName + " ? " + exp);
		return exp;
	}

	public void clear() {
		data.clear();
		expirations.clear();
	}
}
