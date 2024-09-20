const backendURL=import.meta.env.VITE_REACT_APP_BACKEND_URL;
import axios from "axios";




export const fetchProductData  = async (artistID) =>{
    console.log("fetchProfileData");
    const token = localStorage.getItem("token");
    const response = await axios.get(`${backendURL}api/v1/products/artist/${artistID}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const activeProducts = response.data.filter(product => product.isActive === true);

    return activeProducts;
};

export const fetchArtistData = async (artistID) =>{
    console.log("fetchArtistData");
    const token = localStorage.getItem("token");
    const response = await axios.get(`${backendURL}api/v1/artist/profile/details/${artistID}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
    return response.data;
}
export const fetchArtistReview = async (artistID) =>{
  console.log("fetchArtistData");
  const token = localStorage.getItem("token");
  const response = await axios.get(`${backendURL}api/v1/ratings/artists/${artistID}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  return response.data;
}
