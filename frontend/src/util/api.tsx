import { useFetchAPI } from "./fetch"

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
    ONE_MAP: (id: number) => `/maps/${id}`
}


/**
 * Represents a CTM map. These are not all the details, but all that are send by the search api endpoint
 */
export interface MCMap {
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
 * @return {[boolean, MCMap, any]} [whether its still loading, the data, error]
 */
export function useAllMaps(): [boolean, MCMap[], any] {
    return useFetchAPI(API_ENDPOINTS.ALL_MAPS({ q: '' }), null, []);
}

const ENTRIES_PER_PAGE: number = 12;

/**
 * Searches for maps from the backend with paging. A react hook
 * @param {string} query the search string
 * @param {page} page the page number
 * @return {[boolean, MCMap, any]} [whether its still loading, the data, error]
 */
export function useSearchMaps(query: string, page: number = 0): [boolean, MCMap[], any] {
    return useFetchAPI(API_ENDPOINTS.ALL_MAPS({ q: query, per_page: ENTRIES_PER_PAGE, page: page }), null, []);
}

/**
 * Get one map from the backend by id. A react hook
 * @param {number} id the id number of the map to get 
 * @return {[boolean, MCMap | null, any]} [whether its still loading, the data, error]
 */
export function useMap(id: number): [boolean, MCMap | null, any] {
    return useFetchAPI(API_ENDPOINTS.ONE_MAP(id), null, null);
}
