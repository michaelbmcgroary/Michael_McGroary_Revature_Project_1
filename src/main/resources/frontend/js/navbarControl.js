"use strict";
let currentUserURL = "http://localhost:7000/current_user";
let logOutURL = "http://localhost:7000//logout";
let navHome = document.getElementById("navHomeLink");
let navTable = document.getElementById("navViewRequestsLink");
let navRequest = document.getElementById("navNewRequestLink");
let navLog = document.getElementById("navLoginLink");


window.onload = function() {checkLog()};
navLog.addEventListener('click', logInOrOut);
//localStorage
//sessionStorage


function checkLog(){
    if(sessionStorage.getItem("loggedIn") == null){
        sessionStorage.setItem("loggedIn", false);
    }
    if(sessionStorage.getItem("loggedIn") == "false"){
        navLog.innerHTML = 'Log In';
        navTable.href = 'login.html';
        navRequest.href = 'login.html';
    } else {
        navLog.innerHTML = 'Logged in as ' + sessionStorage.getItem("displayName") + '. Log Out?';
        navTable.href = 'reimbursementTable.html';
        navRequest.href = 'NewReimbursement.html';
    }
}

function logInOrOut(){
    console.log(sessionStorage.getItem("loggedIn"));
    if(sessionStorage.getItem("loggedIn") == "false"){
        window.location.replace("login.html");
    } else {
        logOut();
        window.location.replace("index.html");
        location.reload();
    }
}

function logOut(){
    sessionStorage.setItem("loggedIn", false);
    console.log("logging Out");
    sessionStorage.setItem("username", "");
    sessionStorage.setItem("password", "");
    sessionStorage.setItem("currUserRole", "Employee");
    sessionStorage.setItem("displayName", "");
    fetch(logOutURL, {
        method: 'POST',
        credentials: 'include'
    })
    .then((data) => {
        return data.json();

    })
    .then((response) => {
        //Intentionally left empty
        
    });
}