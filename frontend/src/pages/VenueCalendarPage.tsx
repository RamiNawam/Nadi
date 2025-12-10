import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../store/authStore';

interface Reservation {
  id: string;
  code: string;
  startTime: string;
  endTime: string;
  status: string;
  contactPhone: string;
  userAccountId: string;
  courtId: string;
  totalPrice: {
    amount: number;
    currency: string;
  };
}

interface Court {
  id: string;
  label: string;
  sport: string;
  capacity: number;
}

const VenueCalendarPage: React.FC = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split('T')[0]);
  const [reservations, setReservations] = useState<Reservation[]>([]);
  const [courts, setCourts] = useState<Court[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [venueId, setVenueId] = useState<string | null>(null);

  useEffect(() => {
    const fetchVenueData = async () => {
      if (!user?.id) return;

      try {
        // Get venue account to find venue ID
        const accountResponse = await fetch(`http://localhost:8080/api/v1/accounts/${user.id}`);
        if (accountResponse.ok) {
          const accountData = await accountResponse.json();
          if (accountData.venue?.id) {
            setVenueId(accountData.venue.id);
            // Note: We'll need to get courts by venue ID - for now, get all courts
            // and filter or add an endpoint later
            await fetchReservationsForDate(selectedDate);
          } else {
            setError('No venue found for your account');
          }
        }
      } catch (err) {
        console.error('Error fetching venue data:', err);
        setError('Failed to load venue data');
      } finally {
        setLoading(false);
      }
    };

    fetchVenueData();
  }, [user?.id]);

  const fetchReservationsForDate = async (date: string) => {
    try {
      // Get all reservations and filter by date
      // In a real implementation, we'd have an endpoint like /v1/reservations/venue/{venueId}/date/{date}
      const response = await fetch('http://localhost:8080/api/v1/reservations');
      if (response.ok) {
        const allReservations: Reservation[] = await response.json();
        // Filter reservations for the selected date
        const dateReservations = allReservations.filter(res => {
          const resDate = new Date(res.startTime).toISOString().split('T')[0];
          return resDate === date;
        });
        setReservations(dateReservations);
      }
    } catch (err) {
      console.error('Error fetching reservations:', err);
      setError('Failed to load reservations');
    }
  };

  useEffect(() => {
    if (venueId) {
      fetchReservationsForDate(selectedDate);
    }
  }, [selectedDate, venueId]);

  const formatTime = (dateString: string) => {
    return new Date(dateString).toLocaleTimeString('en-US', { 
      hour: '2-digit', 
      minute: '2-digit',
      hour12: true 
    });
  };

  const getTimeSlots = () => {
    const slots = [];
    for (let hour = 0; hour < 24; hour++) {
      slots.push(`${hour.toString().padStart(2, '0')}:00`);
      slots.push(`${hour.toString().padStart(2, '0')}:30`);
    }
    return slots;
  };

  const getReservationsForTimeSlot = (timeSlot: string) => {
    const [hour, minute] = timeSlot.split(':').map(Number);
    const slotStart = new Date(`${selectedDate}T${timeSlot}:00`);
    const slotEnd = new Date(slotStart.getTime() + 30 * 60 * 1000); // 30 minutes later

    return reservations.filter(res => {
      const resStart = new Date(res.startTime);
      const resEnd = new Date(res.endTime);
      // Check if reservation overlaps with this time slot
      return resStart < slotEnd && resEnd > slotStart;
    });
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-gray-600">Loading calendar...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-red-600">{error}</div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="container mx-auto px-4">
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-3xl font-bold text-gray-900">Reservations Calendar</h1>
          <button
            onClick={() => navigate('/homeVenue')}
            className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
          >
            Back to Dashboard
          </button>
        </div>

        <div className="bg-white rounded-lg shadow-md p-6 mb-6">
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Select Date
          </label>
          <input
            type="date"
            value={selectedDate}
            onChange={(e) => setSelectedDate(e.target.value)}
            className="border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
          />
        </div>

        <div className="bg-white rounded-lg shadow-md p-6">
          <h2 className="text-xl font-semibold mb-4">
            {new Date(selectedDate).toLocaleDateString('en-US', { 
              weekday: 'long', 
              year: 'numeric', 
              month: 'long', 
              day: 'numeric' 
            })}
          </h2>

          {reservations.length === 0 ? (
            <div className="text-center py-8 text-gray-500">
              No reservations for this date
            </div>
          ) : (
            <div className="space-y-4">
              {reservations.map(reservation => (
                <div
                  key={reservation.id}
                  className="border border-gray-200 rounded-lg p-4 hover:bg-gray-50"
                >
                  <div className="flex justify-between items-start">
                    <div>
                      <div className="font-semibold text-gray-900">
                        Reservation #{reservation.code}
                      </div>
                      <div className="text-sm text-gray-600 mt-1">
                        <div>Time: {formatTime(reservation.startTime)} - {formatTime(reservation.endTime)}</div>
                        <div>Court ID: {reservation.courtId}</div>
                        <div>Contact: {reservation.contactPhone}</div>
                        <div>Status: <span className={`font-medium ${
                          reservation.status === 'CONFIRMED' ? 'text-green-600' :
                          reservation.status === 'PENDING' ? 'text-yellow-600' :
                          'text-gray-600'
                        }`}>{reservation.status}</span></div>
                      </div>
                    </div>
                    <div className="text-right">
                      <div className="text-lg font-semibold text-indigo-600">
                        ${reservation.totalPrice?.amount || 0} {reservation.totalPrice?.currency || 'USD'}
                      </div>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default VenueCalendarPage;

