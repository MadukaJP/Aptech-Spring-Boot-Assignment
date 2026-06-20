// =========================================================================
// STUDENT TASK: UPDATE THIS URL
// Replace this with the live URL of your Spring Boot Railway application.
// Do NOT put a trailing slash at the end of the URL.
// Example: const BACKEND_URL = "https://my-spring-app.up.railway.app";
// =========================================================================
const BACKEND_URL = "http://localhost:5000"; // Change this before deploying!

// DOM Elements
const eventsGrid = document.getElementById('events-grid');
const errorBanner = document.getElementById('error-banner');

// 1. Fetch Events on Page Load
document.addEventListener('DOMContentLoaded', fetchEvents);

async function fetchEvents() {
    try {
        // This makes an HTTP GET request to your Spring Boot API
        const response = await fetch(`${BACKEND_URL}/api/events`);
        
        if (!response.ok) {
            throw new Error(`Server returned ${response.status}: ${response.statusText}`);
        }

        const events = await response.json();
        renderEvents(events);

    } catch (error) {
        showError("Failed to connect to the backend. Is your Spring Boot server running and CORS enabled? Details: " + error.message);
        eventsGrid.innerHTML = '';
    }
}

// 2. Render Events to the Screen
function renderEvents(events) {
    eventsGrid.innerHTML = ''; // Clear loading text

    if (events.length === 0) {
        eventsGrid.innerHTML = '<div class="loading">No events found in the database.</div>';
        return;
    }

    events.forEach(event => {
        const card = document.createElement('div');
        card.className = 'card';
        
        const isSoldOut = event.availableTickets <= 0;
        const ticketClass = isSoldOut ? 'tickets sold-out' : 'tickets';
        const ticketText = isSoldOut ? 'Sold Out' : `${event.availableTickets} Tickets Left`;

        card.innerHTML = `
            <h3>${event.title}</h3>
            <p>📅 <strong>${event.date}</strong></p>
            <p>📍 <strong>${event.location}</strong></p>
            <div class="${ticketClass}">${ticketText}</div>
            <button 
                onclick="registerForEvent(${event.id})" 
                ${isSoldOut ? 'disabled' : ''}>
                ${isSoldOut ? 'Registration Closed' : 'Register Now'}
            </button>
        `;
        eventsGrid.appendChild(card);
    });
}

// 3. Register for an Event
async function registerForEvent(eventId) {
    try {
        // This makes an HTTP POST request to your Spring Boot API
        const response = await fetch(`${BACKEND_URL}/api/events/${eventId}/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            alert('Successfully registered!');
            fetchEvents(); // Refresh the grid to show the updated ticket count
        } else {
            const errorText = await response.text();
            showError("Registration failed: " + errorText);
        }

    } catch (error) {
        showError("Network error during registration: " + error.message);
    }
}

function showError(message) {
    errorBanner.textContent = message;
    errorBanner.classList.remove('hidden');
}
