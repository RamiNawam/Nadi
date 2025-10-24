import { Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
import Layout from './components/Layout';

/**
 * Main App component that defines the application routing structure.
 * 
 * This component sets up the main routes for the Nadi application:
 * - Home page with welcome message
 * - Future routes for user management, venue search, and reservations
 */
function App() {
  return (
    <Layout>
      <Routes>
        <Route path="/" element={<HomePage />} />
        {/* TODO: Add more routes as features are implemented */}
        {/* <Route path="/venues" element={<VenuesPage />} /> */}
        {/* <Route path="/reservations" element={<ReservationsPage />} /> */}
        {/* <Route path="/profile" element={<ProfilePage />} /> */}
      </Routes>
    </Layout>
  );
}

export default App;

