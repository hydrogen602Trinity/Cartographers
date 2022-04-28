package com.ctmrepository;

import com.ctmrepository.controller.MinecraftMapController;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        assertThat(controller.getMapById(0)).isNull();
        for (int i = 1; i <= size; i++) {
            assertThat(controller.getMapById(i)).isNotNull();
        }
    }

    @Test
    void mapsAreSearchable() throws Exception {
        // Test that Maps are searchable
        assertThat(controller.getMapSearch("", 1, 20, true)).isNotNull();
        assertThat(controller.getMapSearch("", 1, 20, false)).isNotNull();

        // Get maps through Hard Search
        assertThat(controller.getMapSearch("Moon", 1, 20, true)
                .getBody().contains(controller
                        .getMapById(1).getBody()))
                .isTrue();
        assertThat(controller.getMapSearch("Monstrosity", 1, 20, true)
                .getBody().contains(controller
                        .getMapById(3).getBody()))
                .isTrue();
        assertThat(controller.getMapSearch("Tala", 1, 20, true)
                .getBody().contains(controller
                        .getMapById(4).getBody()))
                .isTrue();
        assertThat(controller.getMapSearch("Tecnocraft2802", 1, 20, true)
                .getBody().contains(controller
                        .getMapById(6).getBody()))
                .isTrue();
        assertThat(controller.getMapSearch("Tecnocraft2802", 1, 20, true)
                .getBody().contains(controller
                        .getMapById(7).getBody()))
                .isTrue();

        // Get Maps Through Soft Search
        assertThat(controller.getMapSearch("Moon", 1, 20, false)
                .getBody().contains(controller
                        .getMapById(1).getBody()))
                .isTrue();
        assertThat(controller.getMapSearch("Monstrosity", 1, 20, false)
                .getBody().contains(controller
                        .getMapById(3).getBody()))
                .isTrue();
        assertThat(controller.getMapSearch("Tala", 1, 20, false)
                .getBody().contains(controller
                        .getMapById(4).getBody()))
                .isTrue();
        assertThat(controller.getMapSearch("Tecnocraft2802", 1, 20, false)
                .getBody().contains(controller
                        .getMapById(6).getBody()))
                .isTrue();
        assertThat(controller.getMapSearch("Tecnocraft2802", 1, 20, false)
                .getBody().contains(controller
                        .getMapById(7).getBody()))
                .isTrue();
    }
}
