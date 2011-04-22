package obvious.util;

/**
 * This interface is implemented by mostly all Obvious components.
 * It allows an Obvious component to render its underlying component
 * in its source implementation.
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
