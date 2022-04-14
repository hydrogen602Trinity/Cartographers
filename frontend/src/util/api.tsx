import { useFetchAPI } from "./fetch"


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
    let [isLoading, data, error] = useFetchAPI('/maps/search');
    if (error) data = [];

    return [isLoading, data, error];
}
