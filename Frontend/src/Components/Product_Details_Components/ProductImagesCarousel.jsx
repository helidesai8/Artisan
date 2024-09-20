import React, { useState } from 'react';

const ProductImagesCarousel = ({ images }) => {
  const [currentImageIndex, setCurrentImageIndex] = useState(0);

  const handleImageChange = (index) => {
    setCurrentImageIndex(index);
  };

  return (
    <div className="flex flex-col items-center justify-center">
      <div className="relative w-4/5 h-96 overflow-hidden">
        <img
          src={images[currentImageIndex]}
          alt={`Product Image ${currentImageIndex}`}
          className="absolute inset-0 w-full h-full object-contain"
        />
        <div className="absolute top-0 right-0 bottom-0 left-0 flex justify-between items-center px-4">
          <button
            className="bg-gray-800 text-white rounded-full p-2 focus:outline-none"
            style={{ marginLeft: '-1.5rem' }}
            onClick={() =>
              handleImageChange((currentImageIndex - 1 + images.length) % images.length)
            }
          >
            &lt;
          </button>
          <button
            className="bg-gray-800 text-white rounded-full p-2 focus:outline-none"
            style={{ marginRight: '-1.5rem' }}
            onClick={() => handleImageChange((currentImageIndex + 1) % images.length)}
          >
            &gt;
          </button>
        </div>
      </div>
      <div className="mt-4 flex">
        {images.map((image, index) => (
          <img
            key={index}
            src={image}
            alt={`Product Image ${index}`}
            className={`w-16 h-16 object-cover border border-gray-300 mx-2 cursor-pointer ${
              index === currentImageIndex ? 'border-blue-500' : ''
            }`}
            onClick={() => handleImageChange(index)}
          />
        ))}
      </div>
    </div>
  );
};

export default ProductImagesCarousel;
