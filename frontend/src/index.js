import React, { useEffect } from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter, Route, Routes, useLocation, useNavigate } from "react-router-dom";
import { SnackbarComponent } from './components/Snackbar';
import './index.css';
import Main from './main';

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

/**
 * View that redirects to the home page
 * @returns {JSX.Element} the view
 */
function Reroute() {
  const nav = useNavigate();

  useEffect(() => {
    nav('/Cartographers');
  });

  return <p>Redirection...</p>
}

console.log('gh pages are working');

// Main render function
ReactDOM.render(
  <React.StrictMode>
    {/* Route System Wrapper */}
    <BrowserRouter>
      {/* SnackbarComponent adds little messages for errors and the like */}
      <SnackbarComponent>
        {/* Set routes here */}
        <Routes>
          <Route path="/" element={<Reroute></Reroute>} />
          <Route path="/Cartographers/" element={<Main />} />
          <Route path="*" element={<NoMatch />} />
        </Routes>
      </SnackbarComponent>
    </BrowserRouter>
  </React.StrictMode>,
  document.getElementById('root')
);

