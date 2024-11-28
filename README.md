## Authors

- [Hansen Billy R](https://github.com/Hansen50)


## Stomazon Mobile Apps (Marketplace)

![Intro_banner_app](https://github.com/user-attachments/assets/09d56d83-fe95-45fb-ab02-3ff1be833430)

Find the Best Online Shopping Experience
with Stomazon – the marketplace where product quality, great prices, and fast service come together. From everyday essentials to exclusive items, Stomazon has it all. Browse the collection, enjoy amazing deals, and experience hassle-free shopping – all in one app! Join the smart shopping community with Stomazon today!

Stomazon is built using Kotlin and XML programming languages. Enjoy the amazing features and experience this app has to offer. Enjoy!!!


## Table of Contents
1. [Demo](#demo)
2. [Presentation Slide](#presentation-slide)
3. [Tech Stack](#tech-stack)
4. [Features](#features)
5. [Library](#libary)
6. [Clean Architecture](#clean-architectures)
7. [Design Pattern](#design-pattern)
8. [Dependency Injection](#dependency-injection)
9. [Prerequisites to Run This Project](#prerequisites-to-run-this-project)
10. [Installation](#installation)


## Demo


## Presentation Slide
[Presentation Slide](https://github.com/user-attachments/files/17919583/PRESENTATION._STOMAZON.pdf)

## Tech Stack
![Tech Stack Stomazon](https://github.com/user-attachments/assets/5d7d0d26-b49d-466e-953d-92b6c8294d10)

## Features
- **Onboard:** A page that introduces the app.
- **Login with Google Authentication:** With this feature, users can log in using Google sign in first.
- **Explore List Porduct:** Find & Explore products that match what you're looking for and choose from various categories.
- **Carts:** Add your dream products to the cart.
- **Checkout product:** Checkout based on the items they want or have listed.
- **Payment Method:** Users can make a payment and choose a payment method after checkout, with multiple payment options available.
- **Transaction Order History:** Users can view the transaction history of orders they’ve made, and the order will be displayed as successful once the payment is completed.

## Libary
- **Firebase Analytics:** A tool that helps in monitoring, tracking, prioritizing, and fixing errors in an app.
- **OkHttp:** A network library that handles HTTP requests and responses in Android and Java applications.
- **API Logging:** API logging interceptor in Kotlin is used to monitor and log the details of API requests and responses, such as URLs, headers, request/response bodies, status codes, and execution time. It helps developers diagnose issues, analyze API performance, and ensure security and audit compliance by capturing detailed logs of API interactions. This is typically implemented using OkHttp's HttpLoggingInterceptor and is often integrated into Retrofit or OkHttp clients.
- **Retrofit:** Used to communicate with a web API through HTTP, requires a network connection.
- **Firebase Authentication:** Provides a method that allows users to log in with their Google account.
- **Room:** Provides an abstraction over SQLite to allow developers to store data locally on Android devices in a simpler and more efficient way.
- **DataStore Preferences:** Save user preferences with DataStore, the latest technology for shared preferences.
- **Dagger Hilt:** Library that makes it easier to use dependency injection (DI) in Android apps in a more efficient and structured way.
- **Material 3 Design:** The basic foundation and materials used when building the user interface or design of an app.
- **Facebook Shimmer:** An open-source library developed by Facebook to provide a shimmer loading effect in Android applications. This shimmer effect is used to enhance the user experience while data is being loaded in the app.

## Clean Architectures
![clean_arch](https://github.com/user-attachments/assets/0e5efe19-91c1-4437-9f1f-9e7cea963757)

**Clean Architecture** is a software design principle used in **Stomazon Mobile Apps** that aims to separate the code into isolated layers, with the goal of simplifying maintenance, testing, and continuous development of the application. Clean Architecture emphasizes the separation of concerns and ensures that business logic and application dependencies are not tightly coupled to a specific UI or framework.

There is a clear separation between the layers of the application, known as the Presentation Layer, Domain Layer, and Data Layer, as follows:

- **Presentation:** This layer contains all the logic related to the display or management of the UI. For example, it includes Activities, Fragments, or ViewModels used to present data to the view and handle user input.

- **Domain:** This layer forms the core business logic of the application. The Domain Layer contains Use Cases that define the rules and higher-level business processes of the application.

- **Data:** This layer is responsible for managing and providing the data needed by the application. The Data Layer interacts with external data sources (such as APIs, local databases, or files) and provides data to the Domain Layer through the Repository.

## Design Pattern
![mvvm](https://github.com/user-attachments/assets/fd8d6f2a-2aea-470c-b986-0afd1aae1431)

**MVVM (Model-View-ViewModel)** is a design pattern used in the development of **Stomazon Mobile Apps**, particularly in user interface (UI)-based applications like Android apps. MVVM separates the application code into three main components, each with distinct responsibilities:

- **Model:** This component represents the data and business logic of the application. The Model is responsible for accessing data, such as fetching data from a database or API, and processing it.
  
- **View:** The View is the user interface (UI) that displays data to the user and handles user interactions (such as button clicks, text input, etc.). The View does not contain business logic; it is focused solely on the display and management of UI elements.
  
- **ViewModel:** The ViewModel acts as a mediator between the Model and the View. It receives data from the Model and transforms it into a format that can be used by the View. The ViewModel also handles presentation logic (such as calculations or data manipulation) and manages the UI state. The ViewModel does not directly know about the View, making the code more testable and decoupled from the UI.

## Dependency Injection
Using Dependency Injection in Android apps helps make the app easier to test, maintain, and develop, with better and more structured object management.

## Prerequisites to Run This Project
To build and run the project locally, ensure you have the following installed:

- **Java 8**
- **Gradle 7.8**
- **Android Studio**

**Clone Project**
```bash
gh repo clone Hansen50/
```

**Build the Debug APK**
```bash
./gradlew assembleDebug
```

**Important Notes**
- It is recommended to build the project using Android Studio for a better development experience. Before generating the APK, you need to set up your own Firebase project and add the google-services.json file, as this project requires Firebase. Additionally, make sure to configure the SHA-1 key.

## Installation
The minimum device requirements to run this application are Android 10 (API 29).



