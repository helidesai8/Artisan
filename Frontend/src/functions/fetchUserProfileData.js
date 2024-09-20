import { data } from 'autoprefixer';
import axios from 'axios';

const backendURL=import.meta.env.VITE_REACT_APP_BACKEND_URL;




export const fetchProfileData  = async () =>{
    console.log("fetchProfileData");
    const token = localStorage.getItem("token");
    const response = await axios.get(`${backendURL}api/v1/user/profile`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
    console.log(response.data);
    return response.data;
};

export const saveProfile=async(streetAddress,city,postalCode,country,contactNumber)=>{
    try
    {
        const token = localStorage.getItem("token");
        const response = await axios.put(
          `${backendURL}api/v1/user/profile`,
          {
            streetAddress: streetAddress,
            city: city,
            postalCode: postalCode,
            country: country,
            contactNumber: contactNumber,
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        )
        return response.data;
    }
    catch(error)
    {
        return error;
    }

};


  export const deleteProfileImage = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.delete(`${backendURL}api/v1/user/profile/profileImage`, {
        headers: {
          Authorization: `Bearer ${token}`, 
        },
      });
  
      console.log(response.data);
      return response.data;
    } catch (error) {
      console.error('Error deleting profile image:', error);
      return error; // You can handle the error according to your needs
    }
  }

  export const saveProfileImage = async (avatarSrc) => {
    try {
      const formData = new FormData();
      formData.append('image', avatarSrc);
  
      const token = localStorage.getItem("token");
      const response = await axios.post(
        `${backendURL}api/v1/user/profile/profileImage`,
        formData,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'multipart/form-data',
          },
        }
      );
      
      return response.data;
    } catch (error) {
      return error;
    }
  };



  

