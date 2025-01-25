import './App.css';
import HeaderNav from './components/header-nav/HeaderNav';
import PageLayout from "./components/main-page/PageLayout";

function App() {
  return (
    <div className="App">
        <header>
            <HeaderNav/>
        </header>
        <main>
            <PageLayout/>
        </main>
    </div>
  );
}

export default App;
