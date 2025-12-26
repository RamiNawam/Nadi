import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../store/authStore';

interface VenueRequest {
  id: string;
  status: string;
  venueName: string;
  submittedAt: string;
}

interface VenueAccount {
  id: string;
  venue?: {
    id: string;
    name: string;
  };
}

const HomeVenue: React.FC = () => {
  const navigate = useNavigate();
  const { user, logout } = useAuth();
  const [loading, setLoading] = useState(true);
  const [venueRequest, setVenueRequest] = useState<VenueRequest | null>(null);
  const [venueAccount, setVenueAccount] = useState<VenueAccount | null>(null);
  const [hasApprovedVenue, setHasApprovedVenue] = useState(false);

  useEffect(() => {
    const fetchVenueStatus = async () => {
      if (!user?.id) return;

      try {
        const accountResponse = await fetch(`http://localhost:8080/api/v1/accounts/${user.id}`);
        if (accountResponse.ok) {
          const accountData = await accountResponse.json();
          setVenueAccount(accountData);
          if (accountData.venue) {
            setHasApprovedVenue(true);
          }
        }

        const requestResponse = await fetch(`http://localhost:8080/api/v1/venue-requests/my-requests/${user.id}/latest`);
        if (requestResponse.ok) {
          const requestData = await requestResponse.json();
          setVenueRequest(requestData);
          if (requestData.status === 'APPROVED') {
            setHasApprovedVenue(true);
          }
        }
      } catch (err) {
        console.error('Error fetching venue status:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchVenueStatus();
  }, [user?.id]);

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-gray-600">Loading...</div>
      </div>
    );
  }

  if (!hasApprovedVenue) {
    return (
      <div className="min-h-screen bg-gray-50">
        <div className="container mx-auto px-4 py-12">
          <div className="flex justify-between items-center mb-8">
            <div>
              <h1 className="text-4xl font-bold text-gray-900">
                Venue Owner Dashboard
              </h1>
              <p className="text-gray-600 mt-2">Welcome, {user?.name}</p>
            </div>
            <button
              onClick={logout}
              className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
            >
              Logout
            </button>
          </div>

          <div className="max-w-2xl mx-auto">
            <div className="bg-white p-8 rounded-lg shadow-md text-center">
              <h2 className="text-2xl font-semibold mb-4">Get Started</h2>
              <p className="text-gray-600 mb-6">
                {venueRequest?.status === 'PENDING' 
                  ? 'Your venue request is pending approval. We will notify you once it\'s reviewed.'
                  : venueRequest?.status === 'REJECTED'
                  ? 'Your previous venue request was rejected. You can submit a new request.'
                  : 'Create your venue request to start managing your sports facility on Nadi.'}
              </p>
              
              {venueRequest?.status === 'PENDING' && (
                <div className="mb-6 p-4 bg-gray-50 border border-gray-200 rounded-md">
                  <p className="text-gray-800">
                    <strong>Status:</strong> Pending Approval
                  </p>
                  <p className="text-sm text-gray-700 mt-1">
                    Submitted on: {new Date(venueRequest.submittedAt).toLocaleDateString()}
                  </p>
                </div>
              )}

              {venueRequest?.status === 'REJECTED' && (
                <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-md">
                  <p className="text-red-800">
                    <strong>Status:</strong> Rejected
                  </p>
                </div>
              )}

              <button
                onClick={() => navigate('/venue/create-request')}
                disabled={venueRequest?.status === 'PENDING'}
                className={`px-6 py-3 rounded-md text-white font-medium ${
                  venueRequest?.status === 'PENDING'
                    ? 'bg-gray-400 cursor-not-allowed'
                    : 'bg-blue-800 hover:bg-blue-900'
                }`}
              >
                {venueRequest?.status === 'PENDING' ? 'Request Pending' : 'Create My Venue'}
              </button>
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="container mx-auto px-4 py-12">
        <div className="flex justify-between items-center mb-8">
          <div>
            <h1 className="text-4xl font-bold text-gray-900">
              Venue Dashboard
            </h1>
            <p className="text-gray-600 mt-2">Welcome, {user?.name}</p>
            {venueAccount?.venue && (
              <p className="text-sm text-gray-500 mt-1">
                Venue: {venueAccount.venue.name}
              </p>
            )}
          </div>
          <button
            onClick={logout}
            className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
          >
            Logout
          </button>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
          <div className="bg-white p-6 rounded-lg shadow-md">
            <h2 className="text-2xl font-semibold mb-4">Reservations Calendar</h2>
            <p className="text-gray-600 mb-4">View and manage daily court bookings</p>
            <button
              onClick={() => navigate('/venue/calendar')}
              className="bg-blue-800 text-white px-4 py-2 rounded hover:bg-blue-900"
            >
              View Calendar
            </button>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-md">
            <h2 className="text-2xl font-semibold mb-4">Venue Information</h2>
            <p className="text-gray-600 mb-4">Update your venue details and settings</p>
            <button
              onClick={() => navigate('/venue/edit')}
              className="bg-blue-800 text-white px-4 py-2 rounded hover:bg-blue-900"
            >
              Edit Venue Information
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default HomeVenue;

