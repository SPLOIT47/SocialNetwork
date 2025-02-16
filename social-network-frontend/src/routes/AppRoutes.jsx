import React from 'react';
import {Navigate, Route, Routes} from "react-router-dom";
import LoginPage from "../components/auth/LoginPage";
import FeedPage from "../components/main-page/FeedPage";
import ProtectedRoute from "../components/auth/ProtectedRoute";
import RegisterPage from "../components/auth/RegisterPage";


const AppRoutes = () => {
    return (
        <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/feed" element={
                <ProtectedRoute>
                    <FeedPage />
                </ProtectedRoute>
            }/>

            <Route path="/" element={<Navigate to="/login" replace /> } />

        </Routes>
    );
};

export default AppRoutes;