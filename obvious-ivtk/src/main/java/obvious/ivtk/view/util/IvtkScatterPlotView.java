package obvious.ivtk.view.util;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import infovis.panel.VisualizationPanel;
import infovis.visualization.DefaultAxisVisualization;
import infovis.visualization.Orientable;
import infovis.visualization.inter.InteractorFactory;
import infovis.visualization.render.DefaultVisualLabel;
import infovis.visualization.render.VisualLabel;
import obvious.ObviousRuntimeException;
import obvious.data.util.Predicate;
import obvious.ivtk.view.IvtkObviousView;
import obvious.viz.Visualization;

/**
 * This class allows us to provide a dedicated views for scatter-plots
 * with the obvious-ivtk binding.
 * @see IvtkObviousView
 * @see IvtkObviousVisualization
 * @author Hemery
 *
 */
public class IvtkScatterPlotView extends IvtkObviousView {

  /**
   * Visualization backing this view.
   */
  private Visualization backingVis;

  /**
   * JSCrollPane for the scatterplot.
   */
  private JScrollPane scrollPanel;

  /**
   * Constructor.
   * @param vis an Obvious scatterplot visualization
   * @param predicate an Obvious predicate
   */
  public IvtkScatterPlotView(Visualization vis, Predicate predicate) {
    super(vis, predicate);
    if (vis.getUnderlyingImpl(infovis.Visualization.class) != null) {
      this.backingVis = vis;
    } else {
      throw new ObviousRuntimeException("Visualization not supported");
    }
    buildAxis();
  }

  /**
   * Build axis for the scatterplot.
   */
  protected void buildAxis() {
    infovis.Visualization vis = (infovis.Visualization)
        backingVis.getUnderlyingImpl(infovis.Visualization.class);
    this.scrollPanel = new JScrollPane(new VisualizationPanel(vis));
    InteractorFactory.installInteractor(vis);
    if (vis.getRulerTable() != null) {
        DefaultAxisVisualization column = new DefaultAxisVisualization(
                vis,
                Orientable.ORIENTATION_NORTH);
        InteractorFactory.installInteractor(column);
        VisualLabel vl = VisualLabel.get(column);
        if (vl instanceof DefaultVisualLabel) {
            DefaultVisualLabel dvl = (DefaultVisualLabel) vl;
            dvl.setOrientation(Orientable.ORIENTATION_NORTH);
            dvl.setOutlined(false);
        }
        VisualizationPanel vp1 = new VisualizationPanel(column);
        vp1.setUsingGradient(false);
        scrollPanel.setColumnHeaderView(vp1);
        DefaultAxisVisualization row = new DefaultAxisVisualization(
                vis,
                Orientable.ORIENTATION_EAST);
        InteractorFactory.installInteractor(row);
        vl = VisualLabel.get(row);
        if (vl instanceof DefaultVisualLabel) {
            DefaultVisualLabel dvl = (DefaultVisualLabel) vl;
            dvl.setOrientation(Orientable.ORIENTATION_EAST);
            dvl.setOutlined(false);
        }
        VisualizationPanel vp2 = new VisualizationPanel(row);
        vp2.setUsingGradient(false);
        scrollPanel.setRowHeaderView(vp2);
    }

  }

  @Override
  public JComponent getViewJComponent() {
    if (scrollPanel == null) {
      return super.getViewJComponent();
    }
    return scrollPanel;
  }

}
