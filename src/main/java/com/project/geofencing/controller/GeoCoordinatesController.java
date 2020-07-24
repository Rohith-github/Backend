package com.project.geofencing.controller;

import com.project.geofencing.exception.ResourceNotFoundException;
import com.project.geofencing.model.GeoCoordinates;
import com.project.geofencing.repository.GeoCoordinatesRepository;
import com.project.geofencing.service.GeoCoordinatesService;
import com.project.geofencing.validaton.GeoCoordinatesValidation;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/geocoord")
@CrossOrigin(origins = "*")
public class GeoCoordinatesController {

    @Autowired
    private GeoCoordinatesRepository geoCoordinatesRepository;

    @Autowired
    private GeoCoordinatesService geoCoordinatesService;

    @Autowired
    private GeoCoordinatesValidation geoCoordinatesValidation;

    private String string;

    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(GeoCoordinatesController.class);

    /**
     * Gets all geo coordinates for the given maptype.
     *
     * @param mapType - pass the type of map for which you want to retrive the coordinates.
     * @return Returns the list of geo coordinates.
     * @throws ResourceNotFoundException - If not found throws exception.
     */
    @ApiOperation("Get all geo coordinates for the given maptype.")
    @GetMapping("/maptype/{mapType}")
    public List<GeoCoordinates> getAllGeoCords(
            @PathVariable(value = "mapType") String mapType)
            throws ResourceNotFoundException {
        log.debug("Received call at getAllGeoCords");
        geoCoordinatesValidation.validateMapType(mapType);
        return geoCoordinatesService.fetchAllGeoCords(mapType);
    }


    /**
     * Gets geo coordinates for the given maptype and id.
     *
     * @param mapType   - pass the type of map for which you want to retrive the coordinates
     * @param geoCordId - pass the id for which you want to retrive the coordinates
     * @return Returns geo coordinates matching with given Id and MapType.
     * @throws ResourceNotFoundException - If not found throws exception.
     */
    @ApiOperation("Get geo coordinates for the given maptype and id.")
    @GetMapping("/maptype/{mapType}/id/{id}")
    public ResponseEntity<GeoCoordinates> getGeoCordsById(
            @PathVariable(value = "mapType") String mapType,
            @PathVariable(value = "id") Long geoCordId)
            throws ResourceNotFoundException {
        log.debug("Received call at getGeoCordsById");
        geoCoordinatesValidation.validateMapType(mapType);
        GeoCoordinates geoCoordinates = geoCoordinatesService.fetchGeoCordsById(mapType, geoCordId);
        return ResponseEntity.ok().body(geoCoordinates);
    }

    /**
     * Gets geo coordinates for the given maptype and id.
     *
     * @param mapType     - pass the type of map for which you want to retrive the coordinates
     * @param geoCordName - pass the name for which you want to retrive the coordinates
     * @return Returns List geo coordinates.
     * @throws ResourceNotFoundException - If not found throws exception.
     */
    @ApiOperation("Get geo coordinates for the given maptype and name.")
    @GetMapping("/maptype/{mapType}/name/{name}")
    public ResponseEntity<List<GeoCoordinates>> getGeoCordsByName(
            @PathVariable(value = "mapType") String mapType,
            @PathVariable(value = "name") String geoCordName)
            throws ResourceNotFoundException {
        log.debug("Received call at getGeoCordsByName");
        geoCoordinatesValidation.validateMapType(mapType);
        List<GeoCoordinates> geoCords = geoCoordinatesService.fetchGeoCordsByName(mapType, geoCordName);
        return ResponseEntity.ok().body(geoCords);
    }

    /**
     * Gets distinct geo coordinates for the given maptype.
     *
     * @param mapType - pass the type of map for which you want to retrive the coordinates
     * @return Returns the  list of geo coordinates names.
     * @throws ResourceNotFoundException - If not found throws exception.
     */
    @ApiOperation("Get all geo coordinates names for the given maptype.")
    @GetMapping("/names/maptype/{mapType}")
    public List<String> getAllDistinctGeoCordsName(
            @PathVariable(value = "mapType") String mapType)
            throws ResourceNotFoundException {
        log.debug("Received call at getAllDistinctGeoCordsName");
        geoCoordinatesValidation.validateMapType(mapType);
        return geoCoordinatesService.fetchGeoCordsAllNames(mapType);
    }

    /**
     * Creates a entry in the database with given request body.
     *
     * @param geoCoordinates - pass the geo coordinates with the name to it.
     * @return Returns the geo coordinates.
     */
    @ApiIgnore
    @PostMapping
    public GeoCoordinates createGeoCords(@Valid @RequestBody GeoCoordinates geoCoordinates) throws ResourceNotFoundException {
        string = geoCoordinates.getCoords();
        geoCoordinatesValidation.validateCoords(string);
        geoCoordinatesValidation.vaildatePolygon(string);
        log.debug("Received call at createGeoCords");
        return geoCoordinatesService.saveGeoCords(geoCoordinates);
    }


    /**
     * Creates multiple entry in the database with given request body.
     *
     * @param geoCords - pass the arrary of geo coordinates with the name to it.
     * @return Returns the list geo coordinates.
     */
    @ApiIgnore
    @PostMapping("/bulk")
    public List<GeoCoordinates> createAllGeoCords(@Valid @RequestBody List<GeoCoordinates> geoCords) throws ResourceNotFoundException {
        for (GeoCoordinates geoCoordinates1 : geoCords) {
            string = geoCoordinates1.getCoords();
            geoCoordinatesValidation.validateCoords(string);
            geoCoordinatesValidation.vaildatePolygon(string);
        }
        log.debug("Received call at createAllGeoCords");
        return geoCoordinatesService.saveAllGeoCords(geoCords);
    }

    /**
     * Updates the geo coordinates for the given id
     *
     * @param geoCordId      - pass the geo coordinates id which you want to update
     * @param geoCordDetails - pass the geo coordinates which you want to update
     * @return Returns the list of updated geo coordinates.
     * @throws ResourceNotFoundException - If not found throws exception
     */
    @ApiIgnore
    @PutMapping("/id/{id}")
    public ResponseEntity<GeoCoordinates> updateGeoCords(
            @PathVariable(value = "id") Long geoCordId,
            @Valid @RequestBody GeoCoordinates geoCordDetails)
            throws ResourceNotFoundException {
        log.debug("Received call at updateGeoCords");
        GeoCoordinates updatedGeoCoordinates = geoCoordinatesService.alterGeoCords(geoCordId, geoCordDetails);
        return ResponseEntity.ok(updatedGeoCoordinates);
    }

    /**
     * Deletes the geo coordinates for the given id
     *
     * @param geoCordId - pass the geo coordinates id which you want to delete
     * @return Returns the map with the status of deletion.
     * @throws Exception - If not found throws exception
     */
    @ApiIgnore
    @DeleteMapping("/id/{id}")
    public Map<String, Boolean> deleteGeoCords(@PathVariable(value = "id") Long geoCordId) throws Exception {
        log.debug("Received call at deleteGeoCords");
        GeoCoordinates geoCoordinates = geoCoordinatesService.removeGeoCords(geoCordId);
        geoCoordinatesRepository.delete(geoCoordinates);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

}
