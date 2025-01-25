import React, { useState, useEffect } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import axios from 'axios';

const MessengerBody = () => {
    const [username, setUsername] = useState('');
    const [userId, setUserId] = useState(null);  // State to store user ID
    const [stompClient, setStompClient] = useState(null);
    const [usernamePageVisible, setUsernamePageVisible] = useState(true);

    useEffect(() => {
        if (userId) {
            const socket = new SockJS('http://localhost:8081/ws');
            const client = new Client({
                webSocketFactory: () => socket,
                reconnectDelay: 5000,
                heartbeatIncoming: 4000,
                heartbeatOutgoing: 4000,
                onConnect: () => {
                    console.log('Connected to WebSocket');
                    // Further message subscriptions can go here
                },
                onStompError: (error) => {
                    console.error('Error with WebSocket connection:', error);
                },
                onDisconnect: () => {
                    console.log('Disconnected from WebSocket');
                }
            });
            setStompClient(client);
            client.activate();

            return () => {
                if (client) {
                    client.deactivate();
                    console.log('Client deactivated');
                }
            };
        }
    }, [userId]);

    const handleUsernameSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await axios.get(`http://localhost:8080/users/getUserInfo`, {
                params: { id: username },
            });
            setUserId(response.data.id);  // Set the retrieved user ID
            setUsernamePageVisible(false);
        } catch (error) {
            console.error("Failed to fetch user ID:", error);
        }
    };

    return (
        <div>
            {usernamePageVisible ? (
                <form onSubmit={handleUsernameSubmit}>
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        placeholder="Enter your name"
                        required
                    />
                    <button type="submit">Connect</button>
                </form>
            ) : (
                <div>
                    <h2>Messenger</h2>
                    {/* Display chat UI here */}
                </div>
            )}
        </div>
    );
};

export default MessengerBody;
