import React from 'react';
import logo from "../assets/logo.png"; // Import the logo

const PageNotFound = () => {
    return (
        <div>
            {/* Navbar */}
            <nav className="navbar fixed top-0 left-0 w-full bg-white z-50 h-7 border-b">
                <div className="nav-logo-container pl-7">
                    <img src={logo} alt="Logo" className="h-18 w-20" />
                </div>
            </nav>

            {/* Page content */}
            <section className="bg-white dark:bg-blue-300 h-screen flex justify-center items-center">
                <div className="py-8 px-4 mx-auto max-w-screen-xl lg:py-16 lg:px-6">
                    <div className="mx-auto max-w-screen-sm text-center">
                        <h1 className="mb-4 text-7xl tracking-tight font-extrabold lg:text-9xl text-primary-600 dark:text-primary-500">404</h1>
                        <p className="mb-4 text-3xl tracking-tight font-bold text-black">Something's missing.</p>
                        <p className="mb-4 text-lg font-light text-black">Sorry, we can't find that page. You'll find lots to explore on the home page. </p>
                        <a href="/" className="inline-flex text-black bg-primary-600 hover:bg-primary-800 focus:ring-4 focus:outline-none focus:ring-primary-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:focus:ring-primary-900 my-4">Back to Homepage</a>
                    </div>   
                </div>
            </section>
        </div>
    );
}

export default PageNotFound;
