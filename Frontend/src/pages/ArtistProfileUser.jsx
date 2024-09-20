import React, { useState, useEffect, useRef ,useCallback} from 'react';
import { useParams,useNavigate } from 'react-router-dom';
import { Cursor } from 'react-simple-typewriter';
import { fetchProductData,fetchArtistData,fetchArtistReview } from '../functions/fetchArtistDetailsForUser';
import logo from "../assets/logo.png"
import Rating from '../Components/Product_Details_Components/Rating';

const ArtistProfileForUser = () => {
    const { artistId } = useParams();
    const [products, setProducts] = useState([]);
    const [artistData, setArtistData] = useState(null);
    const [currentText, setCurrentText] = useState('');
    const [slideImages, setSlideImages] = useState([]);
    const [reviews, setReviews] = useState([]); // Initialize reviews state with an empty array
    
    const  navigate = useNavigate();


    useEffect(() => {
        if (!localStorage.getItem('token')) {
          navigate('/');
        }
      }, [navigate]);

    useEffect(() => {
        const fetchData = async () => {
           try{
                const artistRespone= await fetchArtistData(artistId);
                setArtistData(artistRespone);
                if (artistRespone && artistRespone.story && artistRespone.story.storyImageUrls) {
                    
                    setSlideImages(artistRespone.story.storyImageUrls);
                }
                const artistReview = await fetchArtistReview(artistId);
                if(artistReview)
                    setReviews(artistReview);
           }catch(e){
               //Redirect to 404 page
                 console.log(e);
                navigate('/404');

           }


            try {
                // Assuming fetchProductData is an async function that fetches product data
                const productData = await fetchProductData(artistId);
                console.log(productData);
                setProducts(productData);
            } catch (error) {
                console.error('Error fetching product data:', error);
            }
        };

        // Call fetchData function
        fetchData();
    }, []);
    
    const totalSlides = artistData && artistData.story && artistData.story.storyImageUrls? artistData.story.storyImageUrls.length: 0;

    useEffect(() => {
        if (artistData && artistData.story && artistData.story.story) {
            setCurrentText(artistData.story.story);
        }
    }, [artistData]);
         
  const index = useRef(0);

  useEffect(() => {
    const intervalId = setInterval(() => {
        if (artistData && artistData.story && artistData.story.story && index.current < artistData.story.story.length) {
                        setCurrentText((value) => {
                            value += currentText[index.current];
                            index.current++;
                            return value;
                        });
                    } else {
                        clearInterval(intervalId);
                    }
                }, 70);

            return () => clearInterval(intervalId);
        }, [currentText]);


  const defaultImage =
    "data:image/png;base64,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";
    const [slideID, setSlideID] = useState(0);
    const nextLinkRef = useRef();

    useEffect(() => {
        if (slideImages.length > 0) {
            const intervalID = setInterval(() => {
                next();
                if (nextLinkRef.current) {
                    nextLinkRef.current.click();
                }
            }, 7000);
    
            return () => clearInterval(intervalID);
        }
    }, [slideImages]);

    const next = useCallback(() => {
        setSlideID((slideID + 1) % slideImages.length);
    }, [slideID, slideImages]);

    const prev = useCallback(() => {
        setSlideID((slideID + slideImages.length - 1) % slideImages.length);
    }, [slideID, slideImages]);

   

    

    return (
      <>
        <nav className='navbar sticky top-0 left-0 w-full bg-white z-50 h-7 border-b'>
        <div className="nav-logo-container pl-7">
        <button onClick={()=>navigate('/')}>
            <img src={logo} alt="" className='h-18 w-20'/>
            </button>
        </div>
        <div className="navbar-links-container pr-6">
            <button onClick={()=>navigate('/user_profile')} style={{color:"#000"}} class="text-gray-800 hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2">Profile</button>
            <button onClick={()=>navigate('/dashboard')} style={{color:"#000"}} class="text-gray-800 hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2">Dashboard</button>
            <button style={{color:"#000"}} href="#" class="text-gray-800 dark:text-white hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2 p-5"
                    onClick={()=>{localStorage.clear();
                        navigate('/');}}>Logout</button>
        </div>
    </nav>



        <div  className="pt-20">
            
        {/* <ArtistDetail /> */}
            <div>
            {artistData && (
                <div className="m-10  flex">
                    <div className="flex-1 pl-5">
                        <h2  className="pt-5  text-2xl font-extrabold flex-grow">Meet the artist {artistData.firstName} {artistData.lastName}!</h2>
                        <p className='pt-3 font-light'>{artistData.city}</p>
                        <h4 className='pt-5  font-medium'>
                            {currentText ? (currentText):(<h1 className="text-1.5xl" >No story added by artist</h1>)}
                            {/* <Cursor /> */}
                        </h4>
                    </div>
                    <div className="avatar mt-4 flex flex-col items-center justify-center">
                        <div className="w-32 rounded-full m-10 overflow-hidden">
                            <img
                                src={artistData && artistData.profileImageUrl ? artistData.profileImageUrl : defaultImage}
                                alt="Avatar"
                                className="w-full h-full object-cover"
                            />
                        </div>
                    </div>
                </div>
            )}
        </div>
         
            <div className="mx-10">
                    {slideImages.length > 0 && (
                        <div className="carousel w-full relative">
                            {slideImages.map((image, index) => (
                                <div key={`slide${index}`} className="carousel-item relative w-full" style={{display: slideID === index ? 'block' : 'none'}}>
                                    <div className="h-400px flex items-center justify-center">
                                        <img src={image} style={{ width: '650px', height: '400px', objectFit: 'cover' }} />
                                    </div>
                                    <div className="absolute top-1/2 transform -translate-y-1/2 flex justify-between w-full">
                                        <button onClick={prev} className="btn btn-circle absolute left-5 bg-slate-100">❮</button>
                                        <button ref={nextLinkRef} onClick={next} className="btn btn-circle absolute right-5 bg-slate-100">❯</button>
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
            </div>

            {/* <Slides /> */}
             <div className="mx-10  mt-20">
            <h2 className="text-2xl font-extrabold m-4">Browse their products</h2>

                    <div className="grid grid-cols-3 gap-4">
                        {products.length == 0 ? (<h1 className="text-1.5xl m-4">No products added</h1>): (<></>)}
                            {products.map(product => (
                                <div key={product.id} className="card w-96 bg-base-100 shadow-xl">
                                    <figure className="px-10 pt-10">
                                    {product.imageUrls.length > 0 && (
                                    <img
                                        src={product.imageUrls[0]} 
                                        alt={product.name}
                                        style={{ objectFit: 'cover', width: '100%', height: '200px' }}
                                    />
                                 )}
                                    </figure>
                                    <div className="card-body items-center text-center">
                                        <h2 className="text-xl font-bold">{product.name}</h2>
                                        <p>{product.description}</p>
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
                            ))}
                    </div>
                </div>
            {/* <ArtistProducts /> */}
            <div className="w-full p-4 mx-10 mt-8 border-t" >
                <h2 className="text-2xl font-semibold mb-4">Artist Reviews</h2>
                {reviews.length === 0 ? (<h1 className="text-1.5xl"> No reviews added </h1>):(<></>)}
                {reviews.map((review, index) => (
                    <div key={index} className="mb-4">
                    <p className="text-lg font-semibold mb-1">{`${review.firstName} ${review.lastName}`}</p>
                    <Rating value={review.rating} />
                    <p className="text-lg">{review.comment}</p>
                    </div>
                ))}
            </div>
            
        </div>
        </>
    );
};

export default ArtistProfileForUser;

