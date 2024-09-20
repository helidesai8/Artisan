import React, { useEffect } from "react";
import { Link } from "react-router-dom";
import { ToastContainer, toast } from 'react-toastify';
import { initiateCheckout } from "../functions/initiateCheckout";
import logo from "../assets/logo.png";
import { useNavigate } from 'react-router-dom';


const Cart = () => {
  const navigate = useNavigate();
  const increaseQuantity = (i) => {

    const products = JSON.parse(localStorage.getItem("Products"));
    products[i].quantity = products[i].quantity+1;

    localStorage.setItem("Products", JSON.stringify(products));

    location.reload();
  }

  const decreaseQuantity = (i) => {

    const products = JSON.parse(localStorage.getItem("Products"));
    if(products[i].quantity <=1)  return;
    products[i].quantity = products[i].quantity-1;

    localStorage.setItem("Products", JSON.stringify(products));
    location.reload();
  }

  const deleteProduct = (id) => {
    const products = JSON.parse(localStorage.getItem("Products"));
    products.splice(id, 1);

    localStorage.setItem("Products", JSON.stringify(products));
    location.reload();
  }

  const InitiateCheckout = async () => {
    try {
       await initiateCheckout();  
    } catch (error) {
      toast.error('Something went wrong, please try again later');
    }
  }

  const products = JSON.parse(localStorage.getItem("Products")) 
  return (
    <>
    {/* <button onClick={addProd}>click</button> */}
    <div class="bg-white border-gray-200 py-1 " style={{backgroundColor:"#fff"}}>
        <div class="flex flex-wrap justify-between items-center mx-auto pl-12">
        <button onClick={()=>navigate('/')}>
            <img src={logo} alt="" className='h-18 w-20'/>
          </button>
          <div class="flex items-center lg:order-2">
              <button onClick={()=>navigate('/dashboard')} style={{color:"#000"}} class="text-gray-800 hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2">Home</button>
              <a style={{color:"#000"}} class="text-gray-800  hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2 p-5"
                onClick={()=>{localStorage.clear();
                    navigate('/');}}>Logout</a>
          </div>
          <div class="hidden justify-between items-center w-full lg:flex lg:w-auto lg:order-1" id="mobile-menu-2">
          </div>
        </div>
    </div>
    <ToastContainer />
      <div className="cart">
        <div className="cart-items-container">
          {(!products || products.length == 0 )&& <h1 style={{fontSize:"40px"}}>Your cart is Empty</h1>}
          {(!products || products.length == 0 ) && <h1 style={{fontSize:"20px"}}>Explore new products</h1>}
          {products && products.map((product, i) => (
            <a
            href="#"
            class="flex flex-col items-center bg-white border border-gray-200 rounded-lg shadow md:flex-row md:max-w-3xl hover:bg-gray-100 "
          >
            <img
              class="object-cover w-full rounded-t-lg h-96 md:h-auto md:w-48 md:rounded-none md:rounded-s-lg "
              src={product.imageUrls[0]}
              alt=""
            />
            <div class="flex flex-col justify-between p-4 leading-normal">
              <h5 class="mb-2 text-2xl font-bold tracking-tight text-gray-900 ">
              {product.name}
              </h5>
              <p class="mb-3 font-normal text-gray-700 ">
              {product.description}
              </p>
              <h4 style={{fontWeight:400, fontSize:"24px", color:"#5e72e4"}}>${product.price}
                <input type="text" disabled style={{backgroundColor: "transparent"}}/>
                </h4>
                <h3 style={{ display: "flex", alignItems: "center" }}>
                Quantity : 
                <button
                  onClick={() => decreaseQuantity(i)}
                  style={{ fontSize: "25px", margin: "0 5px", border: "1px solid #ccc", borderRadius: "4px", padding: "0px",  width:"6%" }}
                >
                  -
                </button>
                <span style={{ fontSize: "20px", margin: "0 5px", borderRadius: "4px", padding: "5px" }}>
                  {product.quantity}
                </span>
                <button
                  onClick={() => increaseQuantity(i)}
                  style={{ fontSize: "25px", margin: "0 5px", border: "1px solid #ccc", borderRadius: "4px", padding: "0px",  width:"6%" }}
                >
                  +
                </button>
                
                <button
                  className="btn bg-red-500 rounded-2xl ml-2"
                  onClick={() => deleteProduct(i)}
                >
                  Delete
                </button>
              </h3>
                  
            </div>
          </a>
          ))}
        </div>
        {products && <div className="summary">
          <div class="max-w-sm p-6 bg-white border border-gray-200 rounded-lg shadow ">
            <a href="#">
              <h5 class="mb-2 text-2xl font-bold tracking-tight text-gray-900">
                Summary
              </h5>
            </a>
            <hr/>
            <br/>
            {products.length > 0 && products.map(product => (
              <>
              <div style={{display:"flex", justifyContent:"space-between"}}>
              <p style={{fontSize:"20px", fontWeight:100}}>{product.name} &nbsp; &nbsp;× {product.quantity}</p> 
              <p style={{fontSize:"20px", fontWeight:300}}>$ {(product.price*product.quantity)}</p>
              </div>
              </>
            ))}
            <hr/>
            <div style={{display:"flex", justifyContent:"space-between"}}>
              <p style={{fontSize:"20px", fontWeight:100}}> </p> 
              <p style={{fontSize:"27px", fontWeight:300, color:"#5e72e4"}}>
                $ {products.reduce((accumulator, product) => {
                return (accumulator + product.price * product.quantity);
              }, 0)}
              </p>  
              </div>
            <br/>
            <button
              onClick= {() => InitiateCheckout()}
              class="inline-flex items-center px-3 py-2 text-sm font-medium text-center text-white bg-blue-700 rounded-lg hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300"
            >
              Checkout
              <svg
                class="rtl:rotate-180 w-3.5 h-3.5 ms-2"
                aria-hidden="true"
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 14 10"
              >
                <path
                  stroke="currentColor"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M1 5h12m0 0L9 1m4 4L9 9"
                />
              </svg>
            </button>
          </div>
        </div>}
      </div>
    </>
  );
};

export default Cart;
