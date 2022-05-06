# Overview
I developed a simple Social Media messaging board using the React.js Javascript framework. The Application backend is built using Firebase, and Firestore NoSQL database. Users are able to login with a Google account and can view messages in the group. Messages will appear as ciphertext unless the user has explicitly been added to the group by the admin. All messages are encrypted using a public RSA key before being published to the database. The private decryption key is securely distributed to trusted users so that they can read messages.
Users and Functionality
When a user logs in for the first time they must use a Google account, anyone who logs in with a new account can only view messages as cipher text. When a new user logs in, the SignIn() function creates a new user object and is added to the Firestore database. This user object contains two elements, their email to link them to their sent messages, and a RSA encrypted copy of their UID used to track permissions. The private key to decrypt this UID is only available to the Admin user. 

The main functionality of the message board is implemented in the MessageBoard component. It calls the 25 most recent messages and attempts to decrypt them if it has the correct key, it then displays the ChatMessage which includes the text and users profile photo. MessageBoard contains a form that allows the user to input a new message, when submitted it calls SendMessage() which posts the new message to the database. 

If the logged in account matches the saved admin UID, and they correctly input the admin password they will be given the option to switch between the MessageBoard and AdminPanel  pages. The AdminPanel allows the admin to see all users who have signed up, with a toggle that gives the option to trust users with the post key or remove their access.

When a user is trusted, addUser() AES encrypts the  post key using that user's UID as the symmetric key, as only the user and admin have access to the UID. The encrypted post key is then added to that user's file. The next time the user accesses the application, the post key can automatically be used to decrypt messages. If the admin decides a user can no longer be trusted, deleteUser() can remove the user by deleting their file in Firestore, removing their access to the post key.

## Encryption and Key Management
Key management is handled using the hybrid-crypto-js library. This offers functionality for AES encryption, and the generation of RSA key pairs. 

There are two available public keys in the client application. The first is publicKeyUID, which is used to encrypt the user's UID when their user profile is being created, the private key is only accessible to the admin. The Second is publicKeyPost which allows any user to encrypt messages. 

AdminPanel checks if a user matches the admin UID or otherwise attempts to access the admin page, they are prompted to enter the admin password. While the UID check acts as a first line of defense, it's the admin password that is the AES encryption key to unlock the private keys stored in the application. These are privateKeyUID and privateKeyPost, the keys that allow the admin to access other users UIDs and the original Post key. As described above, the admin will then encrypt the privateKeyPost with AES before adding it to the database. 

## Backend Functionality
Firestore is used as the backend of the application, and adds its own layer of security. Firestore encrypts all data automatically with AES 256 bit encryption before it is written to the disk. Data is automatically decrypted for valid users. Encryption keys are further encrypted by a rotating set of master keys. While the firestore database can be accessed by a user with a valid Google account, further client side encryption provides maximum security.

The database  contains tree collections. In the users collection, which tracks all enrolled users, and their data as described above. The admin user can access a list of all users, while any other user can only access their own user data. The second collection contains all posted messages, with the text encrypted in the database, any user can view all messages but may not be able to decrypt its contents. The final collection holds the current public keys and encrypted private keys.




# Getting Started with Create React App

This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

## Available Scripts

In the project directory, you can run:

### `npm start`

Runs the app in the development mode.\
Open [http://localhost:3000](http://localhost:3000) to view it in your browser.

The page will reload when you make changes.\
You may also see any lint errors in the console.

### `npm test`

Launches the test runner in the interactive watch mode.\
See the section about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.

### `npm run build`

Builds the app for production to the `build` folder.\
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.\
Your app is ready to be deployed!

See the section about [deployment](https://facebook.github.io/create-react-app/docs/deployment) for more information.

### `npm run eject`

**Note: this is a one-way operation. Once you `eject`, you can't go back!**

If you aren't satisfied with the build tool and configuration choices, you can `eject` at any time. This command will remove the single build dependency from your project.

Instead, it will copy all the configuration files and the transitive dependencies (webpack, Babel, ESLint, etc) right into your project so you have full control over them. All of the commands except `eject` will still work, but they will point to the copied scripts so you can tweak them. At this point you're on your own.

You don't have to ever use `eject`. The curated feature set is suitable for small and middle deployments, and you shouldn't feel obligated to use this feature. However we understand that this tool wouldn't be useful if you couldn't customize it when you are ready for it.

## Learn More

You can learn more in the [Create React App documentation](https://facebook.github.io/create-react-app/docs/getting-started).

To learn React, check out the [React documentation](https://reactjs.org/).

### Code Splitting

This section has moved here: [https://facebook.github.io/create-react-app/docs/code-splitting](https://facebook.github.io/create-react-app/docs/code-splitting)

### Analyzing the Bundle Size

This section has moved here: [https://facebook.github.io/create-react-app/docs/analyzing-the-bundle-size](https://facebook.github.io/create-react-app/docs/analyzing-the-bundle-size)

### Making a Progressive Web App

This section has moved here: [https://facebook.github.io/create-react-app/docs/making-a-progressive-web-app](https://facebook.github.io/create-react-app/docs/making-a-progressive-web-app)

### Advanced Configuration

This section has moved here: [https://facebook.github.io/create-react-app/docs/advanced-configuration](https://facebook.github.io/create-react-app/docs/advanced-configuration)

### Deployment

This section has moved here: [https://facebook.github.io/create-react-app/docs/deployment](https://facebook.github.io/create-react-app/docs/deployment)

### `npm run build` fails to minify

This section has moved here: [https://facebook.github.io/create-react-app/docs/troubleshooting#npm-run-build-fails-to-minify](https://facebook.github.io/create-react-app/docs/troubleshooting#npm-run-build-fails-to-minify)
