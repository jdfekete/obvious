package obvious.prefuse.viz.util;

import obvious.viz.Action;

/**
 * PrefuseObviousAction class.
 * @author Hemery
 *
 */
public class PrefuseObviousAction implements Action {

  /**
   * Underlying prefuse action.
   */
  private prefuse.action.Action action;

  /**
   * Constructor.
   * @param act a prefuse action
   */
  public PrefuseObviousAction(prefuse.action.Action act) {
    this.action = act;
  }

  /**
   * Return the underlying implementation.
   * @param type targeted class
   * @return prefuse action instance or null
   */
  public Object getUnderlyingImpl(Class<?> type) {
    if (type != null && type.equals(prefuse.action.Action.class)) {
      return action;
    } else if (type != null && type.equals(prefuse.action.ActionList.class)
        && action instanceof prefuse.action.ActionList) {
      return (prefuse.action.ActionList) action;
    }  else {
      return null;
    }
  }

}
