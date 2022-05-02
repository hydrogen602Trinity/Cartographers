package com.ctmrepository.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.ctmrepository.model.MinecraftMap;
import com.ctmrepository.repository.MinecraftMapRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@CrossOrigin(origins = { "http://localhost:3000", "https://hydrogen602trinity.github.io" })
@RestController
@RequestMapping("/")
public class MinecraftMapController {

    @Autowired
    MinecraftMapRepository minecraftMapRepository;

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
    public ResponseEntity<List<MinecraftMap>> getMapSearch(
            @RequestParam() String q,
            @RequestParam(required = false, defaultValue = "1") @Min(1) int page,
            @RequestParam(required = false, defaultValue = "20") @Min(1) @Max(100) int per_page,
            @RequestParam(required = false, defaultValue = "true") boolean strict) {
        try {
            List<MinecraftMap> maps = new ArrayList<MinecraftMap>();
            List<MinecraftMap> publishedMaps = minecraftMapRepository.findByPublished(true);

            q = q.toUpperCase().replaceAll("_", " ").trim();

            if (strict) {
                strictSearchSort(publishedMaps, q.toUpperCase()).forEach(maps::add);
            } else {
                fuzzySearchSort(publishedMaps, q.toUpperCase()).forEach(maps::add);
            }

            maps = paginateList(maps, page, per_page);

            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS).cachePublic())
                    .body(maps);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    private List<MinecraftMap> strictSearchSort(List<MinecraftMap> maps, String search) {
        List<MinecraftMap> relevantMaps = new ArrayList<MinecraftMap>();
        for (MinecraftMap map : maps) {
            if (map.getName().toUpperCase().contains(search)
                    || map.getAuthor().toUpperCase().contains(search))
                relevantMaps.add(map);
            else {
                String[] words = search.split(" ");
                for (int i = 0; i < words.length; i++) {
                    if (map.getName().toUpperCase().contains(words[i])
                            || map.getAuthor().toUpperCase().contains(words[i])) {
                        relevantMaps.add(map);
                        i = words.length;
                    }
                }
            }
        }
        return relevantMaps;
    }

    // Next, given a list of maps and the search string,
    // sort the list of maps by the Levenshtein Distances and Return
    private List<MinecraftMap> fuzzySearchSort(List<MinecraftMap> maps, String search) {
        List<Double> dists = new ArrayList<>();
        for (MinecraftMap map : maps) {
            dists.add(getLargestJWDist(map, search, search.split(" ")));
        }
        mergeSort(maps, dists);
        return maps;
    }

    private void mergeSort(List<MinecraftMap> maps, List<Double> dists) {
        int start = 0;
        int end = dists.size() - 1;
        mergeSort(maps, dists, start, end);
    }

    private void mergeSort(List<MinecraftMap> maps, List<Double> dists, int low, int high) {
        if (low < high) {
            int mid = (low + high) / 2;
            mergeSort(maps, dists, low, mid);
            mergeSort(maps, dists, mid + 1, high);

            if (dists.get(mid) < dists.get(mid + 1))
                merge(maps, dists, low, mid, high);
        }
    }

    private void merge(List<MinecraftMap> maps, List<Double> dists, int low, int mid, int high) {
        int i = low,
                j = mid + 1,
                k = 0;
        Double[] tempDists = new Double[high - low + 1];
        MinecraftMap[] tempMaps = new MinecraftMap[high - low + 1];

        while (i <= mid && j <= high) {
            if (dists.get(i) > dists.get(j)) {
                tempDists[k] = dists.get(i);
                tempMaps[k] = maps.get(i);
                k++;
                i++;
            } else {
                tempDists[k] = dists.get(j);
                tempMaps[k] = maps.get(j);
                k++;
                j++;
            }
        }
        while (j <= high) {
            tempDists[k] = dists.get(j);
            tempMaps[k] = maps.get(j);
            k++;
            j++;
        }
        while (i <= mid) {
            tempDists[k] = dists.get(i);
            tempMaps[k] = maps.get(i);
            k++;
            i++;
        }
        k = 0;
        for (i = low; i <= high; i++) {
            dists.set(i, tempDists[k]);
            maps.set(i, tempMaps[k]);
            k++;
        }
    }

    double getLargestJWDist(MinecraftMap map, String search, String[] words) {
        double largestTitleSearch = getJaroWinklerDistance(map.getName().toUpperCase(), search);
        String largestTitle = search;
        double largestAuthorSearch = getJaroWinklerDistance(map.getAuthor().toUpperCase(), search);
        String largestAuthor = search;
        if (words.length > 1) {
            for (int i = 0; i < words.length; i++) {
                double newJWTitle = getJaroWinklerDistance(map.getName().toUpperCase(), words[i]);
                double newJWAuthor = getJaroWinklerDistance(map.getAuthor().toUpperCase(), words[i]);
                largestTitleSearch = newJWTitle > largestTitleSearch ? newJWTitle : largestTitleSearch;
                largestTitle = newJWTitle > largestTitleSearch ? words[i] : largestTitle;
                largestAuthorSearch = newJWAuthor > largestAuthorSearch ? newJWAuthor : largestAuthorSearch;
                largestAuthor = newJWAuthor > largestAuthorSearch ? words[i] : largestAuthor;
            }
        }

        if (map.getName().toUpperCase().contains(largestTitle)) {
            largestTitleSearch *= 2;
        }
        if (map.getAuthor().toUpperCase().contains(largestAuthor)) {
            largestAuthorSearch *= 2;
        }

        return largestTitleSearch + largestAuthorSearch;
    }

    double getJaroWinklerDistance(String s1, String s2) {
        double jaro_dist = getJaroDistance(s1, s2);
        // If the jaro Similarity is above a threshold
        if (jaro_dist > 0.7) {
            // Find the length of common prefix
            int prefix = 0;
            for (int i = 0; i < Math.min(s1.length(), s2.length()); i++) {
                // If the characters match
                if (s1.charAt(i) == s2.charAt(i))
                    prefix++;
                else
                    break;
            }
            // Maximum of 4 characters are allowed in prefix
            prefix = Math.min(4, prefix);
            // Calculate jaro winkler Similarity
            jaro_dist += 0.1 * prefix * (1 - jaro_dist);
        }
        return jaro_dist;
    }

    double getJaroDistance(String s1, String s2) {
        if (s1.equals(s2))
            return 1.0;

        int s_len = s1.length();
        int t_len = s2.length();

        if (s_len == 0 || t_len == 0) {
            return 1;
        }

        // Maximum distance upto which matching
        // is allowed
        int match_distance = (int) (Math.floor(Math.max(s_len, t_len) / 2) - 1);

        boolean[] s_matches = new boolean[s1.length()];
        boolean[] t_matches = new boolean[s2.length()];

        int matches = 0;
        int transpositions = 0;

        for (int i = 0; i < s_len; i++) {
            int start = Integer.max(0, i - match_distance);
            int end = Integer.min(i + match_distance + 1, t_len);

            for (int j = start; j < end; j++) {
                if (t_matches[j])
                    continue;
                if (s1.charAt(i) != s2.charAt(j))
                    continue;
                s_matches[i] = true;
                t_matches[j] = true;
                matches++;
                break;
            }
        }

        if (matches == 0)
            return 0;

        int k = 0;
        for (int i = 0; i < s_len; i++) {
            if (!s_matches[i])
                continue;
            while (!t_matches[k])
                k++;
            if (s1.charAt(i) != s2.charAt(k))
                transpositions++;
            k++;
        }

        return (((double) matches / s_len) +
                ((double) matches / t_len) +
                (((double) matches - transpositions / 2.0) / matches)) / 3.0;
    }

    @GetMapping("/maps/all-maps")
    public ResponseEntity<List<MinecraftMap>> getPublishedMaps() {
        try {
            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic())
                    .body(minecraftMapRepository.findByPublished(true));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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
                return new ResponseEntity<>(getMapById(id).getBody(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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
                return new ResponseEntity<>(getUnpublishedMapById(id).getBody(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Add one map to the list
     * 
     * @param map the map to be added from the frontend
     * 
     */
    @PostMapping("/maps/upload") // New API Endpoint
    public void addMap(List<MinecraftMap> maps, MinecraftMap map) {

        maps.add(map);

        return;
    }

    /*
     * AUSTIN STUFF
     * //
     * // 1) Convert Data into MinecraftMap Object
     * // - If given MinecraftMap object, then just add object
     * //
     * // Am I making a frontend function to send the params to the backend?\
     * // How will the data be sent to the backend? As a list of strings or a
     * // MinecraftMap object
     * //
     * // How do I get params from frontend, and get an object from the params
     */

    /*
     * @GetMapping("/maps")
     * public ResponseEntity<List<MinecraftMap>> getAllMaps(@RequestParam(required =
     * false) String name) {
     * try {
     * List<MinecraftMap> maps = new ArrayList<MinecraftMap>();
     * 
     * if (name == null)
     * minecraftMapRepository.findAll().forEach(maps::add);
     * else
     * // Search for Name of Map
     * minecraftMapRepository.findAll().forEach(maps::add);
     * // minecraftMapRepository.findByNameContaining(name).forEach(maps::add);
     * 
     * if (maps.isEmpty()) {
     * return new ResponseEntity<>(HttpStatus.NO_CONTENT);
     * }
     * 
     * return new ResponseEntity<>(maps, HttpStatus.OK);
     * } catch (Exception e) {
     * return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
     * }
     * }
     */

    // @PostMapping("/maps")
    // public ResponseEntity<MinecraftMap> createMap(@RequestBody MinecraftMap map)
    // {
    // try {
    // MinecraftMap _map = minecraftMapRepository
    // .save(new MinecraftMap(map.getName(), map.getMinecraftVersion(), 0, false));
    // return new ResponseEntity<>(_map, HttpStatus.CREATED);
    // } catch (Exception e) {
    // return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    // }

    // @PutMapping("/maps/{id}")
    // public ResponseEntity<MinecraftMap> updateMap(@PathVariable("id") long id,
    // @RequestBody MinecraftMap map) {
    // Optional<MinecraftMap> mapData = minecraftMapRepository.findById(id);

    // if (mapData.isPresent()) {
    // MinecraftMap _map = mapData.get();
    // _map.setName(map.getName());
    // _map.setMinecraftVersion(map.getMinecraftVersion());
    // _map.setDownloadCount(map.getDownloadCount());
    // return new ResponseEntity<>(minecraftMapRepository.save(_map),
    // HttpStatus.OK);
    // } else {
    // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    // }
    // }

    // @DeleteMapping("/maps/{id}")
    // public ResponseEntity<HttpStatus> deleteMap(@PathVariable("id") long id) {
    // try {
    // minecraftMapRepository.deleteById(id);
    // return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    // } catch (Exception e) {
    // return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    // }

    // @DeleteMapping("/maps")
    // public ResponseEntity<HttpStatus> deleteAllMaps() {
    // try {
    // minecraftMapRepository.deleteAll();
    // return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    // } catch (Exception e) {
    // return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    // }

    // }

    // @GetMapping("/maps/verified")
    // public ResponseEntity<List<MinecraftMap>> findByVerified() {
    // try {
    // List<MinecraftMap> maps = minecraftMapRepository.findByVerified(true);

    // if (maps.isEmpty()) {
    // return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    // }
    // return new ResponseEntity<>(maps, HttpStatus.OK);
    // } catch (Exception e) {
    // return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    // }

}