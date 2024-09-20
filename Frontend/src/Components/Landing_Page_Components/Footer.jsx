import React from "react";
import Logo from "../../assets/logo.png";
// import { BsTwitter } from "react-icons/bs";
// import { SiLinkedin } from "react-icons/si";
// import { BsYoutube } from "react-icons/bs";
// import { FaFacebookF } from "react-icons/fa";

const Footer = () => {
  return (
    <div className="footer-wrapper">
      <div className="" style={{margin: "0 10px", width:"100%", display:"flex", justifyContent: "space-around"}}>
      <div className="footer-section-one">
        <div className="footer-logo-container" style={{margin: "0 10px"}}>
          <img src={Logo} alt="" />
        </div>
      </div>
        <div className="footer-section-columns" style={{margin: "0 10px"}}>
          <span>Contact us - </span>
          <span>(+1)782-882-9999</span>
          <span>(+1)782-882-9998</span>
        </div>
        <div className="footer-section-columns" style={{margin: "0 30px"}}>
          <span>Email us - </span>
          <span>contact.tradeartz@gmail.com</span>
        </div>
        <div className="footer-section-columns">
          <span>Terms & Conditions</span>
          <span>Privacy Policy</span>
        </div>
      </div>
    </div>
  );
};

export default Footer;