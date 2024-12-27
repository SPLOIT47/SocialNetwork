import {useState} from "react";

const AuthMenu = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const handleLogin = () => {
        if (username && password) {
            const token = 'some_token'; // TODO: request to server
            localStorage.setItem('jwt_token', token);
            window.location.href = '/feed';
            window.location.reload();
        }
    }

    return (
        <div>
            <input
                type="text"
                placeholder="Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
            />
            <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            <button onClick={handleLogin}>Login</button>
        </div>
    );
}

export default AuthMenu;