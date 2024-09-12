import React from 'react';

const HeaderNavNotification = () => {
    const [isMenuOpen, setIsMenuOpen] = React.useState(false);

    const toggleMenu = () => {
        setIsMenuOpen(!isMenuOpen);
    }

    return (
        <li className='hv-notification-container'>
            <button className='hv-notification-button' onClick={toggleMenu}>
                <i className='fas fa-solid fa-bell hv-notification-icon'></i>
            </button>
            {isMenuOpen && (
                <div className='hv-notification-menu-container'>Aboba</div>
            )}
        </li>
    );
};

export default HeaderNavNotification;