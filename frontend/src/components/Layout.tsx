import React from 'react';
import { useLocation, Link } from 'react-router-dom';
import { useAuth } from '../store/authStore';

/**
 * Layout component that wraps the entire application.
 * 
 * This component provides:
 * - Navigation header (minimal for landing page)
 * - Main content area
 * - Footer
 * - Consistent layout structure
 */
interface LayoutProps {
  children: React.ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
  const location = useLocation();
  const { user, isAuthenticated } = useAuth();
  const isLandingPage = location.pathname === '/' || location.pathname === '/homeGuest';

  // Helper function to check if a path is active
  const isActive = (path: string) => {
    if (path === '/home') {
      return location.pathname === '/home' || location.pathname === '/homeGuest';
    }
    return location.pathname === path || location.pathname.startsWith(path + '/');
  };

  // Get active link classes
  const getLinkClasses = (path: string) => {
    const baseClasses = "transition-colors";
    if (isActive(path)) {
      return `${baseClasses} text-blue-600 font-medium`;
    }
    return `${baseClasses} text-gray-600 hover:text-gray-900`;
  };

  if (isLandingPage) {
    // Minimal layout for landing page
    return (
      <div className="min-h-screen">
        {children}
      </div>
    );
  }

  return (
    <div className="min-h-screen flex flex-col">
      {/* Header */}
      <header className="bg-white shadow-sm border-b border-gray-200">
        <div className="container mx-auto px-4 py-4">
          <div className="flex items-center justify-between">
            <Link to={isAuthenticated && user?.accountType === 'developer' ? '/homeDeveloper' : '/homeGuest'}>
              <h1 className="text-2xl font-bold text-blue-800 cursor-pointer">Nadi</h1>
            </Link>
            <nav className="flex space-x-6">
              {user?.accountType === 'developer' ? (
                <>
                  <Link to="/homeDeveloper" className={getLinkClasses('/homeDeveloper')}>
                    Dashboard
                  </Link>
                  <Link to="/developer/create-venue" className={getLinkClasses('/developer/create-venue')}>
                    Create Venue
                  </Link>
                  <Link to="/developer/admin" className={getLinkClasses('/developer/admin')}>
                    Admin Panel
                  </Link>
                </>
              ) : (
                <>
                  <Link to={isAuthenticated ? '/home' : '/homeGuest'} className={getLinkClasses('/home')}>
                    Home
                  </Link>
                  <Link to="/search" className={getLinkClasses('/search')}>
                    Search Venues
                  </Link>
                </>
              )}
            </nav>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="flex-1">
        {children}
      </main>

      {/* Footer */}
      <footer className="bg-gray-800 text-white py-8">
        <div className="container mx-auto px-4 text-center">
          <p className="text-gray-400">
            Sports court reservation platform for Beirut
          </p>
        </div>
      </footer>
    </div>
  );
};

export default Layout;

