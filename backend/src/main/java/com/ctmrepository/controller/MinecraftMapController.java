package com.ctmrepository.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.ctmrepository.model.MinecraftMap;
import com.ctmrepository.repository.MinecraftMapRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class MinecraftMapController {

    @Autowired
    MinecraftMapRepository minecraftMapRepository;

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @CrossOrigin(origins = { "http://localhost:3000", "https://hydrogen602trinity.github.io/" })
    @GetMapping("/maps/search")
    public ResponseEntity<List<MinecraftMap>> getMapSearch() {
        try {
            List<MinecraftMap> maps = new ArrayList<MinecraftMap>();

            minecraftMapRepository.findAll().forEach(maps::add);

            System.out.println("it runs");

            return new ResponseEntity<>(maps, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    @GetMapping("/maps/{id}")
    public ResponseEntity<MinecraftMap> getMapById(@PathVariable("id") long id) {
        Optional<MinecraftMap> mapData = minecraftMapRepository.findById(id);

        if (mapData.isPresent()) {
            return new ResponseEntity<>(mapData.get(), HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Start Here: calling the Sort by Relevance function,
    // given the full map database and string to search for
    public ResponseEntity<List<Long>> searchThroughMaps(String search) {
        List<Long> sortedMaps = sortByRelevance(minecraftMapRepository.findAll(), search);
        return new ResponseEntity<>(sortedMaps, HttpStatus.OK);
    }

    // Next, given a list of maps and the search string,
    // sort the list of maps by the Levenshtein Distances and Return
    public List<Long> sortByRelevance(List<MinecraftMap> maps, String search) {
        // Get Levenshtein Distances for names
        List<Integer> mapValues = new ArrayList<Integer>();
        for (int i = 0; i < maps.size(); i++) {
            mapValues.add(Integer.valueOf(getLevenshteinDistance(maps.get(i).getName(), search)));
        }

        // Custom Sort by Levenshtein Distances
        // Get smallest relative distance, throw it into the new map, repeat, n^2 time
        List<Long> sortedMapIDs = new ArrayList<Long>();
        while (!maps.isEmpty()) {
            int index = 0;
            Integer indexValue = mapValues.get(index);
            for (int i = 0; i < mapValues.size(); i++) {
                if (mapValues.get(i) < indexValue) {
                    index = i;
                    indexValue = mapValues.get(index);
                }
            }
            sortedMapIDs.add(maps.get(index).getId());
            maps.remove(index);
            mapValues.remove(index);
        }
        return sortedMapIDs;
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
