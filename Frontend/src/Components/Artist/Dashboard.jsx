import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useRecoilState } from "recoil";
// import { productsState } from "../../atoms/Products";
import axios from "axios";
// import { getAllProducts } from "../../Services/ProductServices";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import logo from "../../assets/logo.png";
import {useNavigate} from 'react-router-dom';
import { IoAdd } from "react-icons/io5";


const Dashboard = () => {
  const token = localStorage.getItem("token");
  const navigate = useNavigate();
  // const token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2hpc2guYmhhc2luQGRhbC5jYSIsImlhdCI6MTcxMDMwOTEwNSwiZXhwIjoyNzEwMzA5MTA1fQ.DT2_NBNlYfSWu-jLnKo0RUY9MJ4Z10SnQ7OSNeSwa94"

  const [product, setProduct] = useState();
  const [sales, setSales] = useState([]);
  const [loading, setLoading] = useState();

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const productsResponse = await axios.get(
          import.meta.env.VITE_REACT_APP_BACKEND_URL+`api/v1/products/artist`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        if(productsResponse.status != 200){
          navigate('/login')
        }
        setProduct([...productsResponse.data]);

        const salesResponse = await axios.get(
          import.meta.env.VITE_REACT_APP_BACKEND_URL+`api/v1/artist-insight/sales`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        console.log("Sales :"+productsResponse.data); 

        setLoading(false);

        // setProduct(productsResponse.data);
        setSales(salesResponse.data);
      } catch (error) {
        navigate('/login')
        console.error("Error fetching data:", error);
      }
    };

    fetchData();
  }, [token]);

  const deleteProduct = async (id) => {
    const deleteResponse = await axios.delete(
      import.meta.env.VITE_REACT_APP_BACKEND_URL+`api/v1/products/${id}`,
      {
        headers: { Authorization: `Bearer ${token}` },
      }
    );
    console.log(deleteResponse);
    location.reload();
  };

  return (
    <>
    <header>
    <div class="bg-white border-gray-200 py-1" style={{backgroundColor:"#fff"}}>
        <div class="flex flex-wrap justify-between items-center mx-auto pl-12">
        <button onClick={()=>navigate('/')}>
          <img src={logo} alt="" className='h-18 w-20'/>
        </button>
            <div class="flex items-center lg:order-2">
                <a href="/artist_insight" style={{color:"#000"}} class="text-gray-800 hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2">Insights</a>
                <a href="/artist_profile" style={{color:"#000"}} class="text-gray-800 hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2">Profile</a>
                <a style={{color:"#000"}} href="#" class="text-gray-800 hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2 p-5"
                  onClick={()=>{localStorage.clear();
                      navigate('/');}}>Logout</a>
            </div>
        </div>
    </div>
</header>
      {loading ? (
        <div className="flex items-center justify-center h-screen">
        <span className="loading loading-spinner loading-lg"></span>
      </div>
      ) : (
        <>
          <div className="dashboard">
            {product &&
              product.map((product) => {
                <h1>{product.name}</h1>;
              })}
            <div className="card-container">
              {/* Card 1 */}
              <div className="card">
                <h4>Products</h4>
                <p>{product && product.length}</p>
              </div>

              {/* Card 2 */}
              <div className="card">
                <h4>Sold</h4>
                {/* <p>{soldProducts.length}</p> */}
                <p>
                {sales && sales
                      .reduce(
                        (total, product) =>
                          total + product.quantity,
                        0
                      )}
                </p>
              </div>

              {/* Card 3 */}
              <div className="card">
                <h4>Total Sales</h4>
                <p>
                  ${" "}
                  {sales &&
                    sales
                      .reduce(
                        (total, product) =>
                          total + product.price * product.quantity,
                        0
                      ).toFixed(2)}
                </p>
              </div>

              {/* Card 4 */}
              <div className="card">
                <h4>Purchases</h4>
                <p>
                  {sales.length}
                </p>
              </div>
            </div>

            {/* Table */}
            <div className="table-container">
              <h2 style={{ fontSize: "1.9rem", color: "#5e72e4" }}>
                Sales Table
              </h2>
              <table className="table bg-white">
                <thead>
                  <tr>
                    <th>Product</th>
                    <th>Purchased By</th>
                    <th>Date</th>
                    <th>Quantity</th>
                    <th>Payment Received</th>
                  </tr>
                </thead>
                <tbody>
                  {sales &&
                    sales.map((element) => (
                      <tr key={element.id}>
                        <td>{element.name}</td>
                        <td>{element.orderBy}</td>
                        <td>{element.orderDate}</td>
                        <td>{element.quantity}</td>
                        <td style={{color: "#3ED800", fontSize:"1.4rem"}}>+ ${element.price * element.quantity}</td>
                      </tr>
                    ))}
                  {/* Add more rows as needed */}
                </tbody>
              </table>
            </div>
            <div className="products-title">
              <h2 style={{ fontSize: "1.9rem", color: "#5e72e4" }}>Products</h2>
              <button className="btn btn-outline btn-info" style={{fontSize:"1.5rem", fontWeight:"bold"}}>
                <Link to={`add-product/${""}`}> <IoAdd /></Link>{" "}
              </button>
            </div>
            <hr style={{ border: "0.5px solid #5e7254" }} />

            <div className="product-cards flex flex-wrap items-stretch justify-evenly">
                {product &&
                product.map(
                  (element) =>
                    element.isActive && (
                      <div key={element.id} className="card w-96 bg-base-100 shadow-xl">
                                    <figure className="px-10 pt-10">
                                    {element.imageUrls.length > 0 && (
                                    <img
                                        src={element.imageUrls[0]} 
                                        alt={element.name}
                                        style={{ objectFit: 'cover', width: '100%', height: '200px' }}
                                    />
                                 )}
                                    </figure>
                                    <div className="card-body items-center text-center">
                                        <h2 className="text-xl font-bold">{element.name}</h2>
                                        <p>{element.description}</p>
                                        <h4>CAD $ {element.price}</h4>
                                        <h2 className="">Quantity : {element.quantity}</h2>
                                        <div className="card-actions">
                                        <div className="card-actions justify-center">
                            <button className="btn btn-outline btn-info" style={{padding: "0 25px"}}>
                              <Link to={`update-product/${element.id}`}>
                                Edit
                              </Link>
                            </button>
                            <button
                              className="btn btn-outline btn-error"
                              onClick={() =>
                                document
                                  .getElementById(`my_modal_${element.id}`)
                                  .showModal()
                              }
                            >
                              Delete
                            </button>
                            <dialog
                              id={`my_modal_${element.id}`}
                              className="modal"
                            >
                              <div className="modal-box modal-card">
                                <h3 className="font-bold text-lg">
                                  Are you sure you want to{" "}
                                  <span style={{ color: "red" }}>DELETE</span>{" "}
                                  the product?
                                </h3>
                                <p className="py-3">
                                  Your product named{" "}
                                  <span style={{ fontWeight: "bold" }}>
                                    {element.name}
                                  </span>{" "}
                                  will be deleted and will no longer be shown to
                                  the users.
                                </p>
                                <p className="py-2">
                                  Choose delete to proceed or cancel to abort.
                                </p>
                                <div className="modal-action">
                                <form method="dialog">
                                  <button className="btn btn-outline">
                                      Cancel
                                    </button>
                                    <button
                                      className="btn btn-outline btn-error"
                                      onClick={() => deleteProduct(element.id)}
                                      style={{margin:"10px"}}
                                    >
                                      Delete
                                    </button>
                                  </form>
                                </div>
                              </div>
                            </dialog>
                          </div>                                        
                                        </div>
                                    </div>
                                </div>
                    )
                    
                )}
            </div>
            <h2
              style={{
                fontSize: "1.9rem",
                color: "#5e72e4",
                margin: "50px 0 20px 0",
              }}
            >
              Recently Deleted
            </h2>
            <hr style={{ border: "0.5px solid #5e7254" }} />
            <div className="product-cards flex flex-wrap items-stretch justify-evenly">
              {product &&
                product.map(
                  (element) =>
                    !element.isActive && (
                      <div key={element.id} className="card w-96 bg-base-100 shadow-xl">
                                    <figure className="px-10 pt-10">
                                    {element.imageUrls.length > 0 && (
                                    <img
                                        src={element.imageUrls[0]} 
                                        alt={element.name}
                                        style={{ objectFit: 'cover', width: '100%', height: '200px' }}
                                    />
                                 )}
                                    </figure>
                                    <div className="card-body items-center text-center">
                                        <h2 className="text-xl font-bold">{element.name}</h2>
                                        <p>{element.description.slice(0,120)}...</p>
                                        <h4>CAD $ {element.price}</h4>
                                        <h2 className="">Quantity : {element.quantity}</h2>
                                        <div className="card-actions">
                                        <div className="card-actions justify-center">
                            <button className="btn btn-outline btn-info" style={{padding: "0 25px"}}>
                              <Link to={`update-product/${element.id}`}>
                                Edit
                              </Link>
                            </button>
                            <button
                              className="btn btn-outline btn-success"
                              onClick={() =>
                                document
                                  .getElementById(`my_modal_${element.id}`)
                                  .showModal()
                              }
                            >
                              Bring back
                            </button>
                            <dialog
                              id={`my_modal_${element.id}`}
                              className="modal"
                            >
                              <div className="modal-box modal-card">
                                <h3 className="font-bold text-lg">
                                  Are you sure you want to{" "}
                                  <span style={{ color: "green" }}>Bring back</span>{" "}
                                  the product?
                                </h3>
                                <p className="py-3">
                                  Your product named{" "}
                                  <span style={{ fontWeight: "bold" }}>
                                    {element.name}
                                  </span>{" "}
                                  will be deleted and will no longer be shown to
                                  the users.
                                </p>
                                <p className="py-2">
                                  Choose delete to proceed or cancel to abort.
                                </p>
                                <div className="modal-action">
                                  <form method="dialog">
                                  <button className="btn btn-outline">
                                      Cancel
                                    </button>
                                    <button
                                      className="btn btn-outline btn-success"
                                      onClick={() => deleteProduct(element.id)}
                                      style={{margin:"10px"}}
                                    >
                                      Bring back
                                    </button>
                                  </form>
                                </div>
                              </div>
                            </dialog>
                          </div>                                        
                                        </div>
                                    </div>
                                </div>
                    )
                )}
            </div>
          </div>
        </>
      )}
    </>
  );
};

export default Dashboard;
