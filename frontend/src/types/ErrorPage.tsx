import { FC } from "react";
import { UseFetchError } from "react-fetch-hook";
import { useNavigate } from "react-router-dom";

interface ErrorProps {
    error: UseFetchError,
}

/**
 * Generic Error Page Component
 * @returns {JSX.Element} the view component
 */
const ErrorPage: FC<ErrorProps> = ({ error }): JSX.Element => {
    const navigate = useNavigate();

    return (
        <div style={{ padding: '1em' }}>
            <h1>{error.status}</h1>
            <button onClick={() => {
                navigate('/');
            }}>Home</button>
        </div>
    );
}

export default ErrorPage
