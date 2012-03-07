package org.vaadin.vol.demo;

import java.util.Date;

import org.vaadin.vol.Bounds;
import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.OpenStreetMapLayer;
import org.vaadin.vol.Point;
import org.vaadin.vol.PolyLine;
import org.vaadin.vol.Style;
import org.vaadin.vol.StyleMap;
import org.vaadin.vol.VectorLayer;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class VectorAnimation extends AbstractVOLTest implements ClickListener {
    private static final int FPS = 15;

    private HorizontalLayout controls;

    private Point[] coordinateList;

    private TextField animationDuration = new TextField();
    private Button stop = new NativeButton("Stop animation", this);
    private Button playData = new NativeButton("Play", this);

    private ProgressIndicator pi = new ProgressIndicator();

    private Bounds bounds;

    private VectorLayer vectorLayer;

    private PolyLine polyline = new PolyLine();

    private OpenLayersMap map;

    private Thread animationThread;

    private boolean stopAnimation;

    public void buttonClick(ClickEvent event) {
        if (event.getButton() == stop) {
            if (animationThread != null) {
                stopAnimation = true;
            }
        } else if (event.getButton() == playData) {
            playDetails();
        } else {
            System.err.println("Unhandled click");
        }
    }

    private void showWholeVector() {
        polyline.setPoints(coordinateList);
        playData.setEnabled(true);
    }

    private void playDetails() {
        pi.setVisible(true);
        animationThread = new Thread(new VectorAnimator(
                Integer.parseInt(animationDuration.toString())));
        animationThread.start();
        playData.setEnabled(false);
    }

    @Override
    public String getDescription() {
        return "Example how to use animate vector.";
    }

    @Override
    protected void setup() {
        super.setup();
        ((VerticalLayout) getContent()).addComponentAsFirst(controls);
    }

    @Override
    Component getMap() {
        if (map == null) {
            initDemoCoordinates();

            map = new OpenLayersMap();

            map.setImmediate(true); // update extent and zoom to server as they
                                    // change

            OpenStreetMapLayer osm = new OpenStreetMapLayer();

            map.setZoom(15);

            map.setSizeFull();

            // add layers

            // base layers
            map.addLayer(osm);

            vectorLayer = new VectorLayer();

            showWholeVector();

            Style defaultstyle = new Style();
            /* Set stroke color to green, otherwise like default style */
            defaultstyle.extendCoreStyle("default");
            defaultstyle.setStrokeColor("#0000ff");
            defaultstyle.setStrokeWidth(3);
            defaultstyle.setFillColor("#adfffc");
            defaultstyle.setFillOpacity(0.4);

            // Make borders of selected graphs bigger
            Style selectStyle = new Style();
            selectStyle.setStrokeWidth(5);

            StyleMap stylemap = new StyleMap(defaultstyle, selectStyle, null);
            // make selectStyle inherit attributes not explicitly set
            stylemap.setExtendDefault(true);
            vectorLayer.setStyleMap(stylemap);
            vectorLayer.addComponent(polyline);

            map.addLayer(vectorLayer);

            map.zoomToExtent(bounds);

            controls = new HorizontalLayout();

            animationDuration.setValue(15);
            controls.addComponent(animationDuration);
            controls.addComponent(playData);
            controls.addComponent(stop);
            controls.addComponent(pi);
            pi.setIndeterminate(true);
            pi.setPollingInterval(1000 / FPS);
            pi.setVisible(false);
        }
        return map;
    }

    private void initDemoCoordinates() {
        /*
         * Note that we intentionally place overlays 10 meter above ground. This
         * way above base layers in google earth.
         */
        // ROME GRA
        coordinateList = new Point[] {
                new Point(12.380218505365, 41.885921026773),
                new Point(12.387084960443, 41.916585114853),
                new Point(12.384338378412, 41.958979493725),
                new Point(12.423477172355, 41.975827259649),
                new Point(12.439270019035, 41.983483862127),
                new Point(12.459869384268, 41.981442191482),
                new Point(12.469482421377, 41.98603585837),
                new Point(12.492828368642, 41.986546245344),
                new Point(12.506561278797, 41.992160232),
                new Point(12.581405639146, 41.95846888581),
                new Point(12.611618041488, 41.920161632571),
                new Point(12.615737914536, 41.896655132614),
                new Point(12.602691649887, 41.865981473335),
                new Point(12.596511840318, 41.852684981749),
                new Point(12.589645385239, 41.834781492781),
                new Point(12.581405639146, 41.819943378401),
                new Point(12.573165893052, 41.813290693541),
                new Point(12.551193236803, 41.801006998284),
                new Point(12.527160644031, 41.795376184184),
                new Point(12.510681151844, 41.795376184184),
                new Point(12.455749511221, 41.800495126537),
                new Point(12.39120483349, 41.81840820477),
                new Point(12.372665404779, 41.864958764445),
                new Point(12.372665404779, 41.865470120935),
                new Point(12.380218505365, 41.885921026773) };

        // compute bounds to use later
        bounds = new Bounds(coordinateList);
    }

    public class VectorAnimator implements Runnable {
        private Date start;
        private double duration;
        private static final int SNAKE_LENGHT = 3;

        public VectorAnimator(int seconds) {
            duration = seconds * 1000;
        }

        public void run() {
            start = new Date();
            stopAnimation = false;

            double progress = 0;
            while (!stopAnimation) {
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                progress = (new Date().getTime() - start.getTime()) / duration;
                if (progress >= 1) {
                    break;
                }

                // TODO fixed length snake would look much smoother

                double position = progress * coordinateList.length;

                double fraction = position - (int) position;

                int curIndex = (int) (progress * coordinateList.length);
                Point front = interpolateFront(curIndex, fraction);
                Point back = interpolateBack(curIndex, fraction);

                Point[] points = new Point[SNAKE_LENGHT + 2];
                points[0] = front;
                points[1] = coordinateList[(curIndex + 1)
                        % coordinateList.length];
                points[2] = coordinateList[curIndex];
                points[3] = coordinateList[(curIndex - 1 + coordinateList.length)
                        % coordinateList.length];
                points[4] = back;

                synchronized (getApplication()) {
                    polyline.setPoints(points);
                }
            }

            synchronized (getApplication()) {
                pi.setVisible(false);
                showWholeVector();
            }
        }

        private Point interpolateFront(int curIndex, double fraction) {
            int nextIndex = (curIndex + 1) % coordinateList.length;
            Point p1 = coordinateList[nextIndex];
            Point p2 = coordinateList[(nextIndex + 1) % coordinateList.length];
            double lon = p1.getLon() + (p2.getLon() - p1.getLon()) * fraction;
            double lat = p1.getLat() + (p2.getLat() - p1.getLat()) * fraction;
            return new Point(lon, lat);
        }

        private Point interpolateBack(int curIndex, double fraction) {
            int idx = (curIndex - 1 + coordinateList.length)
                    % coordinateList.length;
            Point p1 = coordinateList[idx];
            Point p2 = coordinateList[(idx - 1 + coordinateList.length)
                    % coordinateList.length];
            double lon = p1.getLon() - (p1.getLon() - p2.getLon())
                    * (1 - fraction);
            double lat = p1.getLat() - (p1.getLat() - p2.getLat())
                    * (1 - fraction);
            return new Point(lon, lat);
        }
    }
}