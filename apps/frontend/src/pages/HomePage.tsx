import React from 'react';

/**
 * HomePage component - the main landing page for Nadi.
 * 
 * This component displays:
 * - Welcome message
 * - Brief description of the platform
 * - Call-to-action buttons for future features
 */
const HomePage: React.FC = () => {
  return (
    <div className="container mx-auto px-4 py-8">
      <div className="text-center">
        <h1 className="text-4xl font-bold text-gray-900 mb-6">
          Welcome to Nadi
        </h1>
        
        <p className="text-xl text-gray-600 mb-8 max-w-2xl mx-auto">
          Your premier destination for reserving sports courts in Beirut. 
          Find and book football, basketball, padel, and tennis courts 
          with ease.
        </p>

        <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-6 mt-12">
          <div className="card text-center">
            <div className="text-4xl mb-4">⚽</div>
            <h3 className="text-lg font-semibold mb-2">Football</h3>
            <p className="text-gray-600">
              Book football courts across Beirut
            </p>
          </div>

          <div className="card text-center">
            <div className="text-4xl mb-4">🏀</div>
            <h3 className="text-lg font-semibold mb-2">Basketball</h3>
            <p className="text-gray-600">
              Reserve basketball courts near you
            </p>
          </div>

          <div className="card text-center">
            <div className="text-4xl mb-4">🎾</div>
            <h3 className="text-lg font-semibold mb-2">Tennis</h3>
            <p className="text-gray-600">
              Find tennis courts for your matches
            </p>
          </div>

          <div className="card text-center">
            <div className="text-4xl mb-4">🏓</div>
            <h3 className="text-lg font-semibold mb-2">Padel</h3>
            <p className="text-gray-600">
              Discover padel courts in the city
            </p>
          </div>
        </div>

        <div className="mt-12">
          <button className="btn btn-primary mr-4">
            Find Venues
          </button>
          <button className="btn btn-secondary">
            Learn More
          </button>
        </div>
      </div>
    </div>
  );
};

export default HomePage;

