import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../store/authStore';

const HomeUser: React.FC = () => {
  const navigate = useNavigate();
  const { user, logout } = useAuth();

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="container mx-auto px-4 py-12">
        <div className="flex justify-between items-center mb-8">
          <div>
            <h1 className="text-4xl font-bold text-gray-900">
              Welcome back, {user?.name}!
            </h1>
            <p className="text-gray-600 mt-2">Find and book your favorite sports courts</p>
          </div>
          <button
            onClick={logout}
            className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
          >
            Logout
          </button>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          <div className="bg-white p-6 rounded-lg shadow-md text-center">
            <div className="text-5xl mb-4">⚽</div>
            <h3 className="text-xl font-semibold mb-2">Football</h3>
            <button
              onClick={() => navigate('/search/football')}
              className="bg-blue-800 text-white px-4 py-2 rounded hover:bg-blue-900 w-full"
            >
              Book a Court
            </button>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-md text-center">
            <div className="text-5xl mb-4">🏀</div>
            <h3 className="text-xl font-semibold mb-2">Basketball</h3>
            <button
              onClick={() => navigate('/search/basketball')}
              className="bg-blue-800 text-white px-4 py-2 rounded hover:bg-blue-900 w-full"
            >
              Book a Court
            </button>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-md text-center">
            <div className="text-5xl mb-4">🎾</div>
            <h3 className="text-xl font-semibold mb-2">Tennis</h3>
            <button
              onClick={() => navigate('/search/tennis')}
              className="bg-blue-800 text-white px-4 py-2 rounded hover:bg-blue-900 w-full"
            >
              Book a Court
            </button>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-md text-center">
            <div className="text-5xl mb-4">🏓</div>
            <h3 className="text-xl font-semibold mb-2">Padel</h3>
            <button
              onClick={() => navigate('/search/padel')}
              className="bg-blue-800 text-white px-4 py-2 rounded hover:bg-blue-900 w-full"
            >
              Book a Court
            </button>
          </div>
        </div>

        <div className="bg-white p-6 rounded-lg shadow-md mb-6">
          <h2 className="text-2xl font-semibold mb-4">My Reservations</h2>
          <p className="text-gray-600">Your booking history will appear here</p>
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

export default HomeUser;

