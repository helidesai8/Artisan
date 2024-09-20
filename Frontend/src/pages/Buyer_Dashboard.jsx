import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useRecoilState } from "recoil";
import { Loggedin_User } from "../atoms/Loggedin_User_Atom";
import Footer from "../Components/Landing_Page_Components/Footer";
import axios from "axios";
import logo from "../assets/logo.png";

export default function Dashboard() {
  const navigate = useNavigate();
  const setUser = useRecoilState(Loggedin_User);
  const [loading, setLoading] = useState(false);
  const productsBackendURL =
    import.meta.env.VITE_REACT_APP_BACKEND_URL + "api/v1/products";
  const artistsBackendURL =
    import.meta.env.VITE_REACT_APP_BACKEND_URL + "api/v1/artist/all";

    const defaultImage = "data:image/png;base64,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"

  useEffect(() => {
    if (!localStorage.getItem("token")) {
      navigate("/");
    }
    if (localStorage.getItem("role") != "buyer") {
      navigate("/");
    }
  }, [navigate]);

  useEffect(() => {
    document.title = "User Dashboard";
  }, []);

  const [products, setProducts] = useState([]);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        setLoading(true);
        const token = localStorage.getItem("token");
        const response = await axios.get(productsBackendURL, {
          headers: { Authorization: `Bearer ${token}` },
        });
        //setProducts(response.data);
        const activeProucts = response.data.filter(
          (product) => product.isActive === true
        );
        setProducts(activeProucts);
        setLoading(false);
      } catch (error) {
        navigate("/pageNotFound");
        console.error("Error fetching products:", error);
      }
    };

    fetchProducts();
  }, []);

  const [artists, setArtists] = useState([]);

  useEffect(() => {
    const fetchArtists = async () => {
      try {
        setLoading(true);
        const token = localStorage.getItem("token");
        const response = await axios.get(artistsBackendURL, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setArtists(response.data);
        console.log(response.data);
        setLoading(false);
      } catch (error) {
        navigate("/pageNotFound");
        console.error("Error fetching artists:", error);
      }
    };

    fetchArtists();
  }, []);

  return (
    <>
      <div>
        <nav className="navbar fixed top-0 left-0 w-full bg-white z-50 h-7 border-b">
          <div className="nav-logo-container pl-7">
            <button onClick={() => navigate("/")}>
              <img src={logo} alt="" className="h-18 w-20" />
            </button>
          </div>
          <div className="navbar-links-container pr-6">
          <button onClick={()=>navigate('/order_history')} style={{color:"#000"}} className="text-gray-800 hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2">Order history</button>
          <button onClick={()=>navigate('/user_profile')} style={{color:"#000"}} className="text-gray-800 hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2">Profile</button>
            <button
              style={{ color: "#000" }}
              className="text-gray-800  hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2 p-5"
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

        {loading ? (<div className="flex items-center justify-center h-screen">
        <span className="loading loading-spinner loading-lg"></span>
      </div>) : (<><div className="pt-16 ">
          <div className="w-7xl m-14 px-1">
            <div className="">
              <h1 className="text-3xl">
                Explore exquisite Artisan creations !
              </h1>
            </div>

            <div className="flex justify-center w-240">
              <div className="carousel carousel-center rounded-box">
                {products.map((product) => (
                  <div
                    key={product.id}
                    className="carousel-item transition duration-300"
                  >
                    <Link to={`/products/${product.id}`}>
                      <div
                        key={product.id}
                        className="card w-96"
                        style={{ height: "80%" }}
                      >
                        <figure className="px-10 pt-10">
                          {product.imageUrls.length > 0 && (
                            <img
                              src={product.imageUrls[0]}
                              alt={product.name}
                              style={{
                                objectFit: "cover",
                                width: "100%",
                                height: "200px",
                              }}
                            />
                          )}
                        </figure>
                        <div className="card-body items-center text-center">
                          <h2 className="text-xl font-bold">{product.name}</h2>
                          <span>{product.description.slice(0, 130)}  {product.description.length > 130 && "..."}</span>
                          <h4>CAD $ {product.price}</h4>
                          <div className="card-actions">
                            <button
                              className="btn btn-outline btn-info"
                              onClick={() => {
                                // console.log(`Button clicked with product ID: ${product.id}`);
                                navigate(`/products/${product.id}`);
                              }}
                            >
                              View Product
                            </button>
                          </div>
                        </div>
                      </div>
                    </Link>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>

        <div className="">
          <div className="w-7xl m-14 px-1">
            <div className="">
              <h1 className="text-3xl">
              Featuring the best artists near you.
              </h1>
            </div>

            <div className="flex justify-center w-240">
              <div className="carousel carousel-center rounded-box">
                {artists.map((artist) => (
                  <div
                    key={artist.id}
                    className="carousel-item transition duration-300"
                  >
                    <Link to={`/artist/${artist.artistId}`}>
                      <div
                        key={artist.id}
                        className="card w-96"
                        style={{ height: "80%" }}
                      >
                        <figure className="w-full h-80">
                          <img
                            src={artist.profileImageUrl || defaultImage}
                            alt={"No Image"}
                            style={{
                                objectFit: "cover",
                                width: "55%",
                                height: "200px",
                                borderRadius: "250px"
                              }}
                          />
                        </figure>
                        <div className="card-body items-center text-center">
                          <h2 className="text-xl font-bold">{artist.firstName}</h2>
                          <span>{artist.aboutMe}</span>
                          <div className="card-actions">
                            <button
                              className="btn btn-outline btn-info"
                              onClick={() => {
                                // console.log(`Button clicked with product ID: ${product.id}`);
                                navigate(`/artist/${artist.artistId}`);
                              }}
                            >
                              Know more
                            </button>
                          </div>
                        </div>
                      </div>
                    </Link>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div></>)}

        

        

      </div>
    </>
  );
}
