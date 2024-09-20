import {useNavigate} from "react-router-dom";
import logo from "../../assets/logo.png";
import { completeOrder } from "../../functions/completeOrder";


function Success() {
    const queryParams = new URLSearchParams(window.location.search)
    const navigate = useNavigate();
    let orderComplete = false;
    completeOrder(queryParams).then((response)=>{
        if(response){
           orderComplete = true;
            localStorage.removeItem("Products");
            navigate('/status', {state: {status: "success", message: "Order Place successfully.", navigate:'/order_history'}});
        }
    }).catch((error) => {
        navigate('/status', { state: {status: "failure", message: "Order could not be completed Please try later", navigate: '/failure' }});
    });

    return (
        <div className="h-screen flex-col">
            <nav className='navbar fixed top-0 left-0 w-full bg-white z-50 h-7 border-b'>
                <div className="nav-logo-container pl-7">
                <button onClick={()=>navigate('/')}>
                    <img src={logo} alt="" className='h-18 w-20'/>
                </button>
                </div>
                <div className="navbar-links-container pr-6">
                <button onClick={()=>navigate('/dashboard')} style={{color:"#000"}} class="text-gray-800 hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2">Home</button>
                <button style={{color:"#000"}} href="#" class="text-gray-800 dark:text-white hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2 p-5"
                    onClick={()=>{localStorage.clear();
                        navigate('/');}}>Logout</button>
                </div>
            </nav>

       
            
            <div id="loader" className="absolute inset-0 flex justify-center items-center   ">
                <div className="grid justify-items-center">
                    <span className="loading loading-spinner text-success loading-lg"></span>
                    <p>Payment succesfull! Please wait while we complete your order.</p>
                </div>
            </div>
        </div>
    )
}

export default Success
