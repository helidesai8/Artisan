import axios from "axios";

const backendURL= import.meta.env.VITE_REACT_APP_BACKEND_URL+ "api/v1/order/checkout";

export async function initiateCheckout(){
    try{
    let request = JSON.parse(localStorage.getItem("Products"));
    let token = localStorage.getItem('token');
    if(token && request){
        await axios.post(backendURL,request,
        {headers:{"Authorization":`Bearer ${token}`}})
        .then((response) => {
          window.location.href = response.data;
        });
    }
    }catch(error){
        throw error;
    }
}