import React, { useState, useEffect } from 'react';
import { BsCart2 } from "react-icons/bs";
import { AiFillHome, AiOutlineShoppingCart, AiOutlineUser } from 'react-icons/ai'; // Import required icons
import logo from "../../assets/logo.png";
import axios from 'axios';
import ProductImagesCarousel from './ProductImagesCarousel';
import { useNavigate, useParams } from 'react-router-dom'; // Import useNavigate and useParams
import Rating from './Rating';


const ProductInfo = () => {

  const navigate = useNavigate();
  useEffect(() => {
    if (!localStorage.getItem('token')) {
      navigate('/');
    }
  }, [navigate]);

  const { id } = useParams(); // Access productId from URL params

  const [cartProducts, setCartProducts] = useState([]);
  const [quantity, setQuantity] = useState(1);
  const [product, setProduct] = useState(null);
  const [showAddToCartMessage, setShowAddToCartMessage] = useState(false);
  const [reviews, setReviews] = useState([]); // Initialize reviews state with an empty array

  useEffect(() => {
    const fetchProductDetails = async () => {
      try {
        const token = localStorage.getItem("token");
        const productResponse = await axios.get(`${import.meta.env.VITE_REACT_APP_BACKEND_URL}api/v1/products/${id}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        if(productResponse)
          setProduct(productResponse.data);
      } catch (error) {
        console.error('Error fetching product details:', error.message);
        navigate('/PageNotFound');
      }
    };

    const fetchReviews = async () => {
      try {
        const token = localStorage.getItem("token");
        const reviewsResponse = await axios.get(`${import.meta.env.VITE_REACT_APP_BACKEND_URL}api/v1/ratings/${id}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        if(reviewsResponse)
          setReviews(reviewsResponse.data);
      } catch (error) {
        console.error('Error fetching reviews:', error.message);
        // Handle error
        navigate('/PageNotFound');
      }
    };

    fetchProductDetails();
    fetchReviews();
  }, [id]); // Fetch product details and reviews whenever id changes


  useEffect(() => {
    const storedProducts = JSON.parse(localStorage.getItem('Products')) || [];
    setCartProducts(storedProducts);
  }, []);

  const handleIncreaseQuantity = () => {
    if (quantity < product.quantity) {
      setQuantity(quantity + 1);
    }
  };

  const handleDecreaseQuantity = () => {
    if (quantity > 1) {
      setQuantity(quantity - 1);
    }
  };

  const handleAddToCart = () => {
    const cartItem = {
      id: product.id,
      name: product.name,
      description: product.description,
      price: product.price,
      quantity: quantity,
      imageUrls: product.imageUrls
    };

    let updatedCartProducts = JSON.parse(localStorage.getItem('Products')) || [];
    const existingItemIndex = updatedCartProducts.findIndex(item => item.id === product.id);

    if (existingItemIndex !== -1) {
      updatedCartProducts[existingItemIndex].quantity = quantity;
    } else {
      updatedCartProducts.push(cartItem);
    }

    localStorage.setItem('Products', JSON.stringify(updatedCartProducts));
    setCartProducts(updatedCartProducts);
    setShowAddToCartMessage(true);
    setTimeout(() => setShowAddToCartMessage(false), 3000); // Hide message after 3 seconds
  };

  // Function to handle redirection to the cart page
  const handleCartClick = () => {
    navigate("/cart");
  };

  return (
    <div>
  <header>
  <div class="bg-white border-gray-200 py-1" style={{backgroundColor:"#fff"}}>
    <div class="flex flex-wrap justify-between items-center mx-auto pl-12">
      <button onClick={()=>navigate('/dashboard')}>
        <img src={logo} alt="" className='h-18 w-20'/>
      </button>
      <div class="flex items-center lg:order-2 relative">
        <a href="/artist_profile" style={{color:"#000", marginRight: '20px'}} class="text-gray-800 hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5">Profile</a>
        <button className="navbar-icon-button" onClick={() => navigate("/cart")} style={{ marginRight: '20px' }}>
          <AiOutlineShoppingCart className="navbar-icon" style={{ fontSize: '24px' }} />
        </button>
        {cartProducts.length !== 0 && 
            <span className="cart-quantity bg-black text-white rounded-full px-2 py-1 text-xs relative top-1 right-9 transform translate-x-1/2 -translate-y-1/2">{cartProducts.length}</span>
          }
        <a style={{color:"#000"}} href="#" class="text-gray-800 hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5">Logout</a>
      </div>
    </div>
  </div>
</header>
      <div className="container mx-auto p-8">
        {/* <h1 className="text-3xl font-semibold mb-8">Product Details</h1> */}
        <div className="flex flex-wrap">
          <div className="w-full md:w-2/3">
            <ProductImagesCarousel images={product?.imageUrls || []} />
          </div>
          <div className="w-full md:w-1/3">
          <div className="w-full p-4">
            <h2 className="text-3xl font-semibold mb-2">{product?.name}</h2>
            <p className="text-lg mb-4">{product?.description}</p>
            <p className="text-lg font-semibold mb-4">Price: <span style={{ color: '#5e72e4' }}>${product?.price}</span></p>
            <div className="flex flex-col items-start mb-4"> {/* Changed flex direction to column */}
              <div className="flex items-center mb-2"> {/* Added margin bottom */}
                <button
                  className="btn btn-sm btn-outline-secondary mr-2"
                  onClick={handleDecreaseQuantity}
                >
                  -
                </button>
                <input
                  type="number"
                  className="form-control w-16 text-center mx-2"
                  value={quantity}
                  onChange={(e) => setQuantity(parseInt(e.target.value))}
                />
                <button
                  className="btn btn-sm btn-outline-secondary ml-2"
                  onClick={handleIncreaseQuantity}
                >
                  +
                </button>
              </div>
              <button className="btn btn-outline btn-success" onClick={handleAddToCart}>
                Add to Cart
              </button>
            </div>
            {showAddToCartMessage && (
              <p className="text-green-500">Product added to the cart.</p>
            )}
          </div>
      
        </div>

        </div>
        <div className="w-full p-4 mt-8 border-t">
          <h2 className="text-2xl font-semibold mb-4">Product Reviews</h2>
          {reviews.map((review, index) => (
            <div key={index} className="mb-4">
              <p className="text-lg font-semibold mb-1">{`${review.firstName} ${review.lastName}`}</p>
              <Rating value={review.rating} />
              <p className="text-lg">{review.comment}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default ProductInfo;
