# Android_Application

This is a single activity application made using MVVM architecture pattern with multiple fragments
1. Signup fragment
   - Where you can signup on firebase and can save user data locally
  
2. Signin fragment
   - Where you can signin, but if the user is already signed in it will redirect to home page
  
3. Home page
   - It has one recycler view with two buttons navigate to profile fragment and audio recording fragment

4. Audio recording fragment
   - It has two buttons for recording the audio and one for playing the audio
  
5. Profile fragment
   - Initially it shows user their email but has the logic for updating the user data
  
  Functionality
  - First time the user will be redirected to the signin fragment if the user is not signed in you can signup using the text below
  - If user is signed in it will redirect to home page
  - On home page first time the data will be loaded through API and will be saved in room database and than you can see the data without the internet or fetching through the API
  - Also has audio recording functionality
  - User data us saved through the shared preferences

Technologies
- Retrofit
- Room database
- Nav graph
- Hilt
- MVVM pattern
- Kotlin
- Firebase

  Link to the signed apk and video of app: https://drive.google.com/drive/folders/158G8jLxMzSrFiQ7knfOwXf0K0qV1QOW3
