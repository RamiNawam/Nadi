import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LandingPage from './pages/LandingPage';
import SearchPage from './pages/SearchPage';
import VenuePage from './pages/VenuePage';
import CreateVenuePage from './pages/CreateVenuePage';
import SportSearchPage from './pages/SportSearchPage';
import Layout from './components/Layout';

/**
 * Application router with basic routes
 */
const AppRouter: React.FC = () => {
  return (
    <Router>
      <Layout>
        <Routes>
          <Route path="/" element={<LandingPage />} />
          <Route path="/search" element={<SearchPage />} />
          <Route path="/search/:sport" element={<SportSearchPage />} />
          <Route path="/venue/:id" element={<VenuePage />} />
          <Route path="/developer/create-venue" element={<CreateVenuePage />} />
        </Routes>
      </Layout>
    </Router>
  );
};

export default AppRouter;
