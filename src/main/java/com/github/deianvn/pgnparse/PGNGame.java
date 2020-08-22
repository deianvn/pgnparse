package com.github.deianvn.pgnparse;

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
