import React from 'react';

const HeaderNavNotification = ({activeMenu, toggleMenu}) => {
    const menuId = 'notification-menu';
   const isMenuOpen = activeMenu === menuId;

    return (
        <li className='hv-notification-container'>
            <button className='hv-notification-button' onClick={() => toggleMenu(menuId)}>
                <i className='fas fa-solid fa-bell hv-notification-icon'></i>
            </button>
            {isMenuOpen && (
                <div className='hv-notification-menu-container'>Aboba</div>
            )}
        </li>
    );
};

export default HeaderNavNotification;