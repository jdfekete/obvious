package obvious.util;

/**
 * Adaptable abstract class.
 * @author Hemery
 *
 */
public interface Adaptable {

  /**
   * Gets the corresponding underlying implementation.
   * @param type target class
   * @return an instance of the underlying implementation or null
   */
  Object getUnderlyingImpl(Class<?> type);

}
