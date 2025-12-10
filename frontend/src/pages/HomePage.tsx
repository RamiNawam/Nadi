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
    <div className="container" style={{ padding: '2rem 1rem', backgroundColor: '#f8fafc', minHeight: '100vh' }}>
      <div className="text-center">
        <h1 style={{ fontSize: '2.5rem', fontWeight: 'bold', marginBottom: '1.5rem', color: '#1e293b' }}>
          Welcome to Nadi
        </h1>
        
        <p style={{ fontSize: '1.25rem', marginBottom: '2rem', color: '#6b7280', maxWidth: '600px', margin: '0 auto 2rem auto' }}>
          Your premier destination for reserving sports courts in Beirut. 
          Find and book football, basketball, padel, and tennis courts 
          with ease.
        </p>

        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))', gap: '1.5rem', marginTop: '3rem' }}>
          <div className="card text-center">
            <div style={{ fontSize: '2.5rem', marginBottom: '1rem' }}>⚽</div>
            <h3 style={{ fontSize: '1.125rem', fontWeight: '600', marginBottom: '0.5rem' }}>Football</h3>
            <p style={{ color: '#6b7280' }}>
              Book football courts across Beirut
            </p>
          </div>

          <div className="card text-center">
            <div style={{ fontSize: '2.5rem', marginBottom: '1rem' }}>🏀</div>
            <h3 style={{ fontSize: '1.125rem', fontWeight: '600', marginBottom: '0.5rem' }}>Basketball</h3>
            <p style={{ color: '#6b7280' }}>
              Reserve basketball courts near you
            </p>
          </div>

          <div className="card text-center">
            <div style={{ fontSize: '2.5rem', marginBottom: '1rem' }}>🎾</div>
            <h3 style={{ fontSize: '1.125rem', fontWeight: '600', marginBottom: '0.5rem' }}>Tennis</h3>
            <p style={{ color: '#6b7280' }}>
              Find tennis courts for your matches
            </p>
          </div>

          <div className="card text-center">
            <div style={{ fontSize: '2.5rem', marginBottom: '1rem' }}>🏓</div>
            <h3 style={{ fontSize: '1.125rem', fontWeight: '600', marginBottom: '0.5rem' }}>Padel</h3>
            <p style={{ color: '#6b7280' }}>
              Discover padel courts in the city
            </p>
          </div>
        </div>

        <div style={{ marginTop: '3rem' }}>
          <button 
            className="btn btn-primary" 
            style={{ marginRight: '1rem' }}
            onClick={() => window.location.href = '/search'}
          >
            Find Venues
          </button>
          <button 
            className="btn btn-secondary"
            onClick={() => window.location.href = '/signin'}
          >
            Sign In / Sign Up
          </button>
        </div>
      </div>
    </div>
  );
};

export default HomePage;

