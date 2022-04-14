/**
 * Handle env variables & config here
 */


/**
 * Gets the url of the backend
 * 
 * env vars: REACT_APP_URL_PRODUCTION, REACT_APP_URL_DEVELOPMENT
 * 
 * NODE_ENV is auto managed based on npm start vs. npm run build
 * Use REACT_APP_FORCE_URL_PRODUCTION=true to use the production backend in development
 * 
 * @returns {string} the url
 */
export function getRestAPI(): string {
    const url = process.env.NODE_ENV === "production" || process.env.REACT_APP_FORCE_URL_PRODUCTION === "true" ?
        process.env.REACT_APP_URL_PRODUCTION :
        process.env.REACT_APP_URL_DEVELOPMENT;

    if (url) return url;

    throw new Error("Invalid Environment URL");
}


/**
 * Gets the necessary root of the path to static content. For example,
 * rendering the logo in development requires `Cartographers/logo.webp`,
 * but in production it needs to be `logo.webp`.
 * 
 * @param path the path to the static content
 * @returns the path with the root added
 */
export function getWithStaticPrefix(path: string): string {
    if (process.env.NODE_ENV === "production") {
        return path;
    }
    else {
        return 'Cartographers/' + path;
    }
}
