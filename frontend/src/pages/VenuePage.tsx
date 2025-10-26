import React from 'react';

/**
 * Venue details page
 */
const VenuePage: React.FC = () => {
  return (
    <div className="container" style={{ padding: '2rem 1rem' }}>
      <h1 style={{ fontSize: '2rem', fontWeight: 'bold', marginBottom: '1rem', color: '#1e293b' }}>Venue Details</h1>
      <div style={{ marginBottom: '2rem' }}>
        {/* TODO: Display venue information */}
        <p style={{ color: '#6b7280' }}>Venue details coming soon...</p>
      </div>
      <div style={{ marginBottom: '2rem' }}>
        {/* TODO: Display available courts */}
        <p style={{ color: '#6b7280' }}>Available courts will be listed here</p>
      </div>
      <div>
        {/* TODO: Implement booking flow */}
        <p style={{ color: '#6b7280' }}>Booking functionality coming soon...</p>
      </div>
    </div>
  );
};

export default VenuePage;
