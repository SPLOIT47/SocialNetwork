import React from 'react';
import '../../../styles/PageBody.css'
import MessengerBody from "./MessengerBody";
import UserList from "./UserList";

const PageBody = () => {
    return (
        <div id='page-body-layout'>
            <div className='page-body'>
                <MessengerBody/>
                <UserList/>
            </div>
        </div>
    );
};

export default PageBody;