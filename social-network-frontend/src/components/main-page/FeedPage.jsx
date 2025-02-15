import React from 'react';
import SideBar from "./page-layout-components/SideBar";
import '../../styles/PageLayout.css'
import PageBody from "./page-layout-components/PageBody";

const PageLayout = () => {
    const handleLogout = () => {
        localStorage.removeItem("jwt_token");
        window.location.href = "/login";
    }

    return (
        <div id='page-layout'>
            <SideBar/>
            <button onClick={handleLogout} className="logout-button">
                Logout
            </button>
            <PageBody/>
        </div>
    );
};

export default PageLayout;