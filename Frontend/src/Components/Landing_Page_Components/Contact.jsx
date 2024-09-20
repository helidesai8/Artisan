import axios from "axios";
import React, { useState } from "react";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const Contact = () => {
  const [email, setEmail] = useState();
  const handleClick = async () => {
    try {
      const response = await axios.post(import.meta.env.VITE_REACT_APP_BACKEND_URL + `api/v1/auth/mail`, {
          email,
          subject: "We're Here to Help - What Can We Assist You With?",
          content: `I hope this email finds you well.
  
          We noticed that you recently reached out to us, and we're here to assist you with any questions or concerns you may have. Your satisfaction is our top priority, and we want to ensure that we address your needs promptly and effectively.
          
          Could you please provide us with more information about the issue you're experiencing? Whether it's a technical issue, a question about our products/services, or any other matter, we're here to help resolve it for you.
          
          Feel free to reply to this email with details about your concern, and our team will work diligently to provide you with the assistance you need.
          
          Thank you for choosing TradArtz. We appreciate your request and look forward to resolving your issue to your satisfaction.`,
      });
  
      console.log(response.data); // Assuming the response contains data you want to log
  } catch (error) {
      console.error("Error:", error);
  }
    setTimeout(() => {  
      toast.success("We have received your query. We will get back to you soon!");
  
    },2200);
  };
  return (
    <div className="contact-page-wrapper">
      <h1 className="primary-heading">Unsure about anything?</h1>
      <h1 className="primary-heading">We got your back!</h1>
      <div className="contact-form-container">
        <input type="email" placeholder="yourmail@gmail.com" onChange={(e) => setEmail(e.target.value)}/>
        <button className="secondary-button" onClick={handleClick}>Submit</button>
      </div>
    </div>
  );
};

export default Contact;