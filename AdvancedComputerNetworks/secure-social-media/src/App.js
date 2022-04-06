import './App.css';

// Import the functions you need from the SDKs you need
import { initializeApp } from "https://www.gstatic.com/firebasejs/9.6.10/firebase-app.js";
import { getAnalytics } from "https://www.gstatic.com/firebasejs/9.6.10/firebase-analytics.js";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyB-hmXieITMOxIqqQtYLuyVOYtD_je7yGA",
  authDomain: "social-media-8d0c6.firebaseapp.com",
  projectId: "social-media-8d0c6",
  storageBucket: "social-media-8d0c6.appspot.com",
  messagingSenderId: "335091159187",
  appId: "1:335091159187:web:7ef5d9399339923f3ebd66",
  measurementId: "G-GYXBV3BH2C"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);

function App() {
  return (
    <div className="App">

    </div>
  );
}

export default App;
