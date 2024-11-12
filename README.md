# WeatherMotion
Weather Motion App Design Documentation for Milestone 1 

1. Main Idea 

Weather Motion is a Kotlin-based mobile application developed with Jetpack Compose for my BSC-MD module assignment. The main purpose of this app is to combine weather forecasting with device motion detection to provide a solution for people who may have difficulty with touch interfaces or struggle with traditional technology. 

By utilizing Android's sensors, the app detects the phone's position to trigger navigation and theme adjustments using accelerometer and gyroscope sensors with gentle tilt detection (the goal is to avoid asking the user to shake the phone but to help users with limited dexterity!). The app is designed for simplicity, with basic color branding (mainly white and no additional data) to help users easily view weather data, access position information, and navigate settings. 

 

2. Objectives 

The key objectives of the Weather Motion App are as follows: 

- Provide easy access to real-time weather data in a clear format. 

- Use device motion sensor to enhance user interaction, especially for those who may have difficulty navigating traditional touch interfaces. 

- Ensure user personalization through settings where users can update preferences and manage their account information. 

3. Design 

3.1 Design Overview 

The app consists of three main screens: Login, Home, and Settings. The design follows a clean and minimalistic approach, using a consistent color scheme to enhance usability and visual clarity. The layout is structured to ensure accessibility and ease of navigation with clear buttons, icons, and labels. 

 

 

3.2 Screen Components 

    Login Page 

The Login Page welcomes users with the app logo, a text input field for the user's name, and a log-in button. This simple layout allows the user to start using the app by entering their name. Upon successful login, the user is redirected to the Home Page. 

     Home Page 

The Home Page serves as the main screen, featuring two primary cards: one for weather data and another for position information. Both cards are visually distinct and provide essential data to the user. The page also includes a top and bottom app bar, allowing users to access the settings page or return to the Login Page easily. Additionally, the app detects device position to toggle theme and navigation. 

     Settings Page 

The Settings Page displays user account details like the name and allows for preference management or logging out. Extended floating action buttons are used to access each of these options, enabling users to manage their preferences and log out if desired. 

3.3 Navigation and Interaction 

The app utilizes Jetpack Compose's “NavController” for navigation between pages. A “NavHost “is set up to define the navigation routes between the Login, Home, and Settings pages. Interaction will be enhanced by the use of motion detection to adjust the user interface based on device orientation, providing a touchless navigation option for users with disabilities to improve accessibility and user experience. 