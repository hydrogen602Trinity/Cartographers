import React from 'react';
import ReactDOMClient from 'react-dom/client';
import { ThemeProvider } from '@mui/material/styles';
import theme from './theme';
import App from './App';
import './index.scss';


let root_container = document.getElementById('root');
if (!root_container) {
  throw new Error("Missing root container element");
}

// Main render function
let root = ReactDOMClient.createRoot(root_container);
root.render(
  <React.StrictMode>
    <ThemeProvider theme={theme}>
      <App />
    </ThemeProvider>
  </React.StrictMode>
);

