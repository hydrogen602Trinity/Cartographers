import React, { useEffect } from 'react';
import ReactDOM from 'react-dom';
import * as ReactDOMClient from 'react-dom/client';
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

let root_container = document.getElementById('root');
if (!root_container) {
  throw new Error("root element missing");
}

// Main render function
let root = ReactDOMClient.createRoot(root_container);
root.render(
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
  </React.StrictMode>
);

