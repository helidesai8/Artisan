import { useSetRecoilState } from "recoil";
import { Loggedin_User } from "../atoms/Loggedin_User_Atom";
import axios from "axios";
const backendURL= import.meta.env.VITE_REACT_APP_BACKEND_URL+ "api/v1/user/me"
export async function  fetchUserData(setUser,token)
{
    
    if(token)
    {
        const response=await axios.get(backendURL,
        {headers:{"Authorization":`Bearer ${token}`}});
        
        setUser(response.data);
    }

};