import React from 'react';
import SideBar from "./page-layout-components/SideBar";
import '../../styles/PageLayout.css'
import PageBody from "./page-layout-components/PageBody";

const PageLayout = () => {
    return (
        <div id='page-layout'>
            <SideBar/>
            <PageBody/>
        </div>
    );
};

export default PageLayout;