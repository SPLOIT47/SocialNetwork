import React from "react";

const HeaderNavProfile = ({activeMenu, toggleMenu}) => {
    const menuId = 'header-nav-profile-menu';
    const isMenuOpen = activeMenu === menuId;

    return (
        <li className='hv-profile-container'>
            <button>
                <i className='fas fa-solid fa-bell hv-notification-icon'></i>
            </button>
        </li>
    )
}

export default HeaderNavProfile;