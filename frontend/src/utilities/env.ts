/**
 * Handle env variables & config here
 */


/**
 * Gets the url of the backend
 * 
 * env vars: REACT_APP_PRODUCTION_API, REACT_APP_DEVELOPMENT_API
 * 
 * NODE_ENV is auto managed based on npm start vs. npm run build
 * Use REACT_APP_USE_PRODUCTION_API=true to use the production backend in development
 * 
 * @returns {string} the url
 */
export function getRestAPI(): string {
    const url = process.env.NODE_ENV === "production" || process.env.REACT_APP_USE_PRODUCTION_API === "true" ?
        process.env.REACT_APP_PRODUCTION_API :
        process.env.REACT_APP_DEVELOPMENT_API;

    if (url) return url;

    throw new Error("Invalid Environment URL");
}
