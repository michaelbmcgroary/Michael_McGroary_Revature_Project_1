"use strict";
let newRequestURL = "http://localhost:7000/reimbursement";
let submitButton = document.getElementById('submitButton');
let dropdownType = document.getElementById("dropdownMenuButton");
let typeLodging = document.getElementById("typeLodging");
let typeTravel = document.getElementById("typeTravel");
let typeFood = document.getElementById("typeFood");
let typeOther = document.getElementById("typeOther");
let descriptionInput = document.getElementById("descriptionInput");
let amountInput = document.getElementById("amountInput");
let type = -1;
let description = "";
let amount = 0.0;
let recieptInput = document.getElementById("customFile");
let noRecieptAreYouSure = false;
let reciept = null;
let reqID = -1;

var formData = new FormData();


submitButton.addEventListener('click', submit);
typeLodging.addEventListener('click', setTypeLodging);
typeTravel.addEventListener('click', setTypeTravel);
typeFood.addEventListener('click', setTypeFood);
typeOther.addEventListener('click', setTypeOther);


function submit(){
    //Check and see if all the fields are populated
    description = descriptionInput.value;
    amount = parseFloat(amountInput.value);
    reciept = recieptInput.value;
    if(type != -1){
        if(description != ""){
            if(amount != NaN && amount > 0.0){
                console.log(reciept);
                console.log(noRecieptAreYouSure);
                if(reciept != "" || noRecieptAreYouSure == true){
                    if(reciept != ""){
                        //if a reciept exists, send it separately from the JSON,
                        //may move this to after the approved submit function and
                        //put the loading of the table page in here after getting
                        //data about the id from the back end

                        //or may have a variable set in here that is checked in another function before
                        //the window moves to the next page

                        //may use this to set file name: new Date().getTime()
                        formData.append("userfile", recieptInput.files[0], "fileNameTest");
                        formData.append("amount", amount);
                        formData.append("type", type);
                        formData.append("description", description);
                        sendFile();
                    } else {
                        formData.append("userfile", null);
                        formData.append("amount", amount);
                        formData.append("type", type);
                        formData.append("description", description);
                        sendFile();
                    }
                } else {
                    noRecieptAreYouSure = true;
                    displayInvalidRequest("No reciept was added, if you don't want to add one, please press submit again");
                }
            } else {
                displayInvalidRequest("Please input amount in form $00.00");
            }
        } else {
            displayInvalidRequest("Must include a description");
        }
    } else {
        displayInvalidRequest("Type of Reimbursement Not Selected");
    }
}

function sendFile(){
    //use reqID
    var request = new XMLHttpRequest();
    request.open("POST", "http://localhost:7000/reimbursement");
    request.send(formData);
    request.onreadystatechange = function(){
        setTimeout(function(){
            window.location.href = 'reimbursementTable.html';
        },2000);
    }
    
}




function setTypeLodging(){
    dropdownType.innerHTML = 'Lodging';
    type = "Lodging";
}

function setTypeTravel(){
    dropdownType.innerHTML = 'Travel';
    type = "Travel";
}

function setTypeFood(){
    dropdownType.innerHTML = 'Food';
    type = "Food";
}

function setTypeOther(){
    dropdownType.innerHTML = 'Other';
    type = "Other";

}



function approvedSubmit() {
    console.log("approved submit");
    
    let data = {
        "amount": amount,
        "description": description,
        "type": type
    };
    
    fetch(newRequestURL, {
        method: 'POST',
        credentials: 'include', // this specifies that when you receive cookies, you should include them in future requests. So in our case, it's important so that the backend can identify if we are logged in or not.
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then((response) => {
        if (response.status === 200) {
            console.log(200);
            window.location.href = 'reimbursementTable.html';
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
