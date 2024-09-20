import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { ToastContainer, toast } from 'react-toastify';
import logo from "../../assets/logo.png";


const AddProduct = (props) => {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  const [name, setName] = useState();
  const [description, setDescription] = useState();
  const [price, setPrice] = useState();
  const [quantity, setQuantity] = useState();
  const [category, setCategory] = useState(`Choose a category`);
  const [categoryList, setCategoryList] = useState();
  const [imagesPreview, setImagesPreview] = useState();
  const [images, setImages] = useState();
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const categories = await axios.get(import.meta.env.VITE_REACT_APP_BACKEND_URL+"api/v1/products/categories", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setLoading(false);
      setCategoryList([...categories.data]);
      // console.log(categoryList)

      } catch (error) {
        navigate('/login')
        console.error("Error fetching data:", error);
      }
    };

    fetchData();
  }, []);

  const onImageChange = (e) => {
    const files = Array.from(e.target.files);
    setImages([...files])
    // console.log(files);

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

  const submitHandler = async(e) => {
    e.preventDefault();
    if(name===undefined || description===undefined || quantity===undefined || category===undefined) toast.error('Please provide all the details.');
    else if(images===undefined) toast.error('Please provide an image');
    else {
      const newProduct = new FormData();
      newProduct.append("product", JSON.stringify({name, description, price, quantity, categoryName:category}));
      images.forEach((image, index) => {
        newProduct.append(`imageUrls`, image);
      });  
    try {
      setLoading(true);
      const response = await axios.post(import.meta.env.VITE_REACT_APP_BACKEND_URL+"api/v1/products", newProduct, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'multipart/form-data',  
        },
      });
      setLoading(false);
      navigate("/artist/dashboard")
    } catch (e) {
      console.log(e);
    }
  }
  };
  return (
    <>
    <header>
    <div class="bg-white border-gray-200 py-1 " style={{backgroundColor:"#fff"}}>
        <div class="flex flex-wrap justify-between items-center mx-auto pl-12">
        <button onClick={()=>navigate('/')}>
          <img src={logo} alt="" className='h-18 w-20'/>
        </button>
            <div class="flex items-center lg:order-2">
                <a href="/artist/dashboard" style={{color:"#000"}} class="text-gray-800 hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2">Insights</a>
                <a href="/artist_profile" style={{color:"#000"}} class="text-gray-800 hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2">Profile</a>
                <a style={{color:"#000"}} class="text-gray-800  hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2 p-5"
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
      </div>) : (<div className="addProduct">
      <ToastContainer />
      <section class="bg-gray-50">
        <div class="flex flex-col items-center justify-center px-6 py-8 mx-9">
          <div class="w-full bg-white rounded-lg shadow xl:max-w-6xl xl:p-0">
            <div class="p-6 space-y-4 md:space-y-6 sm:p-8">
              <h1 class="text-xl font-bold leading-tight tracking-tight text-gray-900 md:text-2xl">
                Add New Product
              </h1>
              <form class="space-y-4 md:space-y-6" onSubmit={submitHandler} enctype="multipart/form-data">
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
                  <textarea
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
                      onChange={(e) => setQuantity(e.target.value)}
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
                      {category}
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
                    {imagesPreview &&
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
                          class="w-8 h-8 mb-4 text-gray-500 "
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
                        <p class="mb-2 text-sm text-gray-500 ">
                          <span class="font-semibold">Click to upload</span> or
                          drag and drop
                        </p>
                        <p class="text-xs text-gray-500 ">
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
                  <button className="btn btn-wide mx-auto block" type="submit">Submit</button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </section>
    </div>)}
    </>
  );
};

export default AddProduct;
