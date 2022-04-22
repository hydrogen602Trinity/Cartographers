package com.ctmrepository.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@CrossOrigin(origins = { "http://localhost:3000", "https://hydrogen602trinity.github.io" })
@RestController
@RequestMapping("/")
public class MinecraftMapController {

    public static <T> List<T> paginateList(List<T> list, Integer page, Integer resultsPerPage) {
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

    /**
     * Sorts map database according to query and returns results corresponding to
     * given page and response limit.
     *
     * @param q     search query
     * @param limit maximum number of results to return per page
     * @param page  page number of results to return
     */
    @GetMapping("/search/maps")
    public ResponseEntity<List<MinecraftMap>> getMapSearch(
            @RequestParam() String q,
            @RequestParam(required = false, defaultValue = "true") boolean hard_search,
            @RequestParam(required = false, defaultValue = "1") @Min(1) int page,
            @RequestParam(required = false, defaultValue = "20") @Min(1) @Max(100) int per_page) {
        try {
            List<MinecraftMap> maps = new ArrayList<MinecraftMap>();

            q = q.replaceAll("_", " ");

            if (hard_search) {
                hardSearchSort(getPublishedMaps(), q.toUpperCase()).forEach(maps::add);
            } else {
                fuzzySearchSort(getPublishedMaps(), q.toUpperCase()).forEach(maps::add);
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

    private List<MinecraftMap> getPublishedMaps() {
        List<MinecraftMap> maps = minecraftMapRepository.findByPublished(true);
        return maps;
    }

    private List<MinecraftMap> getUnpublishedMaps() {
        List<MinecraftMap> maps = minecraftMapRepository.findByPublished(false);
        return maps;
    }

    private List<MinecraftMap> hardSearchSort(List<MinecraftMap> maps, String search) {
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
        Collections.sort(maps, new Comparator<MinecraftMap>() {
            public int compare(MinecraftMap m1, MinecraftMap m2) {
                double mapJWD1 = getLargestJWDist(m1, search, search.split(" "));
                double mapJWD2 = getLargestJWDist(m2, search, search.split(" "));
                double jwdComp = mapJWD2 - mapJWD1;

                if (Math.abs(jwdComp) > 0.1) {
                    return (int) Math.round(jwdComp * 100);
                }

                Long m1D = m1.getDownload_count();
                Long m2D = m2.getDownload_count();
                return m1D.compareTo(m2D);
            }
        });
        return maps;
    }

    double getLargestJWDist(MinecraftMap map, String search, String[] words) {
        double largestTitleSearch = getJaroWinklerDistance(map.getName().toUpperCase(), search);
        double largestAuthorSearch = getJaroWinklerDistance(map.getAuthor().toUpperCase(), search);
        for (int i = 0; i < words.length; i++) {
            double newJWTitle = getJaroWinklerDistance(map.getName().toUpperCase(), words[i]);
            double newJWAuthor = getJaroWinklerDistance(map.getAuthor().toUpperCase(), words[i]);
            largestTitleSearch = newJWTitle > largestTitleSearch ? newJWTitle : largestTitleSearch;
            largestAuthorSearch = newJWAuthor > largestAuthorSearch ? newJWAuthor : largestAuthorSearch;
        }
        return largestTitleSearch + largestAuthorSearch;
    }

    double getJaroWinklerDistance(String s1, String s2)
    {
        double jaro_dist = getJaroDistance(s1, s2);
        // If the jaro Similarity is above a threshold
        if (jaro_dist > 0.7)
        {
            // Find the length of common prefix
            int prefix = 0;
            for (int i = 0;
                 i < Math.min(s1.length(), s2.length()); i++)
            {
                // If the characters match
                if (s1.charAt(i) == s2.charAt(i))
                    prefix++;
                    // Else break
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

    // Java implementation of above approach
    double getJaroDistance(String s1, String s2) {
        // If the Strings are equal
        if (s1 == s2)
            return 1.0;

        // Length of two Strings
        int len1 = s1.length(),
                len2 = s2.length();

        // Maximum distance upto which matching
        // is allowed
        int max_dist = (int) (Math.floor(Math.max(len1, len2) / 2) - 1);

        // Count of matches
        int match = 0;

        // Hash for matches
        int hash_s1[] = new int[s1.length()];
        int hash_s2[] = new int[s2.length()];

        // Traverse through the first String
        for (int i = 0; i < len1; i++) {
            // Check if there is any matches
            for (int j = Math.max(0, i - max_dist); j < Math.min(len2, i + max_dist + 1); j++)

                // If there is a match
                if (s1.charAt(i) == s2.charAt(j) && hash_s2[j] == 0) {
                    hash_s1[i] = 1;
                    hash_s2[j] = 1;
                    match++;
                    break;
                }
        }
        // If there is no match
        if (match == 0)
            return 0.0;
        // Number of transpositions
        double t = 0;
        int point = 0;

        // Count number of occurrences
        // where two characters match but
        // there is a third matched character
        // in between the indices
        for (int i = 0; i < len1; i++)
            if (hash_s1[i] == 1) {
                // Find the next matched character
                // in second String
                while (hash_s2[point] == 0)
                    point++;
                if (s1.charAt(i) != s2.charAt(point++))
                    t++;
            }
        t /= 2;
        // Return the Jaro Similarity
        return (((double) match) / ((double) len1)
                + ((double) match) / ((double) len2)
                + ((double) match - t) / ((double) match))
                / 3.0;
    }    
    
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