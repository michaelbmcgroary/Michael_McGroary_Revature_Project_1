//This is the start of the actual control for this page, above is all Navbar control
let newUserURL = "http://localhost:7000/newUser";
let submitButton = document.getElementById('submitButton');
let usernameInput = document.getElementById("usernameInput");
let firstNameInput = document.getElementById("firstNameInput");
let lastNameInput = document.getElementById("lastNameInput");
let emailInput = document.getElementById("emailInput");
let passwordInput = document.getElementById("passwordInput");
let confirmPasswordInput = document.getElementById("confirmPasswordInput");

let username = "";
let firstName = "";
let lastName = "";
let email = "";
let password = "";


submitButton.addEventListener('click', submit);
displayInvalidRequest("Please note: Creation of Account does not automatically log you in.")



function submit(){
    console.log("submit");
    //Check and see if all the fields are populated
    username = usernameInput.value;
    firstName = firstNameInput.value;
    lastName = lastNameInput.value;
    email = emailInput.value;
    password = passwordInput.value;
    //I don't like this many nested if statements, but it's as effecient as a try catch with a
    //value to keep track of where the error is
    if(passwordInput.value == confirmPasswordInput.value){
        if(username != ""){
            if(firstName != ""){
                if(lastName != ""){
                    if(email != ""){
                        if(password != ""){
                            //Must pass all these checks before information is submitted
                            approvedSubmit();
                        } else {
                            displayInvalidRequest("Must enter a password");
                        }
                    } else {
                        displayInvalidRequest("Must enter an email address");
                    }
                } else {
                    displayInvalidRequest("Must input a last name")
                }
            } else {
                displayInvalidRequest("Must input a first name")
            }
        } else {
            displayInvalidRequest("Must input a username");
        }
    } else {
        displayInvalidRequest("Passwords must match");
    }
}


function approvedSubmit() {
    console.log("approved submit");
    
    let postData = {
        "username": username,
        "password": password,
        "firstName": firstName,
        "lastName": lastName,
        "email": email
    };
    
    fetch(newUserURL, {
        method: 'POST',
        credentials: 'include', // this specifies that when you receive cookies, you should include them in future requests. So in our case, it's important so that the backend can identify if we are logged in or not.
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(postData)
    }).then((response) => {
        if (response.status === 200) {
            console.log(200);
            window.location.href = 'login.html';
        } else /*if (response.status === 400)*/ {
            console.log(400);
            displayInvalidRequest("An error occured within the system, please try again later");
        }
    })
}



function clearErrorMessage(){
    let errorMessage = document.getElementById('errorMessage');
    if(errorMessage != null){
        errorMessage.remove();
    }
}

function displayInvalidRequest(errorString) {
    clearErrorMessage();
    let bodyElement = document.getElementById('checkError');
    let pElement = document.createElement('p');
    pElement.setAttribute("id", "errorMessage");
    pElement.style.color = 'red';
    pElement.innerHTML = errorString;
    bodyElement.appendChild(pElement);
    console.log("invalid request");
}
