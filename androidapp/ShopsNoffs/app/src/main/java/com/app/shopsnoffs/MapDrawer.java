package com.app.shopsnoffs;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;


public class MapDrawer {

    private GoogleMap map;
    private static int EARTH_RADIUS = 6371000;

    public MapDrawer(GoogleMap map) {
        this.map = map;
    }

    private LatLng getPoint(LatLng center, int radius, double angle) {
        // Get the coordinates of a circle point at the given angle
        double east = radius * Math.cos(angle);
        double north = radius * Math.sin(angle);

        double cLat = center.latitude;
        double cLng = center.longitude;
        double latRadius = EARTH_RADIUS * Math.cos(cLat / 180 * Math.PI);

        double newLat = cLat + (north / EARTH_RADIUS / Math.PI * 180);
        double newLng = cLng + (east / latRadius / Math.PI * 180);

        return new LatLng(newLat, newLng);
    }

    public Polygon drawCircle(LatLng center, int radius) {
        // Clear the map to remove the previous circle
        map.clear();
        // Generate the points
        List<LatLng> points = new ArrayList<LatLng>();
        int totalPonts = 50; // number of corners of the pseudo-circle
        for (int i = 0; i < totalPonts; i++) {
            points.add(getPoint(center, (radius*(1200)), i * 2 * Math.PI / totalPonts));
        }
        // Create and return the polygon
        return map.addPolygon(new PolygonOptions().addAll(points).strokeWidth(4).strokeColor(0x299abb).fillColor(0xaa33c1ea));
    }
}
