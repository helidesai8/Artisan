import { useEffect, useState } from "react";
import { React } from 'react';
import ArtistCategoryInsight from "../Components/Artist_Sale_Insight_Components/ArtistCategoryInsight";
import ArtistMonthlyInsight from "../Components/Artist_Sale_Insight_Components/ArtistMonthlyInsight";
import ArtistCategoryRating from "../Components/Artist_Sale_Insight_Components/ArtistCategoryRating";
import { getArtistInsight } from "../functions/getArtistInsight";
import { json } from "react-router-dom/dist";
import { useNavigate } from 'react-router-dom';
import logo from "../assets/logo.png"

function ArtistDashBoard() 
{
  const navigate = useNavigate();
    
  useEffect(() => {
    if (!localStorage.getItem('token')) {
      navigate('/');
    }
  }, [navigate]);
  useEffect(() => {
    document.title = "Artist Dashboard";
    
  }, []);

  const [yearlyData, setYearlyData] = useState([]);
  const [monthlyData, setMonthlyData] =useState([]);
  const [categoryData, setCategoryData] = useState([]);
  const [categoryRating, setCategoryRating] = useState([]);


  useEffect(() => {
    getArtistInsight().then((data) => {
      if(data){
        setCategoryData(data.categoryInsights);
        setYearlyData(data.yearlyInsights);
        if(data.yearlyInsights.length > 0){
          setMonthlyData(data.yearlyInsights[0].monthlyInsight);
        }  
        setCategoryRating(data.categoryRating);
      }
    });
  }, []);

  let _onSelect =(e)=> {
        if (e.target.value !== '') {    
            let yearlyInsight = yearlyData.find(t => t.year == e.target.value);
            if(yearlyInsight != undefined){
                setMonthlyData(yearlyInsight.monthlyInsight);
            }
        } 
    }
  return (
  <div>
     <nav className='navbar sticky top-0 left-0 w-full bg-white z-50 h-7 border-b'>
      <div className="nav-logo-container pl-7">
        <button onClick={()=>navigate('/')}>
          <img src={logo} alt="" className='h-18 w-20'/>
          </button>
      </div>
      <div className="navbar-links-container pr-6">
      <button onClick={()=>navigate('/artist_profile')} style={{color:"#000"}} class="text-gray-800 hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2">Profile</button>
          <button onClick={()=>navigate('/artist/dashboard')} style={{color:"#000"}} class="text-gray-800 hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2">Dashboard</button>
          <button style={{color:"#000"}} href="#" class="text-gray-800 dark:text-white hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2 p-5"
                  onClick={()=>{localStorage.clear();
                      navigate('/');}}>Logout</button>
      </div>
    </nav>
    <div className="flex flex-col w-full">
      <div className="card-insight rounded-box place-items-center">
        <ArtistCategoryInsight data = {categoryData}></ArtistCategoryInsight>
      </div>
      <div className="divider self-center w-2/5"></div>
      <div className="card-insight rounded-box place-items-center">
        <ArtistCategoryRating data = {categoryRating}></ArtistCategoryRating>
      </div>
      <div className="divider self-center w-2/5"></div> 
      <div className="flex self-center w-2/5 ">
        <div className="grid flex-grow card-insight place-items-center">
          { yearlyData?.length > 0 &&
            <label className="form-control w-full max-w-xs">
              <div className="label self-center">
                <span className="label-text">Select Year of Sale</span>
              </div>
              <select className="select select-bordered w-full max-w-xs" onChange={_onSelect}>
                    {yearlyData.map((data) => <option key={data.year} value={data.year}>{data.year}</option>)}
              </select>
            </label>
          }
          <ArtistMonthlyInsight data ={monthlyData}></ArtistMonthlyInsight>
        </div>
      </div>
    </div>
    </div>
  )
}



export default ArtistDashBoard;