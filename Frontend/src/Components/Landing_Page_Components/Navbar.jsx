import React, { useState, useEffect } from 'react';
import logo from "../../assets/logo.png";
import { BsCart2 } from "react-icons/bs";
import { HiOutlineBars3 } from "react-icons/hi2";
import HomeIcon from "@mui/icons-material/Home";
import InfoIcon from "@mui/icons-material/Info";
import CommentRoundedIcon from "@mui/icons-material/CommentRounded";
import PhoneRoundedIcon from "@mui/icons-material/PhoneRounded";
import ShoppingCartRoundedIcon from "@mui/icons-material/ShoppingCartRounded";  
import { useNavigate } from "react-router-dom";
import { Link } from "react-scroll";
import './Navbar.css';
 
const Navbar = () => {
  const navigate = useNavigate();
 
  const [openMenu, setOpenMenu] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false); // State to track login status
 
  // Function to handle logout
  const handleLogout = () => {
    localStorage.removeItem("token"); // Remove token from localStorage
    setIsLoggedIn(false); // Update login status
    navigate('/login'); // Redirect to login page
  };
 
  // Check login status on component mount
  useEffect(() => {
    const token = localStorage.getItem("token");
    setIsLoggedIn(!!token); // Set isLoggedIn based on token existence
  }, []);
 
  const menuOptions = [
    {
      text: "Home",
      icon: <HomeIcon />,
    },
    {
      text: "About",
      icon: <InfoIcon />,
    },
    {
      text: "Testimonials",
      icon: <CommentRoundedIcon />,
    },
    {
      text: "Contact",
      icon: <PhoneRoundedIcon />,
    },
    {
      text: "Cart",
      icon: <ShoppingCartRoundedIcon />,
    },
  ];
 
  return (
<nav className='navbar fixed top-0 left-0 w-full bg-white z-50 h-7 border-b'>
<div className="nav-logo-container pl-7">
<img src={logo} alt="" className='h-18 w-20'/>
</div>
<div className="navbar-links-container pr-6">
<Link to="home" smooth={true} duration={500} offset={-50} className="hover:cursor-pointer">
          Home
</Link>
<Link to="about" smooth={true} duration={500} offset={-50} className="hover:cursor-pointer">
          About
</Link>
<Link to="testimonials" smooth={true} duration={500} offset={-50} className="hover:cursor-pointer">
          Testimonials
</Link>
<Link to="contact" smooth={true} duration={500} offset={-50} className="hover:cursor-pointer">
          Contact
</Link>
        {/* Conditionally render login/logout button based on login status */}
        {isLoggedIn ? (
<button className="primary-button" onClick={handleLogout}>Logout</button>
        ) : (
<button className="primary-button" onClick={()=>navigate('/login')}>Login</button>
        )}
</div>
<div className="navbar-menu-container">
<HiOutlineBars3 onClick={() => setOpenMenu(true)} />
</div>
</nav>
  );
};
 
export default Navbar;