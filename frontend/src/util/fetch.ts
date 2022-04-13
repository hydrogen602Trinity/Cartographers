import { useNavigate } from "react-router-dom";
import { useSnackbar } from "../components/Snackbar";
import useFetch from "react-fetch-hook";
import { useEffect } from "react";
import { getRestAPI } from "./env_config";


/**
 * A react hook for getting data. The hook manages error handling
 * @param {string} path the api endpoint
 * @param {[any]} dependsArray values that should trigger a reload of data if they change
 * @returns {[isLoading, data, error]} whether its still loading, the data, error 
 */
export function useFetchAPI(path: string, dependsArray: any[] | null = null): [boolean, any, useFetch.UseFetchError | undefined] {
    if (path.startsWith('/')) {
        path = path.substring(1);
    }

    const dispatchMsg = useSnackbar();
    const nav = useNavigate();
    const args = {
        //credentials: 'include',
        depends: (dependsArray) ? dependsArray : undefined
    };

    const { isLoading, data, error } = useFetch(
        fullPath(path), args);

    useEffect(() => {
        if (error) {
            if (error.status === 401) {
                dispatchMsg({ type: 'error', text: 'Authentication Required' });
                nav('/Cartographers');
            }
            else {
                dispatchMsg({ type: 'error', text: error.message })
            }
        }
        // eslint-disable-next-line
    }, [error]);

    return [isLoading, data, error];
}

/**
 * Adds the api path to an api endpoint
 * @param {string} path the api endpoint 
 * @returns {string} path with http and the api path added
 */
function fullPath(path: string): string {
    return getRestAPI() + path
}

