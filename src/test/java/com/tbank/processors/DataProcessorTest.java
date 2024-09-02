package com.tbank.processors;

import com.tbank.model.City;
import com.tbank.model.Coords;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class DataProcessorTest {

    private final DataProcessor<City> cityProcessor = new DataProcessor<>();

    @Test
    void testReadValidJson() {
        City city = cityProcessor.readJson("src/test/resources/city.json", City.class);
        assertNotNull(city);
        assertEquals("spb", city.getSlug());
        assertEquals(59.939095, city.getCoords().getLat());
        assertEquals(30.315868, city.getCoords().getLon());
    }

    @Test
    void testReadInvalidJson() {
        City city = cityProcessor.readJson("src/test/resources/city-error.json", City.class);
        assertNull(city);
    }

    @Test
    void testConvertToXml() {
        City city = new City("spb", new Coords(59.939095, 30.315868));
        String xml = cityProcessor.toXML(city);
        assertNotNull(xml);
        assertTrue(xml.contains("<City><slug>spb</slug><coords><lat>59.939095</lat><lon>30.315868</lon></coords></City>"));
    }

    @Test
    void testSaveToFile() {
        String data = "<City><slug>spb</slug></City>";
        String filePath = "src/test/resources/output.xml";
        cityProcessor.saveToFile(data, filePath);
        assertTrue(new File(filePath).exists());
    }
}
