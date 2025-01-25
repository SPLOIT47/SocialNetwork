import React from 'react';

const HeaderNavProfile = ({activeMenu, toggleMenu}) => {
    const menuId = 'profile-menu';
    const isMenuOpen = activeMenu === menuId;
    const [profileImage, setProfileImage] = React.useState('');
    const menuButtons = [
        { iconClass: 'fas fa-regular fa-user', text: 'Profile' },
        { iconClass: 'fas fa-thin fa-gear', text: 'Settings' },
        { iconClass: 'fas fa-sign-out-alt', text: 'Logout' }
    ]

    const loadProfileImage = async () => {
        const defaultIcon = `${process.env.PUBLIC_URL}/images/default-icon.png`;
        const userIcon = `${process.env.PUBLIC_URL}/images/user-icon.jpg`;

        return new Promise((resolve) => {
            const img = new Image();
            img.src = userIcon;

            img.onload = () => resolve(userIcon);
            img.onerror = () => resolve(defaultIcon);
        });
    };

    React.useEffect(() => {
        const fetchProfileImage = async () => {
            const img = await loadProfileImage();
            setProfileImage(img);
        };
        fetchProfileImage().then(r => {});
    }, []);

    return (
        <li className='hv-profile-container'>
            <button className='hv-profile-button' onClick={() => toggleMenu(menuId)}>
                <img src={profileImage} alt="img" className='image'/>
            </button>

            {isMenuOpen && (
                <div className='hv-profile-menu-container'>
                    {menuButtons.map((item) => (
                        <button className='hv-profile-menu-button'>
                            <i className={item.iconClass} style={{paddingRight: '10px'}}></i>
                            <span>{item.text}</span>
                        </button>
                    ))
                    }
                </div>
            )}
        </li>
    );
};

export default HeaderNavProfile;
