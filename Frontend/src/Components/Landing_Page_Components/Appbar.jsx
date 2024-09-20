import { useNavigate } from "react-router-dom"
import {useRecoilValue} from 'recoil'
import { Loggedin_User } from "../../atoms/Loggedin_User_Atom";
import logo from "../../assets/logo.png";

export function Appbar(calledFrom) {
   const navigate=useNavigate();
   const user=useRecoilValue(Loggedin_User);
   console.log("the recoil value is")
   console.log(user);
   var showLogin = true;
   if (calledFrom.data === 'login_artist' || calledFrom.data === 'login_user'){
      showLogin = false;
   }
  
  return (
    <nav className='navbar fixed top-0 left-0 w-full bg-white z-50 h-7 border-b'>
      <div className="nav-logo-container pl-7">
      <button onClick={()=>navigate('/')}>
          <img src={logo} alt="" className='h-18 w-20'/>
        </button>
      </div>
    <div className="navbar-links-container pr-6">
         {showLogin && <button style={{color:"#000"}} class="text-gray-800 hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2" onClick={()=>navigate('/login')}>Login</button>}
         
         {calledFrom.data != 'register_artist' && <button  style={{color:"#000"}} class="text-gray-800 hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2" onClick={()=>navigate('/login_artist')}>Join as Artist</button>}
    </div>
  </nav>
  )
}