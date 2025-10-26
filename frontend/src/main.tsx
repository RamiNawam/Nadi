import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import './index.css';

/**
 * Main entry point for the Nadi frontend application.
 * 
 * This file initializes the React application with:
 * - Global CSS styles
 * - Root component mounting
 */
ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
);

