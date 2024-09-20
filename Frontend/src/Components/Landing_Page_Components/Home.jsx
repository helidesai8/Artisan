import React from "react";
//import BannerBackground from "../Assets/home-banner-background.png";
import BannerImage from "../../assets/home-banner-image.jpg";
// import Navbar from "./Navbar";
import Navbar from "./Navbar";
import { FiArrowRight } from "react-icons/fi";
import { useNavigate } from "react-router-dom"

const Home = () => {
  const navigate=useNavigate();

  return (
    <div className="home-container pt-20" >
      <Navbar />
      <div className="home-banner-container">
        {/* <div className="home-bannerImage-container">
          <img src={BannerBackground} alt="" />
        </div> */}
        <div className="home-text-section " >
          <h1 className="primary-heading">
            Where Artisans Meet Admirers
          </h1>
          <p className="primary-text">
            Explore a curated collection of handcrafted goods, each telling a story and bearing the mark of skilled artisans from our city.
          </p>
          <button className="secondary-button" onClick={()=>navigate('/login')}>
            Checkout products <FiArrowRight />{" "}
          </button>
        </div>
        <div className="home-image-section">
          <img src={BannerImage} alt="" />
        </div>
      </div>
    </div>
  );
};

export default Home;

