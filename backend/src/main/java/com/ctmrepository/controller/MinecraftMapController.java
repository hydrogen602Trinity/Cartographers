package com.ctmrepository.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.ctmrepository.model.MinecraftMap;
import com.ctmrepository.repository.MinecraftMapRepository;

import org.springframework.beans.factory.annotation.Autowired;
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
            @RequestParam(required = false, defaultValue = "1") @Min(1) int page,
            @RequestParam(required = false, defaultValue = "20") @Min(1) @Max(100) int per_page) {
        try {
            List<MinecraftMap> maps = new ArrayList<MinecraftMap>();

            q = q.replaceAll("_", " ");
            sortByRelevance(minecraftMapRepository.findAll(), q.toUpperCase()).forEach(maps::add);
            maps = paginateList(maps, page, per_page);

            return new ResponseEntity<>(maps, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

        if (mapData.isPresent()) {
            return new ResponseEntity<>(mapData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Next, given a list of maps and the search string,
    // sort the list of maps by the Levenshtein Distances and Return
    public List<MinecraftMap> sortByRelevance(List<MinecraftMap> maps, String search) {
        // Get Levenshtein Distances for names
        List<Integer> levenschteinValues = new ArrayList<Integer>();
        for (int i = 0; i < maps.size(); i++) {
            levenschteinValues
                    .add(Integer.valueOf(getLevenshteinDistance(maps.get(i).getName().toUpperCase(), search)));
        }

        // Custom Sort by Levenshtein Distances
        // Get smallest relative distance, throw it into the new map, repeat, n^2 time
        List<MinecraftMap> sortedMaps = new ArrayList<MinecraftMap>();
        while (!maps.isEmpty()) {
            int index = -1;
            Integer indexValue = Integer.MAX_VALUE;
            for (int i = 0; i < levenschteinValues.size(); i++) {
                if (levenschteinValues.get(i) < indexValue) {
                    index = i;
                    indexValue = levenschteinValues.get(index);
                }
            }
            sortedMaps.add(maps.get(index));
            maps.remove(index);
            levenschteinValues.remove(index);
        }
        return sortedMaps;
    }

    // A comparison where the larger the int the more different the strings are
    // Made by the number of addition, subtractions, or substitutions needed to
    // match x to y
    public int getLevenshteinDistance(String x, String y) {
        int[][] dp = new int[x.length() + 1][y.length() + 1];

        for (int i = 0; i <= x.length(); i++) {
            for (int j = 0; j <= y.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = min(dp[i - 1][j - 1]
                            + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)),
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1);
                }
            }
        }

        return dp[x.length()][y.length()];
    }

    // return if there is a substitution cost or not
    public static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    // return the smallest of the int numbers
    public static int min(int... numbers) {
        return Arrays.stream(numbers)
                .min().orElse(Integer.MAX_VALUE);
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