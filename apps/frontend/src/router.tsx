import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
import SearchPage from './pages/SearchPage';
import VenuePage from './pages/VenuePage';
import Layout from './components/Layout';

/**
 * Application router with basic routes
 */
const AppRouter: React.FC = () => {
  return (
    <Router>
      <Layout>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/search" element={<SearchPage />} />
          <Route path="/venue/:id" element={<VenuePage />} />
        </Routes>
      </Layout>
    </Router>
  );
};

export default AppRouter;
