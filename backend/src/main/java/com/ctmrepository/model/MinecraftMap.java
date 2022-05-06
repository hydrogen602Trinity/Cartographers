package com.ctmrepository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ctm_maps")
public class MinecraftMap {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @Column(name = "upload_date")
    private long upload_date;

    @NotNull
    @Column(name = "author")
    private String author;

    @NotNull
    @Column(name = "length")
    private String length;

    @NotNull
    @Min(1)
    @Column(name = "objective_main")
    private int objective_main;

    @Column(name = "objective_bonus")
    private int objective_bonus;

    @NotNull
    @Column(name = "difficulty")
    private String difficulty;

    @Column(name = "description_short", length = 2048)
    private String description_short;

    @Column(name = "download_count")
    private long download_count;

    @NotNull
    @Column(name = "type")
    private String type;

    @Column(name = "image_url")
    private String image_url;

    @NotNull
    @Column(name = "series")
    private String series;

    @NotNull
    @Column(name = "mc_version")
    private String mc_version;

    @Column(name = "published")
    private boolean published;

    public boolean isPublished() {
        return published;
    }

    public void publish() {
        published = true;
    }

    public void retract() {
        published = false;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUpload_date() {
        return this.upload_date;
    }

    public void setUpload_date(long upload_date) {
        this.upload_date = upload_date;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLength() {
        return this.length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public int getObjective_main() {
        return this.objective_main;
    }

    public void setObjective_main(int objective_main) {
        this.objective_main = objective_main;
    }

    public int getObjective_bonus() {
        return this.objective_bonus;
    }

    public void setObjective_bonus(int objective_bonus) {
        this.objective_bonus = objective_bonus;
    }

    public String getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getDescription_short() {
        return this.description_short;
    }

    public void setDescription_short(String description_short) {
        this.description_short = description_short;
    }

    public long getDownload_count() {
        return this.download_count;
    }

    public void setDownload_count(long download_count) {
        this.download_count = download_count;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage_url() {
        return this.image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getSeries() {
        return this.series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getMc_version() {
        return this.mc_version;
    }

    public void setMc_version(String mc_version) {
        this.mc_version = mc_version;
    }

    public MinecraftMap() {
    }

    public MinecraftMap(String name, long upload_date, String author, String length, int objective_main,
            int objective_bonus, String difficulty, String description_short, long download_count, String type,
            String image_url, String series, String mc_version, boolean published) {
        this.name = name;
        this.upload_date = upload_date;
        this.author = author;
        this.length = length;
        this.objective_main = objective_main;
        this.objective_bonus = objective_bonus;
        this.difficulty = difficulty;
        this.description_short = description_short;
        this.download_count = download_count;
        this.type = type;
        this.image_url = image_url;
        this.series = series;
        this.mc_version = mc_version;
        this.published = published;
    }

    // @Override
    // public String toString() {
    // return "MinecraftMap [id=" + id + ", name=" + name + ", mc_version=" +
    // minecraftVersion + ", download_count="
    // + downloadCount + ", verified=" + verified + "]";
    // }

    // Austin 5/5/2022
    // Added @NotNull to some variables in map
}
