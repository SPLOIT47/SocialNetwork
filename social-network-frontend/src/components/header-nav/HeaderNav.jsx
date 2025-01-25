import React from 'react';
import '../../styles/HeaderNav.css'
import '../../styles/variables.css';
import HeaderNavLogo from "./header-nav-components/HeaderNavLogo";
import HeaderNavSearch from "./header-nav-components/HeaderNavSearch";
import HeaderNavNotification from "./header-nav-components/HeaderNavNotification";
import HeaderNavProfile from "./header-nav-components/HeaderNavProfile";

const HeaderNav = () => {
    const [activeMenu, setActiveMenu] = React.useState(null);
    const toggleMenu = (menuName) => {
        setActiveMenu(activeMenu === menuName? null : menuName);
    }

    return (
        <div className='page-header-container'>
            <div className='page-header-wrapper'>
                <header className='page-header'>
                    <ul className="header-nav">
                        <HeaderNavLogo/>
                        <HeaderNavSearch/>
                        <HeaderNavNotification activeMenu={activeMenu} toggleMenu={toggleMenu} />
                        <HeaderNavProfile activeMenu={activeMenu} toggleMenu={toggleMenu} />
                    </ul>
                </header>

            </div>

        </div>
    );
};

export default HeaderNav;