import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import './index.css'
import { RecoilRoot as RecoilBoot } from 'recoil'

ReactDOM.createRoot(document.getElementById('root')).render(
  <RecoilBoot>
    <App />
  </RecoilBoot>
)
