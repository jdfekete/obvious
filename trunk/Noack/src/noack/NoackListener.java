package noack;

public class NoackListener implements GraphListener {


  /**
   * The number of time beginEdit has been called minus the number of time
   * endEdit has been called.
   */
  private int inhibitNotify = 0;

  @Override
  public void beginEdit() {
    inhibitNotify++;
  }


  @Override
  public void endEdit() {
    inhibitNotify--;
    if (inhibitNotify <= 0) {
        inhibitNotify = 0;
    }
  }

  @Override
  public void graphChanged(Graph g, String source, String target, int type) {
    if (inhibitNotify != 0) {
      return;
    }
  }

}
