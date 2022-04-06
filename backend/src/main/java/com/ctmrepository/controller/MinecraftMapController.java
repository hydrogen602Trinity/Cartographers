package com.ctmrepository.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ctmrepository.model.MinecraftMap;
import com.ctmrepository.repository.MinecraftMapRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class MinecraftMapController {

    @Autowired
    MinecraftMapRepository minecraftMapRepository;

    @GetMapping("/maps")
    public ResponseEntity<List<MinecraftMap>> getAllMaps(@RequestParam(required = false) String name) {
        try {
            List<MinecraftMap> maps = new ArrayList<MinecraftMap>();

            if (name == null)
                minecraftMapRepository.findAll().forEach(maps::add);
            else
                minecraftMapRepository.findByNameContaining(name).forEach(maps::add);

            if (maps.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(maps, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/maps/{id}")
    public ResponseEntity<MinecraftMap> getMapById(@PathVariable("id") long id) {
        Optional<MinecraftMap> mapData = minecraftMapRepository.findById(id);

        if (mapData.isPresent()) {
            return new ResponseEntity<>(mapData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/maps")
    public ResponseEntity<MinecraftMap> createMap(@RequestBody MinecraftMap map) {
        try {
            MinecraftMap _map = minecraftMapRepository
                    .save(new MinecraftMap(map.getName(), map.getMinecraftVersion(), 0, false));
            return new ResponseEntity<>(_map, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/maps/{id}")
    public ResponseEntity<MinecraftMap> updateMap(@PathVariable("id") long id, @RequestBody MinecraftMap map) {
        Optional<MinecraftMap> mapData = minecraftMapRepository.findById(id);

        if (mapData.isPresent()) {
            MinecraftMap _map = mapData.get();
            _map.setName(map.getName());
            _map.setMinecraftVersion(map.getMinecraftVersion());
            _map.setDownloadCount(map.getDownloadCount());
            return new ResponseEntity<>(minecraftMapRepository.save(_map), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/maps/{id}")
    public ResponseEntity<HttpStatus> deleteMap(@PathVariable("id") long id) {
        try {
            minecraftMapRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/maps")
    public ResponseEntity<HttpStatus> deleteAllMaps() {
        try {
            minecraftMapRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/maps/verified")
    public ResponseEntity<List<MinecraftMap>> findByVerified() {
        try {
            List<MinecraftMap> maps = minecraftMapRepository.findByVerified(true);

            if (maps.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(maps, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
