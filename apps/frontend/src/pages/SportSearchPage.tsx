import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';

const SportSearchPage: React.FC = () => {
  const { sport } = useParams<{ sport: string }>();
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedArea, setSelectedArea] = useState('');

  const sportInfo = {
    football: {
      name: 'Football',
      icon: '⚽',
      color: 'from-green-400 to-green-600',
      description: 'Find football courts for your matches'
    },
    basketball: {
      name: 'Basketball',
      icon: '🏀',
      color: 'from-orange-400 to-orange-600',
      description: 'Discover basketball courts near you'
    },
    tennis: {
      name: 'Tennis',
      icon: '🎾',
      color: 'from-yellow-400 to-yellow-600',
      description: 'Reserve tennis courts for your games'
    },
    padel: {
      name: 'Padel',
      icon: '🏓',
      color: 'from-blue-400 to-blue-600',
      description: 'Book padel courts for your matches'
    }
  };

  const currentSport = sportInfo[sport as keyof typeof sportInfo];

  const areas = [
    'Downtown Beirut',
    'Hamra',
    'Achrafieh',
    'Verdun',
    'Raouche',
    'Mar Mikhael',
    'Gemmayze',
    'Badaro'
  ];

  // Mock venues data
  const mockVenues = [
    {
      id: 1,
      name: `${currentSport?.name} Center Beirut`,
      location: 'Downtown Beirut',
      price: '25',
      rating: 4.8,
      distance: '0.5 km',
      courts: 3,
      image: `https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400&h=300&fit=crop&crop=center`
    },
    {
      id: 2,
      name: `Elite ${currentSport?.name} Club`,
      location: 'Hamra',
      price: '30',
      rating: 4.6,
      distance: '1.2 km',
      courts: 2,
      image: `https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400&h=300&fit=crop&crop=center`
    },
    {
      id: 3,
      name: `Beirut ${currentSport?.name} Arena`,
      location: 'Achrafieh',
      price: '20',
      rating: 4.5,
      distance: '2.1 km',
      courts: 4,
      image: `https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400&h=300&fit=crop&crop=center`
    }
  ];

  const handleBookVenue = (venueId: number) => {
    // Navigate to booking page or show booking modal
    alert(`Booking ${mockVenues.find(v => v.id === venueId)?.name}`);
  };

  if (!currentSport) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <h1 className="text-4xl font-bold text-gray-800 mb-4">Sport Not Found</h1>
          <button 
            onClick={() => navigate('/')}
            className="bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700"
          >
            Back to Home
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <div className={`bg-gradient-to-r ${currentSport.color} text-white py-12`}>
        <div className="container mx-auto px-4">
          <button 
            onClick={() => navigate('/')}
            className="mb-4 text-white/80 hover:text-white transition-colors"
          >
            ← Back to Home
          </button>
          <div className="text-center">
            <div className="text-6xl mb-4">{currentSport.icon}</div>
            <h1 className="text-4xl font-bold mb-2">{currentSport.name} Courts</h1>
            <p className="text-xl text-white/90">{currentSport.description}</p>
          </div>
        </div>
      </div>

      {/* Search Filters */}
      <div className="bg-white shadow-sm py-6">
        <div className="container mx-auto px-4">
          <div className="flex flex-col md:flex-row gap-4 max-w-4xl mx-auto">
            <div className="flex-1">
              <input
                type="text"
                placeholder="Search venues..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
            </div>
            <div className="md:w-64">
              <select
                value={selectedArea}
                onChange={(e) => setSelectedArea(e.target.value)}
                className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              >
                <option value="">All Areas</option>
                {areas.map(area => (
                  <option key={area} value={area}>{area}</option>
                ))}
              </select>
            </div>
            <button className="bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700 transition-colors">
              Search
            </button>
          </div>
        </div>
      </div>

      {/* Venues Grid */}
      <div className="container mx-auto px-4 py-8">
        <div className="mb-6">
          <h2 className="text-2xl font-bold text-gray-800 mb-2">Available Venues</h2>
          <p className="text-gray-600">Found {mockVenues.length} venues for {currentSport.name}</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {mockVenues.map((venue) => (
            <div key={venue.id} className="bg-white rounded-xl shadow-lg overflow-hidden hover:shadow-xl transition-shadow">
              <div className="h-48 bg-gray-200 relative">
                <img 
                  src={venue.image} 
                  alt={venue.name}
                  className="w-full h-full object-cover"
                />
                <div className="absolute top-4 right-4 bg-white px-2 py-1 rounded-full text-sm font-semibold">
                  {venue.distance}
                </div>
              </div>
              
              <div className="p-6">
                <h3 className="text-xl font-bold text-gray-800 mb-2">{venue.name}</h3>
                <p className="text-gray-600 mb-3">{venue.location}</p>
                
                <div className="flex items-center justify-between mb-4">
                  <div className="flex items-center">
                    <span className="text-yellow-500 mr-1">★</span>
                    <span className="font-semibold">{venue.rating}</span>
                  </div>
                  <div className="text-sm text-gray-600">
                    {venue.courts} courts available
                  </div>
                </div>

                <div className="flex items-center justify-between">
                  <div className="text-2xl font-bold text-blue-600">
                    ${venue.price}/hour
                  </div>
                  <button
                    onClick={() => handleBookVenue(venue.id)}
                    className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors"
                  >
                    Book Now
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* No Results */}
      {mockVenues.length === 0 && (
        <div className="container mx-auto px-4 py-16 text-center">
          <div className="text-6xl mb-4">🔍</div>
          <h3 className="text-2xl font-bold text-gray-800 mb-2">No venues found</h3>
          <p className="text-gray-600 mb-6">Try adjusting your search criteria</p>
          <button 
            onClick={() => {
              setSearchTerm('');
              setSelectedArea('');
            }}
            className="bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700 transition-colors"
          >
            Clear Filters
          </button>
        </div>
      )}
    </div>
  );
};

export default SportSearchPage;
