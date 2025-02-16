import './App.css';
import HeaderNav from './components/header-nav/HeaderNav';
import LoginPage from "./components/auth/LoginPage";
import {useEffect, useState} from "react";
import AppRoutes from "./routes/AppRoutes";

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
               {/*{isAuthenticated ? <HeaderNav/> : null}*/}
               <HeaderNav></HeaderNav>
           </header>
           <main>
               <AppRoutes />
           </main>
       </div>
   );
}

export default App;
