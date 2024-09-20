import axios from 'axios';
const backendURL=import.meta.env.VITE_REACT_APP_BACKEND_URL+"api/v1/artist/profile";



export const handleSaveProfile  = async (id) =>{
    
    const token = localStorage.getItem("token");
    const headers = { 
        headers: {
            Authorization: `Bearer ${token}`,
        },
    };
    const data={
        
    }
    
    console.log(response.data);
    return response.data;


};
