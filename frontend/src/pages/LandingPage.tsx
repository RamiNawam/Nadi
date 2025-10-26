import React from 'react';
import { useNavigate } from 'react-router-dom';

const LandingPage: React.FC = () => {
  const navigate = useNavigate();

  const sports = [
    {
      id: 'football',
      name: 'Football',
      icon: '⚽',
      description: 'Book football courts for your matches',
      color: 'from-green-400 to-green-600'
    },
    {
      id: 'basketball',
      name: 'Basketball',
      icon: '🏀',
      description: 'Find basketball courts near you',
      color: 'from-orange-400 to-orange-600'
    },
    {
      id: 'tennis',
      name: 'Tennis',
      icon: '🎾',
      description: 'Reserve tennis courts for your games',
      color: 'from-yellow-400 to-yellow-600'
    },
    {
      id: 'padel',
      name: 'Padel',
      icon: '🏓',
      description: 'Book padel courts for your matches',
      color: 'from-blue-400 to-blue-600'
    }
  ];

  const handleBookCourt = (sport: string) => {
    navigate(`/search/${sport}`);
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Hero Section */}
      <div className="bg-gradient-to-r from-blue-600 to-purple-600 text-white py-20">
        <div className="container mx-auto px-4 text-center">
          <h1 className="text-5xl font-bold mb-6">Nadi</h1>
          <p className="text-xl mb-8 max-w-2xl mx-auto">
            Discover and book sports courts in Beirut. Find the perfect venue for your next game.
          </p>
          <div className="flex justify-center space-x-4">
            <button 
              onClick={() => navigate('/search')}
              className="bg-white text-blue-600 px-8 py-3 rounded-lg font-semibold hover:bg-gray-100 transition-colors"
            >
              Browse All Venues
            </button>
            <button 
              onClick={() => navigate('/developer/create-venue')}
              className="border-2 border-white text-white px-8 py-3 rounded-lg font-semibold hover:bg-white hover:text-blue-600 transition-colors"
            >
              Add Your Venue
            </button>
          </div>
        </div>
      </div>

      {/* Sports Grid */}
      <div className="container mx-auto px-4 py-16">
        <div className="text-center mb-12">
          <h2 className="text-4xl font-bold text-gray-800 mb-4">Choose Your Sport</h2>
          <p className="text-xl text-gray-600">Select a sport to find and book courts</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8 max-w-6xl mx-auto">
          {sports.map((sport) => (
            <div
              key={sport.id}
              className={`bg-gradient-to-br ${sport.color} rounded-2xl p-8 text-white text-center transform hover:scale-105 transition-all duration-300 shadow-lg hover:shadow-xl`}
            >
              <div className="text-6xl mb-4">{sport.icon}</div>
              <h3 className="text-2xl font-bold mb-3">{sport.name}</h3>
              <p className="text-white/90 mb-6 text-sm">{sport.description}</p>
              <button
                onClick={() => handleBookCourt(sport.id)}
                className="bg-white text-gray-800 px-6 py-3 rounded-lg font-semibold hover:bg-gray-100 transition-colors w-full"
              >
                Book a Court
              </button>
            </div>
          ))}
        </div>
      </div>

      {/* Features Section */}
      <div className="bg-white py-16">
        <div className="container mx-auto px-4">
          <div className="text-center mb-12">
            <h2 className="text-4xl font-bold text-gray-800 mb-4">Why Choose Nadi?</h2>
            <p className="text-xl text-gray-600">The best way to book sports courts in Beirut</p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8 max-w-4xl mx-auto">
            <div className="text-center">
              <div className="bg-blue-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
                <span className="text-2xl">📍</span>
              </div>
              <h3 className="text-xl font-semibold mb-2">Find Nearby Venues</h3>
              <p className="text-gray-600">Discover sports courts close to your location</p>
            </div>

            <div className="text-center">
              <div className="bg-green-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
                <span className="text-2xl">⚡</span>
              </div>
              <h3 className="text-xl font-semibold mb-2">Instant Booking</h3>
              <p className="text-gray-600">Book courts instantly with real-time availability</p>
            </div>

            <div className="text-center">
              <div className="bg-purple-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
                <span className="text-2xl">🏆</span>
              </div>
              <h3 className="text-xl font-semibold mb-2">Quality Venues</h3>
              <p className="text-gray-600">Access to the best sports facilities in Beirut</p>
            </div>
          </div>
        </div>
      </div>

      {/* Footer */}
      <div className="bg-gray-800 text-white py-12">
        <div className="container mx-auto px-4 text-center">
          <h3 className="text-2xl font-bold mb-4">Ready to Play?</h3>
          <p className="text-gray-300 mb-6">Join thousands of sports enthusiasts in Beirut</p>
          <button 
            onClick={() => navigate('/search')}
            className="bg-blue-600 text-white px-8 py-3 rounded-lg font-semibold hover:bg-blue-700 transition-colors"
          >
            Start Booking Now
          </button>
        </div>
      </div>
    </div>
  );
};

export default LandingPage;
