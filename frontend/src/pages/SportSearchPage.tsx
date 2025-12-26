import React, { useEffect, useMemo, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { apiClient } from '../services/ApiClient';

const SportSearchPage: React.FC = () => {
  const { sport } = useParams<{ sport: string }>();
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedArea, setSelectedArea] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [venues, setVenues] = useState<Array<{
    id: string;
    name: string;
    address: string;
    cafeteriaAvailable: boolean;
    location?: { lat?: number; lng?: number };
  }>>([]);

  const sportInfo = {
    football: {
      name: 'Football',
      icon: '⚽',
      color: 'from-blue-800 to-blue-900',
      description: 'Find football courts for your matches'
    },
    basketball: {
      name: 'Basketball',
      icon: '🏀',
      color: 'from-blue-800 to-blue-900',
      description: 'Discover basketball courts near you'
    },
    tennis: {
      name: 'Tennis',
      icon: '🎾',
      color: 'from-blue-800 to-blue-900',
      description: 'Reserve tennis courts for your games'
    },
    padel: {
      name: 'Padel',
      icon: '🏓',
      color: 'from-blue-800 to-blue-900',
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

  useEffect(() => {
    const fetchVenues = async () => {
      setLoading(true);
      setError(null);
      try {
        const results = await apiClient.get<typeof venues>('/venues');
        setVenues(results || []);
      } catch (err) {
        console.error(err);
        setError('Failed to load venues. Please try again.');
      } finally {
        setLoading(false);
      }
    };
    fetchVenues();
  }, []);

  const filteredVenues = useMemo(() => {
    return venues.filter(v => {
      const matchesArea = selectedArea ? v.address?.toLowerCase().includes(selectedArea.toLowerCase()) : true;
      const matchesSearch = searchTerm
        ? v.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
          v.address.toLowerCase().includes(searchTerm.toLowerCase())
        : true;
      return matchesArea && matchesSearch;
    });
  }, [venues, searchTerm, selectedArea]);

  const handleBookVenue = (venueId: string) => {
    navigate(`/venue/${venueId}`);
  };

  if (!currentSport) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <h1 className="text-4xl font-bold text-gray-800 mb-4">Sport Not Found</h1>
          <button 
            onClick={() => navigate('/')}
            className="bg-blue-800 text-white px-6 py-3 rounded-lg hover:bg-blue-900"
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
            <button className="bg-blue-800 text-white px-6 py-3 rounded-lg hover:bg-blue-900 transition-colors">
              Search
            </button>
          </div>
        </div>
      </div>

      {/* Venues Grid */}
      <div className="container mx-auto px-4 py-8">
        <div className="mb-6">
          <h2 className="text-2xl font-bold text-gray-800 mb-2">Available Venues</h2>
          <p className="text-gray-600">
            {loading ? 'Loading venues...' : `Found ${filteredVenues.length} venues`}
          </p>
          {error && <p className="text-red-600 text-sm mt-1">{error}</p>}
        </div>

        {!loading && filteredVenues.length === 0 && (
          <div className="text-gray-600">No venues found. Try a different search.</div>
        )}

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredVenues.map((venue) => (
            <div key={venue.id} className="bg-white rounded-xl shadow-lg overflow-hidden hover:shadow-xl transition-shadow">
              <div className="h-48 bg-gray-200 relative flex items-center justify-center text-gray-500 text-sm">
                {venue.location?.lat && venue.location?.lng
                  ? `Lat: ${venue.location.lat.toFixed(3)}, Lng: ${venue.location.lng.toFixed(3)}`
                  : 'Venue'}
              </div>
              
              <div className="p-6">
                <h3 className="text-xl font-bold text-gray-800 mb-2">{venue.name}</h3>
                <p className="text-gray-600 mb-3">{venue.address}</p>
                
                <div className="flex items-center justify-between mb-4">
                  <div className="text-sm text-gray-600">
                    Cafeteria: {venue.cafeteriaAvailable ? 'Yes' : 'No'}
                  </div>
                  <div className="text-sm text-gray-600">
                    ID: {venue.id.slice(0, 8)}...
                  </div>
                </div>

                <div className="flex items-center justify-between">
                  <div className="text-lg font-semibold text-blue-600">
                    {currentSport.name}
                  </div>
                  <button
                    onClick={() => handleBookVenue(venue.id)}
                    className="bg-blue-800 text-white px-4 py-2 rounded-lg hover:bg-blue-900 transition-colors"
                  >
                    View
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default SportSearchPage;
