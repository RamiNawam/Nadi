import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../store/authStore';

interface VenueRequest {
  id: string;
  venueAccountId: string;
  venueName: string;
  address: string;
  status: string;
  submittedAt: string;
  cafeteriaAvailable: boolean;
  numberOfCourts?: Record<string, number>;
  playersPerCourt?: Record<string, number>;
  pricePerHour?: Record<string, { amount: number; currency: string }>;
}

interface Court {
  name: string;
  sport: string;
  capacity: number;
  surface: string;
  indoor: boolean;
  pricePerHour: number;
}

const HomeDeveloper: React.FC = () => {
  const navigate = useNavigate();
  const { user, logout } = useAuth();
  const [venueRequests, setVenueRequests] = useState<VenueRequest[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [processingRequestId, setProcessingRequestId] = useState<string | null>(null);
  const [inspectingRequest, setInspectingRequest] = useState<VenueRequest | null>(null);
  const [isEditing, setIsEditing] = useState(false);
  const [editFormData, setEditFormData] = useState<{
    venueName: string;
    address: string;
    cafeteriaAvailable: boolean;
    courts: Court[];
  } | null>(null);

  useEffect(() => {
    loadVenueRequests();
  }, []);

  const loadVenueRequests = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch('http://localhost:8080/api/v1/venue-requests/pending');
      if (response.ok) {
        const data = await response.json();
        setVenueRequests(data);
      } else {
        console.warn('Failed to load venue requests');
      }
    } catch (err) {
      console.error('Error loading venue requests:', err);
      setError('Failed to load venue requests');
    } finally {
      setLoading(false);
    }
  };

  const handleApproveRequest = async (requestId: string) => {
    setProcessingRequestId(requestId);
    setError(null);
    try {
      const response = await fetch(`http://localhost:8080/api/v1/venue-requests/${requestId}/approve`, {
        method: 'PUT'
      });

      if (response.ok) {
        setVenueRequests(prev => prev.filter(req => req.id !== requestId));
        loadVenueRequests();
      } else {
        const errorData = await response.json().catch(() => ({ message: 'Failed to approve request' }));
        setError(errorData.message || 'Failed to approve venue request');
      }
    } catch (err) {
      setError('Network error. Please try again.');
      console.error('Error approving request:', err);
    } finally {
      setProcessingRequestId(null);
    }
  };

  const handleRejectRequest = async (requestId: string) => {
    const reason = window.prompt('Please provide a reason for rejection (optional):') || 'No reason provided';
    setProcessingRequestId(requestId);
    setError(null);
    try {
      const response = await fetch(`http://localhost:8080/api/v1/venue-requests/${requestId}/reject`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ reason })
      });

      if (response.ok) {
        setVenueRequests(prev => prev.filter(req => req.id !== requestId));
      } else {
        const errorData = await response.json().catch(() => ({ message: 'Failed to reject request' }));
        setError(errorData.message || 'Failed to reject venue request');
      }
    } catch (err) {
      setError('Network error. Please try again.');
      console.error('Error rejecting request:', err);
    } finally {
      setProcessingRequestId(null);
    }
  };

  const handleInspectRequest = async (requestId: string) => {
    try {
      const response = await fetch(`http://localhost:8080/api/v1/venue-requests/${requestId}`);
      if (response.ok) {
        const fullRequest = await response.json();
        setInspectingRequest(fullRequest);
        
        const courts: Court[] = [];
        if (fullRequest.numberOfCourts) {
          Object.keys(fullRequest.numberOfCourts).forEach(sport => {
            const courtCount = fullRequest.numberOfCourts[sport] || 1;
            for (let i = 0; i < courtCount; i++) {
              courts.push({
                name: `${sport} Court ${i + 1}`,
                sport: sport,
                capacity: fullRequest.playersPerCourt?.[sport] || 4,
                surface: '', // Surface not stored in backend currently
                indoor: false,
                pricePerHour: fullRequest.pricePerHour?.[sport]?.amount || 0
              });
            }
          });
        }
        
        setEditFormData({
          venueName: fullRequest.venueName || '',
          address: fullRequest.address || '',
          cafeteriaAvailable: fullRequest.cafeteriaAvailable || false,
          courts: courts.length > 0 ? courts : [{ name: '', sport: '', capacity: 4, surface: '', indoor: false, pricePerHour: 0 }]
        });
        setIsEditing(false);
      } else {
        setError('Failed to load request details');
      }
    } catch (err) {
      console.error('Error loading request:', err);
      setError('Network error. Please try again.');
    }
  };

  const handleSaveEdit = async () => {
    if (!inspectingRequest || !editFormData) return;

    setProcessingRequestId(inspectingRequest.id);
    setError(null);
    try {
      // Build the request payload - aggregate by sport
      const numberOfCourts: Record<string, number> = {};
      const playersPerCourt: Record<string, number> = {};
      const pricePerHour: Record<string, { amount: number; currency: string }> = {};

      editFormData.courts.forEach(court => {
        if (court.sport) {
          // Count courts per sport
          numberOfCourts[court.sport] = (numberOfCourts[court.sport] || 0) + 1;
          // Use average capacity (or first court's capacity) per sport
          if (!playersPerCourt[court.sport]) {
            playersPerCourt[court.sport] = court.capacity;
          }
          // Use average price (or first court's price) per sport
          if (!pricePerHour[court.sport]) {
            pricePerHour[court.sport] = {
              amount: court.pricePerHour,
              currency: 'USD'
            };
          }
        }
      });

      const response = await fetch(`http://localhost:8080/api/v1/venue-requests/${inspectingRequest.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          venueName: editFormData.venueName,
          address: editFormData.address,
          cafeteriaAvailable: editFormData.cafeteriaAvailable,
          numberOfCourts,
          playersPerCourt,
          pricePerHour
        })
      });

      if (response.ok) {
        const updatedRequest = await response.json();
        setInspectingRequest(updatedRequest);
        setIsEditing(false);
        // Reload requests list
        loadVenueRequests();
      } else {
        const errorData = await response.json().catch(() => ({ message: 'Failed to update request' }));
        setError(errorData.message || 'Failed to update venue request');
      }
    } catch (err) {
      setError('Network error. Please try again.');
      console.error('Error updating request:', err);
    } finally {
      setProcessingRequestId(null);
    }
  };

  // Get surface options based on sport
  const getSurfaceOptions = (sport: string) => {
    if (sport === 'TENNIS') {
      return ['Grass', 'Clay', 'Hard'];
    } else if (sport === 'FOOTBALL') {
      return ['Fake Grass', 'Real Grass', 'Hard'];
    } else {
      return ['Indoor', 'Outdoor', 'Hard Court', 'Artificial Grass', 'Natural Grass', 'Clay'];
    }
  };

  const updateCourtField = (index: number, field: keyof Court, value: string | number | boolean) => {
    if (!editFormData) return;
    setEditFormData({
      ...editFormData,
      courts: editFormData.courts.map((court, i) => {
        if (i === index) {
          const updated = { ...court, [field]: value };
          // Reset surface when sport changes
          if (field === 'sport') {
            updated.surface = '';
          }
          return updated;
        }
        return court;
      })
    });
  };

  const addCourt = () => {
    if (!editFormData) return;
    setEditFormData({
      ...editFormData,
      courts: [...editFormData.courts, { name: '', sport: '', capacity: 4, surface: '', indoor: false, pricePerHour: 0 }]
    });
  };

  const removeCourt = (index: number) => {
    if (!editFormData) return;
    setEditFormData({
      ...editFormData,
      courts: editFormData.courts.filter((_, i) => i !== index)
    });
  };

  const AVAILABLE_SPORTS = ['FOOTBALL', 'BASKETBALL', 'PADEL', 'TENNIS'];

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="container mx-auto px-4 py-12">
        <div className="flex justify-between items-center mb-8">
          <div>
            <h1 className="text-4xl font-bold text-gray-900">
              Developer Dashboard
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

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div className="bg-white p-6 rounded-lg shadow-md">
            <h2 className="text-2xl font-semibold mb-4">Create New Venue</h2>
            <p className="text-gray-600 mb-4">Add a new sports venue to the platform</p>
            <button
              onClick={() => navigate('/developer/create-venue')}
              className="bg-blue-800 text-white px-6 py-3 rounded-lg font-semibold hover:bg-blue-900 transition w-full"
            >
              Create Venue
            </button>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-md">
            <h2 className="text-2xl font-semibold mb-4">Manage Accounts</h2>
            <p className="text-gray-600 mb-4">View and manage all user accounts</p>
            <button
              onClick={() => navigate('/developer/admin#accounts')}
              className="bg-blue-800 text-white px-6 py-3 rounded-lg font-semibold hover:bg-blue-900 transition w-full"
            >
              Manage Accounts
            </button>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-md">
            <h2 className="text-2xl font-semibold mb-4">Manage Venues</h2>
            <p className="text-gray-600 mb-4">View and manage all venues</p>
            <button
              onClick={() => navigate('/developer/admin#venues')}
              className="bg-blue-800 text-white px-6 py-3 rounded-lg font-semibold hover:bg-blue-900 transition w-full"
            >
              Manage Venues
            </button>
          </div>
        </div>

        <div className="bg-white p-6 rounded-lg shadow-md mb-6">
          <div className="flex justify-between items-center mb-4">
            <div>
              <h2 className="text-2xl font-semibold mb-2">Pending Venue Requests</h2>
              <p className="text-gray-600">Review and approve venue requests from venue owners</p>
            </div>
            <button
              onClick={loadVenueRequests}
              className="bg-blue-800 text-white px-4 py-2 rounded hover:bg-blue-900 text-sm"
            >
              Refresh
            </button>
          </div>

          {error && (
            <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
              {error}
            </div>
          )}

          {loading ? (
            <div className="text-center py-8 text-gray-500">Loading venue requests...</div>
          ) : venueRequests.length === 0 ? (
            <div className="text-center py-8 text-gray-500">
              No pending venue requests
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Venue Name
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Address
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Submitted
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Cafeteria
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Actions
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {venueRequests.map((request) => (
                    <tr key={request.id} className="hover:bg-gray-50">
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {request.venueName}
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-500">
                        {request.address}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {new Date(request.submittedAt).toLocaleDateString()}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm">
                        {request.cafeteriaAvailable ? (
                          <span className="px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full bg-gray-100 text-gray-800">
                            Yes
                          </span>
                        ) : (
                          <span className="px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full bg-gray-100 text-gray-800">
                            No
                          </span>
                        )}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm">
                        <div className="flex gap-2">
                          <button
                            onClick={() => handleInspectRequest(request.id)}
                            className="px-3 py-1 rounded text-xs font-medium bg-blue-800 text-white hover:bg-blue-900"
                          >
                            Inspect
                          </button>
                          <button
                            onClick={() => handleApproveRequest(request.id)}
                            disabled={processingRequestId === request.id}
                            className={`px-3 py-1 rounded text-xs font-medium ${
                              processingRequestId === request.id
                                ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                                : 'bg-blue-800 text-white hover:bg-blue-900'
                            }`}
                          >
                            {processingRequestId === request.id ? 'Processing...' : 'Approve'}
                          </button>
                          <button
                            onClick={() => handleRejectRequest(request.id)}
                            disabled={processingRequestId === request.id}
                            className={`px-3 py-1 rounded text-xs font-medium ${
                              processingRequestId === request.id
                                ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                                : 'bg-red-600 text-white hover:bg-red-700'
                            }`}
                          >
                            Reject
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>

        <div className="bg-white p-6 rounded-lg shadow-md">
          <h2 className="text-2xl font-semibold mb-4">System Overview</h2>
          <div className="grid grid-cols-3 gap-4">
            <div className="text-center">
              <div className="text-3xl font-bold text-indigo-600">0</div>
              <div className="text-gray-600">Total Venues</div>
            </div>
            <div className="text-center">
              <div className="text-3xl font-bold text-green-600">0</div>
              <div className="text-gray-600">Active Courts</div>
            </div>
            <div className="text-center">
              <div className="text-3xl font-bold text-blue-600">{venueRequests.length}</div>
              <div className="text-gray-600">Pending Requests</div>
            </div>
          </div>
        </div>
      </div>

      {/* Inspect/Edit Modal */}
      {inspectingRequest && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-lg shadow-xl max-w-4xl w-full max-h-[90vh] overflow-y-auto">
            <div className="p-6 border-b border-gray-200">
              <div className="flex justify-between items-center">
                <h2 className="text-2xl font-bold text-gray-900">
                  {isEditing ? 'Edit Venue Request' : 'Inspect Venue Request'}
                </h2>
                <div className="flex gap-2">
                  {!isEditing && (
                    <button
                      onClick={() => setIsEditing(true)}
                      className="px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-700"
                    >
                      Edit
                    </button>
                  )}
                  <button
                    onClick={() => {
                      setInspectingRequest(null);
                      setIsEditing(false);
                      setEditFormData(null);
                    }}
                    className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600"
                  >
                    Close
                  </button>
                </div>
              </div>
            </div>

            <div className="p-6">
              {error && (
                <div className="mb-4 p-4 bg-red-100 border border-red-400 text-red-700 rounded">
                  {error}
                </div>
              )}

              {isEditing && editFormData ? (
                <div className="space-y-6">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Venue Name *
                    </label>
                    <input
                      type="text"
                      value={editFormData.venueName}
                      onChange={(e) => setEditFormData({ ...editFormData, venueName: e.target.value })}
                      className="w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Address *
                    </label>
                    <input
                      type="text"
                      value={editFormData.address}
                      onChange={(e) => setEditFormData({ ...editFormData, address: e.target.value })}
                      className="w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                    />
                  </div>

                  <div className="flex items-center">
                    <input
                      type="checkbox"
                      checked={editFormData.cafeteriaAvailable}
                      onChange={(e) => setEditFormData({ ...editFormData, cafeteriaAvailable: e.target.checked })}
                      className="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                    />
                    <label className="ml-2 block text-sm text-gray-700">
                      Cafeteria Available
                    </label>
                  </div>

                  <div>
                    <div className="flex justify-between items-center mb-2">
                      <label className="block text-sm font-medium text-gray-700">
                        Courts *
                      </label>
                      <button
                        onClick={addCourt}
                        className="text-indigo-600 hover:text-indigo-800 text-sm font-medium"
                      >
                        + Add Court
                      </button>
                    </div>
                    {editFormData.courts.map((court, index) => (
                      <div key={index} className="mb-4 p-4 border border-gray-200 rounded-md">
                        <div className="grid grid-cols-6 gap-4">
                          <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">
                              Court Name
                            </label>
                            <input
                              type="text"
                              value={court.name}
                              onChange={(e) => updateCourtField(index, 'name', e.target.value)}
                              className="w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                              placeholder="e.g., Court 1"
                            />
                          </div>
                          <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">
                              Sport
                            </label>
                            <select
                              value={court.sport}
                              onChange={(e) => updateCourtField(index, 'sport', e.target.value)}
                              className="w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                            >
                              <option value="">Select Sport</option>
                              {AVAILABLE_SPORTS.map(s => (
                                <option key={s} value={s}>{s}</option>
                              ))}
                            </select>
                          </div>
                          <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">
                              Capacity
                            </label>
                            <input
                              type="number"
                              min="1"
                              value={court.capacity}
                              onChange={(e) => updateCourtField(index, 'capacity', parseInt(e.target.value) || 1)}
                              className="w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                            />
                          </div>
                          <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">
                              Surface
                            </label>
                            <select
                              value={court.surface}
                              onChange={(e) => updateCourtField(index, 'surface', e.target.value)}
                              disabled={!court.sport}
                              className="w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                            >
                              <option value="">Select Sport First</option>
                              {court.sport && getSurfaceOptions(court.sport).map(surface => (
                                <option key={surface} value={surface}>{surface}</option>
                              ))}
                            </select>
                          </div>
                          <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">
                              Indoor
                            </label>
                            <div className="flex items-center" style={{ minHeight: '2.5rem' }}>
                              <input
                                type="checkbox"
                                checked={court.indoor}
                                onChange={(e) => updateCourtField(index, 'indoor', e.target.checked)}
                                className="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded m-0"
                              />
                            </div>
                          </div>
                          <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">
                              Price/Hour (USD)
                            </label>
                            <div className="flex gap-2">
                              <input
                                type="number"
                                min="0"
                                step="0.01"
                                value={court.pricePerHour}
                                onChange={(e) => updateCourtField(index, 'pricePerHour', parseFloat(e.target.value) || 0)}
                                className="flex-1 border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                              />
                              <button
                                onClick={() => removeCourt(index)}
                                className="px-3 py-2 bg-red-600 text-white rounded hover:bg-red-700 text-sm"
                              >
                                Remove
                              </button>
                            </div>
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>

                  <div className="flex justify-end gap-2 pt-4 border-t">
                    <button
                      onClick={() => setIsEditing(false)}
                      className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
                    >
                      Cancel
                    </button>
                    <button
                      onClick={handleSaveEdit}
                      disabled={processingRequestId === inspectingRequest.id}
                      className={`px-4 py-2 rounded-md text-white ${
                        processingRequestId === inspectingRequest.id
                          ? 'bg-gray-400 cursor-not-allowed'
                          : 'bg-indigo-600 hover:bg-indigo-700'
                      }`}
                    >
                      {processingRequestId === inspectingRequest.id ? 'Saving...' : 'Save Changes'}
                    </button>
                  </div>
                </div>
              ) : (
                <div className="space-y-4">
                  <div>
                    <label className="text-sm font-medium text-gray-700">Venue Name</label>
                    <p className="mt-1 text-gray-900">{inspectingRequest.venueName}</p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-gray-700">Address</label>
                    <p className="mt-1 text-gray-900">{inspectingRequest.address}</p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-gray-700">Status</label>
                    <p className="mt-1">
                      <span className={`px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${
                        inspectingRequest.status === 'PENDING' ? 'bg-gray-100 text-gray-800' :
                        inspectingRequest.status === 'APPROVED' ? 'bg-blue-100 text-blue-800' :
                        'bg-red-100 text-red-800'
                      }`}>
                        {inspectingRequest.status}
                      </span>
                    </p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-gray-700">Submitted</label>
                    <p className="mt-1 text-gray-900">
                      {new Date(inspectingRequest.submittedAt).toLocaleString()}
                    </p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-gray-700">Cafeteria Available</label>
                    <p className="mt-1 text-gray-900">
                      {inspectingRequest.cafeteriaAvailable ? 'Yes' : 'No'}
                    </p>
                  </div>
                  {editFormData && editFormData.courts.length > 0 && (
                    <div>
                      <label className="text-sm font-medium text-gray-700 mb-2 block">Courts</label>
                      <div className="space-y-2">
                        {editFormData.courts.map((court, index) => (
                          <div key={index} className="p-3 border border-gray-200 rounded-md">
                            <div className="font-medium text-gray-900">{court.name || `Court ${index + 1}`}</div>
                            <div className="text-sm text-gray-600 mt-1">
                              <div>Sport: {court.sport || 'Not specified'}</div>
                              <div>Capacity: {court.capacity} people</div>
                              <div>Surface: {court.surface || 'Not specified'}</div>
                              <div>Indoor: {court.indoor ? 'Yes' : 'No'}</div>
                              <div>Price per Hour: ${court.pricePerHour || 0} USD</div>
                            </div>
                          </div>
                        ))}
                      </div>
                    </div>
                  )}
                </div>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default HomeDeveloper;

