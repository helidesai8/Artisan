import React from 'react'
import AboutBackgroundImage from "../../assets/about-background-image.jpg";
import { useNavigate } from "react-router-dom"



const About = () => {
  const navigate=useNavigate();

  return (
    <div>
        <div className="about-section-container">
      <div className="about-section-image-container">
        <img src={AboutBackgroundImage} alt="" />
      </div>
      <div className="about-section-text-container">
        <p className="primary-subheading">About</p>
        <h1 className="primary-heading">
            Discover a world of artistic expression
        </h1>
        <p className="primary-text">
            Our mission is to empower local artists by providing a platform to showcase their talent while offering buyers unique, one-of-a-kind creations.
        </p>
        <p className="primary-text">
            We believe art should be accessible, empowering, and celebrated.
        </p>
        <div className="about-buttons-container">
          <button className="secondary-button" onClick={()=>navigate('/login_artist')}>Become a Seller</button>
        </div>
      </div>
    </div>
    </div>
  )
}

export default About