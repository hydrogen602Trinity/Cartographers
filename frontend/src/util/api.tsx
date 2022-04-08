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
    // let m: any = {
    //     image_url: 'https://avatars.githubusercontent.com/u/55502571?s=400&u=81eee2f66bb699cdcc56b2d8d8b1edbb53ea5536&v=4',
    //     name: 'Empty Prop 1',
    //     author: 'hydrogen602',
    //     description_short: 'A basic prop for testing cause the backend doesn\'t work'
    // };
    // return [false, [
    //     m, m, m, m, m, m, m
    // ], undefined];

    let [isLoading, data, error] = useFetchAPI('/maps/search');

    if (error) {
        data = [];
    }

    return [isLoading, data, error];
}
