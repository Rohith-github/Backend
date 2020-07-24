package com.project.geofencing.service;

import com.project.geofencing.exception.ResourceNotFoundException;
import com.project.geofencing.model.GeoCoordinates;

import java.util.List;

public interface GeoCoordinatesService {

    GeoCoordinates saveGeoCords(GeoCoordinates geoCoordinates);

    List<GeoCoordinates> saveAllGeoCords(List<GeoCoordinates> geoCords);

    List<GeoCoordinates> fetchAllGeoCords(String type) throws ResourceNotFoundException;

    GeoCoordinates fetchGeoCordsById(String type, Long id) throws ResourceNotFoundException;

    List<GeoCoordinates> fetchGeoCordsByName(String type, String name) throws ResourceNotFoundException;

    List<String> fetchGeoCordsAllNames(String type) throws ResourceNotFoundException;

    GeoCoordinates removeGeoCords(Long id) throws Exception;

    GeoCoordinates alterGeoCords(Long id, GeoCoordinates geoCoordinates) throws ResourceNotFoundException;
}
