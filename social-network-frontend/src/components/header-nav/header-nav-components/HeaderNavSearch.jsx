import React from 'react';
import '../../../styles/HeaderNav.css'

const HeaderNavSearch = () => {
    const [InputText, setInputText] = React.useState('');

    const handleInputChange = (e) => {
        setInputText(e.target.value);
    }

    return (
        <li className='header-nav-search'>
            <div className='input-container'>
                <i className="fas fa-solid fa-magnifying-glass icon"></i>
                <input
                    className='hv-input-field'
                    type='text'
                    placeholder='Search...'
                    value={InputText}
                    onChange={handleInputChange}
                />
            </div>
        </li>
    );
};

export default HeaderNavSearch;