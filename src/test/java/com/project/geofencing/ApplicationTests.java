package com.project.geofencing;

import com.project.geofencing.exception.ResourceNotFoundException;
import com.project.geofencing.model.GeoCoordinates;
import com.project.geofencing.validaton.GeoCoordinatesValidation;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String getRootUrl() {
        return "http://localhost:" + port;
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void testValidateCoords() {
        GeoCoordinatesValidation geoCoordinatesValidation = new GeoCoordinatesValidation();
        String[] string = {"12.935022836471118,77.61171432204848;12.934913042283574,-77.61184977360374;"
                , "12.935022836471118,77.61171432204848;12.934913042283574,77.611849lhfid77360374;"};
        for (String s : string) {
            try {
                Assert.assertTrue(geoCoordinatesValidation.validateCoords(s));
            } catch (ResourceNotFoundException e) {
                Assert.assertEquals(e.getMessage(), "Invaild co ordinates format");
                Assert.assertEquals(e.getMessage(), "Invaild co ordinates format");
            }
        }
    }

    @Test
    public void testValidatePolygon() {
        GeoCoordinatesValidation geoCoordinatesValidation = new GeoCoordinatesValidation();
        String[] string = {"12.935022836471118,77.61171432204848;12.934913042283574,-77.61184977360374;",
                "12.93502283647111877.61171432204848;12.934913042283574,-77.61184977360374;"};
        for (String s : string) {
            try {
                Assert.assertTrue(geoCoordinatesValidation.vaildatePolygon(s));
            } catch (ResourceNotFoundException e) {
                Assert.assertEquals(e.getMessage(), "Polygon cannot be formed using given co ordinates");
            }
        }
    }

    @Test
    public void testValidateMapType() {
        GeoCoordinatesValidation geoCoordinatesValidation = new GeoCoordinatesValidation();
        geoCoordinatesValidation.mapTypes = new String[]{"GoogleMaps", "MapBox"};
        String[] string = {"GoogleMaps", "MapBox", "", "googlemaps", "maps"};
        for (String s : string) {
            try {
                Assert.assertTrue(geoCoordinatesValidation.validateMapType(s));
            } catch (ResourceNotFoundException e) {
                Assert.assertEquals(e.getMessage(), "Invaild Map type: " + s
                        + " :: Expected: " + Arrays.asList(geoCoordinatesValidation.mapTypes));
            }
        }
    }

    @Test
    public void testGetAllGeoCords() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/maptype/GoogleMaps",
                HttpMethod.GET, entity, String.class);

        Assert.assertNotNull(response.getBody());
    }

    @Test
    public void testGetGeoCordsById() {
        GeoCoordinates geoCoordinates = restTemplate.getForObject(getRootUrl() + "/maptype/GoogleMaps/id/1", GeoCoordinates.class);
        Assert.assertNotNull(geoCoordinates);
    }

    @Test
    public void testGetGeoCordsByName() {
        GeoCoordinates geoCoordinates = restTemplate.getForObject(getRootUrl() + "/maptype/MapBox/name/goefence-1", GeoCoordinates.class);
        Assert.assertNotNull(geoCoordinates);
    }

    @Test
    public void testGetAllDistinctGeoCordsName() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/names/maptype/MapBox",
                HttpMethod.GET, entity, String.class);

        Assert.assertNotNull(response.getBody());
    }

    @Test
    public void testCreateGeoCords() {
        GeoCoordinates geoCoordinates = new GeoCoordinates();
        geoCoordinates.setCoords("132435437");
        geoCoordinates.setCreatedBy("admin");
        geoCoordinates.setUpdatedBy("admin");

        ResponseEntity<GeoCoordinates> postResponse = restTemplate.postForEntity(getRootUrl(), geoCoordinates, GeoCoordinates.class);
        Assert.assertNotNull(postResponse);
        Assert.assertNotNull(postResponse.getBody());
    }

    @Test
    public void testUpdateGeoCords() {
        int id = 1;
        GeoCoordinates geoCoordinates = restTemplate.getForObject(getRootUrl() + "id/1" + id, GeoCoordinates.class);
        geoCoordinates.setCoords("413532624");
        geoCoordinates.setUpdatedBy("admin");

        restTemplate.put(getRootUrl() + "/geoCords/" + id, geoCoordinates);

        GeoCoordinates updatedGeoCoordinates = restTemplate.getForObject(getRootUrl() + "id/1" + id, GeoCoordinates.class);
        Assert.assertNotNull(updatedGeoCoordinates);
    }

    @Test
    public void testDeletePost() {
        int id = 2;
        GeoCoordinates geoCoordinates = restTemplate.getForObject(getRootUrl() + "id/2" + id, GeoCoordinates.class);
        Assert.assertNotNull(geoCoordinates);

        restTemplate.delete(getRootUrl() + "/users/" + id);

        try {
            geoCoordinates = restTemplate.getForObject(getRootUrl() + "id/2" + id, GeoCoordinates.class);
        } catch (final HttpClientErrorException e) {
            Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }
    }

}
