package com.ctmrepository.repository;

import com.ctmrepository.model.MinecraftMap;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MinecraftMapRepository extends JpaRepository<MinecraftMap, Long> {
    // List<MinecraftMap> findByVerified(boolean verified);

    // List<MinecraftMap> findByNameContaining(String name);
}
