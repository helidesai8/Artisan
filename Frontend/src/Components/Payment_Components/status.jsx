import logo from "../../assets/logo.png";
import paymentFailed from "../../assets/payment-failure.png";
import orderCompleteImg from "../../assets/order-complete.png";
import { useLocation } from "react-router-dom";
import {useNavigate} from "react-router-dom";

function Status() {
    const navigate = useNavigate();
    const location = useLocation();
    const data = location.state;
    let status = null;
    if(data && data.status){
        status = data.status;
        setTimeout(()=> {
            navigate(data.navigate);
        }, 3000);
    }
    else{
        setTimeout(()=> {
            navigate('/cart');
        }, 3000);
    }
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

       
            <div id="loader" className="absolute inset-0 flex justify-center items-center">
                <div className="grid justify-items-center">
                    { status == "success" &&
                            <img src={orderCompleteImg}></img>
                    }
                    { (status == "failure" || status == null) &&
                            <img src={paymentFailed} className="h-28"/>
                    }
                    { (status == "success" || status == "failure") &&
                            <h3>{data.message}</h3>
                    }
                    {status == null &&
                            <h3>Payment Failed</h3>
                    }                   
                    <h4>Please try again.</h4>
                </div>
            </div>
        </div>
    )
}

export default Status
