package com.ctmrepository;

import com.ctmrepository.controller.MinecraftMapController;
import com.ctmrepository.model.MinecraftMap;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private MinecraftMapController controller;

    @Test
    void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
        assertThat(controller.index().equals("Greetings from Spring Boot!")).isTrue();
    }

    @Test
    void mapsLoad() throws Exception {
        assertThat(controller.getMapCount()).isNotNull();
        assertThat(controller.getMapCount().getStatusCodeValue() > 0).isTrue();
    }

    @Test
    void mapsAreAccessible() throws Exception {
        assertThat(controller.getMapCount().getStatusCodeValue() > 0).isTrue();
        int size = controller.getMapCount().getStatusCodeValue();
        assertThat(controller.getMapById(0).getStatusCode().equals(HttpStatus.NOT_FOUND)).isTrue();
        for (int i = 1; i <= size; i++) {
            assertThat(controller.getMapById(i)).isNotNull();
        }
    }

    @Test
    void mapsAreStrictSearchable() throws Exception {
        // Test that Maps are searchable
        assertThat(controller.getMapSearch("", 1, 20, true)).isNotNull();

        // General Case
        int max_test_map_size = Math.min(controller.getMapCount().getBody() / 2, 25);
        MinecraftMap[] testMaps = new MinecraftMap[max_test_map_size];
        for (int i = 0; i < testMaps.length; i++) {
            testMaps[i] = controller.getMapById(
                    (int) (Math.random() * (controller.getMapCount().getBody() - 1) + 1))
                    .getBody();
        }
        for (MinecraftMap map : testMaps) {
            List<MinecraftMap> searchMap = controller
                    .getMapSearch(map.getName(), 1, 20, true).getBody();
            assertThat(searchMap.stream().filter(o -> o.getId() == map.getId())
                    .findFirst().isPresent()).isTrue();
        }

        // Specific Case
        List<MinecraftMap> searchMap = controller
                .getMapSearch("Tecnocraft2802", 1, 20, true).getBody();
        assertThat(searchMap.stream().filter(o -> o.getId() == 6)
                .findFirst().isPresent()).isTrue();
        assertThat(searchMap.stream().filter(o -> o.getId() == 7)
                .findFirst().isPresent()).isTrue();
    }

    @Test
    void mapsAreFuzzySearchable() throws Exception {
        // Test that Maps are searchable
        assertThat(controller.getMapSearch("", 1, 20, true)).isNotNull();
        assertThat(controller.getMapSearch("", 1, 20, false)).isNotNull();

        // General Case
        int max_test_map_size = Math.min(controller.getMapCount().getBody() / 2, 25);
        MinecraftMap[] testMaps = new MinecraftMap[max_test_map_size];
        for (int i = 0; i < testMaps.length; i++) {
            testMaps[i] = controller.getMapById(
                    (int) (Math.random() * (controller.getMapCount().getBody() - 1) + 1))
                    .getBody();
        }
        for (MinecraftMap map : testMaps) {
            List<MinecraftMap> searchMap = controller
                    .getMapSearch(map.getName(), 1, 20, false).getBody();
            assertThat(searchMap.stream().filter(o -> o.getId() == map.getId())
                    .findFirst().isPresent()).isTrue();
        }

        // Specific Case
        List<MinecraftMap> searchMap = controller
                .getMapSearch("Tecnocraft2802", 1, 20, false).getBody();
        assertThat(searchMap.stream().filter(o -> o.getId() == 6)
                .findFirst().isPresent()).isTrue();
        assertThat(searchMap.stream().filter(o -> o.getId() == 7)
                .findFirst().isPresent()).isTrue();
    }

    @Test
    @Order(6)
    void canPublishMaps() {
            
    }

    @Test
    @Order(7)
    void canRetractMaps() {
            
    }
}
