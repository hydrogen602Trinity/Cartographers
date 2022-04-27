import React, { useEffect } from 'react';

import Home from './pages/Home';
import { HashRouter, Route, Routes, useNavigate } from "react-router-dom";
import { SnackbarComponent } from './components/Snackbar';
import { ParamFilter } from './components/MapView';
import NoMatch from './components/NoMatch';

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

function App() {
  return (
    <React.StrictMode>
      <HashRouter>
        <div>
          <SnackbarComponent>
            <Routes>
              <Route path="/" element={<Reroute />} />
              <Route path="/Cartographers/">
                <Route path="" element={<Home />} />
                <Route path="maps/:id/*" element={<ParamFilter />} />
              </Route>
              <Route path="*" element={<NoMatch />} />
            </Routes>
          </SnackbarComponent>
          <Home />
        </div>
      </HashRouter>
    </React.StrictMode>
  );
}

export default App;
