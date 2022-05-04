package com.ctmrepository.repository;

import java.util.List;

import com.ctmrepository.model.MinecraftMap;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MinecraftMapRepository extends JpaRepository<MinecraftMap, Long> {
    List<MinecraftMap> findByPublished(boolean published);

    List<MinecraftMap> findByOrderByDownloadCountAsc();

    List<MinecraftMap> findByOrderByDownloadCountDesc();

    List<MinecraftMap> findByOrderByUploadDateDesc();
    
    List<MinecraftMap> findByDifficultyOrderByDownloadCountDesc(String difficulty);

    List<MinecraftMap> findByTypeOrderByDownloadCountDesc(String type);

    List<MinecraftMap> findByNameContaining(String name);
}
