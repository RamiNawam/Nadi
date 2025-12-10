import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../store/authStore';

interface Court {
  name: string;
  sport: string;
  capacity: number;
  surface: string;
  indoor: boolean;
  pricePerHour: number;
}

// Available sports based on backend SportType enum
const AVAILABLE_SPORTS = [
  { id: 'FOOTBALL', name: 'Football' },
  { id: 'BASKETBALL', name: 'Basketball' },
  { id: 'PADEL', name: 'Padel' },
  { id: 'TENNIS', name: 'Tennis' }
];

interface OpeningHours {
  [key: string]: string;
}

/**
 * Venue owner page for creating venue requests
 * Allows venue owners to submit venue requests with sports, courts, pricing, and other settings
 */
const CreateVenueRequestPage: React.FC = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Form state
  const [formData, setFormData] = useState({
    name: '',
    phone: '',
    address: '',
    city: 'Beirut',
    country: 'Lebanon',
    description: '',
    courts: [] as Court[],
    openingHours: {
      Monday: '09:00-22:00',
      Tuesday: '09:00-22:00',
      Wednesday: '09:00-22:00',
      Thursday: '09:00-22:00',
      Friday: '09:00-22:00',
      Saturday: '08:00-23:00',
      Sunday: '08:00-23:00'
    } as OpeningHours
  });

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

  // Add court
  const addCourt = () => {
    setFormData(prev => ({
      ...prev,
      courts: [...prev.courts, {
        name: '',
        sport: '',
        capacity: 4,
        surface: '',
        indoor: false,
        pricePerHour: 50
      }]
    }));
  };

  // Update court
  const updateCourt = (index: number, field: keyof Court, value: string | number | boolean) => {
    setFormData(prev => ({
      ...prev,
      courts: prev.courts.map((court, i) => {
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
    }));
  };

  // Remove court
  const removeCourt = (index: number) => {
    setFormData(prev => ({
      ...prev,
      courts: prev.courts.filter((_, i) => i !== index)
    }));
  };

  // Update opening hours
  const updateOpeningHours = (day: string, hours: string) => {
    setFormData(prev => ({
      ...prev,
      openingHours: { ...prev.openingHours, [day]: hours }
    }));
  };

  // Handle form submission
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setSuccess(false);
    setError(null);

    if (!user?.id) {
      setError('You must be logged in to create a venue request');
      setLoading(false);
      return;
    }

    if (formData.courts.length === 0) {
      setError('Please add at least one court');
      setLoading(false);
      return;
    }

    try {
      // Build the venue request payload - aggregate by sport
      const numberOfCourts: Record<string, number> = {};
      const playersPerCourt: Record<string, number> = {};
      const pricePerHour: Record<string, { amount: number; currency: string }> = {};

      formData.courts.forEach(court => {
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

      const submitData = {
        venueAccountId: user.id,
        venueName: formData.name,
        address: formData.address,
        cafeteriaAvailable: false, // Default, can be updated later
        numberOfCourts,
        playersPerCourt,
        pricePerHour
      };

      const response = await fetch('http://localhost:8080/api/v1/venue-requests', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(submitData),
      });

      if (response.ok) {
        setSuccess(true);
        // Reset form
        setFormData(prev => ({
          ...prev,
          name: '',
          phone: '',
          address: '',
          description: '',
          courts: []
        }));
        setTimeout(() => {
          navigate('/homeVenue');
        }, 2000);
      } else {
        const errorData = await response.json().catch(() => ({ message: 'Failed to create venue request' }));
        setError(errorData.message || 'Failed to create venue request');
        console.error('Failed to create venue request:', errorData);
      }
    } catch (error) {
      setError('Network error. Please try again.');
      console.error('Error creating venue request:', error);
    } finally {
      setLoading(false);
    }
  };

  const days = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];

  return (
    <div className="container" style={{ padding: '2rem 1rem', maxWidth: '800px' }}>
      <div style={{ marginBottom: '2rem' }}>
        <h1 style={{ fontSize: '2rem', fontWeight: 'bold', marginBottom: '0.5rem', color: '#1e293b' }}>
          Create Venue Request
        </h1>
        <p style={{ color: '#6b7280' }}>
          Submit a request to add your sports venue with courts, pricing, and operating hours
        </p>
      </div>

      {success && (
        <div style={{ 
          backgroundColor: '#d1fae5', 
          border: '1px solid #10b981', 
          borderRadius: '0.5rem', 
          padding: '1rem', 
          marginBottom: '2rem',
          color: '#065f46'
        }}>
          ✅ Venue request submitted successfully! Redirecting...
        </div>
      )}

      {error && (
        <div style={{ 
          backgroundColor: '#fee2e2', 
          border: '1px solid #ef4444', 
          borderRadius: '0.5rem', 
          padding: '1rem', 
          marginBottom: '2rem',
          color: '#991b1b'
        }}>
          ❌ {error}
        </div>
      )}

      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
        {/* Basic Information */}
        <div className="card">
          <h3 style={{ fontSize: '1.25rem', fontWeight: '600', marginBottom: '1rem', color: '#1e293b' }}>
            Basic Information
          </h3>
          
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', marginBottom: '1rem' }}>
            <div>
              <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '500', color: '#374151' }}>
                Venue Name *
              </label>
              <input
                type="text"
                required
                value={formData.name}
                onChange={(e) => setFormData(prev => ({ ...prev, name: e.target.value }))}
                style={{
                  width: '100%',
                  padding: '0.75rem',
                  border: '1px solid #d1d5db',
                  borderRadius: '0.5rem',
                  fontSize: '1rem'
                }}
                placeholder="e.g., Beirut Sports Center"
              />
            </div>
            
            <div>
              <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '500', color: '#374151' }}>
                Phone Number *
              </label>
              <input
                type="tel"
                required
                value={formData.phone}
                onChange={(e) => setFormData(prev => ({ ...prev, phone: e.target.value }))}
                style={{
                  width: '100%',
                  padding: '0.75rem',
                  border: '1px solid #d1d5db',
                  borderRadius: '0.5rem',
                  fontSize: '1rem'
                }}
                placeholder="+961-1-234567"
              />
            </div>
          </div>

          <div style={{ marginBottom: '1rem' }}>
            <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '500', color: '#374151' }}>
              Address *
            </label>
            <input
              type="text"
              required
              value={formData.address}
              onChange={(e) => setFormData(prev => ({ ...prev, address: e.target.value }))}
              style={{
                width: '100%',
                padding: '0.75rem',
                border: '1px solid #d1d5db',
                borderRadius: '0.5rem',
                fontSize: '1rem'
              }}
              placeholder="e.g., Hamra Street, Beirut"
            />
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', marginBottom: '1rem' }}>
            <div>
              <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '500', color: '#374151' }}>
                City
              </label>
              <input
                type="text"
                value={formData.city}
                onChange={(e) => setFormData(prev => ({ ...prev, city: e.target.value }))}
                style={{
                  width: '100%',
                  padding: '0.75rem',
                  border: '1px solid #d1d5db',
                  borderRadius: '0.5rem',
                  fontSize: '1rem'
                }}
              />
            </div>
            
            <div>
              <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '500', color: '#374151' }}>
                Country
              </label>
              <input
                type="text"
                value={formData.country}
                onChange={(e) => setFormData(prev => ({ ...prev, country: e.target.value }))}
                style={{
                  width: '100%',
                  padding: '0.75rem',
                  border: '1px solid #d1d5db',
                  borderRadius: '0.5rem',
                  fontSize: '1rem'
                }}
              />
            </div>
          </div>

          <div>
            <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '500', color: '#374151' }}>
              Description
            </label>
            <textarea
              value={formData.description}
              onChange={(e) => setFormData(prev => ({ ...prev, description: e.target.value }))}
              rows={3}
              style={{
                width: '100%',
                padding: '0.75rem',
                border: '1px solid #d1d5db',
                borderRadius: '0.5rem',
                fontSize: '1rem',
                resize: 'vertical'
              }}
              placeholder="Describe your venue, facilities, and amenities..."
            />
          </div>
        </div>

        {/* Courts */}
        <div className="card">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
            <h3 style={{ fontSize: '1.25rem', fontWeight: '600', color: '#1e293b' }}>
              Courts
            </h3>
            <button
              type="button"
              onClick={addCourt}
              style={{
                backgroundColor: '#3b82f6',
                color: 'white',
                border: 'none',
                borderRadius: '0.5rem',
                padding: '0.5rem 1rem',
                fontSize: '0.875rem',
                cursor: 'pointer'
              }}
            >
              + Add Court
            </button>
          </div>

          {formData.courts.map((court, index) => (
            <div key={index} style={{ 
              border: '1px solid #e5e7eb', 
              borderRadius: '0.5rem', 
              padding: '1rem', 
              marginBottom: '1rem',
              backgroundColor: '#f9fafb'
            }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
                <h4 style={{ fontSize: '1rem', fontWeight: '600', color: '#1e293b' }}>
                  Court {index + 1}
                </h4>
                <button
                  type="button"
                  onClick={() => removeCourt(index)}
                  style={{
                    backgroundColor: '#ef4444',
                    color: 'white',
                    border: 'none',
                    borderRadius: '0.25rem',
                    padding: '0.25rem 0.5rem',
                    fontSize: '0.75rem',
                    cursor: 'pointer'
                  }}
                >
                  Remove
                </button>
              </div>

              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr 1fr 1fr 1fr', gap: '1rem' }}>
                <div>
                  <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '500', color: '#374151' }}>
                    Court Name *
                  </label>
                  <input
                    type="text"
                    required
                    value={court.name}
                    onChange={(e) => updateCourt(index, 'name', e.target.value)}
                    style={{
                      width: '100%',
                      padding: '0.75rem',
                      border: '1px solid #d1d5db',
                      borderRadius: '0.5rem',
                      fontSize: '1rem'
                    }}
                    placeholder="e.g., Court 1"
                  />
                </div>

                <div>
                  <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '500', color: '#374151' }}>
                    Sport *
                  </label>
                  <select
                    required
                    value={court.sport}
                    onChange={(e) => updateCourt(index, 'sport', e.target.value)}
                    style={{
                      width: '100%',
                      padding: '0.75rem',
                      border: '1px solid #d1d5db',
                      borderRadius: '0.5rem',
                      fontSize: '1rem',
                      backgroundColor: 'white'
                    }}
                  >
                    <option value="">Select Sport</option>
                    {AVAILABLE_SPORTS.map(s => (
                      <option key={s.id} value={s.id}>{s.name}</option>
                    ))}
                  </select>
                </div>

                <div>
                  <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '500', color: '#374151' }}>
                    Capacity (People) *
                  </label>
                  <input
                    type="number"
                    min="1"
                    max="50"
                    required
                    value={court.capacity}
                    onChange={(e) => updateCourt(index, 'capacity', parseInt(e.target.value))}
                    style={{
                      width: '100%',
                      padding: '0.75rem',
                      border: '1px solid #d1d5db',
                      borderRadius: '0.5rem',
                      fontSize: '1rem'
                    }}
                    placeholder="4"
                  />
                </div>

                <div>
                  <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '500', color: '#374151' }}>
                    Surface *
                  </label>
                  <select
                    required
                    value={court.surface}
                    onChange={(e) => updateCourt(index, 'surface', e.target.value)}
                    disabled={!court.sport}
                    style={{
                      width: '100%',
                      padding: '0.75rem',
                      border: '1px solid #d1d5db',
                      borderRadius: '0.5rem',
                      fontSize: '1rem',
                      backgroundColor: court.sport ? 'white' : '#f3f4f6'
                    }}
                  >
                    <option value="">Select Sport First</option>
                    {court.sport && getSurfaceOptions(court.sport).map(surface => (
                      <option key={surface} value={surface}>{surface}</option>
                    ))}
                  </select>
                </div>

                <div>
                  <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '500', color: '#374151' }}>
                    Indoor
                  </label>
                  <div style={{ display: 'flex', alignItems: 'center', minHeight: '2.75rem' }}>
                    <input
                      type="checkbox"
                      checked={court.indoor}
                      onChange={(e) => updateCourt(index, 'indoor', e.target.checked)}
                      style={{
                        width: '1.25rem',
                        height: '1.25rem',
                        cursor: 'pointer',
                        margin: 0
                      }}
                    />
                  </div>
                </div>

                <div>
                  <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '500', color: '#374151' }}>
                    Price/Hour (USD) *
                  </label>
                  <input
                    type="number"
                    min="0"
                    step="0.01"
                    required
                    value={court.pricePerHour}
                    onChange={(e) => updateCourt(index, 'pricePerHour', parseFloat(e.target.value))}
                    style={{
                      width: '100%',
                      padding: '0.75rem',
                      border: '1px solid #d1d5db',
                      borderRadius: '0.5rem',
                      fontSize: '1rem'
                    }}
                  />
                </div>
              </div>
            </div>
          ))}

          {formData.courts.length === 0 && (
            <p style={{ color: '#6b7280', fontStyle: 'italic', textAlign: 'center', padding: '2rem' }}>
              No courts added yet. Click "Add Court" to get started.
            </p>
          )}
        </div>

        {/* Opening Hours */}
        <div className="card">
          <h3 style={{ fontSize: '1.25rem', fontWeight: '600', marginBottom: '1rem', color: '#1e293b' }}>
            Opening Hours
          </h3>
          
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 2fr', gap: '1rem', alignItems: 'center' }}>
            {days.map(day => (
              <React.Fragment key={day}>
                <label style={{ fontWeight: '500', color: '#374151' }}>
                  {day}
                </label>
                <input
                  type="text"
                  value={formData.openingHours[day]}
                  onChange={(e) => updateOpeningHours(day, e.target.value)}
                  style={{
                    padding: '0.75rem',
                    border: '1px solid #d1d5db',
                    borderRadius: '0.5rem',
                    fontSize: '1rem'
                  }}
                  placeholder="09:00-22:00"
                />
              </React.Fragment>
            ))}
          </div>
          
          <p style={{ fontSize: '0.875rem', color: '#6b7280', marginTop: '1rem' }}>
            💡 Format: HH:MM-HH:MM (e.g., 09:00-22:00) or "Closed" for days when venue is closed
          </p>
        </div>

        {/* Submit Button */}
        <div style={{ display: 'flex', justifyContent: 'center', marginTop: '2rem' }}>
          <button
            type="submit"
            disabled={loading || formData.courts.length === 0}
            style={{
              backgroundColor: loading ? '#9ca3af' : '#10b981',
              color: 'white',
              border: 'none',
              borderRadius: '0.5rem',
              padding: '1rem 2rem',
              fontSize: '1rem',
              fontWeight: '600',
              cursor: loading ? 'not-allowed' : 'pointer',
              minWidth: '200px'
            }}
          >
            {loading ? 'Submitting Request...' : 'Submit Request'}
          </button>
        </div>
      </form>
    </div>
  );
};

export default CreateVenueRequestPage;
