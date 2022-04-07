package com.ctmrepository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ctm_maps")
public class MinecraftMap {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "mc_version")
    private String minecraftVersion;

    @Column(name = "download_count")
    private int downloadCount;

    @Column(name = "verified")
    private boolean verified;

    public MinecraftMap() {

    }

    public MinecraftMap(String name, String minecraftVersion, int downloadCount, boolean isVerified) {
        this.name = name;
        this.minecraftVersion = minecraftVersion;
        this.downloadCount = downloadCount;
        this.verified = isVerified;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMinecraftVersion() {
        return minecraftVersion;
    }

    public void setMinecraftVersion(String minecraftVersion) {
        this.minecraftVersion = minecraftVersion;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean isVerified) {
        this.verified = isVerified;
    }

    @Override
    public String toString() {
        return "MinecraftMap [id=" + id + ", name=" + name + ", mc_version=" + minecraftVersion + ", download_count="
                + downloadCount + ", verified=" + verified + "]";
    }

}
