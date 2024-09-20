import { Suspense, lazy } from 'react'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import './App.css'
import { useSetRecoilState } from 'recoil'
import { Loggedin_User } from './atoms/Loggedin_User_Atom'
const LandingPage = lazy(() => import('./pages/Landing'))
const RegisterPageForBuyer = lazy(() => import('./pages/Register_For_Buyer'))
const LoginPageForBuyer = lazy(() => import('./pages/Login_For_Buyer'))
const DashboardPageForBuyer = lazy(() => import('./pages/Buyer_Dashboard'));
const DashboardPageForArtist = lazy(() => import('./pages/Artist_Dashboard'));
const RegisterPageForArtist = lazy(() => import('./pages/Register_For_Artist'));
const LoginPageForArtist = lazy(() => import('./pages/Login_For_Artist'));
const ArtistInsightDashBoard = lazy(() => import('./pages/Artist_Insight_Dashboard'));
const ArtistProfile = lazy(() => import('./pages/Artist_Profile'));
const Status = lazy(() => import('./Components/Payment_Components/status'));
const Success = lazy(() => import('./Components/Payment_Components/successCallback'))
const OrderHistoryPage = lazy(() => import('./pages/Order_History'));

const ArtistProfileForUser = lazy(() => import('./pages/ArtistProfileUser'))
const PageNotFound = lazy(() => import('./pages/PageNotFound'))
import { useEffect } from 'react'
import { fetchUserData } from './functions/LoggedInUser'
import { RecoilRoot as RecoilBoot } from 'recoil'
import Dashboard from './Components/Artist/Dashboard'
import AddProduct from './Components/Artist/AddProduct'
import UpdateProduct from './Components/Artist/UpdateProduct'
import Cart from './pages/Cart'
import ProductInfo from './Components/Product_Details_Components/ProductInfo'
const UserProfile = lazy(() => import('./pages/User_Profile'));
import Footer from './Components/Landing_Page_Components/Footer'


const Loader = () => (
  <div className="flex items-center justify-center h-screen">
    <span className="loading loading-spinner loading-lg"></span>
  </div>
);

function App() {

  const setUser = useSetRecoilState(Loggedin_User);

  

  return (
    <>
    
    <BrowserRouter>
    <Suspense fallback={<Loader />}>

      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/register" element={<RegisterPageForBuyer />}/>
        <Route path="/login" element={<LoginPageForBuyer />} />
        <Route path="/dashboard" element={<DashboardPageForBuyer/>}/>
        <Route path="/dashobard_artist" element={<DashboardPageForArtist/>}/>
        <Route path="/register_artist" element={<RegisterPageForArtist/>}/>
        <Route path="/login_artist" element={<LoginPageForArtist/>}/>
        <Route path="/artist_insight" element={<ArtistInsightDashBoard/>}/>
        <Route path="/artist_profile" element={<ArtistProfile/>}/>
        <Route path="/artist/dashboard" element={<Dashboard/>}/>
        <Route path="/artist/dashboard/add-product" element={<AddProduct/>}/>
        <Route path="/artist/dashboard/update-product/:id" element={<UpdateProduct/>}/>
        <Route path="/cart" element={<Cart/>}/>
        <Route path="/user_profile" element={<UserProfile/>}/>
        <Route path="/success" element={<Success/>}/>
        <Route path="/status" element={<Status/>}/>
        <Route path="/order_history" element={<OrderHistoryPage/>}/>

        <Route path="/artist/:artistId" element={<ArtistProfileForUser />} />
        <Route path="*" element={<PageNotFound/>} />

        <Route path="/products/:id" element={<ProductInfo/>}></Route>
      
      </Routes>
      </Suspense>
      </BrowserRouter>
      
      <div className="bg-white">
    <Footer/>
    </div>
    </>
    
  )
}

export default App
