package com.ctmrepository.repository;

import java.util.List;

import com.ctmrepository.model.MinecraftMap;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MinecraftMapRepository extends JpaRepository<MinecraftMap, Long> {
    List<MinecraftMap> findByPublished(boolean published);
/*
    List<MinecraftMap> findByOrderByDownload_countAsc();

    List<MinecraftMap> findByOrderByDownload_countDesc();

    List<MinecraftMap> findByOrderByUploadDateDesc();
    
    List<MinecraftMap> findByDifficultyOrderByDownload_countDesc(String difficulty);

    List<MinecraftMap> findByTypeOrderByDownload_countDesc(String type);
*/
    List<MinecraftMap> findByNameContaining(String name);
}
