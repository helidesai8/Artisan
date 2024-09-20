import About from "../Components/Landing_Page_Components/About";
import Contact from "../Components/Landing_Page_Components/Contact";
import Footer from "../Components/Landing_Page_Components/Footer";
import Testimonial from "../Components/Landing_Page_Components/Testimonial";
import { Appbar } from "../Components/Landing_Page_Components/Appbar";
// import { Home } from "../components/Landing_Page_Components/Home";
import Home from '../Components/Landing_Page_Components/Home';
import { Element } from "react-scroll";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useEffect } from "react";

export default function Landing() {
  useEffect(() => {
    document.title = "Artisian | Where Every Skill Tells A Story";
    
  }, []);
  
  return (
    <>
    <div className="px-24 ">
      {/* <Appbar /> */}
      {/* <h1 className="text-3xl font-bold "> */}
      {/* <Home/> */}
      {/* <About/> */}
      <ToastContainer />
      <Element name="home">
        <Home />
      </Element>
      <Element name="about">
        <About />
      </Element>
      <Element name="testimonials">
        <Testimonial />
      </Element>
      <Element name="contact">
        <Contact />
      </Element>
      {/* <Element name="footer">
        <Footer />
      </Element> */}
      {/* <Testimonial/>
      <Contact/> */}
      

    {/* </h1> */}
    </div>
    {/* <div className="bg-white">
    <Footer/>
    </div> */}
    </>
  )
}