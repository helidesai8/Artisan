import axios from "axios";
const backendURL= import.meta.env.VITE_REACT_APP_BACKEND_URL+ "api/v1/order/complete-order?";

export async function completeOrder(sessionId)
{
        try{
            let request = JSON.parse(localStorage.getItem("Products"));
            let token = localStorage.getItem('token');
            if(token){
                const response=await axios.post(backendURL+sessionId, request,
                {headers:{"Authorization":`Bearer ${token}`}});
                return response.data;
            }
        } catch (error) {
            var msg = error.response?.data?.message;
            if(!msg){
                "Something went wrong";
            }
            // TODO: decide on an approach to show user the errors. toast or something else?
            console.log(msg);
            throw error;
        }
};