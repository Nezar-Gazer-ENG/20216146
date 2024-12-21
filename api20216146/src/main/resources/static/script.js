document.getElementById('fetchUsersBtn').addEventListener('click', () => {
    fetch('/users') 
        .then(response => response.json())
        .then(users => {
            const container = document.getElementById('usersContainer');
            container.innerHTML = ''; 
            if (users.length === 0) {
                container.innerHTML = '<p>No users found.</p>';
            } else {
                const list = document.createElement('ul');
                users.forEach(user => {
                    const item = document.createElement('li');
                    item.textContent = `Name: ${user.name}, Email: ${user.email}`;
                    list.appendChild(item);
                });
                container.appendChild(list);
            }
        })
        .catch(error => {
            console.error('Error fetching users:', error);
        });
});

const CURRENT_USER_ID = 1;



window.addEventListener('DOMContentLoaded', () => {
  const roomsContainer = document.getElementById('roomsContainer');
  const searchRoomsBtn = document.getElementById('searchRoomsBtn');
  const roomTypeSelect = document.getElementById('roomTypeSelect');

  if (roomsContainer && searchRoomsBtn && roomTypeSelect) {
    fetchRooms();

    searchRoomsBtn.addEventListener('click', () => {
      fetchRooms(roomTypeSelect.value);
    });
  }
});


function fetchRooms(roomType = '') {
  fetch('/hotelrooms')
    .then(response => response.json())
    .then(hotelRooms => {
      let filteredRooms = hotelRooms;

      if (roomType) {
        filteredRooms = hotelRooms.filter(room => room.roomType === roomType);
      }

      renderRooms(filteredRooms);
    })
    .catch(error => {
      console.error('Error fetching hotel rooms:', error);
    });
}


function renderRooms(rooms) {
  const roomsContainer = document.getElementById('roomsContainer');
  if (!roomsContainer) return;

  roomsContainer.innerHTML = '';

  if (rooms.length === 0) {
    roomsContainer.innerHTML = '<p>No rooms found.</p>';
    return;
  }

  rooms.forEach(room => {
    const card = document.createElement('div');
    card.className = 'room-card';

    const title = document.createElement('h3');
    title.textContent = `${room.roomType} Room #${room.roomNumber}`;

    const price = document.createElement('p');
    price.textContent = `Price per night: $${room.pricePerNight}`;

    const availability = document.createElement('p');
    availability.textContent = room.available ? 'Available' : 'Not Available';

    const bookBtn = document.createElement('button');
    bookBtn.className = 'book-btn';
    bookBtn.textContent = 'Book Now';
    bookBtn.disabled = !room.available; 

    bookBtn.addEventListener('click', () => {
      bookRoom(room);
    });

    card.appendChild(title);
    card.appendChild(price);
    card.appendChild(availability);
    card.appendChild(bookBtn);

    roomsContainer.appendChild(card);
  });
}

function bookRoom(room) {
  const bookingData = {
    user: { id: CURRENT_USER_ID },
    hotelRoom: { id: room.id },
    bookingDate: new Date(), 
    totalAmount: room.pricePerNight
  };

  fetch('/bookings', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(bookingData)
  })
    .then(response => {
      if (!response.ok) {
        throw new Error('Failed to book the room');
      }
      return response.json();
    })
    .then(data => {
      alert(`Booked room #${data.hotelRoom.id} successfully!`);
      fetchRooms();
    })
    .catch(error => {
      console.error('Error booking the room:', error);
      alert('Failed to book the room.');
    });
}

window.addEventListener('DOMContentLoaded', () => {
    const signUpForm = document.getElementById('signUpForm');
    if (signUpForm) {
      signUpForm.addEventListener('submit', handleSignUp);
    }
  
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
      loginForm.addEventListener('submit', handleLogin);
    }

  });
  
  function handleSignUp(event) {
    event.preventDefault();
    
    const name = document.getElementById('signUpName').value.trim();
    const username = document.getElementById('signUpUsername').value.trim();
    const email = document.getElementById('signUpEmail').value.trim();
    const password = document.getElementById('signUpPassword').value;
    const confirmPassword = document.getElementById('signUpConfirmPassword').value;
  
    if (password !== confirmPassword) {
      alert('Passwords do not match!');
      return;
    }
  
    const userData = { name, username, email, phoneNumber: "", password };
    
    fetch('/users', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json'},
      body: JSON.stringify(userData)
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('Failed to create user');
      }
      return response.json();
    })
    .then(data => {
      alert('User created successfully! Please log in.');
      window.location.href = '/login.html';
    })
    .catch(error => {
      console.error('Error signing up:', error);
      alert('Error creating user.');
    });
  }
  
  function handleLogin(event) {
    event.preventDefault();
    
    const username = document.getElementById('loginUsername').value.trim();
    const password = document.getElementById('loginPassword').value;
  
    fetch('/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json'},
      body: JSON.stringify({ username, password })
    })
    .then(response => {
      if (response.status === 200) {
        return response.text();
      } else {
        throw new Error('Invalid credentials');
      }
    })
    .then(() => {
      return fetch('/users').then(res => res.json()).then(users => {
        const user = users.find(u => u.username === username);
        if (user) {
          localStorage.setItem('loggedInUserId', user.id);
          alert('Login successful!');
          window.location.href = '/index.html';
        } else {
          alert('User not found after login (unexpected).');
        }
      });
    })
    .catch(error => {
      console.error('Error logging in:', error);
      alert('Invalid credentials, please try again.');
    });
  }