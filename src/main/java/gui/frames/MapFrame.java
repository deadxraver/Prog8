package gui.frames;

import elements.Coordinates;
import elements.Movie;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactory;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

public class MapFrame extends JFrame {
	static {
		createMap();
	}

	private static Movie movie;

	private static MapFrame MAP_FRAME;

	private MapFrame() {
		super();
	}

	private static void createMap() {
		MAP_FRAME = new MapFrame();
		MAP_FRAME.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		MAP_FRAME.setSize(800, 600);

		JXMapViewer mapViewer = new JXMapViewer();

		TileFactoryInfo info = new OSMTileFactoryInfo();
		TileFactory tileFactory = new org.jxmapviewer.viewer.DefaultTileFactory(info);
		mapViewer.setTileFactory(tileFactory);
		// 59.956669, 30.308519
		// 59.851297, 30.377158
		GeoPosition itmoshki = new GeoPosition(59.956669, 30.308519);
		mapViewer.setZoom(5);
		mapViewer.setAddressLocation(itmoshki);

		Set<Waypoint> waypoints = new HashSet<>();
		WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<>();
		waypointPainter.setWaypoints(waypoints);
		mapViewer.setOverlayPainter(waypointPainter);

		mapViewer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					Point p = e.getPoint();
					GeoPosition geoPosition = mapViewer.convertPointToGeoPosition(p);
					waypoints.add(new DefaultWaypoint(geoPosition));
					waypointPainter.setWaypoints(waypoints);
					mapViewer.repaint();
					if (movie != null) movie.setCoordinates(new Coordinates(
							(float) geoPosition.getLongitude(),
							geoPosition.getLatitude()
					));
				}
			}
		});

		JSlider sliderHorizontal = new JSlider(JSlider.HORIZONTAL, -180, 180, 0);
		sliderHorizontal.addChangeListener(e -> {
			int value = sliderHorizontal.getValue();
			GeoPosition center = mapViewer.getAddressLocation();
			mapViewer.setAddressLocation(new GeoPosition(center.getLatitude(), value));
			mapViewer.repaint();
		});

		JSlider sliderVertical = new JSlider(JSlider.VERTICAL, -90, 90, 0);
		sliderVertical.addChangeListener(e -> {
			int value = sliderVertical.getValue();
			GeoPosition center = mapViewer.getAddressLocation();
			mapViewer.setAddressLocation(new GeoPosition(value, center.getLongitude()));
			mapViewer.repaint();
		});

		JSlider sliderZoom = new JSlider(JSlider.HORIZONTAL, 0, tileFactory.getInfo().getMaximumZoomLevel(), mapViewer.getZoom());
		sliderZoom.addChangeListener(e -> {
			int zoom = sliderZoom.getValue();
			mapViewer.setZoom(zoom);
			mapViewer.repaint();
		});

		MAP_FRAME.setLayout(new BorderLayout());
		MAP_FRAME.getContentPane().add(new JScrollPane(mapViewer), BorderLayout.CENTER);
		MAP_FRAME.getContentPane().add(sliderHorizontal, BorderLayout.SOUTH);
		MAP_FRAME.getContentPane().add(sliderVertical, BorderLayout.WEST);
		MAP_FRAME.getContentPane().add(sliderZoom, BorderLayout.NORTH);
	}

	public static void setMovie(Movie movie) {
		MapFrame.movie = movie;
	}

	public static MapFrame getInstance() {
		return MAP_FRAME;
	}
}
