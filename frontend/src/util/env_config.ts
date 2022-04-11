/**
 * Handle env variables & config here
 */


/**
 * Gets the url of the backend
 * 
 * env vars: REACT_APP_IS_PRODUCTION, REACT_APP_FORCE_PROD
 * 
 * REACT_APP_IS_PRODUCTION is auto managed based on npm start vs. npm run build
 * Use FORCE_PROD to use the production backend in development
 * 
 * @returns {string} the url
 */
export function get_url(): string {
    // console.log(process.env)
    if (process.env.REACT_APP_IS_PRODUCTION || process.env.REACT_APP_FORCE_PROD) {
        return 'https://' + process.env.REACT_APP_PROD_API + '/api/';
    }
    else {
        return 'http://' + process.env.REACT_APP_DEV_API + '/api/';
    }
}
