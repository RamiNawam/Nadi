import React, { useState, useEffect } from 'react';

interface Sport {
  id: string;
  name: string;
  minPlayers: number;
  maxPlayers: number;
  defaultSlotMinutes: number;
}

interface SportConfig {
  sportId: string;
  courtCount: number;
  surface: string;
  pricePerHour: number;
}

interface OpeningHours {
  [key: string]: string;
}

/**
 * Developer page for creating new venues
 * Allows developers to create venues with sports, courts, pricing, and other settings
 */
const CreateVenuePage: React.FC = () => {
  const [sports, setSports] = useState<Sport[]>([]);
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);

  // Form state
  const [formData, setFormData] = useState({
    name: '',
    phone: '',
    address: '',
    city: 'Beirut',
    country: 'Lebanon',
    description: '',
    latitude: 33.8938,
    longitude: 35.5018,
    ownerId: '', // Will be set to developer account
    sports: [] as SportConfig[],
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

  // Load available sports
  useEffect(() => {
    const loadSports = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/v1/admin/venues/sports');
        if (response.ok) {
          const sportsData = await response.json();
          setSports(sportsData);
        }
      } catch (error) {
        console.error('Failed to load sports:', error);
      }
    };

    loadSports();
  }, []);

  // Add sport configuration
  const addSport = () => {
    setFormData(prev => ({
      ...prev,
      sports: [...prev.sports, {
        sportId: '',
        courtCount: 1,
        surface: 'Indoor',
        pricePerHour: 50
      }]
    }));
  };

  // Update sport configuration
  const updateSport = (index: number, field: keyof SportConfig, value: string | number) => {
    setFormData(prev => ({
      ...prev,
      sports: prev.sports.map((sport, i) => 
        i === index ? { ...sport, [field]: value } : sport
      )
    }));
  };

  // Remove sport configuration
  const removeSport = (index: number) => {
    setFormData(prev => ({
      ...prev,
      sports: prev.sports.filter((_, i) => i !== index)
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

    try {
      // Set owner ID to developer account (you can get this from auth context later)
      const submitData = {
        ...formData,
        ownerId: '68fd24fbee48bc24ea4f87fe' // Developer account ID
      };

      const response = await fetch('http://localhost:8080/api/v1/admin/venues', {
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
          sports: []
        }));
      } else {
        console.error('Failed to create venue');
      }
    } catch (error) {
      console.error('Error creating venue:', error);
    } finally {
      setLoading(false);
    }
  };

  const days = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];

  return (
    <div className="container" style={{ padding: '2rem 1rem', maxWidth: '800px' }}>
      <div style={{ marginBottom: '2rem' }}>
        <h1 style={{ fontSize: '2rem', fontWeight: 'bold', marginBottom: '0.5rem', color: '#1e293b' }}>
          Create New Venue
        </h1>
        <p style={{ color: '#6b7280' }}>
          Add a new sports venue with courts, pricing, and operating hours
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
          ✅ Venue created successfully!
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

        {/* Location */}
        <div className="card">
          <h3 style={{ fontSize: '1.25rem', fontWeight: '600', marginBottom: '1rem', color: '#1e293b' }}>
            Location (Map Coordinates)
          </h3>
          
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
            <div>
              <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '500', color: '#374151' }}>
                Latitude
              </label>
              <input
                type="number"
                step="any"
                value={formData.latitude}
                onChange={(e) => setFormData(prev => ({ ...prev, latitude: parseFloat(e.target.value) }))}
                style={{
                  width: '100%',
                  padding: '0.75rem',
                  border: '1px solid #d1d5db',
                  borderRadius: '0.5rem',
                  fontSize: '1rem'
                }}
                placeholder="33.8938"
              />
            </div>
            
            <div>
              <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '500', color: '#374151' }}>
                Longitude
              </label>
              <input
                type="number"
                step="any"
                value={formData.longitude}
                onChange={(e) => setFormData(prev => ({ ...prev, longitude: parseFloat(e.target.value) }))}
                style={{
                  width: '100%',
                  padding: '0.75rem',
                  border: '1px solid #d1d5db',
                  borderRadius: '0.5rem',
                  fontSize: '1rem'
                }}
                placeholder="35.5018"
              />
            </div>
          </div>
          
          <p style={{ fontSize: '0.875rem', color: '#6b7280', marginTop: '0.5rem' }}>
            💡 Tip: Use Google Maps to find exact coordinates for your venue location
          </p>
        </div>

        {/* Sports and Courts */}
        <div className="card">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
            <h3 style={{ fontSize: '1.25rem', fontWeight: '600', color: '#1e293b' }}>
              Sports & Courts
            </h3>
            <button
              type="button"
              onClick={addSport}
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
              + Add Sport
            </button>
          </div>

          {formData.sports.map((sport, index) => (
            <div key={index} style={{ 
              border: '1px solid #e5e7eb', 
              borderRadius: '0.5rem', 
              padding: '1rem', 
              marginBottom: '1rem',
              backgroundColor: '#f9fafb'
            }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
                <h4 style={{ fontSize: '1rem', fontWeight: '600', color: '#1e293b' }}>
                  Sport Configuration {index + 1}
                </h4>
                <button
                  type="button"
                  onClick={() => removeSport(index)}
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

              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr 1fr', gap: '1rem' }}>
                <div>
                  <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '500', color: '#374151' }}>
                    Sport *
                  </label>
                  <select
                    required
                    value={sport.sportId}
                    onChange={(e) => updateSport(index, 'sportId', e.target.value)}
                    style={{
                      width: '100%',
                      padding: '0.75rem',
                      border: '1px solid #d1d5db',
                      borderRadius: '0.5rem',
                      fontSize: '1rem'
                    }}
                  >
                    <option value="">Select Sport</option>
                    {sports.map(s => (
                      <option key={s.id} value={s.id}>{s.name}</option>
                    ))}
                  </select>
                </div>

                <div>
                  <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '500', color: '#374151' }}>
                    Court Count *
                  </label>
                  <input
                    type="number"
                    min="1"
                    max="10"
                    required
                    value={sport.courtCount}
                    onChange={(e) => updateSport(index, 'courtCount', parseInt(e.target.value))}
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
                    Surface *
                  </label>
                  <select
                    required
                    value={sport.surface}
                    onChange={(e) => updateSport(index, 'surface', e.target.value)}
                    style={{
                      width: '100%',
                      padding: '0.75rem',
                      border: '1px solid #d1d5db',
                      borderRadius: '0.5rem',
                      fontSize: '1rem'
                    }}
                  >
                    <option value="Indoor">Indoor</option>
                    <option value="Outdoor">Outdoor</option>
                    <option value="Artificial Grass">Artificial Grass</option>
                    <option value="Natural Grass">Natural Grass</option>
                    <option value="Clay">Clay</option>
                    <option value="Hard Court">Hard Court</option>
                  </select>
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
                    value={sport.pricePerHour}
                    onChange={(e) => updateSport(index, 'pricePerHour', parseFloat(e.target.value))}
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

          {formData.sports.length === 0 && (
            <p style={{ color: '#6b7280', fontStyle: 'italic', textAlign: 'center', padding: '2rem' }}>
              No sports added yet. Click "Add Sport" to get started.
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
            disabled={loading || formData.sports.length === 0}
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
            {loading ? 'Creating Venue...' : 'Create Venue'}
          </button>
        </div>
      </form>
    </div>
  );
};

export default CreateVenuePage;
