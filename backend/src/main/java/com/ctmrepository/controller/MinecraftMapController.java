package com.ctmrepository.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    // Note that in the url, '_' is assumed to be the spaces
    @GetMapping("/search/{search}")
    public ResponseEntity<List<Long>> searchThroughMaps(@PathVariable("search") String search) {
        search = search.replaceAll("_", " ");
        List<Long> sortedMaps = sortByRelevance(minecraftMapRepository.findAll(), search.toUpperCase());
        return new ResponseEntity<>(sortedMaps, HttpStatus.OK);
    }

    // Next, given a list of maps and the search string,
    // sort the list of maps by the Levenshtein Distances and Return
    public List<Long> sortByRelevance(List<MinecraftMap> maps, String search) {
        Collections.sort( maps, new Comparator<MinecraftMap>() {
            public int compare(MinecraftMap m1, MinecraftMap m2) {
                int mapLeven1 = getLevenshteinDistance(m1.getName().toUpperCase(), search);
                int mapLeven2 = getLevenshteinDistance(m2.getName().toUpperCase(), search);
                int levenComp = mapLeven1 - mapLeven2;

                System.out.println(m1.getName()+" ("+mapLeven1+")"+" / "+m2.getName()+" ("+mapLeven2+")"+" = "+levenComp);
                if (levenComp != 0) {
                    return levenComp;
                }

                Long m1D = m1.getDownload_count();
                Long m2D = m2.getDownload_count();
                return m1D.compareTo(m2D);
            }
        });

        List<Long> sortedMapIDs = maps.stream().map(MinecraftMap::getId).collect(Collectors.toList());

        return sortedMapIDs;  
        /*
        // Get Levenshtein Distances for names
        List<Integer> levenschteinValues = new ArrayList<Integer>();
        for (int i = 0; i < maps.size(); i++) {
            levenschteinValues
                    .add(Integer.valueOf(getLevenshteinDistance(maps.get(i).getName().toUpperCase(), search)));
        }

        // Custom Sort by Levenshtein Distances
        // Get smallest relative distance, throw it into the new map, repeat, n^2 time
        List<Long> sortedMapIDs = new ArrayList<Long>();
        while (!maps.isEmpty()) {
            int index = -1;
            Integer indexValue = Integer.MAX_VALUE;
            for (int i = 0; i < levenschteinValues.size(); i++) {
                if (levenschteinValues.get(i) < indexValue) {
                    index = i;
                    indexValue = levenschteinValues.get(index);
                }
            }
            sortedMapIDs.add(maps.get(index).getId());
            maps.remove(index);
            levenschteinValues.remove(index);
        }
        return sortedMapIDs;
        */
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
                            + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)), // Substitute
                            dp[i - 1][j] + 2, // Delete 
                            dp[i][j - 1] + 1); // Insert
                }
            }
        }

        //  System.out.println("Lengths: "+x.length()+" - "+y.length());
        return dp[x.length()][y.length()];
    }

    // return if there is a substitution cost or not
    public static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 3;
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
