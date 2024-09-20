import { BottomWarning } from "../Components/User_Registration_Components/BottomWarning"
import { Button } from "../Components/User_Registration_Components/Button"
import { Heading } from "../Components/User_Registration_Components/Heading"
import { InputBox } from "../Components/User_Registration_Components/InputBox"
import { SubHeading } from "../Components/User_Registration_Components/SubHeading"
import { Appbar } from "../Components/Landing_Page_Components/Appbar"
import { useNavigate } from "react-router-dom"
import { useState } from "react"  
import { handleLogin } from "../functions/handleLogin"
import { useEffect } from "react"




export default function Signin ()  {

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [errors, setErrors] = useState({});
  const [error, setError] = useState("");
  const backendURL = import.meta.env.VITE_REACT_APP_BACKEND_URL +"api/v1/auth/artist/login";
  
  const successNavigationUrl = "/artist/dashboard";
  const role="artist";



  const navigate = useNavigate();
  useEffect(() => {
    if (localStorage.getItem('token') && localStorage.getItem('role') === 'artist'){
      navigate(successNavigationUrl);
    }
  }, [navigate]);
  useEffect(() => {
    document.title = "Artist Login";
    
  }, []);


  const handleLoginClick = async (e) => {
    e.preventDefault();
    handleLogin(username, password, setErrors, setError, navigate,backendURL,successNavigationUrl,role);
  };
  

  return (
    <div className="bg-slate-300 min-h-screen flex flex-col">
      <Appbar data = {'login_artist'}/>
      <div className="flex flex-grow justify-center" style={{backgroundColor:"#f6f6f6"}}>
        <div className="flex flex-col sm:flex-row justify-between w-full max-w-screen-lg mx-auto p-4 mb-3 ">
          <div className="w-full sm:w-1/2 flex flex-col justify-center items-center">
            <Heading label={"Welcome, Artist!"} />
            <p className="text-slate-500 text-md mt-3 text-center">
            Login to platform where you can showcase your wide range of local tranditional arts.
            </p>
            <SubHeading label={"Enter your account details to access your account"} />
          </div>
          <div className="w-full lg:pl-48  md:w-1/2 flex flex-col justify-center items-center">
            <div className="rounded-lg bg-white w-full md:w-96 text-center p-2 px-4 mx-auto">
              <Heading label={"Log in "} />
              <InputBox
                onChange={(e) => {
                  setUsername(e.target.value);
                  setErrors((prevErrors) => ({ ...prevErrors, username: null }));
                }}
                placeholder="Enter your email address"
                label={"Email"}
              />
              <div style={{ height: '15px', color: 'red' }} className="text-left ml-1">
                {errors.username && <p className="text-red-500">{errors.username}</p>}
              </div>
              <InputBox onChange={(e) => {
                                setPassword(e.target.value)
              }} placeholder="Enter your password" label={"Password"} type="password" />
              <div style={{ height: '40px', color: 'red' }} className="text-left ml-1">
                {errors.password && <p className="text-red-500">{errors.password}</p>}
              </div>
              <div className="pt-4">
                <Button type="submit" label={"Sign in"} onClick={handleLoginClick} />
                <div style={{ height: '20px', color: 'red' }}>
                  {error && <p className="text-red-500 pt-2">{error}</p>}
                </div>
              </div>
              <BottomWarning label={"Don't have an account?"} buttonText={"Register"} to={"/register_artist"} />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}