# TASK2-REST-API-CLIENT

COMPANY: CODETECH IT SOLUTIONS

NAME:ALLADA PAVAN KUMAR

INTERN ID:CT06DF1718

DOMAIN: JAVA PROGRAMMING

DURATION: 6 WEEKS

MENTOR:NEELA SANTHOSH KUMAR

DESCRIPTION: As part of my Java programming internship at Codetech IT Solutions, under the mentorship of Neela Santhosh, I was assigned Task-2: REST API Client. The objective was to write a Java application that consumes a public REST API and displays the fetched data in a structured format. This task introduced me to network programming in Java and helped me learn about handling HTTP requests and parsing JSON responses programmatically.

For this task, I used BlueJ as my primary development environment. BlueJ is a beginner-friendly, open-source Java IDE that allows easy management of projects and compilation and execution of Java programs. Its integrated terminal window and class structure diagram made it convenient to design, run, and test this Java-based API client.

The program begins by importing necessary Java classes such as java.net.HttpURLConnection, java.net.URL, java.io.BufferedReader, and java.io.InputStreamReader, which handle network connections and input streams from URLs. I selected https://jsonplaceholder.typicode.com/users/1 as a placeholder public REST API for this demonstration. This particular API returns JSON data representing user details, making it ideal for illustrating HTTP GET requests and JSON response handling.

The logic starts by creating a URL object with the desired API endpoint. I then opened an HTTP connection using HttpURLConnection and set the request method to GET. The program reads the response code to confirm a successful connection, and if it returns HTTP_OK (200), it reads the incoming data using a BufferedReader object. Each line of the response is appended to a StringBuffer object to form the full JSON response.

Once the data is completely read, the program displays it directly on the BlueJ terminal window. If any errors occur during the connection, they are caught using a try-catch block, and the stack trace is printed for debugging. Comments have been added at each step to explain the purpose of different code sections for clarity.

For syntax verification and minor improvements, I referred to Oracle’s official Java documentation for classes like HttpURLConnection and BufferedReader. Open-source educational websites such as GeeksforGeeks, W3Schools, and JavaPoint were valuable resources in understanding network programming concepts and JSON handling in Java.

OUTPUT

<img width="1015" height="116" alt="Image" src="https://github.com/user-attachments/assets/f9bb05e5-c9e0-49c9-a2ac-fb8261236b6c" />
