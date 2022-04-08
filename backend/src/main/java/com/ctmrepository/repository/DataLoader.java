package com.ctmrepository.repository;

import com.ctmrepository.model.MinecraftMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private MinecraftMapRepository repo;

    @Autowired
    public DataLoader(MinecraftMapRepository repo) {
        this.repo = repo;
        LoadData();
    }

    // String name, long upload_date, String author, String length, int
    // objective_main,
    // int objective_bonus, String difficulty, String description_short, long
    // download_count, String type,
    // String image_url, String series, String mc_version

    private void LoadData() {
        repo.save(new MinecraftMap("Moonlight", 0, "The CTMC", "Long", 17, 54, "Medium",
                "Moonlight is the lovechild of the Rookiewreck mapping event, featuring dozens of builders creating full-length areas over the span of a single month!",
                899, "Linear Branching", "https://hydrogen602trinity.github.io/Cartographers/5886020644119590.webp",
                "Rookiewreck", "1.17.1"));

        repo.save(new MinecraftMap("Divinity's End", 0, "The DE Team", "Long", 13, 16, "Medium",
                "Divinity's End is a massive collaborative CTM map featuring areas from some of the best mapmakers the CTMC has to offer. As the final entry on the Pantheon series, it features action packed, heavily customized gameplay for you to enjoy either by yourself or with as many friends as you want.",
                21850, "Branching", "https://hydrogen602trinity.github.io/Cartographers/div.webp",
                "Pantheon", "1.16.5"));
    }
}
