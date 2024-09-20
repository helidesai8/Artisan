import { useState } from "react"
import { BottomWarning } from "../Components/User_Registration_Components/BottomWarning"
import { Button } from "../Components/User_Registration_Components/Button"
import { Heading } from "../Components/User_Registration_Components/Heading"
import { InputBox } from "../Components/User_Registration_Components/InputBox"
import { SubHeading } from "../Components/User_Registration_Components/SubHeading"
import { Appbar } from "../Components/Landing_Page_Components/Appbar"
import { useNavigate } from "react-router-dom"
import { handleSignup } from "../functions/handleSignup"
import { validatePassword } from "../functions/validatepassword"
import { useEffect } from 'react';





  

export default function Signup() {
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errors, setErrors] = useState({});
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const backendURL = import.meta.env.VITE_REACT_APP_BACKEND_URL+"api/v1/auth/artist/register";
  const successNavigationUrl = "/artist_profile";
  const role="artist";


  useEffect(() => {
    if (localStorage.getItem('token') && localStorage.getItem('role') === 'artist') {
      navigate(successNavigationUrl);
    }
  }, [navigate]);

  useEffect(() => {
    document.title = "Artist Registeration";
    
  }, []);
  
  const handleSignupClick = (e) => {
    e.preventDefault();
    handleSignup(firstName, lastName, username, password, setErrors,setError, navigate,backendURL,successNavigationUrl,role);
  };


  return (

  <div className="bg-slate-300 min-h-screen flex flex-col">
    <Appbar data = {'register_artist'}/>
    <div className="flex flex-grow justify-center" style={{backgroundColor:"#f6f6f6"}}>
    <div className="flex flex-col sm:flex-row justify-between w-full max-w-screen-lg mx-auto p-4 mb-3">
      
      <div className="w-full sm:w-1/2 flex flex-col justify-center items-center">
            <Heading label={"Welcome Artist!"} />
            <p className="text-slate-500 text-md  text-center mt-3">
             Register to get access to a platform where you can showcase your wide range of local tranditional arts.
            </p>
            <SubHeading label={"Enter your infromation to create an account as a artist"} />
          </div>
     <div className="w-full lg:pl-48  md:w-1/2 flex flex-col justify-center items-center">
      <div className="rounded-lg bg-white w-full md:w-96 text-center p-2 px-4 mx-auto">
        <Heading label={"Register"} />
        <InputBox  onChange={e => {
                    setFirstName(e.target.value);
                    setErrors(prevErrors => ({ ...prevErrors, firstName: null }));
                    }} placeholder="Enter your first name " label={"First Name"} />
        <div style={{ height: '15px', color: 'red'}} className="text-left ml-1 mt-1">{errors.firstName && <p className="text-red-500">{errors.firstName}</p>}</div>
        <InputBox onChange={e => {
                    setLastName(e.target.value);
                    setErrors(prevErrors => ({ ...prevErrors, lastName: null }));
        }} placeholder="Enter your last name" label={"Last Name"} />
        <div style={{ height: '15px', color: 'red' }} className="text-left ml-1 ">{errors.lastName && <p className="text-red-500">{errors.lastName}</p>}</div>
        <InputBox onChange={e => {
                            setUsername(e.target.value);
                            setErrors(prevErrors => ({ ...prevErrors, username: null }));
                            }} placeholder="Enter your email address" label={"Email"} />
        <div style={{ height: '15px', color: 'red' }} className="text-left ml-1">{errors.username && <p className="text-red-500">{errors.username}</p>}</div>

        <InputBox onChange={e => {
                              const password = e.target.value;
                              setPassword(password);
                              const passwordError = validatePassword(password);
                              setErrors(prevErrors => ({ ...prevErrors, password: passwordError }));
                            }} placeholder="Enter your password" label={"Password"} type="password" />
        <div style={{ height: '40px', color: 'red' }} className="text-left ml-1">{errors.password && <p className="text-red-500">{errors.password}</p>}</div>
         <div className="pt-4">
        <Button type="submit" label={"Sign up"} onClick={handleSignupClick} />
        <div style={{ height: '25px', color: 'red' }} className="text-left ml-1">{error && <p className="text-red-500 pt-2">{error}</p>}</div>

        </div>
        <BottomWarning label={"Already have an account?"} buttonText={"Login"} to={"/login_artist"} />
      </div>
    </div>
    </div>
    </div>
  </div>

  )
}


