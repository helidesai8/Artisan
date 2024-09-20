import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useRecoilState } from "recoil";
import { Loggedin_User } from "../atoms/Loggedin_User_Atom";
import Footer from "../Components/Landing_Page_Components/Footer";
import axios from "axios";
import logo from "../assets/logo.png";

import React from "react";

export default function OrderHistoryPage() {
  const navigate = useNavigate();
  const setUser = useRecoilState(Loggedin_User);
  const [loading, setLoading] = useState(false);
  const orderHistoryBackendURL =
    import.meta.env.VITE_REACT_APP_BACKEND_URL + "api/v1/order/history";
  const addFeedbackURL =
    import.meta.env.VITE_REACT_APP_BACKEND_URL + "api/v1/ratings/";
  const [feedbackForm, setFeedbackForm] = useState(null);

  const openFeedbackForm = (productId) => {
    setFeedbackForm(productId);
  };

  const closeFeedbackForm = () => {
    setFeedbackForm(null);
  };

  const submitFeedback = async (productId, rating, feedback) => {
    try {
      setLoading(true);
      const token = localStorage.getItem("token");
      await axios.post(
        `${addFeedbackURL}${productId}`,
        {
          rating: rating,
          comment: feedback,
        },
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      //fetchOrders();
      closeFeedbackForm();
      setLoading(false);
    } catch (error) {
      navigate("/pageNotFound");
      console.error("Error submitting feedback:", error);
    }
  };

  useEffect(() => {
    if (!localStorage.getItem("token")) {
      navigate("/");
    }
  }, [navigate]);

  useEffect(() => {
    document.title = "Order History";
  }, []);

  const [orders, setOrders] = useState([]);

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        setLoading(true);
        const token = localStorage.getItem("token");
        const response = await axios.get(orderHistoryBackendURL, {
          headers: { Authorization: `Bearer ${token}` },
        });
        //setProducts(response.data);
        setOrders(response.data);
        console.log(response.data);
        setLoading(false);
      } catch (error) {
        navigate("/pageNotFound");
        console.error("Error fetching products:", error);
      }
    };

    fetchOrders();
  }, []);

  return (
    <div>
      //Navbar component
      <nav className="navbar fixed top-0 left-0 w-full bg-white z-50 h-7 border-b">
        <div className="nav-logo-container pl-7">
          <button onClick={() => navigate("/")}>
            <img src={logo} alt="" className="h-18 w-20" />
          </button>
        </div>
        <div className="navbar-links-container pr-6">
          <button
            onClick={() => navigate("/dashboard")}
            style={{ color: "#000" }}
            class="text-gray-800 hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2"
          >
            Home
          </button>
          <button
            style={{ color: "#000" }}
            href="#"
            class="text-gray-800 dark:text-white hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2 p-5"
            onClick={() => {
              localStorage.clear();
              navigate("/");
            }}
          >
            Logout
          </button>
        </div>
      </nav>
      <div className="my-1"></div>
      //UI for order history page
      {loading ? (<div className="flex items-center justify-center h-screen">
        <span className="loading loading-spinner loading-lg"></span>
      </div>) : (<><div className="p-8">
        <h1 className="text-3xl font-bold mb-4">Order History</h1>
        {orders.length > 0 &&
          orders.map((order) => (
            <div
              className="rounded-lg border border-gray-300 shadow-md mb-8 p-6"
              key={order.orderId}
            >
              <div className="flex justify-between items-center mb-4">
                <span className="text-lg font-semibold">
                  Order Number: {order.orderId}
                </span>
                <span className="text-gray-500">
                  Order Date: {new Date(order.orderDate).toLocaleDateString()}
                </span>
              </div>
              <div className="overflow-x-auto">
                <table className="min-w-full">
                  <thead>
                    <tr>
                      <th className="px-4 py-2">Product</th>
                      <th className="px-4 py-2">Quantity</th>
                      <th className="px-4 py-2">Amount</th>
                      <th className="px-4 py-2"></th>
                    </tr>
                  </thead>
                  <tbody>
                    {order.items.map((item) => (
                      <tr key={item.product.id}>
                        <td className="px-4 py-2">
                          <div className="flex items-start space-x-4">
                            <img
                              src={item.product.imageUrls[0]}
                              alt={item.product.name}
                              className="w-12 h-12 object-cover rounded-lg"
                            />
                            <div>
                              <span className="block font-semibold">
                                {item.product.name}
                              </span>
                              <span className="text-gray-500">
                                {item.product.categoryName}
                              </span>
                            </div>
                          </div>
                        </td>
                        <td className="px-4 py-2 text-center">
                          {item.quantity}
                        </td>
                        <td className="px-4 py-2 text-center">
                          ${item.amount.toFixed(2)}
                        </td>
                        <td className="px-4 py-2 text-center">
                          <button
                            className="bg-blue-500 hover:bg-blue-600 text-white font-semibold py-2 px-4 rounded"
                            onClick={() => openFeedbackForm(item.product.id)}
                          >
                            Add Feedback
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
              <div className="mt-4">
                <span className="font-semibold">Total:</span>
                <span className="ml-2">${order.total.toFixed(2)}</span>
              </div>
              <div className="mt-2">
                <span className="font-semibold">Payment Method:</span>
                <span className="ml-2">
                  {order.paymentDetail.provider} -{" "}
                  {order.paymentDetail.card_number}
                </span>
              </div>
              <div className="mt-2">
                <span className="font-semibold">Shipping Address:</span>
                <span className="ml-2">
                  {order.shippingBillingDetail.line1},{" "}
                  {order.shippingBillingDetail.city},{" "}
                  {order.shippingBillingDetail.state},{" "}
                  {order.shippingBillingDetail.country},{" "}
                  {order.shippingBillingDetail.postal_code}
                </span>
              </div>
            </div>
          ))}
        {orders.length == 0 && <h2>No orders yet....</h2>}
        {feedbackForm !== null && (
          <FeedbackForm
            productId={feedbackForm}
            onClose={closeFeedbackForm}
            onSubmit={submitFeedback}
          />
        )}
      </div></>)}
    </div>
  );
}

const FeedbackForm = ({ productId, onClose, onSubmit }) => {
  const [productRating, setProductRating] = useState(0);
  const [productFeedback, setProductFeedback] = useState("");
  const [artistRating, setArtistRating] = useState(0);
  const [artistFeedback, setArtistFeedback] = useState("");
  const orderHistoryBackendURL =
    import.meta.env.VITE_REACT_APP_BACKEND_URL + "api/v1/order/history";
  const addFeedbackURL =
    import.meta.env.VITE_REACT_APP_BACKEND_URL + "api/v1/ratings/";

  const handleProductStarClick = (starValue) => {
    setProductRating(starValue);
  };

  const handleProductFeedbackChange = (event) => {
    setProductFeedback(event.target.value);
  };

  const handleArtistStarClick = (starValue) => {
    setArtistRating(starValue);
  };

  const handleArtistFeedbackChange = (event) => {
    setArtistFeedback(event.target.value);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    await submitProductFeedback();
    await submitArtistFeedback();
    onClose();
  };

  const submitProductFeedback = async () => {
    await onSubmit(productId, productRating, productFeedback);
  };

  const submitArtistFeedback = async () => {
    try {
      const token = localStorage.getItem("token");
      const productDetails = orders.find((order) =>
        order.items.some((item) => item.product.id === productId)
      );
      const artistId = productDetails.items.find(
        (item) => item.product.id === productId
      ).product.artistId;
      await axios.post(
        `${addFeedbackURL}artists/${artistId}`,
        {
          rating: artistRating,
          comment: artistFeedback,
        },
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
    } catch (error) {
      console.error("Error submitting artist feedback:", error);
    }
  };

  const [orders, setOrders] = useState([]);

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(orderHistoryBackendURL, {
          headers: { Authorization: `Bearer ${token}` },
        });
        //setProducts(response.data);
        setOrders(response.data);
        console.log(response.data);
      } catch (error) {
        console.error("Error fetching products:", error);
      }
    };

    fetchOrders();
  }, []);

  return (
    <div className="fixed top-0 left-0 w-full h-full flex items-center justify-center bg-gray-900 bg-opacity-50">
      <div
        className="bg-white p-8 rounded-lg shadow-md"
        style={{ width: "80%" }}
      >
        <h2 className="text-xl font-semibold mb-4">Provide Feedback</h2>
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label className="block text-sm font-semibold mb-2">
              Product Rating:
            </label>
            {/* Star icons for product rating */}
            <div className="rating">
              {[1, 2, 3, 4, 5].map((starValue) => (
                <input
                  key={starValue}
                  type="radio"
                  name="product-rating"
                  className={`mask mask-star-2 bg-orange-400 ${
                    starValue === productRating ? "checked" : ""
                  }`}
                  onClick={() => handleProductStarClick(starValue)}
                />
              ))}
            </div>
          </div>
          <div className="mb-4">
            <label className="block text-sm font-semibold mb-2">
              Product Feedback:
            </label>
            <textarea
              className="w-full px-3 py-2 border border-gray-300 rounded-md"
              rows="4"
              value={productFeedback}
              onChange={handleProductFeedbackChange}
            ></textarea>
          </div>
          <div className="mb-4">
            <label className="block text-sm font-semibold mb-2">
              Artist Rating:
            </label>
            {/* Star icons for artist rating */}
            <div className="rating">
              {[1, 2, 3, 4, 5].map((starValue) => (
                <input
                  key={starValue}
                  type="radio"
                  name="artist-rating"
                  className={`mask mask-star-2 bg-orange-400 ${
                    starValue === artistRating ? "checked" : ""
                  }`}
                  onClick={() => handleArtistStarClick(starValue)}
                />
              ))}
            </div>
          </div>
          <div className="mb-4">
            <label className="block text-sm font-semibold mb-2">
              Artist Feedback:
            </label>
            <textarea
              className="w-full px-3 py-2 border border-gray-300 rounded-md"
              rows="4"
              value={artistFeedback}
              onChange={handleArtistFeedbackChange}
            ></textarea>
          </div>
          <div className="flex justify-end">
            <button
              type="button"
              className="bg-gray-400 hover:bg-gray-500 text-white font-semibold py-2 px-4 mr-2 rounded"
              onClick={onClose}
            >
              Cancel
            </button>
            <button
              type="submit"
              className="bg-blue-500 hover:bg-blue-600 text-white font-semibold py-2 px-4 rounded"
            >
              Submit
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
