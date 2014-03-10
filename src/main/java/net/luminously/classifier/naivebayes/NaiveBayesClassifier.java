package net.luminously.classifier.naivebayes;

import java.util.EnumMap;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.luminously.classifier.Classifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

/**
 * A Classifier implementation using the Naive Bayes method.
 * 
 * The Naive Bayes method essentially chooses the classification with the
 * highest probability using the formula:
 *  
 *    P(Classification) * Product(P(Term_i | Classification))
 * 
 * where:
 *    P(Classification) = (Messages of Classification) / (Total Messages)
 *   and
 *    P(Term_i | Classification) = (Occurrences of Term in Classification) / (Total Occurrences in Classification)
 * 
 * 
 * @author Andrew Lo
 *
 * @param <E> Enum class representing the possible classifications 
 */
public class NaiveBayesClassifier<E extends Enum<E>> implements Classifier {
  private static final Logger LOGGER = LoggerFactory.getLogger(NaiveBayesClassifier.class);
  private static final double OMEGA = 1; // Constant used in Laplace smoothing
  
  private EnumMap<E, Classification> stats;
  private Class<E> clazz;
  private int totalMessages;
  
  public NaiveBayesClassifier(Class<E> clazz) {
    Preconditions.checkNotNull(clazz, "clazz must not be null");
    this.clazz = clazz;
    
    totalMessages = 0;
    stats = Maps.newEnumMap(clazz);
  }

  @Override
  public void add(String type, String message) {
    Preconditions.checkNotNull(type, "type must not be null");
    Preconditions.checkNotNull(message, "message must not be null");
    
    E messageType = null;
    try {
      messageType = Enum.valueOf(clazz, type);
    } catch (IllegalArgumentException e) {
      LOGGER.info("Found message with invalid type [" + type + "]");
      return;
    }
    
    message = message.toLowerCase(); // normalize for case
    if (stats.containsKey(messageType)) {
      stats.get(messageType).addMessage(message);
    } else {
      Classification classification = new Classification();
      classification.addMessage(message);
      stats.put(messageType, classification);
    }
    
    totalMessages++;
  }

  @Override
  public String query(String message) {
    Preconditions.checkNotNull(message, "message must not be null");
    Preconditions.checkArgument(totalMessages > 0, "No training messages added yet");
    
    TreeMap<Double, E> probabilities = Maps.newTreeMap();
    
    for (Entry<E, Classification> stat : stats.entrySet()) {
      // Calculate probability
      Double probability = classificationProbability(stat.getKey()) * messageProbability(stat.getKey(), message);
      
      if (probabilities.containsKey(probability)) {
        // TODO Should change this to a List structure to deal with collisions 
        LOGGER.warn("Collision while inserting probability {} into map. A value may have been overwritten.", probability);
      }
      
      LOGGER.info("Inserted \"{}\" ({})", stat.getKey().name(), probability);
      probabilities.put(probability, stat.getKey());
    }
    
    return probabilities.lastEntry().getValue().name();
  }

  /**
   * Calculates the probability that the message is of a specified classification.
   * 
   * @return Double value between 0 and 1.
   */
  private double classificationProbability(E type) {
    Preconditions.checkNotNull(type, "type must not be null");
    double messageCount = stats.get(type).getMessageCount();
    // Apply Laplace Smoothing:
    // (x_i + \omega) / (N + \omega * d) where i = 1 .. d
    return (messageCount + OMEGA) / (totalMessages + OMEGA * stats.size());
  }
  
  /**
   * Returns the probability of the message given a classification.
   * 
   * @param type    Classification type to query
   * @param message Message to query
   * @return        Probability of the message
   */
  private double messageProbability(E type, String message) {
    Preconditions.checkNotNull(type, "type must not be null");
    Preconditions.checkNotNull(message, "message must not be null");
    
    double product = 1;
    
    String[] terms = message.split(" ");
    for (String term : terms) {
      product *= termProbability(type, term, terms.length);
    }
    
    return product;
  }
  
  /**
   * Returns the probability of a term given a classification.
   * 
   * @param type      Classification type to query
   * @param term      Term to query
   * @param termCount Number of terms in message (used for Laplace smoothing)
   * @return          Probability of the individual term
   */
  private double termProbability(E type, String term, int termCount) {
    Preconditions.checkNotNull(type, "type must not be null");
    Preconditions.checkNotNull(term, "term must not be null");
    
    Classification classification = stats.get(type);
    
    // No messages, we know nothing about this classification
    if (classification.getTermCount() == 0) {
      return 0;
    }
    
    // Padding to avoid zero in numerator
    return ((double) classification.getOccurrences(term) + OMEGA) / (classification.getTermCount() + OMEGA * termCount);
  }
}
