package com.ctmrepository.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.ctmrepository.model.MinecraftMap;
import com.ctmrepository.model.SearchQueryAndResult;
import com.ctmrepository.repository.MinecraftMapRepository;
import com.ctmrepository.response.SearchResponseEntity;
import com.ctmrepository.service.MinecraftService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@CrossOrigin(origins = { "http://localhost:3000", "https://hydrogen602trinity.github.io" })
@RestController
@RequestMapping("/")
@EnableCaching
public class MinecraftMapController {

    @Autowired
    MinecraftMapRepository minecraftMapRepository;

    @Autowired
    MinecraftService service;

    @Autowired
    CacheManager cacheManager;

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    /**
     * Get the total count of published maps.
     * This is necessary to compute the number of pages in the frontend.
     */
    @GetMapping("/maps/count")
    public ResponseEntity<Integer> getMapCount() {
        return new ResponseEntity<>((int) minecraftMapRepository.count(), HttpStatus.OK);
    }

    /**
     * Get one map by id
     * 
     * @param id the id of the map to get
     */
    @GetMapping("/maps/{id}")
    public ResponseEntity<MinecraftMap> getMapById(@PathVariable("id") long id) {
        Optional<MinecraftMap> mapData = minecraftMapRepository.findById(id);

        if (mapData.isPresent() && mapData.get().isPublished()) {
            return new ResponseEntity<>(mapData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * @param id
     * @return
     */
    @GetMapping("/admin/unpublished-maps/{id}")
    public ResponseEntity<MinecraftMap> getUnpublishedMapById(@PathVariable("id") long id) {
        Optional<MinecraftMap> mapData = minecraftMapRepository.findById(id);

        if (mapData.isPresent() && !mapData.get().isPublished()) {
            return new ResponseEntity<>(mapData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Sorts map database according to query and returns results corresponding to
     * given page and response limit.
     *
     * @param q        search query string
     * @param page     page number of results to return
     * @param per_page maximum number of results to return per page
     * @param strict   if true, search will only return results with exact query
     *                 matches
     */
    @GetMapping("/search/maps")
    public ResponseEntity<SearchResponseEntity> getMapSearch(
            @RequestParam() String q,
            @RequestParam(required = false, defaultValue = "1") @Min(1) int page,
            @RequestParam(required = false, defaultValue = "20") @Min(1) @Max(100) int per_page,
            @RequestParam(required = false, defaultValue = "true") boolean strict) {
        try {
            q = q.toUpperCase().replaceAll("_", " ").trim();

            SearchQueryAndResult maps;
            maps = service.sortByQuery(q, per_page, strict, minecraftMapRepository);
            List<MinecraftMap> outMaps = service.convertList(minecraftMapRepository,
                    paginateList(maps.maps, page, per_page));

            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic())
                    .body(new SearchResponseEntity(maps.max_page, outMaps));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @return
     */
    @GetMapping("/admin/publishing/new-maps")
    public ResponseEntity<List<MinecraftMap>> getUnpublishedMaps() {
        try {
            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic())
                    .body(minecraftMapRepository.findByPublished(false));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param id
     * @return
     */
    @GetMapping("admin/publishing/publish-map")
    public ResponseEntity<MinecraftMap> publishMap(
            @RequestParam() long id) {
        try {
            ResponseEntity<MinecraftMap> receive = getUnpublishedMapById(id);
            if (receive.getStatusCode().equals(HttpStatus.OK)) {
                MinecraftMap map = receive.getBody();
                map.publish();
                minecraftMapRepository.deleteById(map.getId());
                MinecraftMap published = minecraftMapRepository.saveAndFlush(map);
                return ResponseEntity.ok()
                        .cacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES).cachePublic())
                        .body(published);
            } else {
                return new ResponseEntity<>(getMapById(id).getBody(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param id
     * @return
     */
    @GetMapping("admin/publishing/retract-map")
    public ResponseEntity<MinecraftMap> retractMap(
            @RequestParam() long id) {
        try {
            ResponseEntity<MinecraftMap> receive = getMapById(id);
            if (receive.getStatusCode().equals(HttpStatus.OK)) {
                MinecraftMap map = receive.getBody();
                map.retract();
                minecraftMapRepository.deleteById(map.getId());
                MinecraftMap retracted = minecraftMapRepository.saveAndFlush(map);
                return ResponseEntity.ok()
                        .cacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES).cachePublic())
                        .body(retracted);
            } else {
                return new ResponseEntity<>(getUnpublishedMapById(id).getBody(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void evictAllcaches() {
        cacheManager.getCacheNames().stream()
                .forEach(cacheName -> cacheManager.getCache(cacheName).clear());
    }

    @Scheduled(fixedRate = 60000)
    public void evictAllcachesAtIntervals() {
        evictAllcaches();
    }

    public List<MinecraftMap> publishedMaps() {
        return minecraftMapRepository.findByPublished(true);
    }

    private static <T> List<T> paginateList(List<T> list, Integer page, Integer resultsPerPage) {
        Integer fromIndex = (page - 1) * resultsPerPage;
        Integer toIndex = fromIndex + resultsPerPage;

        if (list.size() >= toIndex) {
            return list.subList(fromIndex, toIndex);
        } else if (list.size() >= fromIndex) {
            return list.subList(fromIndex, list.size());
        } else {
            return new ArrayList<T>();
        }
    }
}