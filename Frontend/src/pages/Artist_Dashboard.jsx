import { useNavigate } from 'react-router-dom';
import { useEffect } from 'react';

export default function Dashboard() {
    const navigate = useNavigate();
    
    useEffect(() => {
      if (!localStorage.getItem('token')) {
        navigate('/');
      }
    }, [navigate]);

    useEffect(() => {
      document.title = "Artist Dashboard";
      
    }, []);
  
    return (
      <div>
      <div className="p-5 flex justify-between items-center bg-black text-white" >
        <div className="flex items-center">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-6 h-6 text-white">
              <path strokeLinecap="round" strokeLinejoin="round" d="M9.53 16.122a3 3 0 0 0-5.78 1.128 2.25 2.25 0 0 1-2.4 2.245 4.5 4.5 0 0 0 8.4-2.245c0-.399-.078-.78-.22-1.128Zm0 0a15.998 15.998 0 0 0 3.388-1.62m-5.043-.025a15.994 15.994 0 0 1 1.622-3.395m3.42 3.42a15.995 15.995 0 0 0 4.764-4.648l3.876-5.814a1.151 1.151 0 0 0-1.597-1.597L14.146 6.32a15.996 15.996 0 0 0-4.649 4.763m3.42 3.42a6.776 6.776 0 0 0-3.42-3.42" />
            </svg>
            <button className="pl-3 hover:text-blue-400 hover:underline"onClick={()=>navigate('/')}>Artist Marketplace</button>
        </div>
        <div className="flex items-center space-x-4 ">
            <button className="mr-3  hover:text-blue-400 hover:underline" onClick={()=>{
                  navigate('/artist_insight');
                }}>Insights</button>
            <button className="mr-3  hover:text-blue-400 hover:underline" onClick={()=>{
              localStorage.clear();
              navigate('/');
            }}>Sign out</button>
        </div>
    </div>
    <h1 className="text-3xl font-bold underline">
      This is the  Artist Dashboard Page
    </h1>
    <button className="btn btn-primary" onClick={()=>navigate('/artist_profile')}>Edit Profile</button>
    </div>
    
    )
}