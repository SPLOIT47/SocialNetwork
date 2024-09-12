import React from 'react';
import './../styles/HeaderNav.css'
import HeaderNavLogo from "./header-nav-components/HeaderNavLogo";
import HeaderNavSearch from "./header-nav-components/HeaderNavSearch";
import HeaderNavNotification from "./header-nav-components/HeaderNavNotification";

const HeaderNav = () => {
    return (
        <div className="header-nav-container">
            <ul className="header-nav">
                <HeaderNavLogo/>
                <HeaderNavSearch/>
                <HeaderNavNotification/>
            </ul>
        </div>
    );
};

export default HeaderNav;