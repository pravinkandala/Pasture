package com.pk.pasture.data.repository;

import com.pk.pasture.data.source.DataSource;
import com.pk.pasture.data.source.InMemoryDataSource;
import com.pk.pasture.model.Polygons;

import java.util.List;


public class LocationRepository {

    private DataSource mDataSource;
    public LocationRepository() { mDataSource = new InMemoryDataSource(); }


    public void add(Polygons polygons) {
        mDataSource.getPolygons().add(polygons);
    }

    /**
     * store list of polygons
     */
    public void addAll(List<Polygons> polygons) {
        mDataSource.getPolygons().clear();
        mDataSource.getPolygons().addAll(polygons);
    }
    /**
     * returns stored list
     */
    public List<Polygons> getAll() {
        return mDataSource.getPolygons();
    }


}
