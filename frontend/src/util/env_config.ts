/**
 * Handle env variables & config here
 */


/**
 * Gets the url of the backend
 * 
 * env vars: REACT_APP_URL_PRODUCTION, REACT_APP_URL_DEVELOPMENT
 * 
 * NODE_ENV is auto managed based on npm start vs. npm run build
 * Use REACT_APP_FORCE_URL_PRODUCTION to use the production backend in development
 * 
 * @returns {string} the url
 */
export function getRestAPI(): string {
    const url = process.env.NODE_ENV === "production" || process.env.REACT_APP_FORCE_URL_PRODUCTION === "true" ?
        process.env.REACT_APP_URL_PRODUCTION :
        process.env.REACT_APP_URL_DEVELOPMENT;

    if (url) return url

    throw new Error("Invalid Environment URL")
}
