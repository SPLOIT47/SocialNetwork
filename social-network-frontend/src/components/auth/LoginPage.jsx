import {useEffect, useState} from "react";
import {Navigate, useNavigate} from "react-router-dom";
import '../../styles/Authentication.css'
import  '../../styles/variables.css'
import  '../../styles/Typography.css'

const LoginPage = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem("jwt_token");

        if (token) {
            navigate('/feed');
        }
    }, [navigate]);

    const handleLogin = () => {
        if (username && password) {
            const token = 'some_token'; // TODO: request to server
            localStorage.setItem('jwt_token', token);
            navigate('/feed');
        }
    }

    const handleRegister = () => {
        navigate('/register');
    }

    return (
        <div className="login-container">
            <div className="login-box">
                <label className="logo-font">SocialNetwork</label>
                <div className="input">
                    <input
                        type="text"
                        id="username"
                        required
                        autoComplete="off"
                        onChange={(e) => setUsername(e.target.value)}
                    />
                    <label htmlFor="username">Username</label>

                </div>
                <div className="input">
                    <input
                        type="password"
                        id="password"
                        required
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <label htmlFor="password">Password</label>
                </div>

                <div className="btn-container">
                    <button onClick={handleLogin} className="btn">Login</button>
                    <button onClick={handleRegister} className="btn">Register</button>
                </div>
            </div>
        </div>
    );
}

export default LoginPage;