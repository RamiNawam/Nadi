import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../store/authStore';

const HomeGuest: React.FC = () => {
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="container mx-auto px-4 py-12">
        <div className="text-center mb-12">
          <h1 className="text-4xl font-bold text-gray-900 mb-4">
            Welcome to Nadi
          </h1>
          <p className="text-xl text-gray-600 mb-8">
            Discover and book sports courts in Beirut
          </p>
          {!isAuthenticated && (
            <button
              onClick={() => navigate('/signin')}
              className="bg-blue-800 text-white px-8 py-3 rounded-lg text-lg font-semibold hover:bg-blue-900 transition"
            >
              Sign In to Book Courts
            </button>
          )}
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-12">
          <div className="bg-white p-6 rounded-lg shadow-md text-center">
            <div className="text-5xl mb-4">⚽</div>
            <h3 className="text-xl font-semibold mb-2">Football</h3>
            <p className="text-gray-600 mb-4">Book football courts</p>
            <button
              onClick={() => navigate('/search/football')}
              className="bg-blue-800 text-white px-4 py-2 rounded hover:bg-blue-900"
            >
              Book a Court
            </button>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-md text-center">
            <div className="text-5xl mb-4">🏀</div>
            <h3 className="text-xl font-semibold mb-2">Basketball</h3>
            <p className="text-gray-600 mb-4">Reserve basketball courts</p>
            <button
              onClick={() => navigate('/search/basketball')}
              className="bg-blue-800 text-white px-4 py-2 rounded hover:bg-blue-900"
            >
              Book a Court
            </button>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-md text-center">
            <div className="text-5xl mb-4">🎾</div>
            <h3 className="text-xl font-semibold mb-2">Tennis</h3>
            <p className="text-gray-600 mb-4">Find tennis courts</p>
            <button
              onClick={() => navigate('/search/tennis')}
              className="bg-blue-800 text-white px-4 py-2 rounded hover:bg-blue-900"
            >
              Book a Court
            </button>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-md text-center">
            <div className="text-5xl mb-4">🏓</div>
            <h3 className="text-xl font-semibold mb-2">Padel</h3>
            <p className="text-gray-600 mb-4">Discover padel courts</p>
            <button
              onClick={() => navigate('/search/padel')}
              className="bg-blue-800 text-white px-4 py-2 rounded hover:bg-blue-900"
            >
              Book a Court
            </button>
          </div>
        </div>

        <div className="text-center">
          <button
            onClick={() => navigate('/search')}
            className="bg-blue-800 text-white px-6 py-3 rounded-lg font-semibold hover:bg-blue-900 transition"
          >
            Browse All Venues
          </button>
        </div>
      </div>
    </div>
  );
};

export default HomeGuest;

