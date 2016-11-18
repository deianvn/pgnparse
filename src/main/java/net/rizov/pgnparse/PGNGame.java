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
import java.util.Set;

public class PGNGame extends PGNMoveContainer {

	private Map<String, String> tags;

	private FENPosition initialPosition;
	
	PGNGame() {
		super();
		tags = new HashMap<String, String>();
	}

	public boolean isCustomInitialPositionUsed() {
        return initialPosition != null;
    }

    public FENPosition getInitialPosition() {
        return initialPosition;
    }

    public void setInitialPosition(FENPosition initialPosition) {
        this.initialPosition = initialPosition;
    }

    public void addTag(String key, String value) {
		tags.put(key, value);
	}

	public void removeTag(String key) {
		tags.remove(key);
	}
	
	public String getTag(String key) {
		return tags.get(key);
	}

	public Set<String> getTagKeys() {
		return tags.keySet();
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

	public String getFEN(PGNMove move) {
		throw new UnsupportedOperationException();
	}
	
}
