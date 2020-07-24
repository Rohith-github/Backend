package com.project.geofencing.service;

import com.project.geofencing.repository.GeoCoordinatesRepository;
import com.project.geofencing.exception.ResourceNotFoundException;
import com.project.geofencing.model.GeoCoordinates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class GeoCoordinatesServiceImpl implements GeoCoordinatesService {

    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(GeoCoordinatesServiceImpl.class);

    @Autowired
    private GeoCoordinatesRepository geoCoordinatesRepository;

    /**
     * Saves the Geo coordinates in the db.
     *
     * @param geoCoordinates - pass geo coordinates to be saved.
     * @return Returns the saved geo coordinates.
     */
    @Override
    public GeoCoordinates saveGeoCords(GeoCoordinates geoCoordinates) {
        log.debug("Received call at saveGeoCords");
        return geoCoordinatesRepository.save(geoCoordinates);
    }

    /**
     * Saves the list of Geo coordinates in the db.
     *
     * @param geoCords - pass list geo coordinates to be saved.
     * @return Returns the list of saved geo coordinates.
     */
    @Override
    public List<GeoCoordinates> saveAllGeoCords(List<GeoCoordinates> geoCords) {
        log.debug("Received call at saveAllGeoCords");
        return geoCoordinatesRepository.saveAll(geoCords);
    }

    /**
     * Fetches all the geo coordinates.
     *
     * @param type - pass the maptype from which it should fetch the coordinates.
     * @return Returns the list of fetched geo coordinates.
     * @throws ResourceNotFoundException - If not found throws exception.
     */
    @Override
    public List<GeoCoordinates> fetchAllGeoCords(String type) throws ResourceNotFoundException {
        log.debug("Received call at fetchAllGeoCords");
        List<GeoCoordinates> geoCoordinatesList = geoCoordinatesRepository.findByMapType(type);
        if (null == geoCoordinatesList) {
            log.error("Geo Co-Ordinates not found");
            throw new ResourceNotFoundException("Geo Co-Ordinates not found");
        }
        log.debug("Geo Co-Ordinates fetched successfully");
        return geoCoordinatesList;
    }

    /**
     * Fetches the geo coordinates for given maptype and id.
     *
     * @param type - pass the maptype from which it should fetch the coordinates.
     * @param id   - pass the id to fetch the coordinates.
     * @return Returns the fetched geo coordinates.
     * @throws ResourceNotFoundException - If not found throws exception.
     */
    @Override
    public GeoCoordinates fetchGeoCordsById(String type, Long id) throws ResourceNotFoundException {
        log.debug("Received call at fetchGeoCordsById");
        GeoCoordinates geoCoordinates = geoCoordinatesRepository.findByMapTypeAndId(type, id);
        if (null == geoCoordinates) {
            log.error("Geo Co-Ordinates not found on :: " + id);
            throw new ResourceNotFoundException("Geo Co-Ordinates not found on :: " + id);
        }
        log.debug("Geo Co-Ordinates fetched successfully with id :: " + id);
        return geoCoordinates;
    }

    /**
     * Fetches the geo coordinates for given maptype and name.
     *
     * @param type - pass the maptype from which it should fetch the coordinates.
     * @param name - pass the name to fetch the coordinates
     * @return Returns the fetched list of geo coordinates.
     * @throws ResourceNotFoundException - If not found throws exception.
     */
    @Override
    public List<GeoCoordinates> fetchGeoCordsByName(String type, String name) throws ResourceNotFoundException {
        log.debug("Received call at fetchGeoCordsByName");
        List<GeoCoordinates> geoCoordinatesList = geoCoordinatesRepository.findByMapTypeAndName(type, name);
        if (null == geoCoordinatesList || geoCoordinatesList.size() == 0) {
            log.error("Geo Co-Ordinates not found with :: " + name);
            throw new ResourceNotFoundException("Geo Co-Ordinates not found with :: " + name);
        }
        log.debug("Geo Co-Ordinates fetched successfully with name ::" + name);
        return geoCoordinatesList;
    }

    /**
     * Fetches the all geo coordinates names for given maptype.
     *
     * @param type - pass the maptype from which it should fetch the coordinates.
     * @return Returns the fetched list of names of geo coordinates.
     * @throws ResourceNotFoundException - If not found throws exception.
     */
    @Override
    public List<String> fetchGeoCordsAllNames(String type) throws ResourceNotFoundException {
        log.debug("Received call at saveAllGeoCords");
        List<String> geStrings = geoCoordinatesRepository.findAllNamesByMapType(type);
        if (null == geStrings || geStrings.size() == 0) {
            log.error("No GeoFence available for maptype  :: " + type);
            throw new ResourceNotFoundException("No GeoFence available for maptype  :: " + type);
        }
        log.debug("Geo Co-Ordinates fetched successfully for maptype ::" + type);
        return geStrings;
    }

    /**
     * Deletes the geo coordinates names for given  id.
     *
     * @param id - pass the id to delete the coordinates.
     * @return Returns the deleted geo coordinates.
     * @throws Exception - If not found throws exception.
     */
    @Override
    public GeoCoordinates removeGeoCords(Long id) throws Exception {
        log.debug("Received call at removeGeoCords");
        return geoCoordinatesRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Geo Co-Ordinates not found on :: " + id));
    }

    /**
     * Updates the geo coordinates names for given  id.
     *
     * @param id             - pass the id to update the coordinates.
     * @param geoCoordinates - pass the new geo coordinates the update the db entry.
     * @return Returns the updated geo coordinates.
     * @throws ResourceNotFoundException
     */
    @Override
    public GeoCoordinates alterGeoCords(Long id, GeoCoordinates geoCoordinates) throws ResourceNotFoundException {
        log.debug("Received call at alterGeoCords");
        GeoCoordinates geoCoordinates1 = geoCoordinatesRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Geo Co-Ordinates not found on :: " + id));

        geoCoordinates1.setCoords(geoCoordinates.getCoords());
        geoCoordinates1.setUpdatedBy(geoCoordinates.getUpdatedBy());
        geoCoordinates1.setUpdatedAt(new Date());
        log.debug("Geo Co-Ordinates deleted successfully with id :: " + id);
        return geoCoordinatesRepository.save(geoCoordinates1);
    }

}
