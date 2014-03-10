package net.luminously.classifier.naivebayes;

import java.util.HashMap;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

/**
 * Represents a classification type for a NaiveBayesClassifier.
 * 
 * @author Andrew Lo
 */
public class Classification {
  private HashMap<String, Integer> stats;
  private int total;
  private int messageCount;

  public Classification() {
    messageCount = 0;
    stats = Maps.newHashMap();
    total = 0;
  }
  
  /**
   * Adds a message to this classification.
   * @param message String message to train Classifier with
   */
  public void addMessage(String message) {
    Preconditions.checkNotNull(message, "message must not be null");
    String[] terms = message.split(" ");
    for (String term: terms) {
      addTerm(term);
    }
    messageCount++;
  }

  /**
   * Adds a term to this classification
   * @param term
   */
  public void addTerm(String term) {
    Preconditions.checkNotNull(term, "term must not be null");
    String normalize = normalizeTerm(term);
    stats.put(normalize, stats.containsKey(normalize) ? stats.get(normalize) + 1 : 1);
    total++;
  }
  
  /**
   * Returns the number of times a term has appeared in this classification.
   * 
   * @param term Term to look up
   * @return Integer number of occurences 
   */
  public int getOccurrences(String term) {
    Preconditions.checkNotNull(term, "term must not be null");
    String normalize = normalizeTerm(term);
    return stats.containsKey(normalize) ? stats.get(normalize) : 0;
  }
  
  /**
   * Returns the number of messages of this classification in the training set.
   * @return
   */
  public int getMessages() {
    return messageCount;
  }
  
  /**
   * Returns a normalized version of a term.
   * 
   * @param term String term to normalize
   * @return String normalized copy of term
   */
  private String normalizeTerm(String term) {
    Preconditions.checkNotNull(term, "term must not be null");
    String normalized = term.replaceAll("[^0-9a-zA-Z]", "").toLowerCase();
    // All numbers linked to same token
    /*
    if (normalized.matches("^[0-9]*$")) {
      normalized = "<numbers>";
    }
    */
    return normalized;
  }
  
  /**
   * Returns the total number of items in this classification.
   * @return Integer number of items
   */
  public int getTotal() {
    return total;
  }
}
