import useFetch from "react-fetch-hook";
import { useFetchAPI } from "utilities/fetch"

/**
 * Utility function
 * Gets parameters, deletes those where the value is undefined, and returns
 * it as a url search parameter string, ready to be put into a URL.
 * @param {any} obj the parameters to format
 * @returns {string} the formatted search parameter, ready to be put into a URL
 */
function search_param_helper(obj: any): string {
    Object.keys(obj).forEach(k => {
        if (obj[k] === undefined) {
            delete obj[k];
        }
    });

    return (new URLSearchParams(obj)).toString();
}

/**
 * Specify API Paths here
 */
const API_ENDPOINTS = {
    ALL_MAPS: (params: { q: string, page?: number, per_page?: number }) => '/search/maps?' + search_param_helper(params),
    ONE_MAP: (id: number) => `/maps/${id}`,
    COUNT_MAPS: '/maps/count'
}


/**
 * Represents a CTM map. These are not all the details, but all that are send by the search api endpoint
 */
export interface MinecraftMap {
    id: number,
    name: string
    upload_date: number,
    author: string,
    length: string,
    objective_main: number,
    objective_bonus: number,
    difficulty: string,
    description_short: string,
    download_count: number,
    type: string,
    image_url: string,
    series: string,
    mc_version: string
}

/**
 * Get all maps from the backend. A react hook
 * @param {any[] | null} dependsArray re-request data when the values in the array change.
 * Set to [] (the default) to never reload data
 * @return {[boolean, MinecraftMap, any]} [whether its still loading, the data, error]
 */
export function useAllMaps(dependsArray: any[] | null = []): [boolean, MinecraftMap[], useFetch.UseFetchError | undefined] {
    return useFetchAPI(API_ENDPOINTS.ALL_MAPS({ q: '' }), dependsArray, []);
}

const ENTRIES_PER_PAGE: number = 12;

/**
 * Searches for maps from the backend with paging. A react hook
 * @param {string} query the search string
 * @param {page} page the page number
 * @param {any[] | null} dependsArray re-request data when the values in the array change.
 * Set to [] (the default) to never reload data
 * @return {[boolean, MinecraftMap, any]} [whether its still loading, the data, error]
 */
export function useGetMapsSearch(query: string, page: number = 0, dependsArray: any[] | null = [], per_page: number = ENTRIES_PER_PAGE): [boolean, MinecraftMap[], useFetch.UseFetchError | undefined] {
    return useFetchAPI(API_ENDPOINTS.ALL_MAPS({ q: query, per_page: per_page, page: page }), dependsArray, []);
}

/**
 * Get one map from the backend by id. A react hook
 * @param {number} id the id number of the map to get
 * @param {any[] | null} dependsArray re-request data when the values in the array change.
 * Set to [] (the default) to never reload data
 * @return {[boolean, MinecraftMap | null, any]} [whether its still loading, the data, error]
 */
export function useGetMap(id: number, dependsArray: any[] | null = []): [boolean, MinecraftMap | null, useFetch.UseFetchError | undefined] {
    return useFetchAPI(API_ENDPOINTS.ONE_MAP(id), dependsArray, null);
}


export function useGetMapsCount(dependsArray: any[] | null = []): [boolean, number, useFetch.UseFetchError | undefined] {
    return useFetchAPI(API_ENDPOINTS.COUNT_MAPS, dependsArray, 0);
}