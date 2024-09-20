import axios from "axios";
import * as zod from "zod";
import { useNavigate } from "react-router-dom";

const SignupSchema = zod.object({
  firstName: zod.string()
    .min(1, "First name should be of minimum length 1.")
    .regex(/^[A-Za-z]+$/, "First name should only contain letters."),
  lastName: zod.string()
    .min(1, "Last name should be of minimum length 1.")
    .regex(/^[A-Za-z]+$/, "Last name should only contain letters."),
  username: zod.string().email("Email should be a valid email."),
  password: zod.string().min(8, "Password should be at least 8 characters long."),
});

export const handleSignup = async (firstName, lastName, username, password, setErrors, setError, navigate,backendURL,successNavigationUrl,role) => {
  const result = SignupSchema.safeParse({ firstName, lastName, username, password });

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
      email:username,
      firstName,
      lastName,
      password
    });
    

    if(response.data.accessToken){
      localStorage.setItem("token",response.data.accessToken);
      localStorage.setItem("role",role);
      window.location.href = successNavigationUrl;
    }

    
    
  } catch (error) {
     
      setError(error.response.data.message);
    
  }
};
