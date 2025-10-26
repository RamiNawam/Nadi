import React from 'react';
import { useLocation } from 'react-router-dom';

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
  const isLandingPage = location.pathname === '/';

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
            <h1 className="text-2xl font-bold text-blue-600">Nadi</h1>
            <nav className="flex space-x-6">
              <a href="/" className="text-gray-600 hover:text-gray-900 transition-colors">
                Home
              </a>
              <a href="/search" className="text-gray-600 hover:text-gray-900 transition-colors">
                Search Venues
              </a>
              <a href="/developer/create-venue" className="text-blue-600 hover:text-blue-700 transition-colors font-medium">
                Create Venue
              </a>
              <a href="/reservations" className="text-gray-600 hover:text-gray-900 transition-colors">
                My Reservations
              </a>
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
          <p>&copy; 2024 Nadi. All rights reserved.</p>
          <p className="mt-2 text-gray-400">
            Sports court reservation platform for Beirut
          </p>
        </div>
      </footer>
    </div>
  );
};

export default Layout;

