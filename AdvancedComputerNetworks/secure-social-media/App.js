import React, { useRef, useState } from 'react';
import './App.css';

import firebase from 'firebase/app';
import 'firebase/firestore';
import 'firebase/auth';
import 'firebase/analytics';

import { useAuthState } from 'react-firebase-hooks/auth';
import { useCollectionData,useDocumentData,useCollectionDataOnce } from 'react-firebase-hooks/firestore';

import CryptoJS from "crypto-js";

import 'bootstrap/dist/css/bootstrap.css';
import { Form } from 'react-bootstrap';
import { useBootstrapPrefix } from 'react-bootstrap/esm/ThemeProvider';

firebase.initializeApp({
  apiKey: "AIzaSyB-hmXieITMOxIqqQtYLuyVOYtD_je7yGA",
  authDomain: "social-media-8d0c6.firebaseapp.com",
  projectId: "social-media-8d0c6",
  storageBucket: "social-media-8d0c6.appspot.com",
  messagingSenderId: "335091159187",
  appId: "1:335091159187:web:7ef5d9399339923f3ebd66",
  measurementId: "G-GYXBV3BH2C"
})

const auth = firebase.auth();
const firestore = firebase.firestore();

// const users = firestore.collection("users");
// const messages = firestore.collection("messages");
// const groups = firestore.collection("groups");

let RSA = require('hybrid-crypto-js').RSA;
let Crypt = require('hybrid-crypto-js').Crypt;
const aes = CryptoJS.AES;
var rsa = new RSA();
var crypt = new Crypt();

const adminUID = "w0WqayWVGCgjtvYljB8x9AVi12A2";
let publicKeyUID = null;
let enPrivateKeyUID = null;
let privateKeyUID = null;

let publicKeyPost = null;
let enPrivateKeyPost = null;
let privateKeyPost = null;
let personalKeyPost = null;
let password = null
let deMessages = null;

// rsa.generateKeyPair(function(keyPair) {
//   // Callback function receives new key pair as a first argument
//   var publicKey = keyPair.publicKey;
//   var privateKey = keyPair.privateKey;
//   var ciphertext = CryptoJS.AES.encrypt(privateKey, 'test').toString();
//   console.log(publicKey);
//   console.log(ciphertext);

//   var bytes  = CryptoJS.AES.decrypt(ciphertext, 'test');
//   var originalText = bytes.toString(CryptoJS.enc.Utf8);

// console.log(originalText); // 'my message'
// });


function App() {

  const [user] = useAuthState(auth);
  const [adminView,setAdminView] = useState(false);
  const [uidVal, uidloading, uiderror] = useDocumentData(firestore.collection("keys").doc('uidKey'));
  if(uidVal) {
    publicKeyUID = uidVal.public;
    enPrivateKeyUID = uidVal.private;
    // var bytes  = CryptoJS.AES.decrypt(enPrivateKeyUID, 'test');
    // privateKeyUID = bytes.toString(CryptoJS.enc.Utf8);
  }
  const [postVal, postloading, posterror] = useDocumentData(firestore.collection("keys").doc('uidKey'));
  if(postVal) {
    publicKeyPost = postVal.public;
    enPrivateKeyPost = postVal.private;
    // var bytes  = CryptoJS.AES.decrypt(enPrivateKeyPost, 'test');
    // privateKeyPost = bytes.toString(CryptoJS.enc.Utf8);
  }

  const buttonHandler = () => {
    if(!adminView) {
    // if (password === null) {
      password = prompt("Please enter the admin password:", "");
      console.log(password)
      var bytes  = CryptoJS.AES.decrypt(enPrivateKeyPost, password);
      privateKeyPost = bytes.toString(CryptoJS.enc.Utf8);
    // }
    }
    setAdminView(current => !current)
  }

  return (
    <div className="App">
      <header>
        <h1>🔐 Secure Social Media 🔐</h1>
        
        {user && auth.currentUser.uid == adminUID ?  
          <Form.Switch 
            onChange={buttonHandler}
            type="switch"
            id="custom-switch"
            label="Admin Panel"
          />
      : <div/>}
        
        <SignOut />
      </header>

      <section>
        {
        (() => {
            if (!user)
                return <SignIn />
            if (adminView)
                return <AdminPanel/>
            else
                return <MessageBoard />
        })()
      }
      </section>

    </div>
  );
}

function SignIn() {
    const usersRef = firestore.collection('users');
    const query = usersRef.limit(25);
    const [users] = useCollectionData(query, { idField: 'id' });
    let userEmails = [];
    for(const x in users) {
      userEmails.push(users[x].email);
    }

  const signInWithGoogle = () => {
    const provider = new firebase.auth.GoogleAuthProvider();
    auth.signInWithPopup(provider);
    firebase.auth().onAuthStateChanged(function(user) {
      if (user) {
        if(userEmails.includes(user.email)) {
          console.log("User already exists")
          for(const x in users) {
            if(users[x].email == auth.currentUser.email) {
              if(users[x].encryptedPostKey != null) {
                var bytes  = CryptoJS.AES.decrypt(users[x].encryptedPostKey, auth.currentUser.uid);
                personalKeyPost = bytes.toString(CryptoJS.enc.Utf8);
              }
            }
          }
        }
        else {
          createUser()
        }
      }
    });
  }

  const createUser = async () => {
    const { photoURL,email,uid } = auth.currentUser;
    const encryptedUID = crypt.encrypt(publicKeyUID, uid);
    await usersRef.add({
      email,
      photoURL,
      encryptedUID,
    })

  }

  return (
    <>
      <button className="sign-in" onClick={signInWithGoogle}>Sign in with Google</button>
    </>
  )

}

function SignOut() {
  return auth.currentUser && (
    <button className="sign-out" onClick={() => auth.signOut()}>Sign Out</button>
  )
}


function MessageBoard() {
  const dummy = useRef();
  const messagesRef = firestore.collection('messages');
  const query = messagesRef.orderBy('createdAt').limit(25);

  const [messages] = useCollectionData(query, { idField: 'id' });

  const [formValue, setFormValue] = useState('');


  const sendMessage = async (e) => {
    e.preventDefault();

    const { email, photoURL } = auth.currentUser;
    //encrypt text
    const encryptedText = crypt.encrypt(publicKeyPost, formValue);
    await messagesRef.add({
      text: encryptedText,
      createdAt: firebase.firestore.FieldValue.serverTimestamp(),
      email,
      photoURL
    })

    setFormValue('');
    dummy.current.scrollIntoView({ behavior: 'smooth' });
  }
  

  return (<>
    <main>
      

      {messages && messages.map(msg => <ChatMessage key={msg.id} message={msg} />)}

      <span ref={dummy}></span>

    </main>

    <form onSubmit={sendMessage}>

      <input value={formValue} onChange={(e) => setFormValue(e.target.value)} placeholder="write message" />

      <button type="submit" disabled={!formValue}>⬆</button>

    </form>
  </>)
}


function ChatMessage(props) {
  let { text, email, photoURL } = props.message;
  if(personalKeyPost){
    var decrypted  = crypt.decrypt(personalKeyPost, text);
    text = decrypted.message;
  }


  const messageClass = email === auth.currentUser.email ? 'sent' : 'received';

  return (<>
    <div className={`message ${messageClass}`}>
      <img src={photoURL} alt="" />
      <p>{text}</p>
    </div>
  </>)
}

function AdminPanel(props) {
  const usersRef = firestore.collection('users');
  const query = usersRef.limit(25);

  const [users] = useCollectionData(query, { idField: 'id' });

  return (
    <>
      <main>
        <p>spacer</p>
        {users && users.map(msg => <UserMSG key={msg.id} message={msg} />)}
      </main>
    </>
  )
}

function UserMSG(props) {
  const { email, photoURL, encryptedUID,id } = props.message;
  const [giveAccess,setAccess] = useState(false);
  const AccessHandler = () => {
    if(giveAccess) {
      deleteUser();
    } else {
      updateUser();
    }
    setAccess(current => !current)
  }

  const updateUser = async () => {
  var bytes  = CryptoJS.AES.decrypt(enPrivateKeyUID, password);
  privateKeyUID = bytes.toString(CryptoJS.enc.Utf8);
  const privateUID = crypt.decrypt(privateKeyUID, encryptedUID).message;
  var encryptedPostKey = CryptoJS.AES.encrypt(privateKeyPost, privateUID).toString();
  await firestore.collection('users').doc(id).update({
    encryptedPostKey
  })

  }

  const deleteUser = async () => {
    // await firestore.collection('users').doc(id).delete();
    console.log("User deleted " + email)
  }

  return (<>
    <div className={`message received`}>
      <img src={photoURL} alt="" />
      <p>{email}</p>
      <div/>
      <Form.Switch 
            onChange={AccessHandler}
            type="switch"
            id="custom-switch"
            label="Give Access"
          />
    </div>
  </>)
}

export default App;
