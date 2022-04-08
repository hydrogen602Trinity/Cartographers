import { useNavigate } from "react-router-dom";
import { useSnackbar } from "../components/Snackbar";
import useFetch from "react-fetch-hook";
import { useEffect } from "react";


/**
 * A react hook for getting data. The hook manages error handling
 * @param {string} path the api endpoint
 * @param {[any]} dependsArray values that should trigger a reload of data if they change
 * @returns {[isLoading, data, error]} whether its still loading, the data, error 
 */
export function useFetchAPI(path, dependsArray = null) {
    if (path.startsWith('/')) {
        path = path.substring(1);
    }

    const dispatchMsg = useSnackbar();
    const nav = useNavigate();
    const args = {
        //credentials: 'include',
    };
    if (dependsArray) {
        args.depends = dependsArray;
    }

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
            // eslint-disable-next-line
        }
    }, [error]);

    return [isLoading, data, error];
}

/**
 * Adds the api path to an api endpoint
 * @param {string} path the api endpoint 
 * @returns path with http and the api path added
 */
export function fullPath(path) {
    if (process.env.REACT_APP_IS_PRODUCTION) {
        return 'https://' + process.env.REACT_APP_PROD_API + '/api/' + path;
    }
    else {
        return 'http://' + process.env.REACT_APP_DEV_API + '/api/' + path;
    }
}

