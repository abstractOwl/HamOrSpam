package net.luminously.classifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;

/**
 * Entrypoint for HamOrSpam.
 * 
 * @author Andrew Lo
 */
public class Main {
  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
  
  /**
   * Types that the message can be.
   */
  private enum Types { HAM, SPAM }
  
  /**
   * Trains the classifier with a specified dataset
   * 
   * @param classifier Classifier implementation to train
   * @param filename   Filename of training dataset
   */
  private static void train(Classifier classifier, String filename) {
    Preconditions.checkNotNull(classifier, "classifier must not be null.");
    Preconditions.checkNotNull(filename, "filename must not be null.");
    
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line;
      
      while ((line = reader.readLine()) != null) {
        String[] explode = line.split("\t");
        classifier.add(explode[0], explode[1]);
      }
      
      reader.close();
    } catch (IOException e) {
      Throwables.propagate(e);
    }
  }
  
  /**
   * Classifies a dataset as ham or spam. Prints out one query result
   * [ham, spam] per line.
   * 
   * @param classifier Classifier implementation to query
   * @param filename   Filename of testing dataset
   */
  private static void test(Classifier classifier, String filename) {
    Preconditions.checkNotNull(classifier, "classifier must not be null.");
    Preconditions.checkNotNull(filename, "filename must not be null.");
    
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line;
      
      while ((line = reader.readLine()) != null) {
        System.out.println(classifier.query(line).toLowerCase());
      }
      
      reader.close();
    } catch (IOException e) {
      Throwables.propagate(e);
    }
  }
  
  public static void main(String args[]) {
    Preconditions.checkArgument(args.length == 2, "Usage: java Classifier training.txt testing.txt");
  }
}
