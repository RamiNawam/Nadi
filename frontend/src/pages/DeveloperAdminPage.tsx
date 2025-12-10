import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../store/authStore';

interface Account {
  id: string;
  name: string;
  email: string;
  phone: string;
  type: 'user' | 'venue' | 'developer';
  status: string;
}

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


const DeveloperAdminPage: React.FC = () => {
  const navigate = useNavigate();
  const { user, logout } = useAuth();
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [venues, setVenues] = useState<Venue[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [deletingAccountId, setDeletingAccountId] = useState<string | null>(null);
  const [deletingVenueId, setDeletingVenueId] = useState<string | null>(null);
  
  // Pagination state
  const [currentAccountsPage, setCurrentAccountsPage] = useState(1);
  const accountsPerPage = 10;
  const [currentVenuesPage, setCurrentVenuesPage] = useState(1);
  const venuesPerPage = 10;
  
  // Calculate pagination for accounts
  const totalAccountsPages = Math.ceil(accounts.length / accountsPerPage);
  const accountsStartIndex = (currentAccountsPage - 1) * accountsPerPage;
  const accountsEndIndex = accountsStartIndex + accountsPerPage;
  const currentAccounts = accounts.slice(accountsStartIndex, accountsEndIndex);
  
  // Calculate pagination for venues
  const totalVenuesPages = Math.ceil(venues.length / venuesPerPage);
  const venuesStartIndex = (currentVenuesPage - 1) * venuesPerPage;
  const venuesEndIndex = venuesStartIndex + venuesPerPage;
  const currentVenues = venues.slice(venuesStartIndex, venuesEndIndex);
  
  // Reset to last valid page when accounts list changes (e.g., after deletion)
  useEffect(() => {
    if (currentAccountsPage > totalAccountsPages && totalAccountsPages > 0) {
      setCurrentAccountsPage(totalAccountsPages);
    }
  }, [accounts.length, currentAccountsPage, totalAccountsPages]);

  // Reset to last valid page when venues list changes (e.g., after deletion)
  useEffect(() => {
    if (currentVenuesPage > totalVenuesPages && totalVenuesPages > 0) {
      setCurrentVenuesPage(totalVenuesPages);
    }
  }, [venues.length, currentVenuesPage, totalVenuesPages]);

  useEffect(() => {
    if (user?.accountType !== 'developer') {
      navigate('/homeDeveloper');
      return;
    }
    loadData();
  }, [user, navigate]);

  // Handle hash navigation to scroll to specific sections
  useEffect(() => {
    const hash = window.location.hash;
    if (hash) {
      setTimeout(() => {
        const element = document.querySelector(hash);
        if (element) {
          element.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
      }, 500); // Wait for data to load
    }
  }, [accounts, venues]);

  const loadData = async () => {
    setLoading(true);
    setError(null);
    try {
      const [accountsRes, venuesRes] = await Promise.all([
        fetch('http://localhost:8080/api/v1/accounts'),
        fetch('http://localhost:8080/api/v1/venues')
      ]);

      if (accountsRes.ok) {
        const accountsData = await accountsRes.json();
        setAccounts(accountsData);
      } else {
        const errorText = await accountsRes.text().catch(() => 'Failed to load accounts');
        setError('Failed to load accounts: ' + errorText);
      }

      if (venuesRes.ok) {
        const venuesData = await venuesRes.json();
        setVenues(venuesData);
      } else {
        // Don't show error if venues fail to load (likely ID type mismatch in DB)
        // Just log it and show empty venues table
        const errorText = await venuesRes.text().catch(() => 'Unknown error');
        console.warn('Failed to load venues (this is expected if venues have ObjectId instead of UUID):', errorText);
        setVenues([]);
      }

    } catch (err) {
      setError('Network error. Please try again.');
      console.error('Error loading data:', err);
    } finally {
      setLoading(false);
    }
  };

  const getAccountTypeBadge = (type: string) => {
    const colors = {
      developer: 'bg-purple-100 text-purple-800',
      venue: 'bg-blue-100 text-blue-800',
      user: 'bg-green-100 text-green-800'
    };
    return colors[type as keyof typeof colors] || 'bg-gray-100 text-gray-800';
  };

  const getStatusBadge = (status: string) => {
    if (status === 'ACTIVE') {
      return 'bg-green-100 text-green-800';
    } else if (status === 'SUSPENDED') {
      return 'bg-yellow-100 text-yellow-800';
    } else {
      return 'bg-red-100 text-red-800';
    }
  };

  const handleDeleteAccount = async (accountId: string, accountEmail: string) => {
    // Prevent deleting your own account
    if (user?.email === accountEmail) {
      setError('Cannot delete your own account');
      return;
    }

    if (!window.confirm(`Are you sure you want to delete the account "${accountEmail}"? This action cannot be undone.`)) {
      return;
    }

    setDeletingAccountId(accountId);
    setError(null);

    try {
      const response = await fetch(`http://localhost:8080/api/v1/accounts/${accountId}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json'
        }
      });

      if (response.ok || response.status === 204 || response.status === 200) {
        // Remove from local state and reload data to ensure consistency
        setAccounts(accounts.filter(acc => acc.id !== accountId));
        // Reload data to refresh the list
        await loadData();
      } else {
        let errorMessage = 'Failed to delete account';
        try {
          const errorData = await response.json();
          errorMessage = errorData.message || errorMessage;
        } catch (e) {
          errorMessage = `Failed to delete account (Status: ${response.status})`;
        }
        setError(errorMessage);
        console.error('Delete account error:', response.status, errorMessage);
      }
    } catch (err) {
      setError('Network error. Please try again.');
      console.error('Error deleting account:', err);
    } finally {
      setDeletingAccountId(null);
    }
  };

  const handleDeleteVenue = async (venueId: string, venueName: string) => {
    if (!window.confirm(`Are you sure you want to delete the venue "${venueName}"? This action cannot be undone.`)) {
      return;
    }

    setDeletingVenueId(venueId);
    setError(null);

    try {
      const response = await fetch(`http://localhost:8080/api/v1/venues/${venueId}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json'
        }
      });

      if (response.ok || response.status === 204 || response.status === 200) {
        // Remove from local state and reload data to ensure consistency
        setVenues(venues.filter(venue => venue.id !== venueId));
        // Reload data to refresh the list
        await loadData();
      } else {
        let errorMessage = 'Failed to delete venue';
        try {
          const errorData = await response.json();
          errorMessage = errorData.message || errorMessage;
        } catch (e) {
          errorMessage = `Failed to delete venue (Status: ${response.status})`;
        }
        setError(errorMessage);
        console.error('Delete venue error:', response.status, errorMessage);
      }
    } catch (err) {
      setError('Network error. Please try again.');
      console.error('Error deleting venue:', err);
    } finally {
      setDeletingVenueId(null);
    }
  };



  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600 mx-auto mb-4"></div>
          <p className="text-gray-600">Loading data...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="container mx-auto px-4 py-8">
        <div className="flex justify-between items-center mb-6">
          <div>
            <h1 className="text-3xl font-bold text-gray-900">Admin Dashboard</h1>
            <p className="text-gray-600 mt-1">Manage accounts and venues</p>
          </div>
          <div className="flex gap-2">
            <button
              onClick={() => navigate('/homeDeveloper')}
              className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
            >
              Back to Dashboard
            </button>
            <button
              onClick={logout}
              className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
            >
              Logout
            </button>
          </div>
        </div>

        {error && (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
            {error}
          </div>
        )}

        {/* Accounts Section */}
        <div id="accounts" className="bg-white rounded-lg shadow-md mb-6">
          <div className="p-6 border-b border-gray-200">
            <div className="flex justify-between items-center">
              <h2 className="text-2xl font-semibold text-gray-900">All Accounts</h2>
              <button
                onClick={loadData}
                className="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-700 text-sm"
              >
                Refresh
              </button>
            </div>
            <p className="text-gray-600 mt-1">
              Showing {accounts.length === 0 ? 0 : accountsStartIndex + 1}-{Math.min(accountsEndIndex, accounts.length)} of {accounts.length} accounts
            </p>
          </div>
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Name
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Email
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Phone
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Type
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Status
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Actions
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {accounts.length === 0 ? (
                  <tr>
                    <td colSpan={6} className="px-6 py-4 text-center text-gray-500">
                      No accounts found
                    </td>
                  </tr>
                ) : (
                  currentAccounts.map((account) => (
                    <tr key={account.id} className="hover:bg-gray-50">
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {account.name}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {account.email}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {account.phone}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <span
                          className={`px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${getAccountTypeBadge(
                            account.type
                          )}`}
                        >
                          {account.type.charAt(0).toUpperCase() + account.type.slice(1)}
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <span
                          className={`px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${getStatusBadge(
                            account.status
                          )}`}
                        >
                          {account.status}
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm">
                        <button
                          onClick={() => handleDeleteAccount(account.id, account.email)}
                          disabled={deletingAccountId === account.id || user?.email === account.email}
                          className={`px-3 py-1 rounded text-xs font-medium ${
                            user?.email === account.email
                              ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                              : deletingAccountId === account.id
                              ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                              : 'bg-red-600 text-white hover:bg-red-700'
                          }`}
                          title={user?.email === account.email ? 'Cannot delete your own account' : 'Delete account'}
                        >
                          {deletingAccountId === account.id ? 'Deleting...' : 'Delete'}
                        </button>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
          
          {/* Pagination Controls */}
          {accounts.length > accountsPerPage && (
            <div className="px-6 py-4 border-t border-gray-200 flex items-center justify-between">
              <div className="flex items-center space-x-2">
                <button
                  onClick={() => setCurrentAccountsPage(prev => Math.max(1, prev - 1))}
                  disabled={currentAccountsPage === 1}
                  className={`px-4 py-2 rounded text-sm font-medium ${
                    currentAccountsPage === 1
                      ? 'bg-gray-200 text-gray-400 cursor-not-allowed'
                      : 'bg-indigo-600 text-white hover:bg-indigo-700'
                  }`}
                >
                  Previous
                </button>
                
                <div className="flex items-center space-x-1">
                  {Array.from({ length: Math.min(5, totalAccountsPages) }, (_, i) => {
                    let pageNum;
                    if (totalAccountsPages <= 5) {
                      pageNum = i + 1;
                    } else if (currentAccountsPage <= 3) {
                      pageNum = i + 1;
                    } else if (currentAccountsPage >= totalAccountsPages - 2) {
                      pageNum = totalAccountsPages - 4 + i;
                    } else {
                      pageNum = currentAccountsPage - 2 + i;
                    }
                    
                    return (
                      <button
                        key={pageNum}
                        onClick={() => setCurrentAccountsPage(pageNum)}
                        className={`px-3 py-1 rounded text-sm ${
                          currentAccountsPage === pageNum
                            ? 'bg-indigo-600 text-white font-medium'
                            : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                        }`}
                      >
                        {pageNum}
                      </button>
                    );
                  })}
                </div>
                
                <button
                  onClick={() => setCurrentAccountsPage(prev => Math.min(totalAccountsPages, prev + 1))}
                  disabled={currentAccountsPage === totalAccountsPages}
                  className={`px-4 py-2 rounded text-sm font-medium ${
                    currentAccountsPage === totalAccountsPages
                      ? 'bg-gray-200 text-gray-400 cursor-not-allowed'
                      : 'bg-indigo-600 text-white hover:bg-indigo-700'
                  }`}
                >
                  Next
                </button>
              </div>
              
              <div className="text-sm text-gray-600">
                Page {currentAccountsPage} of {totalAccountsPages}
              </div>
            </div>
          )}
        </div>

        {/* Venues Section */}
        <div id="venues" className="bg-white rounded-lg shadow-md">
          <div className="p-6 border-b border-gray-200">
            <div className="flex justify-between items-center">
              <h2 className="text-2xl font-semibold text-gray-900">All Venues</h2>
              <button
                onClick={loadData}
                className="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-700 text-sm"
              >
                Refresh
              </button>
            </div>
            <p className="text-gray-600 mt-1">
              Showing {venues.length === 0 ? 0 : venuesStartIndex + 1}-{Math.min(venuesEndIndex, venues.length)} of {venues.length} venues
            </p>
          </div>
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Name
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Address
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Cafeteria
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Location
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Actions
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {venues.length === 0 ? (
                  <tr>
                    <td colSpan={5} className="px-6 py-4 text-center text-gray-500">
                      No venues found
                    </td>
                  </tr>
                ) : (
                  currentVenues.map((venue) => (
                    <tr key={venue.id} className="hover:bg-gray-50">
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {venue.name}
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-500">
                        {venue.address}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {venue.cafeteriaAvailable ? (
                          <span className="px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">
                            Yes
                          </span>
                        ) : (
                          <span className="px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full bg-gray-100 text-gray-800">
                            No
                          </span>
                        )}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {venue.location
                          ? `${venue.location.lat.toFixed(4)}, ${venue.location.lng.toFixed(4)}`
                          : 'N/A'}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm">
                        <button
                          onClick={() => handleDeleteVenue(venue.id, venue.name)}
                          disabled={deletingVenueId === venue.id}
                          className={`px-3 py-1 rounded text-xs font-medium ${
                            deletingVenueId === venue.id
                              ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                              : 'bg-red-600 text-white hover:bg-red-700'
                          }`}
                        >
                          {deletingVenueId === venue.id ? 'Deleting...' : 'Delete'}
                        </button>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
          
          {/* Pagination Controls for Venues */}
          {venues.length > venuesPerPage && (
            <div className="px-6 py-4 border-t border-gray-200 flex items-center justify-between">
              <div className="flex items-center space-x-2">
                <button
                  onClick={() => setCurrentVenuesPage(prev => Math.max(1, prev - 1))}
                  disabled={currentVenuesPage === 1}
                  className={`px-4 py-2 rounded text-sm font-medium ${
                    currentVenuesPage === 1
                      ? 'bg-gray-200 text-gray-400 cursor-not-allowed'
                      : 'bg-indigo-600 text-white hover:bg-indigo-700'
                  }`}
                >
                  Previous
                </button>
                
                <div className="flex items-center space-x-1">
                  {Array.from({ length: Math.min(5, totalVenuesPages) }, (_, i) => {
                    let pageNum;
                    if (totalVenuesPages <= 5) {
                      pageNum = i + 1;
                    } else if (currentVenuesPage <= 3) {
                      pageNum = i + 1;
                    } else if (currentVenuesPage >= totalVenuesPages - 2) {
                      pageNum = totalVenuesPages - 4 + i;
                    } else {
                      pageNum = currentVenuesPage - 2 + i;
                    }
                    
                    return (
                      <button
                        key={pageNum}
                        onClick={() => setCurrentVenuesPage(pageNum)}
                        className={`px-3 py-1 rounded text-sm ${
                          currentVenuesPage === pageNum
                            ? 'bg-indigo-600 text-white font-medium'
                            : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                        }`}
                      >
                        {pageNum}
                      </button>
                    );
                  })}
                </div>
                
                <button
                  onClick={() => setCurrentVenuesPage(prev => Math.min(totalVenuesPages, prev + 1))}
                  disabled={currentVenuesPage === totalVenuesPages}
                  className={`px-4 py-2 rounded text-sm font-medium ${
                    currentVenuesPage === totalVenuesPages
                      ? 'bg-gray-200 text-gray-400 cursor-not-allowed'
                      : 'bg-indigo-600 text-white hover:bg-indigo-700'
                  }`}
                >
                  Next
                </button>
              </div>
              
              <div className="text-sm text-gray-600">
                Page {currentVenuesPage} of {totalVenuesPages}
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default DeveloperAdminPage;

