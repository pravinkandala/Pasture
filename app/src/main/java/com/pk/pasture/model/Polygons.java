package com.pk.pasture.model;


import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Polygons implements Serializable{

    Polygons polygon;

    private List<LatLng> val = new ArrayList<>();

    public Polygons getPolygon() { return polygon; }
    public void setPolygon(Polygons polygon) { this.polygon = polygon; }


}
