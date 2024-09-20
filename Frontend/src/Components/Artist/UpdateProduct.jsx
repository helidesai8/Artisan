import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useRecoilState } from "recoil";
// import { productsState } from "../../atoms/Products";
import axios from "axios";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import logo from "../../assets/logo.png";


const UpdateProduct = (props) => {

  const token = localStorage.getItem("token");
  // const token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2hpc2guYmhhc2luQGRhbC5jYSIsImlhdCI6MTcxMDMwOTEwNSwiZXhwIjoyNzEwMzA5MTA1fQ.DT2_NBNlYfSWu-jLnKo0RUY9MJ4Z10SnQ7OSNeSwa94"


  const { id } = useParams();
  const navigate = useNavigate();
  // console.log(products)

  // const [products, setProducts] = useRecoilState(productsState)

  const [name, setName] = useState();
  const [description, setDescription] = useState();
  const [price, setPrice] = useState();
  const [quantity, setQuantity] = useState();
  const [category, setCategory] = useState(`Choose a category`);
  const [categoryList, setCategoryList] = useState();
  const [response, setResponse] = useState();
  const [imagesPreview, setImagesPreview] = useState([]);
  const [images, setImages] = useState();
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    
    try{
      const fetchData = async () => {
        setLoading(true);
  
        const product = await axios.get(import.meta.env.VITE_REACT_APP_BACKEND_URL+`api/v1/products/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      const categories = await axios.get(import.meta.env.VITE_REACT_APP_BACKEND_URL+"api/v1/products/categories", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
  
        setLoading(false);
        
      setCategoryList([...categories.data]);
      setName(product.data.name);
      setDescription(product.data.description);
      setCategory(product.data.categoryName);
      setPrice(product.data.price);
      setQuantity(product.data.quantity);
      setResponse(product.data);
      setImages([product.data.imageUrls[0]]);
      }
      fetchData();
    }catch(e){
      navigate('/login')
    }
  
  }, [])

  const submitHandler = async(e) => {
    e.preventDefault();
    const newProduct = new FormData();
    newProduct.append("product", JSON.stringify({name, description, price, quantity, categoryName:category}));
    images.forEach((image, index) => {
    newProduct.append(`images`, image);

  });
  
    try {
      const response = await axios.put(import.meta.env.VITE_REACT_APP_BACKEND_URL+`api/v1/products/${id}`, newProduct, {
        headers: {
          Authorization: `Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2hpc2guYmhhc2luQGRhbC5jYSIsImlhdCI6MTcxMDMwOTEwNSwiZXhwIjoyNzEwMzA5MTA1fQ.DT2_NBNlYfSWu-jLnKo0RUY9MJ4Z10SnQ7OSNeSwa94  `,
          'Content-Type': 'multipart/form-data',  
        },
      });
      navigate("/artist/dashboard")
      console.log(response);
    } catch (e) {
      console.log(e);
    }
  };
  
  const onImageChange = (e) => {
    const files = Array.from(e.target.files);
    setImages([...files])

    files.forEach((file) => {
      const reader = new FileReader();

      reader.onload = () => {
        if (reader.readyState === 2) {
          setImagesPreview([reader.result]);
        }
      };

      reader.readAsDataURL(file);
    });
  };
  return (
    <>
    <header>
    <div class="bg-white border-gray-200 py-1 dark:bg-gray-800" style={{backgroundColor:"#fff"}}>
        <div class="flex flex-wrap justify-between items-center mx-auto pl-12">
        <button onClick={()=>navigate('/')}>
          <img src={logo} alt="" className='h-18 w-20'/>
        </button>
            <div class="flex items-center lg:order-2">
                <a href="#" style={{color:"#000"}} class="text-gray-800 dark:text-white hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2">Insights</a>
                <a href="#" style={{color:"#000"}} class="text-gray-800 dark:text-white hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2">Profile</a>
                <a style={{color:"#000"}} class="text-gray-800 dark:text-white hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2 p-5"
                  onClick={()=>{localStorage.clear();
                      navigate('/');}}>Logout</a>
            </div>
            <div class="hidden justify-between items-center w-full lg:flex lg:w-auto lg:order-1" id="mobile-menu-2">
            </div>
        </div>
    </div>
</header>
    {loading ? (<div className="flex items-center justify-center h-screen">
        <span className="loading loading-spinner loading-lg"></span>
      </div>
) : (<><div className="updateProduct">
      <ToastContainer/>
      <section class="bg-gray-50">
        <div class="flex flex-col items-center justify-center px-6 py-8 mx-9">
          <div class="w-full bg-white rounded-lg shadow">
            <div class="p-6 space-y-4 md:space-y-6 sm:p-8">
              <h1 class="text-xl font-bold leading-tight tracking-tight text-gray-900 md:text-2xl ">
                Update Product
              </h1>
              <form class="space-y-4 md:space-y-6" action="#" onSubmit={submitHandler}>
                <div>
                  <label
                    for="email"
                    class="block mb-2 text-sm font-medium text-gray-900"
                  >
                    Product Name
                  </label>
                  <input
                    type="text"
                    name="Product Name"
                    id="name"
                    class="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
                    placeholder="Enter product name"
                    required=""
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                  />
                </div>
                <div>
                  <label
                    for="password"
                    class="block mb-2 text-sm font-medium text-gray-900"
                  >
                    Description
                  </label>
                  <input
                    type="text"
                    name="Description"
                    id="description"
                    placeholder="Enter description"
                    class="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
                    required=""
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                  />
                </div>
                <div class="flex justify-around items-center">
                  <div class="w-full">
                    <label
                      for="price"
                      class="block mb-2 text-sm font-medium text-gray-900"
                    >
                      Price
                    </label>
                    <input
                      type="number"
                      name="price"
                      id="price"
                      placeholder="Enter price"
                      class="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 w-4/5 p-2.5"
                      required=""
                      value={price}
                      onChange={(e) => setPrice(e.target.value)}
                    />
                  </div>

                  <div class="w-full">
                    <label
                      for="quantity"
                      class="block mb-2 text-sm font-medium text-gray-900"
                    >
                      Quantity
                    </label>
                    <input
                      type="number"
                      name="quantity"
                      id="quantity"
                      placeholder="Enter quantity"
                      class="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 w-4/5 p-2.5"
                      required=""
                      value={quantity}
                      onChange={(e) => {setQuantity(e.target.value); console.log(e.target.value)}}
                    />
                  </div>
                </div>

                <div>
                  <label
                    for="price"
                    class="block mb-2 text-sm font-medium text-gray-900"
                  >
                    Choose category
                  </label>
                  <div className="dropdown">
                    <div tabIndex={0} role="button" className="btn m-1">
                      {category == null ? ("Choose a category") : category}
                    </div>
                    <ul
                      tabIndex={0}
                      className="dropdown-content z-[1] menu p-2 shadow bg-base-100 rounded-box w-52"
                    >
                      {categoryList && categoryList.map((category) => (
                        <li>
                        <a onClick={() => setCategory(category.name)}>
                          {category.name}
                        </a>
                      </li>
                      ))}
                    </ul>
                  </div>
                </div>

                <div>
                  <label
                    for="price"
                    class="block mb-2 text-sm font-medium text-gray-900"
                  >
                    Select images
                  </label>
                  <div className="images">
                        {imagesPreview.length==0 &&  <img
                          src={images}
                          alt="Preview"
                          style={{ maxWidth: "100%", maxHeight: "200px" }}
                          className="image"
                        />}
                    {imagesPreview.length>0 &&
                      imagesPreview.map((image) => (
                        <img
                          src={image}
                          alt="Preview"
                          style={{ maxWidth: "100%", maxHeight: "200px" }}
                          className="image"
                        />
                      ))}
                  </div>
                  <div class="flex items-center justify-center w-full">
                    <label
                      for="dropzone-file"
                      class="flex flex-col items-center justify-center w-full h-80 border-2 border-gray-300 border-dashed rounded-lg cursor-pointer bg-gray-50"
                    >
                      <div class="flex flex-col items-center justify-center pt-5 pb-6">
                        <svg
                          class="w-8 h-8 mb-4 text-gray-500"
                          aria-hidden="true"
                          xmlns="http://www.w3.org/2000/svg"
                          fill="none"
                          viewBox="0 0 20 16"
                        >
                          <path
                            stroke="currentColor"
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M13 13h3a3 3 0 0 0 0-6h-.025A5.56 5.56 0 0 0 16 6.5 5.5 5.5 0 0 0 5.207 5.021C5.137 5.017 5.071 5 5 5a4 4 0 0 0 0 8h2.167M10 15V6m0 0L8 8m2-2 2 2"
                          />
                        </svg>
                        <p class="mb-2 text-sm text-gray-500">
                          <span class="font-semibold">Click to upload</span> or
                          drag and drop
                        </p>
                        <p class="text-xs text-gray-500">
                          SVG, PNG, JPG (MAX. 800x400px)
                        </p>
                      </div>
                      <input
                        id="dropzone-file"
                        type="file"
                        class="hidden"
                        accept="image/jpeg,image/png"
                        onChange={onImageChange}
                      />
                    </label>
                  </div>
                </div>
                <div>
                  <button className="btn btn-wide mx-auto block" type="submit" onClick={submitHandler}>Submit</button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </section>
    </div></>)}
    </>
  );
};

export default UpdateProduct;



