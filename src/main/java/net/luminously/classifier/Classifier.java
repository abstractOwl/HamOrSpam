package net.luminously.classifier;

/**
 * Interface implemented by classes which classify messages based on a training
 * dataset.
 * 
 * @author Andrew Lo
 */
public interface Classifier {
  /**
   * Adds a message to the training set of <code>type</code>.
   * 
   * @param type Type <code>message</code> is classified as
   * @param message Message to add to training set
   */
  void add(String type, String message);
  
  /**
   * Returns a classification based on the training dataset.
   * 
   * @param message String message to be classified 
   * @return Type which the message was classified as
   */
  String query(String message);
}
