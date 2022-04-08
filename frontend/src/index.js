import React, { useEffect } from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter, Route, Routes, useLocation, useNavigate } from "react-router-dom";
import { SnackbarComponent } from './components/Snackbar';
import './index.css';
import Main from './main';

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

function Reroute() {
  const nav = useNavigate();

  useEffect(() => {
    nav('/Cartographers');
  });

  return <p>Redirection...</p>
}

ReactDOM.render(
  <React.StrictMode>
    <BrowserRouter>
      <SnackbarComponent>
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

