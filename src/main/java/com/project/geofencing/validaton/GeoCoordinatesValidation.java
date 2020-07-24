package com.project.geofencing.validaton;

import com.project.geofencing.controller.GeoCoordinatesController;
import com.project.geofencing.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
@Component
public class GeoCoordinatesValidation {

    @Value("${mapType}")
    public String[] mapTypes;

    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(GeoCoordinatesController.class);

    /**
     * To Check if the given maptype  present or not in maptypes array.
     *
     * @param mapType - pass the maptype to validate
     * @throws ResourceNotFoundException - If not found throws exception.
     */
    public boolean validateMapType(String mapType) throws ResourceNotFoundException {
        boolean flag = true;
        if (!Arrays.asList(mapTypes).contains(mapType)) {
            flag = false;
            log.error("Invaild Map type: " + mapType
                    + " :: Expected: " + Arrays.asList(mapTypes));
            throw new ResourceNotFoundException("Invaild Map type: " + mapType
                    + " :: Expected: " + Arrays.asList(mapTypes));
        }
        log.debug("Map Type vaildated successfully");
        return flag;
    }

    /**
     * To Check if the given co ordinates matches the regular expression or not
     *
     * @param coords - pass the co ordinates to validate.
     * @throws ResourceNotFoundException If not matched throws exception.
     */
    public boolean validateCoords(String coords) throws ResourceNotFoundException {
        String regex = "^[0-9.,;/-]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(coords);
        boolean flag = true;
        if (!m.matches()) {
            flag = false;
            log.error("Invaild co ordinates format");
            throw new ResourceNotFoundException("Invaild co ordinates format");
        }
        return flag;
    }

    /**
     * To Check if the given co ordinates forms a polygon or not
     *
     * @param coords - pass the co ordinates to validate .
     * @throws ResourceNotFoundException not matched throws exception.
     */
    public boolean vaildatePolygon(String coords) throws ResourceNotFoundException {
        boolean flag = true;
        if (coords != null && coords.length() > 0) {
            int length = coords.length();
            if (coords.charAt(length - 1) == ';') {
                coords = coords.substring(0, (length - 1));
            }
            int valuesCount = (coords.split(";")).length;
            if (valuesCount == (coords.split(",")).length - 1) {
                log.debug("Polygon can be formed using given co ordinates");
            } else {
                log.error("Polygon cannot be formed using given co ordinates");
                throw new ResourceNotFoundException("Polygon cannot be formed using given co ordinates");
            }
        } else {
            flag = false;
            log.error("Co ordinates are not passed");
            throw new ResourceNotFoundException("Co ordinates are not passed");
        }
        return flag;
    }
}
