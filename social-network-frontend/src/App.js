import './App.css';
import HeaderNav from './components/header-nav/HeaderNav';
import AuthMenu from "./components/auth-menu/AuthMenu";
import {useEffect, useState} from "react";

function App() {
    const [isAuthenticated, setIsAuthenticated] = useState(false);

    useEffect(() => {
        const token = localStorage.getItem('jwt_token');

        if (token) {
            setIsAuthenticated(true);
        } else {
            setIsAuthenticated(false);
        }
    }, [])

   return (
       <div className="App">
           <header>
               {isAuthenticated ? <HeaderNav/> : null}
           </header>
           <main>
               {isAuthenticated ? null : <AuthMenu/>}
           </main>
       </div>
   );
}

export default App;
