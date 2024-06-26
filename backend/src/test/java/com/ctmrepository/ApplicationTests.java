package com.ctmrepository;

import com.ctmrepository.controller.MinecraftMapController;
import com.ctmrepository.model.MinecraftMap;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private MinecraftMapController controller;

    /**
     * @throws Exception
     */
    @Test
    void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
        assertThat(controller.index().equals("Greetings from Spring Boot!")).isTrue();
    }

    /**
     * @throws Exception
     */
    @Test
    void mapsLoad() throws Exception {
        assertThat(controller.getMapCount()).isNotNull();
        assertThat(controller.getMapCount().getBody() > 0).isTrue();
    }

    /**
     * @throws Exception
     */
    @Test
    void mapsAreAccessible() throws Exception {
        assertThat(controller.getMapCount().getStatusCodeValue() > 0).isTrue();
        int size = controller.getMapCount().getStatusCodeValue();
        assertThat(controller.getMapById(0).getStatusCode().equals(HttpStatus.NOT_FOUND)).isTrue();
        for (int i = 1; i <= size; i++) {
            assertThat(controller.getMapById(i)).isNotNull();
        }
    }

    /**
     * @throws Exception
     */
    @Test
    void mapsAreStrictSearchable() throws Exception {
        // Test that Maps are searchable
        assertThat(controller.getMapSearch("", 1, 20, true)).isNotNull();

        // General Case
        int max_test_map_size = Math.min(controller.getMapCount().getBody() / 2, 25);
        MinecraftMap[] testMaps = new MinecraftMap[max_test_map_size];
        List<MinecraftMap> pubMaps = controller.publishedMaps();
        for (int i = 0; i < testMaps.length; i++) {
            int rand = (int) (Math.random() * pubMaps.size());
            testMaps[i] = pubMaps.get(rand);
            pubMaps.remove(rand);
        }
        for (MinecraftMap map : testMaps) {
            List<MinecraftMap> searchMap = controller
                    .getMapSearch(map.getName(), 1, 20, true).getBody().data;
            assertThat(searchMap.stream().filter(o -> o.getId() == map.getId())
                    .findFirst().isPresent()).isTrue();
        }
    }

    /**
     * @throws Exception
     */
    @Test
    void mapsAreFuzzySearchable() throws Exception {
        // Test that Maps are searchable
        assertThat(controller.getMapSearch("", 1, 20, true)).isNotNull();
        assertThat(controller.getMapSearch("", 1, 20, false)).isNotNull();

        // General Case
        int max_test_map_size = Math.min(controller.getMapCount().getBody() / 2, 25);
        MinecraftMap[] testMaps = new MinecraftMap[max_test_map_size];
        List<MinecraftMap> pubMaps = controller.publishedMaps();
        for (int i = 0; i < testMaps.length; i++) {
            int rand = (int) (Math.random() * pubMaps.size());
            testMaps[i] = pubMaps.get(rand);
            pubMaps.remove(rand);
        }
        for (MinecraftMap map : testMaps) {
            List<MinecraftMap> searchMap = controller
                    .getMapSearch(map.getName(), 1, 20, false).getBody().data;
            assertThat(searchMap.stream().filter(o -> o.getId() == map.getId())
                    .findFirst().isPresent()).isTrue();
        }
    }

    /**
     * @throws Exception
     */
    @Test
    void searchHasMaxPages() {
        assertThat(controller.getMapSearch("", 1, 20, true)).isNotNull();
        assertThat(controller.getMapSearch("", 1, 20, true).getBody().max_page > 0).isTrue();
    }

    @Test
    void canAccessUnpublishedMaps() throws Exception {
        assertThat(controller.getUnpublishedMaps()).isNotNull();
        assertThat(controller.getUnpublishedMaps().getStatusCode().equals(HttpStatus.OK)).isTrue();
    }

    /**
     * @throws Exception
     */
    @Test
    void canPublishMaps() throws Exception {
        // Try and publish already published maps, prove you can't
        List<MinecraftMap> pubMaps = controller.publishedMaps();
        for (int i = 0; i < 5; i++) {
            MinecraftMap testMap = pubMaps.get(
                    (int) (Math.random() * pubMaps.size()));
            assertThat(controller.publishMap(testMap.getId())
                    .getStatusCode().equals(HttpStatus.BAD_REQUEST)).isTrue();
        }

        // Try and publish not-published maps, prove you can
        List<MinecraftMap> unPubMaps = controller.getUnpublishedMaps().getBody();
        for (int i = 0; i < Math.min(10, unPubMaps.size()); i++) {
            int rand = (int) Math.random() * unPubMaps.size();
            MinecraftMap publishedTestMap = unPubMaps.get(rand);
            unPubMaps.remove(rand);
            assertThat(controller.publishMap(publishedTestMap.getId())
                    .getStatusCode().equals(HttpStatus.OK)).isTrue();
        }

        // Try and mess it up, prove internal server error
        pubMaps = controller.publishedMaps();
        for (int i = 0; i < Math.min(15, pubMaps.size()); i++) {
            int rand = (int) Math.random() * pubMaps.size();
            MinecraftMap pubMap = pubMaps.get(rand);
            pubMaps.remove(rand);
            assertThat(controller.publishMap(pubMap.getId())
                    .getStatusCode().equals(HttpStatus.OK)).isFalse();
        }
    }

    /**
     * @throws Exception
     */
    @Test
    void canRetractMaps() throws Exception {
        // Try and retract not-published maps, prove you can't
        List<MinecraftMap> unPubMaps = controller.getUnpublishedMaps().getBody();
        for (int i = 0; i < Math.min(unPubMaps.size(), 25); i++) {
            MinecraftMap testMap = unPubMaps.get(
                    (int) (Math.random() * unPubMaps.size()));
            assertThat(controller.retractMap(testMap.getId())
                    .getStatusCode().equals(HttpStatus.BAD_REQUEST)).isTrue();
        }

        // Try and retract published maps, prove you can
        List<MinecraftMap> pubMaps = controller.publishedMaps();
        int j = Math.min(15, pubMaps.size());
        for (int i = 0; i < j; i++) {
            int rand = (int) (Math.random() * unPubMaps.size());
            assertThat(controller.retractMap(pubMaps.get(rand).getId())
                    .getStatusCode().equals(HttpStatus.OK)).isTrue();
            pubMaps.remove(rand);
        }

        // Try and mess it up, prove internal server error
        unPubMaps = controller.getUnpublishedMaps().getBody();
        for (int i = 0; i < Math.min(15, unPubMaps.size()); i++) {
            int rand = (int) Math.random() * unPubMaps.size();
            MinecraftMap unPubMap = unPubMaps.get(rand);
            unPubMaps.remove(rand);
            assertThat(controller.retractMap(unPubMap.getId())
                    .getStatusCode().equals(HttpStatus.OK)).isFalse();
        }
    }
}
