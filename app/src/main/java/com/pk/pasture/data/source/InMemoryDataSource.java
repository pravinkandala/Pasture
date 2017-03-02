package com.pk.pasture.data.source;

import com.pk.pasture.model.Polygons;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDataSource implements DataSource {
    private static List<Polygons> mPolygons = new ArrayList<>();
    public List<Polygons> getPolygons() { return mPolygons; }
}
