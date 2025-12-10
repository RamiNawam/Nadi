import React, { useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, useNavigate } from 'react-router-dom';
import HomeGuest from './pages/HomeGuest';
import HomeUser from './pages/HomeUser';
import HomeVenue from './pages/HomeVenue';
import HomeDeveloper from './pages/HomeDeveloper';
import DeveloperAdminPage from './pages/DeveloperAdminPage';
import SearchPage from './pages/SearchPage';
import VenuePage from './pages/VenuePage';
import CreateVenuePage from './pages/CreateVenuePage';
import CreateVenueRequestPage from './pages/CreateVenueRequestPage';
import VenueCalendarPage from './pages/VenueCalendarPage';
import EditVenuePage from './pages/EditVenuePage';
import SportSearchPage from './pages/SportSearchPage';
import SignInPage from './pages/SignInPage';
import Layout from './components/Layout';
import { useAuth } from './store/authStore';

/**
 * Navigation guard component
 */
const ProtectedRoute: React.FC<{ children: React.ReactNode; requiredRole?: 'user' | 'venue' | 'developer' }> = ({ 
  children, 
  requiredRole 
}) => {
  const { isAuthenticated, user } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/signin');
      return;
    }

    if (requiredRole && user?.accountType !== requiredRole) {
      // Redirect to appropriate home based on account type
      switch (user?.accountType) {
        case 'user':
          navigate('/home', { replace: true });
          break;
        case 'venue':
          navigate('/homeVenue', { replace: true });
          break;
        case 'developer':
          navigate('/homeDeveloper', { replace: true });
          break;
        default:
          navigate('/homeGuest', { replace: true });
      }
    }
  }, [isAuthenticated, user, requiredRole, navigate]);

  if (!isAuthenticated || (requiredRole && user?.accountType !== requiredRole)) {
    return null;
  }

  return <>{children}</>;
};

/**
 * Redirect guard for sign-in page
 */
const SignInGuard: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { isAuthenticated, user } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (isAuthenticated && user) {
      // Redirect based on account type
      switch (user.accountType) {
        case 'user':
          navigate('/home', { replace: true });
          break;
        case 'venue':
          navigate('/homeVenue', { replace: true });
          break;
        case 'developer':
          navigate('/homeDeveloper', { replace: true });
          break;
        default:
          navigate('/homeGuest', { replace: true });
      }
    }
  }, [isAuthenticated, user, navigate]);

  if (isAuthenticated && user) {
    return null;
  }

  return <>{children}</>;
};

/**
 * Root redirect component
 */
const RootRedirect: React.FC = () => {
  const { isAuthenticated, user } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/homeGuest', { replace: true });
    } else {
      // Redirect based on account type
      switch (user?.accountType) {
        case 'user':
          navigate('/home', { replace: true });
          break;
        case 'venue':
          navigate('/homeVenue', { replace: true });
          break;
        case 'developer':
          navigate('/homeDeveloper', { replace: true });
          break;
        default:
          navigate('/homeGuest', { replace: true });
      }
    }
  }, [isAuthenticated, user, navigate]);

  return null;
};

/**
 * Application router with role-based routes and navigation guards
 */
const AppRouter: React.FC = () => {
  return (
    <Router>
      <Layout>
        <Routes>
          {/* Root redirects based on auth state */}
          <Route path="/" element={<RootRedirect />} />
          
          {/* Guest landing page (no auth required) */}
          <Route path="/homeGuest" element={<HomeGuest />} />
          
          {/* Sign-in page (redirects if already authenticated) */}
          <Route 
            path="/signin" 
            element={
              <SignInGuard>
                <SignInPage />
              </SignInGuard>
            } 
          />
          
          {/* User home (requires user account) */}
          <Route 
            path="/home" 
            element={
              <ProtectedRoute requiredRole="user">
                <HomeUser />
              </ProtectedRoute>
            } 
          />
          
          {/* Venue owner home (requires venue account) */}
          <Route 
            path="/homeVenue" 
            element={
              <ProtectedRoute requiredRole="venue">
                <HomeVenue />
              </ProtectedRoute>
            } 
          />
          
          {/* Developer home (requires developer account) */}
          <Route 
            path="/homeDeveloper" 
            element={
              <ProtectedRoute requiredRole="developer">
                <HomeDeveloper />
              </ProtectedRoute>
            } 
          />
          
          {/* Developer-only routes */}
          <Route 
            path="/developer/create-venue" 
            element={
              <ProtectedRoute requiredRole="developer">
                <CreateVenuePage />
              </ProtectedRoute>
            } 
          />
          
          <Route 
            path="/developer/admin" 
            element={
              <ProtectedRoute requiredRole="developer">
                <DeveloperAdminPage />
              </ProtectedRoute>
            } 
          />
          
          {/* Venue owner routes */}
          <Route 
            path="/venue/create-request" 
            element={
              <ProtectedRoute requiredRole="venue">
                <CreateVenueRequestPage />
              </ProtectedRoute>
            } 
          />
          
          <Route 
            path="/venue/calendar" 
            element={
              <ProtectedRoute requiredRole="venue">
                <VenueCalendarPage />
              </ProtectedRoute>
            } 
          />
          
          <Route 
            path="/venue/edit" 
            element={
              <ProtectedRoute requiredRole="venue">
                <EditVenuePage />
              </ProtectedRoute>
            } 
          />
          
          {/* Public routes */}
          <Route path="/search" element={<SearchPage />} />
          <Route path="/search/:sport" element={<SportSearchPage />} />
          <Route path="/venue/:id" element={<VenuePage />} />
        </Routes>
      </Layout>
    </Router>
  );
};

export default AppRouter;
