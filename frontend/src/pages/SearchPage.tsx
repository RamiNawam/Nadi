import React from 'react';

/**
 * Search page for finding venues and courts
 */
const SearchPage: React.FC = () => {
  return (
    <div className="container" style={{ padding: '2rem 1rem' }}>
      <h1 style={{ fontSize: '2rem', fontWeight: 'bold', marginBottom: '1rem', color: '#1e293b' }}>Find Sports Venues</h1>
      <div style={{ marginBottom: '2rem' }}>
        {/* TODO: Implement search filters */}
        <p style={{ color: '#6b7280' }}>Search functionality coming soon...</p>
      </div>
      <div>
        {/* TODO: Display search results */}
        <p style={{ color: '#6b7280' }}>Search results will appear here</p>
      </div>
    </div>
  );
};

export default SearchPage;
