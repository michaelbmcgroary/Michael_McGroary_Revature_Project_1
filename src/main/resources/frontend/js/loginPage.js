"use strict";
let loginURL = "http://localhost:7000/login";
let submitButton = document.getElementById('submissionButton');
let successfulLogin = false;



//button.addEventListener('click', sendTestGetRequest);
submitButton.addEventListener('click', login);


function login(evt) {
    evt.preventDefault();
    let un = document.getElementById("usernameInput").value;
    let pw = document.getElementById("passwordInput").value;

    let postData = {
        username: un,
        password: pw
    };

    console.log(postData);

    fetch('http://localhost:7000/login', {
        method: 'POST',
        credentials: 'include', // this specifies that when you receive cookies, you should include them in future requests. So in our case, it's important so that the backend can identify if we are logged in or not.
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(postData)
    }).then((data) => {
        if (data.status === 200) {
            successfulLogin = true;
        } else /*if (response.status === 400)*/ {
            successfulLogin = false;
            console.log(400);
            displayInvalidLogin("Invalid Credentials");
        }

        return data.json();

        // then should return a promise, always
    }).then((response) => {
        if(successfulLogin){
            sessionStorage.setItem("loggedIn", true);
            sessionStorage.setItem("displayName", response.firstName + " " + response.lastName);
            if(response.userRole.roleID == 1){
                sessionStorage.setItem("currUserRole", "Manager");
            } else {
                sessionStorage.setItem("currUserRole", "Employee");
            }
            sessionStorage.setItem("username", response.username);



            window.location.href = 'index.html';
        }
    });
}

function clearErrorMessage(){
    let errorMessage = document.getElementById('errorMessage');
    if(errorMessage != null){
        errorMessage.remove();
    }
}

function displayInvalidLogin(errorString) {
    clearErrorMessage();
    let bodyElement = document.getElementById('checkError');
    let pElement = document.createElement('p');
    pElement.setAttribute("id", "errorMessage");
    pElement.style.color = 'red';
    pElement.innerHTML = errorString;
    bodyElement.appendChild(pElement);
    console.log("invalid request");
}
