package com.project.geofencing.repository;

import com.project.geofencing.model.GeoCoordinates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeoCoordinatesRepository extends JpaRepository<GeoCoordinates, Long> {

    List<GeoCoordinates> findByMapType(String maptype);

    GeoCoordinates findByMapTypeAndId(String maptype, Long id);

    List<GeoCoordinates> findByMapTypeAndName(String maptype, String name);

    List<String> findAllNamesByMapType(String type);
}
