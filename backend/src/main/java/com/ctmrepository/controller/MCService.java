package com.ctmrepository.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ctmrepository.model.MinecraftMap;
import com.ctmrepository.model.SearchFilter;
import com.ctmrepository.model.SearchFilterLong;
import com.ctmrepository.model.SearchQueryAndResult;
import com.ctmrepository.repository.MinecraftMapRepository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class MCService {

    public List<MinecraftMap> filter(List<MinecraftMap> start, SearchFilter[] filters) {
        List<MinecraftMap> out = new ArrayList<>();
        for (MinecraftMap map : start) {
            boolean clears = true;
            for (SearchFilter filter : filters) {
                if (filter.getSearch_filter().equals("UploadDate")) {
                    SearchFilterLong recastFilter = (SearchFilterLong) filter;
                    if (!recastFilter.isInBounds(map.getUpload_date())) {
                        clears = false;
                        break;
                    }
                }
                if (filter.getSearch_filter().equals("Length") && !filter.isInBounds(map.getLength())) {
                    clears = false;
                    break;
                }
                if (filter.getSearch_filter().equals("Objective Main")) {
                    SearchFilterLong recastFilter = (SearchFilterLong) filter;
                    if (!recastFilter.isInBounds(map.getObjective_main())) {
                        clears = false;
                        break;
                    }
                }
                if (filter.getSearch_filter().equals("Objective Bonus")) {
                    SearchFilterLong recastFilter = (SearchFilterLong) filter;
                    if (!recastFilter.isInBounds(map.getObjective_bonus())) {
                        clears = false;
                        break;
                    }
                }
                if (filter.getSearch_filter().equals("Difficulty") && !filter.isInBounds(map.getDifficulty())) {
                    clears = false;
                    break;
                }
                if (filter.getSearch_filter().equals("Download Count")) {
                    SearchFilterLong recastFilter = (SearchFilterLong) filter;
                    if (!recastFilter.isInBounds(map.getDownload_count())) {
                        clears = false;
                        break;
                    }
                }
                if (filter.getSearch_filter().equals("Type") && !filter.isInBounds(map.getType())) {
                    clears = false;
                    break;
                }
                if (filter.getSearch_filter().equals("Series") && !filter.isInBounds(map.getSeries())) {
                    clears = false;
                    break;
                }
                if (filter.getSearch_filter().equals("Mc Version") && !filter.isInBounds(map.getMc_version())) {
                    clears = false;
                    break;
                }
            }
            if (clears) {
                start.add(map);
            }
        }
        return out;
    }

    @Cacheable("search")
    public SearchQueryAndResult sortByQuery(String q, int per_page, boolean strict,
            MinecraftMapRepository minecraftMapRepository, SearchFilter[] filters) {
        List<MinecraftMap> publishedMaps = minecraftMapRepository.findByPublished(true);
        if (filters.length != 0) {
            publishedMaps = filter(publishedMaps, filters);
        }
        List<Long> maps = new ArrayList<Long>();

        if (strict) {
            strictSearchSort(publishedMaps, q.toUpperCase()).forEach(maps::add);
        } else {
            fuzzySearchSort(publishedMaps, q.toUpperCase()).forEach(maps::add);
        }
        int max_pages = (int) Math.ceil(maps.size() / (.0 + per_page));

        return new SearchQueryAndResult(q, max_pages, strict, maps);
    }

    public List<Long> strictSearchSort(List<MinecraftMap> maps, String search) {
        List<Long> relevantMaps = new ArrayList<Long>();
        for (MinecraftMap map : maps) {
            if (map.getName().toUpperCase().contains(search)
                    || map.getAuthor().toUpperCase().contains(search))
                relevantMaps.add(map.getId());
            else {
                String[] words = search.split(" ");
                for (int i = 0; i < words.length; i++) {
                    if (map.getName().toUpperCase().contains(words[i])
                            || map.getAuthor().toUpperCase().contains(words[i])) {
                        relevantMaps.add(map.getId());
                        i = words.length;
                    }
                }
            }
        }
        return relevantMaps;
    }

    // Next, given a list of maps and the search string,
    // sort the list of maps by the Levenshtein Distances and Return
    public List<Long> fuzzySearchSort(List<MinecraftMap> maps, String search) {
        List<Double> dists = new ArrayList<>();
        for (MinecraftMap map : maps) {
            dists.add(getLargestJWDist(map, search, search.split(" ")));
        }
        mergeSort(maps, dists, 0, dists.size() - 1);
        List<Long> out = new ArrayList<>();
        for (MinecraftMap map : maps) {
            out.add(map.getId());
        }
        return out;
    }

    public void mergeSort(List<MinecraftMap> maps, List<Double> dists, int low, int high) {
        if (low < high) {
            int mid = (low + high) / 2;
            mergeSort(maps, dists, low, mid);
            mergeSort(maps, dists, mid + 1, high);

            if (dists.get(mid) < dists.get(mid + 1))
                merge(maps, dists, low, mid, high);
        }
    }

    public void merge(List<MinecraftMap> maps, List<Double> dists, int low, int mid, int high) {
        int i = low,
                j = mid + 1,
                k = 0;
        Double[] tempDists = new Double[high - low + 1];
        MinecraftMap[] tempMaps = new MinecraftMap[high - low + 1];

        while (i <= mid && j <= high) {
            if (dists.get(i) > dists.get(j)) {
                tempDists[k] = dists.get(i);
                tempMaps[k] = maps.get(i);
                k++;
                i++;
            } else {
                tempDists[k] = dists.get(j);
                tempMaps[k] = maps.get(j);
                k++;
                j++;
            }
        }
        while (j <= high) {
            tempDists[k] = dists.get(j);
            tempMaps[k] = maps.get(j);
            k++;
            j++;
        }
        while (i <= mid) {
            tempDists[k] = dists.get(i);
            tempMaps[k] = maps.get(i);
            k++;
            i++;
        }
        k = 0;
        for (i = low; i <= high; i++) {
            dists.set(i, tempDists[k]);
            maps.set(i, tempMaps[k]);
            k++;
        }
    }

    double getLargestJWDist(MinecraftMap map, String search, String[] words) {
        double largestTitleSearch = getJaroWinklerDistance(map.getName().toUpperCase(), search);
        String largestTitle = search;
        double largestAuthorSearch = getJaroWinklerDistance(map.getAuthor().toUpperCase(), search);
        String largestAuthor = search;
        if (words.length > 1) {
            for (int i = 0; i < words.length; i++) {
                double newJWTitle = getJaroWinklerDistance(map.getName().toUpperCase(), words[i]);
                double newJWAuthor = getJaroWinklerDistance(map.getAuthor().toUpperCase(), words[i]);
                largestTitleSearch = newJWTitle > largestTitleSearch ? newJWTitle : largestTitleSearch;
                largestTitle = newJWTitle > largestTitleSearch ? words[i] : largestTitle;
                largestAuthorSearch = newJWAuthor > largestAuthorSearch ? newJWAuthor : largestAuthorSearch;
                largestAuthor = newJWAuthor > largestAuthorSearch ? words[i] : largestAuthor;
            }
        }

        if (map.getName().toUpperCase().contains(largestTitle)) {
            largestTitleSearch *= 2;
        }
        if (map.getAuthor().toUpperCase().contains(largestAuthor)) {
            largestAuthorSearch *= 2;
        }

        return largestTitleSearch + largestAuthorSearch;
    }

    double getJaroWinklerDistance(String s1, String s2) {
        double jaro_dist = getJaroDistance(s1, s2);
        // If the jaro Similarity is above a threshold
        if (jaro_dist > 0.7) {
            // Find the length of common prefix
            int prefix = 0;
            for (int i = 0; i < Math.min(s1.length(), s2.length()); i++) {
                // If the characters match
                if (s1.charAt(i) == s2.charAt(i))
                    prefix++;
                else
                    break;
            }
            // Maximum of 4 characters are allowed in prefix
            prefix = Math.min(4, prefix);
            // Calculate jaro winkler Similarity
            jaro_dist += 0.1 * prefix * (1 - jaro_dist);
        }
        return jaro_dist;
    }

    double getJaroDistance(String s1, String s2) {
        if (s1.equals(s2))
            return 1.0;

        int s_len = s1.length();
        int t_len = s2.length();

        if (s_len == 0 || t_len == 0) {
            return 1;
        }

        // Maximum distance upto which matching
        // is allowed
        int match_distance = (int) (Math.floor(Math.max(s_len, t_len) / 2) - 1);

        boolean[] s_matches = new boolean[s1.length()];
        boolean[] t_matches = new boolean[s2.length()];

        int matches = 0;
        int transpositions = 0;

        for (int i = 0; i < s_len; i++) {
            int start = Integer.max(0, i - match_distance);
            int end = Integer.min(i + match_distance + 1, t_len);

            for (int j = start; j < end; j++) {
                if (t_matches[j])
                    continue;
                if (s1.charAt(i) != s2.charAt(j))
                    continue;
                s_matches[i] = true;
                t_matches[j] = true;
                matches++;
                break;
            }
        }

        if (matches == 0)
            return 0;

        int k = 0;
        for (int i = 0; i < s_len; i++) {
            if (!s_matches[i])
                continue;
            while (!t_matches[k])
                k++;
            if (s1.charAt(i) != s2.charAt(k))
                transpositions++;
            k++;
        }

        return (((double) matches / s_len) +
                ((double) matches / t_len) +
                (((double) matches - transpositions / 2.0) / matches)) / 3.0;
    }

    public List<MinecraftMap> convertList(MinecraftMapRepository repo, List<Long> lst) {
        List<MinecraftMap> out = new ArrayList<>();
        for (Long id : lst) {
            Optional<MinecraftMap> mapData = repo.findById(id);
            if (mapData.isPresent()) {
                out.add(mapData.get());
            }
        }
        return out;
    }

    @Cacheable("home")
    public List<List<MinecraftMap>> getHomepageMaps(int count, MinecraftMapRepository repo) {
        List<List<MinecraftMap>> maps = new ArrayList<List<MinecraftMap>>();
        /*
         * maps.add(topPopularMaps(count, repo));
         * maps.add(newestMaps(count, repo));
         * maps.add(bestEasyMaps(count, repo));
         * maps.add(bestMediumMaps(count, repo));
         * maps.add(bestHardMaps(count, repo));
         */
        return maps;
    }
}
/*
 * public List<MinecraftMap> topPopularMaps(int count, MinecraftMapRepository
 * repo) {
 * return MinecraftMapController.paginateList(
 * repo.findByOrderByDownload_countDesc(), 1, count);
 * }
 * 
 * public List<MinecraftMap> newestMaps(int count, MinecraftMapRepository repo)
 * {
 * return MinecraftMapController.paginateList(
 * repo.findByOrderByUploadDateDesc(), 1, count);
 * }
 * 
 * public List<MinecraftMap> bestEasyMaps(int count, MinecraftMapRepository
 * repo) {
 * return MinecraftMapController.paginateList(
 * repo.findByDifficultyOrderByDownload_countDesc("Easy"), 1, count);
 * }
 * 
 * public List<MinecraftMap> bestMediumMaps(int count, MinecraftMapRepository
 * repo) {
 * return MinecraftMapController.paginateList(
 * repo.findByDifficultyOrderByDownload_countDesc("Medium"), 1, count);
 * }
 * 
 * public List<MinecraftMap> bestHardMaps(int count, MinecraftMapRepository
 * repo) {
 * return MinecraftMapController.paginateList(
 * repo.findByDifficultyOrderByDownload_countDesc("Hard"), 1, count);
 * }
 */
