import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter, Route, Routes, useLocation } from "react-router-dom";
import { SnackbarComponent } from './components/Snackbar';
import './index.css';
import Main from './main';

function NoMatch() {
  let location = useLocation();

  return (
    <div style={{ padding: '1em' }}>
      <h1>404</h1>
      <h3>
        No match for <code>{location.pathname}</code>
      </h3>
    </div>
  );
}

ReactDOM.render(
  <React.StrictMode>
    <BrowserRouter>
      <SnackbarComponent>
        <Routes>
          <Route path="/" element={<Main />} />
          <Route path="*" element={<NoMatch />} />
        </Routes>
      </SnackbarComponent>
    </BrowserRouter>
  </React.StrictMode>,
  document.getElementById('root')
);

