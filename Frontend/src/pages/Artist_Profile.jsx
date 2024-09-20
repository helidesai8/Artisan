import React, { useEffect,useState} from 'react';

import { fetchProfileData, saveProfile, saveProfileImage } from '../functions/fetchArtistProfileData';


import { artistProfileState } from '../atoms/Artist_Profile_Atoms/artistProfileAtom';

import { avatarSource} from '../atoms/Artist_Profile_Atoms/avatarScoureAtom';
import { useRecoilState } from 'recoil';
import { cityAtom } from '../atoms/Artist_Profile_Atoms/cityAtom';
import { oneLinerAtom } from '../atoms/Artist_Profile_Atoms/oneLinerAtom';
import { artistStoryAtom } from '../atoms/Artist_Profile_Atoms/artistStoryAtom';
import { storyPhotosAtom } from '../atoms/Artist_Profile_Atoms/storyPhotosAtom';

import { saveArtistStory } from '../functions/fetchArtistProfileData';
import { saveStoryImages } from '../functions/fetchArtistProfileData';
import { deleteStoryImages } from '../functions/fetchArtistProfileData';
import { deleteProfileImage } from '../functions/fetchArtistProfileData';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { set } from 'zod';
import {useNavigate} from 'react-router-dom';
import logo from "../assets/logo.png"







const ArtistProfile = () => {
  const navigate = useNavigate();
  useEffect(() => {
    if (!localStorage.getItem('token')) {
      navigate('/login');
    }
    if(localStorage.getItem('role') != 'artist') {
      navigate('/login');
    }
  }, [navigate]);
   const [artistProfile, setArtistProfileState] = useRecoilState(artistProfileState);
   const [lastUpdate, setLastUpdate] = useState(Date.now());
   const [fileNames, setFileNames] = useRecoilState(storyPhotosAtom);
   const [city, setCity] = useRecoilState(cityAtom);
   const [selectedAccordion, setSelectedAccordion] = useState(1);
   const [artistStory, setArtistStory] = useRecoilState(artistStoryAtom);
   const [avatarFile, setAvatarFile] = useState(null);
   const [files, setFiles] = useState(null);
   const defaultImage="data:image/png;base64,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";
   const [avatarSrc, setAvatarSrc] = useRecoilState(avatarSource);
  
   
   useEffect(() => {
     const fetchData = async () => {
       const data = await fetchProfileData();
       
       setArtistProfileState(data);
       setCity(data.city);
       setOneLiner(data.aboutMe);
       if(data.story){
         setArtistStory(data.story.story);
         setFileNames(data.story.storyImageUrls);
        }
        const fileExtension = data.profileImageUrl.split('.').pop();

        
          if (data.profileImageUrl) {
            
          setAvatarSrc(data.profileImageUrl);
        }
        
        
      };
      
      fetchData();
    }, [lastUpdate]);
    
    
    const handleAccordionChange = (index) => {
    
    setSelectedAccordion(index === selectedAccordion ? null : index);
    
  };


  const handleFileChange = (e) => {
    const file = e.target.files[0];

    if (file) {
      setAvatarFile(file);
      const reader = new FileReader();

      reader.onloadend = () => {
        setAvatarSrc(reader.result);
      };

      reader.readAsDataURL(file);
    }
  };

    
    
    const handleCityChange = (e) => {
        
        setCity(e.target.value);
    };

    const [oneLiner, setOneLiner] = useRecoilState(oneLinerAtom);
    const handleOneLiner = (e) => {
      const oneLiner1=e.target.value;
      setOneLiner(oneLiner1);
      console.log(oneLiner1);  
  }


  const handleStoryChange = (e) => {
    const story=e.target.value;
    setArtistStory(story);
    
};

const handleFilesChange = async (e) => {
  setFiles(e.target.files);
};





const ProfileSaveClick = async (oneLiner, city) => {
  try {
    const response = await saveProfile(oneLiner, city);
    
    toast.success('Profile saved successfully');
  } catch (error) {
    
    toast.error('Error saving profile');
  }
}

const ArtistStorySaveClick = async (artistStory) => {
  try{
    if (!artistStory) {
      
      toast.error('Please provide a story.');
      return;
    }
    const response = await saveArtistStory(artistStory);
    toast.success('Story saved successfully');
  }catch (error) {
    toast.error('Error saving story');
  }
 
}

const ArtistProfilePhotoSaveClick = async (avatarFile) => {
  try{
    console.log('ArtistProfilePhotoSaveClick called with:');

    const response= await saveProfileImage(avatarFile);
    if(response.data)
      {
        const proileImageName=response.data.profileImageName;
        setAvatarSrc('data:image/jpeg;base64,' + proileImageName);
        toast.success('Profile image saved successfully');
      }
  }
  catch (error) {
    toast.error('Error saving profile image');
  }

}

const ArtistStoryPhotosSaveClick = async (artistStory) => {
  if (!artistStory) {
      
    toast.error('Please provide a story.');
    return;
  }
  if(files && files.length > 0){
    try {
      const response = await saveStoryImages(files);
      toast.success('Story images saved successfully');
    }catch (error) {
      toast.error('Error saving story images');
    }  
  }
}


const StoryImagesDeleteClick = async () => {
  try {
    await deleteStoryImages();
    toast.success('Story images deleted successfully');
    setFiles(null);
    setFileNames([]);
    window.location.reload();    
  } catch (error) {
    toast.error('Error deleting story images');
  }
}

const ProfileImageDeleteClick = async () => {
  try {
     await deleteProfileImage();
    toast.success('Profile image deleted successfully');
    setAvatarSrc(null);
    
  } catch (error) {
    toast.error('Error deleting profile image');
  }
}





  return (
    <>
    <nav className='navbar fixed top-0 left-0 w-full bg-white z-50 h-7 border-b'>
      <div className="nav-logo-container pl-7">
      <button onClick={()=>navigate('/')}>
          <img src={logo} alt="" className='h-18 w-20'/>
      </button>
      </div>
      <div className="navbar-links-container pr-6">
          <button onClick={()=>navigate('/artist_insight')} style={{color:"#000"}} class="text-gray-800 hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2">Insights</button>
          <button onClick={()=>navigate('/artist/dashboard')} style={{color:"#000"}} class="text-gray-800 hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2">Dashboard</button>
          <button style={{color:"#000"}} href="#" class="text-gray-800 dark:text-white hover:bg-gray-50 focus:ring-4 focus:ring-gray-300 font-medium rounded-lg text-sm px-4 lg:px-5 py-2 lg:py-2.5 mr-2 p-5"
                  onClick={()=>{localStorage.clear();
                      navigate('/');}}>Logout</button>
      </div>
    </nav>
    <div className="flex  mt-16 items-center justify-center min-h-screen overflow-y-auto">
      <div className="max-h-full overflow-y-auto">
      <ToastContainer />

        <div className="join join-vertical w-full mb-6 ">
          <div className="collapse collapse-arrow join-item border  border-base-300 ">
            <input
              type="radio"
              name="my-accordion-4"
              id="profile"
              checked={selectedAccordion === 1}
              onChange={() => handleAccordionChange(1)}
            />
            <label htmlFor="profile" style={{ backgroundColor: 'oklch(0.235742 0.066235 313.19)' }} className="collapse-title text-xl font-medium text-white ">
              Your Profile
            </label>
            
            <div className={`collapse-content ${selectedAccordion === 1 ? 'open' : ''} overflow-y-auto`}>
                <div className="flex">
                    <div className="flex-1 h-full">
                    <span className="label-text-lg  mt-4 text-center block">Upload your profile photo</span>

                        <div className="avatar mt-4 flex flex-col items-center justify-center">
                          <div className="w-32 rounded-full overflow-hidden">
                            <img src={avatarSrc || (artistProfile.profileImageUrl ? defaultImage : avatarSrc)} alt="Avatar" className="w-full h-full object-cover"  />
                          </div>
                        </div>
                        <div className="mt-4 flex items-center justify-center">
                          <input type="file" className="file-input file-input-bordered file-input-md w-full max-w-xs" onChange={handleFileChange} accept="image/png, image/gif, image/jpeg" />
                        </div>
                        <div className="flex  mt-4 justify-end gap-4">
                        <button className="btn btn-outline  btn-error" onClick={ProfileImageDeleteClick}>
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" className="w-6 h-6">
                        <path strokeLinecap="round" strokeLinejoin="round" d="m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 0 0-7.5 0" />
                        </svg>
                        Delete
                        </button>
                        <button className="btn btn-outline btn-success " onClick={() => ArtistProfilePhotoSaveClick(avatarFile)}>
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" className="w-6 h-6">
                                <path strokeLinecap="round" strokeLinejoin="round" d="M9 3.75H6.912a2.25 2.25 0 0 0-2.15 1.588L2.35 13.177a2.25 2.25 0 0 0-.1.661V18a2.25 2.25 0 0 0 2.25 2.25h15A2.25 2.25 0 0 0 21.75 18v-4.162c0-.224-.034-.447-.1-.661L19.24 5.338a2.25 2.25 0 0 0-2.15-1.588H15M2.25 13.5h3.86a2.25 2.25 0 0 1 2.012 1.244l.256.512a2.25 2.25 0 0 0 2.013 1.244h3.218a2.25 2.25 0 0 0 2.013-1.244l.256-.512a2.25 2.25 0 0 1 2.013-1.244h3.859M12 3v8.25m0 0-3-3m3 3 3-3" />
                            </svg>
                        Save
                        </button>
                    </div>
                    </div>
                    <div className="flex-1  flex flex-col items-center justify-center ">
                          <label className="form-control w-full max-w-xs">
                              <div className="label">
                                  <span className="label-text">First Name</span>
                              </div>
                              <input type="text" placeholder="Type here" className="input input-bordered w-full max-w-xs" value={artistProfile.firstName} disabled/>            
                          </label>

                          <label className="form-control w-full max-w-xs">
                              <div className="label">
                                  <span className="label-text">Last Name </span>
                              </div>
                              <input type="text" placeholder="Type here" className="input input-bordered w-full max-w-xs"  disabled value={artistProfile.lastName}/>            
                          </label>
                    </div>
                </div>
                <div className="mt-6 flex ">
                <label className="form-control w-full max-w-xs">
                    <div className="label">
                        <span className="label-text">City </span>
                    </div>
                    <input type="text" placeholder="Enter your city" className="input input-bordered w-full " value={city || ' '} onChange={(e)=>setCity(e.target.value)} />

                    
           
                </label>
                
                </div>
                    <label className="form-control mt-6">
                                <div className="label">
                                    <span className="label-text">About me </span>
                                </div>
                                <textarea className="textarea textarea-bordered h-12"  onChange={handleOneLiner} value={oneLiner ||' ' }placeholder="Enter something about yorself"></textarea>
                    </label>
                    <div className="flex  mt-4 justify-end gap-4">
               
                  <button className="btn btn-outline btn-success"  onClick={() => ProfileSaveClick(oneLiner,city)} >
                   <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" className="w-6 h-6">
                       <path strokeLinecap="round" strokeLinejoin="round" d="M9 3.75H6.912a2.25 2.25 0 0 0-2.15 1.588L2.35 13.177a2.25 2.25 0 0 0-.1.661V18a2.25 2.25 0 0 0 2.25 2.25h15A2.25 2.25 0 0 0 21.75 18v-4.162c0-.224-.034-.447-.1-.661L19.24 5.338a2.25 2.25 0 0 0-2.15-1.588H15M2.25 13.5h3.86a2.25 2.25 0 0 1 2.012 1.244l.256.512a2.25 2.25 0 0 0 2.013 1.244h3.218a2.25 2.25 0 0 0 2.013-1.244l.256-.512a2.25 2.25 0 0 1 2.013-1.244h3.859M12 3v8.25m0 0-3-3m3 3 3-3" />
                   </svg>
               Save
                </button>
              </div>
                
            </div>
          
          </div>
        </div>
        
        <div className="join join-vertical w-full">
          <div className="collapse collapse-arrow join-item border border-base-300">
            <input
              type="radio"
              name="my-accordion-4"
              id="story"
              checked={selectedAccordion === 2}
              onChange={() => handleAccordionChange(2)}
            />
            <label htmlFor="story"  style={{ backgroundColor: 'oklch(0.235742 0.066235 313.19)' }} className="collapse-title text-xl font-medium  text-white">
              Your Story
            </label>
            <div className={`collapse-content ${selectedAccordion === 2 ? 'open' : ''}`}>
                    <label className="form-control">
                            <div className="label">
                                <span className="label-text">Enter your story in given box below: </span>
                            </div>
                            <textarea value={artistStory || ''} onChange={handleStoryChange} className="textarea textarea-bordered h-24" placeholder="Maximum 250 characters allowed." required></textarea>
                    </label>
                <div className="flex  mt-4 justify-end gap-4">
               
                      <button className="btn btn-outline btn-success" onClick={() => ArtistStorySaveClick(artistStory)} >
                          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" className="w-6 h-6">
                              <path strokeLinecap="round" strokeLinejoin="round" d="M9 3.75H6.912a2.25 2.25 0 0 0-2.15 1.588L2.35 13.177a2.25 2.25 0 0 0-.1.661V18a2.25 2.25 0 0 0 2.25 2.25h15A2.25 2.25 0 0 0 21.75 18v-4.162c0-.224-.034-.447-.1-.661L19.24 5.338a2.25 2.25 0 0 0-2.15-1.588H15M2.25 13.5h3.86a2.25 2.25 0 0 1 2.012 1.244l.256.512a2.25 2.25 0 0 0 2.013 1.244h3.218a2.25 2.25 0 0 0 2.013-1.244l.256-.512a2.25 2.25 0 0 1 2.013-1.244h3.859M12 3v8.25m0 0-3-3m3 3 3-3" />
                          </svg>
                      Save
                      </button>
                </div>
                <label className="label-text mb-2">Upload images to depict your story to the audience</label>
                <input type="file"  onChange={handleFilesChange} className="mt-2 file-input file-input-bordered file-input-md w-full max-w-s" multiple accept="image/png, image/gif, image/jpeg"  />
                {files && files.length > 1 && (
                <div className="mt-2">
                  <p className="text-sm text-black-100">
                    Files selected: {Array.from(files).map(file => file.name).join(', ')}
                  </p>
                </div>
                )}
                {/* {fileNames && fileNames.length > 0 && (
                  <div className="mt-2">
                    <p className="text-sm text-black-100">
                      Stored files: {fileNames.join(', ')}
                    </p>
                  </div>
                )} */}
                <div className="mt-3 text-sm text-black-100 italic pt-3" id="file_input_help">
                  <div>Only PNG, JPG, GIF supported.This is a multiple file input, so you can select multiple files.</div>
                  <div>Story is required to save images.</div>
                </div>

               <div className="flex  mt-4 justify-end gap-4">
                <button className="btn btn-outline  btn-error" onClick={StoryImagesDeleteClick}>
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" className="w-6 h-6">
                <path strokeLinecap="round" strokeLinejoin="round" d="m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 0 0-7.5 0" />
                </svg>
                Delete
                </button>
                <button className="btn btn-outline btn-success" onClick={() => ArtistStoryPhotosSaveClick(artistStory)}>
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" className="w-6 h-6"  >
                        <path strokeLinecap="round" strokeLinejoin="round" d="M9 3.75H6.912a2.25 2.25 0 0 0-2.15 1.588L2.35 13.177a2.25 2.25 0 0 0-.1.661V18a2.25 2.25 0 0 0 2.25 2.25h15A2.25 2.25 0 0 0 21.75 18v-4.162c0-.224-.034-.447-.1-.661L19.24 5.338a2.25 2.25 0 0 0-2.15-1.588H15M2.25 13.5h3.86a2.25 2.25 0 0 1 2.012 1.244l.256.512a2.25 2.25 0 0 0 2.013 1.244h3.218a2.25 2.25 0 0 0 2.013-1.244l.256-.512a2.25 2.25 0 0 1 2.013-1.244h3.859M12 3v8.25m0 0-3-3m3 3 3-3" />
                    </svg>
                Save
                </button>
            </div>
             
            
            </div>
            
          </div>
        </div>
        </div>
      </div>
      </>
    
  );
};

export default ArtistProfile;