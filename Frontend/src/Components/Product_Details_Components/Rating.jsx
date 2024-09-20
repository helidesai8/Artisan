import React from 'react';
import { BsStarFill, BsStarHalf, BsStar } from 'react-icons/bs';

const Rating = ({ value }) => {
  const stars = [];
  const fullStars = Math.floor(value);
  const hasHalfStar = value % 1 !== 0;

  // Add full stars
  for (let i = 0; i < fullStars; i++) {
    stars.push(<BsStarFill key={i} className="text-yellow-400" />);
  }

  // Add half star if applicable
  if (hasHalfStar) {
    stars.push(<BsStarHalf key="half" className="text-yellow-400" />);
  }

  // Add empty stars to complete the rating out of 5
  const remainingStars = 5 - stars.length;
  for (let i = 0; i < remainingStars; i++) {
    stars.push(<BsStar key={`empty-${i}`} className="text-gray-300" />);
  }

  return (
    <div className="flex items-center">
      {stars.map((star, index) => (
        <span key={index}>{star}</span>
      ))}
    </div>
  );
};

export default Rating;
