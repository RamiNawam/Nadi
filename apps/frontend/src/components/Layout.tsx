import React from 'react';

/**
 * Layout component that wraps the entire application.
 * 
 * This component provides:
 * - Navigation header
 * - Main content area
 * - Footer
 * - Consistent layout structure
 */
interface LayoutProps {
  children: React.ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
  return (
    <div className="min-h-screen flex flex-col">
      {/* Header */}
      <header className="bg-white shadow-sm border-b">
        <div className="container mx-auto px-4 py-4">
          <div className="flex items-center justify-between">
            <h1 className="text-2xl font-bold text-blue-600">Nadi</h1>
            <nav className="space-x-4">
              {/* TODO: Add navigation links */}
              <a href="/" className="text-gray-600 hover:text-gray-900">
                Home
              </a>
              <a href="/venues" className="text-gray-600 hover:text-gray-900">
                Venues
              </a>
              <a href="/reservations" className="text-gray-600 hover:text-gray-900">
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

