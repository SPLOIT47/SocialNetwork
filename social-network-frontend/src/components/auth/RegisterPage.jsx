import React from 'react';
import {useNavigate} from "react-router-dom";

const RegisterPage = () => {
    const [username, setUsername] = React.useState('');
    const [password, setPassword] = React.useState('');
    const [email, setEmail] = React.useState('');
    const [passwordConfirm, setPasswordConfirm] = React.useState('');
    const [firstName, setFirstName] = React.useState('');
    const [lastName, setLastName] = React.useState('');
    const navigate = useNavigate();

    const handleRegister = () => {
        if (password !== passwordConfirm) {}

        navigate('/login')
    }

    return (
        <div className="register-container">
            <div className="register-box">
                <div className="input">
                    <input
                        type="text"
                        id="username"
                        required
                        autoComplete="off"
                        onChange={e => setUsername(e.target.value)}
                    />
                    <label htmlFor="username">Username</label>
                </div>
                <div className="input">
                    <input
                        type="text"
                        id="username"
                        required
                        autoComplete="off"
                        onChange={e => setPassword(e.target.value)}
                    />
                    <label htmlFor="username">Password</label>
                </div>
                <div className="input">
                    <input
                        type="text"
                        id="username"
                        required
                        autoComplete="off"
                        onChange={e => setPasswordConfirm(e.target.value)}
                    />
                    <label htmlFor="username">Confirm Password</label>
                </div>
                <div className="input">
                    <input
                        type="text"
                        id="username"
                        required
                        autoComplete="off"
                        onChange={e => setFirstName(e.target.value)}
                    />
                    <label htmlFor="username">First name</label>
                </div>
                <div className="input">
                    <input
                        type="text"
                        id="username"
                        required
                        autoComplete="off"
                        onChange={e => setLastName(e.target.value)}
                    />
                    <label htmlFor="username">Last name</label>
                </div>
                <div className="input">
                    <input
                        type="text"
                        id="username"
                        required
                        autoComplete="off"
                        onChange={e => setEmail(e.target.value)}
                    />
                    <label htmlFor="username">Email</label>
                </div>

                <div className="btn-container">
                    <button onClick={handleRegister} className="btn">Register</button>
                </div>
            </div>
        </div>
    );
};

export default RegisterPage;