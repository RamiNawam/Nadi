import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../store/authStore';

interface Venue {
  id: string;
  name: string;
  address: string;
  cafeteriaAvailable: boolean;
  location?: {
    lat: number;
    lng: number;
  };
}

const EditVenuePage: React.FC = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);
  const [venue, setVenue] = useState<Venue | null>(null);

  const [formData, setFormData] = useState({
    name: '',
    address: '',
    cafeteriaAvailable: false,
    latitude: '',
    longitude: ''
  });

  useEffect(() => {
    const fetchVenue = async () => {
      if (!user?.id) return;

      try {
        const accountResponse = await fetch(`http://localhost:8080/api/v1/accounts/${user.id}`);
        if (accountResponse.ok) {
          const accountData = await accountResponse.json();
          if (accountData.venue) {
            setVenue(accountData.venue);
            setFormData({
              name: accountData.venue.name || '',
              address: accountData.venue.address || '',
              cafeteriaAvailable: accountData.venue.cafeteriaAvailable || false,
              latitude: accountData.venue.location?.lat?.toString() || '',
              longitude: accountData.venue.location?.lng?.toString() || ''
            });
          } else {
            setError('No venue found for your account');
          }
        }
      } catch (err) {
        console.error('Error fetching venue:', err);
        setError('Failed to load venue information');
      } finally {
        setLoading(false);
      }
    };

    fetchVenue();
  }, [user?.id]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? (e.target as HTMLInputElement).checked : value
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setSuccess(false);
    setSaving(true);

    if (!venue?.id) {
      setError('Venue not found');
      setSaving(false);
      return;
    }

    try {
      const location = formData.latitude && formData.longitude
        ? {
            lat: parseFloat(formData.latitude),
            lng: parseFloat(formData.longitude)
          }
        : null;

      const response = await fetch(`http://localhost:8080/api/v1/venues/${venue.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          name: formData.name,
          address: formData.address,
          cafeteriaAvailable: formData.cafeteriaAvailable,
          location: location || { lat: 33.8938, lng: 35.5018 } // Default Beirut coordinates
        })
      });

      if (response.ok) {
        setSuccess(true);
        setTimeout(() => {
          navigate('/homeVenue');
        }, 2000);
      } else {
        const errorData = await response.json().catch(() => ({ message: 'Failed to update venue' }));
        setError(errorData.message || 'Failed to update venue information');
      }
    } catch (err) {
      console.error('Error updating venue:', err);
      setError('Network error. Please try again.');
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-gray-600">Loading venue information...</div>
      </div>
    );
  }

  if (!venue) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-red-600">{error || 'Venue not found'}</div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-2xl mx-auto px-4">
        <div className="bg-white rounded-lg shadow-md p-6">
          <div className="flex justify-between items-center mb-6">
            <h1 className="text-3xl font-bold text-gray-900">Edit Venue Information</h1>
            <button
              onClick={() => navigate('/homeVenue')}
              className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
            >
              Cancel
            </button>
          </div>

          {success && (
            <div className="mb-4 p-4 bg-blue-100 border border-blue-400 text-blue-700 rounded">
              Venue information updated successfully! Redirecting...
            </div>
          )}

          {error && (
            <div className="mb-4 p-4 bg-red-100 border border-red-400 text-red-700 rounded">
              {error}
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Venue Name *
              </label>
              <input
                type="text"
                name="name"
                required
                value={formData.name}
                onChange={handleChange}
                className="w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Address *
              </label>
              <input
                type="text"
                name="address"
                required
                value={formData.address}
                onChange={handleChange}
                className="w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              />
            </div>

            <div className="flex items-center">
              <input
                type="checkbox"
                name="cafeteriaAvailable"
                checked={formData.cafeteriaAvailable}
                onChange={handleChange}
                className="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
              />
              <label className="ml-2 block text-sm text-gray-700">
                Cafeteria Available
              </label>
            </div>

            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Latitude
                </label>
                <input
                  type="number"
                  name="latitude"
                  step="any"
                  value={formData.latitude}
                  onChange={handleChange}
                  className="w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                  placeholder="33.8938"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Longitude
                </label>
                <input
                  type="number"
                  name="longitude"
                  step="any"
                  value={formData.longitude}
                  onChange={handleChange}
                  className="w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                  placeholder="35.5018"
                />
              </div>
            </div>

            <div className="flex justify-end space-x-4">
              <button
                type="button"
                onClick={() => navigate('/homeVenue')}
                className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
              >
                Cancel
              </button>
              <button
                type="submit"
                disabled={saving}
                className={`px-6 py-2 rounded-md text-white ${
                  saving
                    ? 'bg-gray-400 cursor-not-allowed'
                    : 'bg-blue-800 hover:bg-blue-900'
                }`}
              >
                {saving ? 'Saving...' : 'Save Changes'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default EditVenuePage;

