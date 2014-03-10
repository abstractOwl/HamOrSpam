package net.luminously.classifier.naivebayes;

import junit.framework.TestCase;

/**
 * Test for the Classification class.
 * @author Andrew Lo
 */
public class ClassificationTest extends TestCase {
  /**
   * Test interactions with an empty Classification.
   */
  public void testEmptyCase() {
    Classification classification = new Classification();
    
    assertEquals(0, classification.getOccurrences("someTerm"));
    assertEquals(0, classification.getMessageCount());
    assertEquals(0, classification.getTermCount());
  }
  
  /**
   * Test adding messages to a Classification.
   */
  public void testAdd() {
    Classification classification = new Classification();
    classification.addMessage("hello world");
    
    assertEquals(1, classification.getMessageCount());
    assertEquals(2, classification.getTermCount());
    assertEquals(1, classification.getOccurrences("hello"));
  }
  
  /**
   * Test string normalization. String normalization removes all symbols except
   * letters and digits from a term and normalizes for letter case.
   */
  public void testNormalizeString() {
    Classification classification = new Classification();
    classification.addMessage("Hello&^world!!1");
    
    assertEquals(1, classification.getMessageCount());
    assertEquals(1, classification.getTermCount());
    assertEquals(1, classification.getOccurrences("hELloWorld1"));
  }
}
