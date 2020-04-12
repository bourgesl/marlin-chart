package org.marlin.chart;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.data.XYDataset;
import org.jfree.data.XYSeries;
import org.jfree.data.XYSeriesCollection;

/**
 *
 * @author bourgesl
 */
public class TestDraw extends JPanel implements ChartChangeListener {

    private final static int MILLIS = 1;

    private final static double MIN = 1.0;
    private final static double MAX = 30.0;
    private final static double STEP_TIME = (MAX / 100.0) * (1e-3 * MILLIS);

    private final JFreeChart chart;

    private boolean refresh = true;

    private BufferedImage chartBuffer = null;

    TestDraw() {
        this.chart = createChart(createDataset());
//        this.chart.setBackgroundPaint(Color.PINK);

        this.chart.addChangeListener(this);

        if (true) {
            final Timer timer = new Timer(MILLIS, new ActionListener() {

                private double x = MIN;

                @Override
                public void actionPerformed(ActionEvent e) {
                    // System.out.println("x = " + x);
                    chart.getXYPlot().getDomainAxis().setRange(x - 1.0, x + 1.0);
                    x += STEP_TIME;
                    if (x > MAX) {
                        x = MIN;
                    }
                }

            });
            timer.setCoalesce(false);
            timer.setRepeats(true);
            timer.start();
        } else {
            new Thread(new Runnable() {
                private double x = MIN;

                @Override
                public void run() {
                    // for ever
                    for (;;) {
                        // System.out.println("x = " + x);
                        chart.getXYPlot().getDomainAxis().setRange(x - 1.0, x + 1.0);

                        x += STEP_TIME;
                        if (x > MAX) {
                            x = MIN;
                        }
                        try {
                            Thread.sleep(MILLIS);
                        } catch (InterruptedException ex) {
                            System.out.println("Interrupted");
                            break;
                        }
                    }
                }
            }).start();
        }
    }

    @Override
    public void chartChanged(ChartChangeEvent event) {
        this.refresh = true;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        final Graphics2D g2 = (Graphics2D) g;

        if (refresh) {
            final Dimension size = getSize();

            if (this.chartBuffer == null
                    || this.chartBuffer.getWidth() != size.width
                    || this.chartBuffer.getHeight() != size.height) {
                this.chartBuffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
            }
            // System.out.println("draw...");

            final Graphics2D g2d = (Graphics2D) this.chartBuffer.getGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            chart.draw(g2d, new Rectangle2D.Double(0, 0, size.width, size.height));
//            bufferG2.dispose();

            refresh = false;
        }

        g2.drawImage(chartBuffer, 0, 0, this);
    }

    private static JFreeChart createChart(final XYDataset dataset) {
        JFreeChart localJFreeChart = ChartFactory.createLineXYChart("Y = f(X)", "X", "Y=f(X)", dataset, false, true, false);

        return localJFreeChart;
    }

    private static XYDataset createDataset() {
        final XYSeries s1 = new XYSeries("Data 1");
        final XYSeries s2 = new XYSeries("Data 2");

        final Random random = new Random(1097);

        final double step = (MAX - MIN) / 200.0;

        for (double x = MIN; x < MAX; x += step) {
            s1.add(x, 500 + 100.0 * random.nextGaussian());
            s2.add(x, 500 + 100.0 * random.nextGaussian());
        }

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(s1);
        dataset.addSeries(s2);

        return dataset;
    }

    public static void main(String[] args) {

        // First display which renderer is tested:
        // JDK9 only:
        System.setProperty("sun.java2d.renderer.verbose", "true");
        System.out.println("Testing renderer: ");
        // Other JDK:
        String renderer = "undefined";
        try {
            renderer = sun.java2d.pipe.RenderingEngine.getInstance().getClass().getName();
            System.out.println(renderer);
        } catch (Throwable th) {
            // may fail with JDK9 jigsaw (jake)
            if (false) {
                System.err.println("Unable to get RenderingEngine.getInstance()");
                th.printStackTrace();
            }
        }

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                final JFrame frame = new JFrame("TEST");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                frame.setMinimumSize(new Dimension(600, 400));

                frame.add(new TestDraw());
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
