import React, { useEffect } from 'react';
import ReactDOMClient from 'react-dom/client';
import { HashRouter, Route, Routes, useNavigate } from "react-router-dom";
import { SnackbarComponent } from './components/Snackbar';
import './index.scss';
import Main from './main';
import { ParamFilter } from './map';
import NoMatch from './no_match';
import UploadMap from './UploadMap';


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

let root_container = document.getElementById('root');
if (!root_container) {
  throw new Error("Missing root container element");
}

// Main render function
let root = ReactDOMClient.createRoot(root_container);
root.render(
  <React.StrictMode>
    {/* Route System Wrapper */}
    <HashRouter>
      {/* SnackbarComponent adds little messages for errors and the like */}
      <SnackbarComponent>
        {/* Set routes here */}
        <Routes>
          <Route path="/" element={<Reroute />} />
          <Route path="/Cartographers/">
            <Route path="" element={<Main />} />
            <Route path="maps/:id/*" element={<ParamFilter />} />
            <Route path="upload" element={<UploadMap />} />
          </Route>
          <Route path="*" element={<NoMatch />} />
        </Routes>
      </SnackbarComponent>
    </HashRouter>
  </React.StrictMode>
);

