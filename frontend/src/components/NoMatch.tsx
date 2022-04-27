import { useLocation, useNavigate } from "react-router-dom";

/**
 * 404 Page Component
 * @returns {JSX.Element} the view component
 */
function NoMatch() {
    let location = useLocation();
    const nav = useNavigate();

    return (
        <div style={{ padding: '1em' }}>
            <h1>404</h1>
            <h3>
                No match for <code>{location.pathname}</code>
            </h3>
            <button onClick={() => {
                nav('/Cartographers');
            }}>Home</button>
        </div>
    );
}

export default NoMatch;
