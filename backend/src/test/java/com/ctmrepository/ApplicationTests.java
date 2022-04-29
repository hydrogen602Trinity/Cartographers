package com.ctmrepository;

import com.ctmrepository.controller.MinecraftMapController;
import com.ctmrepository.model.MinecraftMap;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
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
    private MinecraftMap[] publishedTestMaps;

    @Test
    @Order(1)
    void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
        assertThat(controller.index().equals("Greetings from Spring Boot!")).isTrue();
    }

    @Test
    @Order(2)
    void mapsLoad() throws Exception {
        assertThat(controller.getMapCount()).isNotNull();
        assertThat(controller.getMapCount().getBody() > 0).isTrue();
    }

    @Test
    @Order(3)
    void mapsAreAccessible() throws Exception {
        assertThat(controller.getMapCount().getStatusCodeValue() > 0).isTrue();
        int size = controller.getMapCount().getStatusCodeValue();
        assertThat(controller.getMapById(0).getStatusCode().equals(HttpStatus.NOT_FOUND)).isTrue();
        for (int i = 1; i <= size; i++) {
            assertThat(controller.getMapById(i)).isNotNull();
        }
    }

    @Test
    @Order(4)
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
    @Order(5)
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
    void canAccessUnpublishedMaps() throws Exception {
        assertThat(controller.getUnpublishedMaps()).isNotNull();
        assertThat(controller.getUnpublishedMaps().getStatusCode().equals(HttpStatus.OK)).isTrue();
    }

    @Test
    @Order(7)
    void canPublishMaps() throws Exception {
        publishedTestMaps = new MinecraftMap[5];

        // Try and publish already published maps, prove you can't
        List<MinecraftMap> pubMaps = controller.getPublishedMaps().getBody();
        for (int i = 0; i < Math.min(controller.getMapCount().getBody() / 2, 25); i++) {
            MinecraftMap testMap = pubMaps.get(
                    (int) (Math.random() * pubMaps.size()));
            assertThat(controller.publishMap(testMap.getId())
                    .getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)).isTrue();
        }

        // Try and publish not-published maps, prove you can
        List<MinecraftMap> unPubMaps = controller.getUnpublishedMaps().getBody();
        for (int i = 0; i < Math.min(publishedTestMaps.length, unPubMaps.size()); i++) {
            int rand = (int) Math.random() * unPubMaps.size();
            publishedTestMaps[i] = unPubMaps.get(rand);
            unPubMaps.remove(rand);
            assertThat(controller.publishMap(publishedTestMaps[i].getId())
                    .getStatusCode().equals(HttpStatus.OK)).isTrue();
        }

        // Try and mess it up, prove internal server error
        pubMaps = controller.getPublishedMaps().getBody();
        for (int i = 0; i < Math.min(publishedTestMaps.length, pubMaps.size()); i++) {
            int rand = (int) Math.random() * pubMaps.size();
            MinecraftMap pubMap = pubMaps.get(rand);
            pubMaps.remove(rand);
            assertThat(controller.publishMap(pubMap.getId())
                    .getStatusCode().equals(HttpStatus.OK)).isFalse();
        }
    }

    @Test
    @Order(8)
    void canRetractMaps() throws Exception {
        if (publishedTestMaps == null) {
            publishedTestMaps = new MinecraftMap[5];
            List<MinecraftMap> unPubMaps = controller.getUnpublishedMaps().getBody();
            for (int i = 0; i < Math.min(publishedTestMaps.length, unPubMaps.size()); i++) {
                int rand = (int) Math.random() * unPubMaps.size();
                publishedTestMaps[i] = unPubMaps.get(rand);
                unPubMaps.remove(rand);
                controller.publishMap(publishedTestMaps[i].getId());
            }
        }

        List<MinecraftMap> pubMaps = controller.getPublishedMaps().getBody();
        List<MinecraftMap> unPubMaps = controller.getUnpublishedMaps().getBody();

        // Try and retract not-published maps, prove you can't
        for (int i = 0; i < Math.min(unPubMaps.size(), 25); i++) {
            MinecraftMap testMap = unPubMaps.get(
                    (int) (Math.random() * unPubMaps.size()));
            assertThat(controller.retractMap(testMap.getId())
                    .getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)).isTrue();
        }

        // Try and retract published maps, prove you can
        // Maps were already set at the beginning of the method or in the
        // canPublishMaps test (above)
        for (int i = 0; i < Math.min(publishedTestMaps.length, pubMaps.size()); i++) {
            assertThat(publishedTestMaps[i].isPublished()).isTrue();
            assertThat(controller.retractMap(publishedTestMaps[i].getId())
                    .getStatusCode().equals(HttpStatus.OK)).isTrue();
        }

        // Try and mess it up, prove internal server error
        unPubMaps = controller.getPublishedMaps().getBody();
        for (int i = 0; i < Math.min(publishedTestMaps.length, unPubMaps.size()); i++) {
            int rand = (int) Math.random() * unPubMaps.size();
            MinecraftMap unPubMap = unPubMaps.get(rand);
            unPubMaps.remove(rand);
            assertThat(controller.retractMap(unPubMap.getId())
                    .getStatusCode().equals(HttpStatus.OK)).isFalse();
        }
    }
}
