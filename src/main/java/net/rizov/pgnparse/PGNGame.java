/*
 * Copyright 2016 Deyan Rizov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
package net.rizov.pgnparse;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PGNGame extends PGNMoveContainer {

	private Map<String, String> tags;
	
	private String pgn;
	
	PGNGame() {
		super();
		tags = new HashMap<String, String>();
	}
	
	PGNGame(String pgn) {
		this();
		this.pgn = pgn;
	}
	
	@Override
	public String toString() {
		return pgn == null ? "" : pgn;
	}
	
	void addTag(String key, String value) {
		tags.put(key, value);
	}
	
	void removeTag(String key) {
		tags.remove(key);
	}
	
	public String getTag(String key) {
		return tags.get(key);
	}
	
	public Iterator<String> getTagKeysIterator() {
		return tags.keySet().iterator();
	}
	
	public boolean containsTagKey(String key) {
		return tags.containsKey(key);
	}
	
	public int getTagsCount() {
		return tags.size();
	}
	
}
