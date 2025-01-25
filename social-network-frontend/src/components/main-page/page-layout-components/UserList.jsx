import React, { useState, useEffect } from 'react';
import '../../../styles/UserList.css';

const UserList = () => {
    const [users, setUsers] = useState([]);
    const [contextMenu, setContextMenu] = useState(null);

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await fetch('http://localhost:8080/users/getAll');
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                const data = await response.json();
                setUsers(data);
            } catch (error) {
                console.error('Error fetching users:', error);
                setUsers([]);
            }
        };

        fetchUsers();
    }, []);

    const handleContextMenu = (event, userId) => {
        event.preventDefault();
        setContextMenu({
            userId,
            xPos: event.pageX,
            yPos: event.pageY,
        });
    };

    const closeContextMenu = () => {
        setContextMenu(null);
    };

    const handleCreateChat = (userId) => {
        console.log(`Create chat with user ${userId}`);
        // TODO: Implement chat creation
        closeContextMenu();
    };

    return (
        <div className='user-list'>
            <h2>User List</h2>
            <ul>
                {users.map(user => (
                    <li key={user.id} onContextMenu={(e) => handleContextMenu(e, user.id)}>
                        {user.nickname} ({user.email})
                    </li>
                ))}
            </ul>
            {contextMenu && (
                <div
                    className='context-menu'
                    style={{ top: contextMenu.yPos, left: contextMenu.xPos }}
                    onMouseLeave={closeContextMenu}
                >
                    <button onClick={() => handleCreateChat(contextMenu.userId)}>Create Chat</button>
                </div>
            )}
        </div>
    );
};

export default UserList;
