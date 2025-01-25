import React from 'react';

const SideBar = () => {
    const menuButtons = [
        { iconClass: 'fas fa-solid fa-house', text: 'Home' },
        { iconClass: 'fas fa-solid fa-newspaper', text: 'Posts' },
        { iconClass: 'fas fa-solid fa-envelope', text: 'Messages' },
        { iconClass: 'fas fa-solid fa-user-group', text: 'Friends' },
        { iconClass: 'fas fa-solid fa-file', text: 'Files' }
    ]

    return (
        <div id='side-bar-layout'>
            <div className="side-bar">
                <div className="side-bar-header">
                    <span id='bar-name' className='side-bar-header-text-style'>Menu</span>
                </div>
                <div className='side-bar-buttons-container'>
                    {menuButtons.map((item, i) => (
                        <button className='side-bar-button'>
                            <i className={item.iconClass} style={{paddingLeft: '10px',paddingRight: '10px', scale: '1.2'}}></i>
                            <span className='side-bar-button-text'>{item.text}</span>
                        </button>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default SideBar;