import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import * as zod from "zod";



const LoginSchema = zod.object({
  username: zod.string().email("Email should be a valid email"),
  password: zod.string().min(1, "Please enter a password"),
});

export const handleLogin = async (username, password, setErrors,setError, navigate,backendURL,successNavigationUrl,role) => {
  const result = LoginSchema.safeParse({ username, password });

  if (!result.success) {
    const errorObject = {};

    result.error.errors.forEach((error) => {
      const key = error.path[0];
      errorObject[key] = error.message;
    });

    setErrors(errorObject);
    return;
  }

  

  try {
    
    const response = await axios.post(backendURL, {
      email: username,
      password,
    });

    
    if(response.data.accessToken){
      localStorage.setItem("token",response.data.accessToken);
      localStorage.setItem("role",role);
      window.location.href = successNavigationUrl;
    }
    
  } catch (error) {
    
    if(error.response.data.message==="Bad credentials")
    {
      setError("Invalid username or password. Please try again.");
    }
    else
    {setError(error.response.data.message);}
  }
};