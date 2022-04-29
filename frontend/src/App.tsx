import { SnackbarComponent } from 'components/Snackbar';
import React from 'react';
import { HashRouter, Route, Routes } from "react-router-dom";
import Home from 'pages/Home';
import MapView from 'pages/MapView';
import NoMatch from 'pages/NoMatch';

function App() {
  return (
    <React.StrictMode>
      <HashRouter basename="/">
        <SnackbarComponent>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/maps/:id/*" element={<MapView />} />
            <Route path="*" element={<NoMatch />} />
          </Routes>
        </SnackbarComponent>
      </HashRouter>
    </React.StrictMode>
  );
}

export default App;
