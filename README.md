# ChatApp
### Chat Application Using WebSocket
**This project is a simple real-time chat application developed using Spring Boot.
It utilizes two different databases - MySQL for storing user information and MongoDB for storing chat messages.
This application also uses AES encryption to protect the confidentiality of messages by encrypting them in the database with a private key.
These private keys strengthen digital privacy by creating a unique layer of security for each pair of users. 
These keys are also generated with the usernames of each user pair to increase privacy.**

#### 1. Clone the repository:
    'git clone https://github.com/omerkincal/chatapp_with_websocket.git
    cd spring-boot-chat-application'

#### 2. Ensure that MongoDB and MySQL are installed on your machine. 
#### 3. If Maven is installed: 
**Open a terminal in the project directory and run:**
   'mvn spring-boot:run'
**Access the application through the browser at http://localhost:9090 after starting.**
#### 4. If Maven is not installed:
**Import the project into your preferred IDE (IntelliJ IDEA, Eclipse, etc.).**
**Run the main application file to start the application.
Access the application through the browser at http://localhost:9090 after starting.**


