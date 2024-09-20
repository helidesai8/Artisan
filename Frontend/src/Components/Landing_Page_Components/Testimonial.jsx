import React from "react";
import ProfilePic from "../../assets/avatar.jpg";
import ProfilePic2 from "../../assets/avatar2.jpg";
import { AiFillStar } from "react-icons/ai";

const Testimonial = () => {
  return (
    <div className="work-section-wrapper">
      <div className="work-section-top">
        <p className="primary-subheading">Testimonials</p>
        <h1 className="primary-heading">What they Say</h1>
        {/* <p className="primary-text">
          Lorem ipsum dolor sit amet consectetur. Non tincidunt magna non et
          elit. Dolor turpis molestie dui magnis facilisis at fringilla quam.
        </p> */}
      </div>
      <div className="testimonial-section-bottom">
        <img src={ProfilePic} alt="" className="rounded-full" />
        <p>
            I love supporting local artists and finding unique, handcrafted products that I can't find anywhere else. 
            This marketplace makes it so easy to browse and discover new artisans in my city!
        </p>
        <div className="testimonials-stars-container flex flex-row">
          <AiFillStar />
          <AiFillStar />
          <AiFillStar />
          <AiFillStar />
        </div>
        <h2>Alexia Rodriguez, Halifax</h2>
      </div>

      <div className="testimonial-section-bottom">
        <img src={ProfilePic2} alt="" className="rounded-full" />
        <p>
        I was searching for a specific type of gift for a friend, and I found the perfect piece on this website. 
        The artisan was even able to customize it for me, which made it extra special!
        </p>
        <div className="testimonials-stars-container flex flex-row">
          <AiFillStar />
          <AiFillStar />
          <AiFillStar />
          <AiFillStar />
          <AiFillStar />
        </div>
        <h2>Max Richards, Dartmouth</h2>
      </div>
    </div>
  );
};

export default Testimonial;