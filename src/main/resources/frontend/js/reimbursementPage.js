"use strict";
//After the navbar controller is done, add the code here to make it work
let reimbURL = "http://localhost:7000/reimbursement";
//For some reason it's not checking the session, use test for now, delete line below this
//let reimbURL = "http://localhost:7000/test";
let actionColumn = document.getElementById("actionColumn");
let button = document.querySelector('#button');
let newButton = document.querySelector('#newButton');
let approveButtons = [];
let denyButtons = [];
let recieptButtons = [];
let row;
let newApproveButton;
let newDenyButton;
let newRecieptButton;
let dropdownMenuButton = document.getElementById("dropdownMenuButton");
let filterAll = document.getElementById("filterAll");
let filterPending = document.getElementById("filterPending");
let filterApproved = document.getElementById("filterApproved");
let filterDenied = document.getElementById("filterDenied");


//change the function that's called to check the current user, and perform the load based on the response
if(sessionStorage.getItem("currUserRole") != "Manager"){
    actionColumn.remove();
    dropdownMenuButton.remove();
}

if(sessionStorage.getItem("statusFilter") == "Pending"){
    retrieveReimbursementsByStatus("Pending");
} else if(sessionStorage.getItem("statusFilter") == "Approved"){
    retrieveReimbursementsByStatus("Approved");
} else if(sessionStorage.getItem("statusFilter") == "Denied"){
    retrieveReimbursementsByStatus("Denied");
} else {
    retrieveReimbursements();
}



newButton.addEventListener('click', newRequestPage);
filterAll.addEventListener('click', filterByAll);
filterPending.addEventListener('click', filterByPending);
filterApproved.addEventListener('click', filterByApproved);
filterDenied.addEventListener('click', filterByDenied);

/*
    Fetch API (modern way)
*/
function retrieveReimbursements() {
    //let id = document.querySelector('#reimbursementID').value;
    //used to be URL + id
    fetch(reimbURL, {
        method: 'GET',
        credentials: 'include'
    })
    .then((data) => {
        return data.json();

        // then should return a promise, always
    })
    .then((response) => {
        if(isArray(response)){
            for(var i in response){
                populateData(response[i]);
            }
        } else {
            populateData(response);
        }
        
    });
}

function retrieveReimbursementsByStatus(status) {
    //let id = document.querySelector('#reimbursementID').value;
    //used to be URL + id
    fetch(reimbURL + "/status/" + status, {
        method: 'GET',
        credentials: 'include'
    })
    .then((data) => {
        return data.json();

        // then should return a promise, always
    })
    .then((response) => {
        if(isArray(response)){
            for(var i in response){
                populateData(response[i]);
            }
        } else {
            populateData(response);
        }
        
    });
}

function newRequestPage(){
    window.location.href = "NewReimbursement.html";
    //change this URL to a page that makes the requests
}

function approveRequest(request) {
    let reqID = request.value;
    console.log(reqID + " APPROVE");
    fetch(reimbURL + "/" + reqID + "/approve", {
        method: 'PUT',
        credentials: 'include'
    })
    .then((data) => {
        return data.json();
    }).then((response) => {
        window.location.href = 'reimbursementTable.html';
    });
}

function denyRequest(request) {
    let reqID = request.value;
    console.log(reqID + " DENY");
    fetch(reimbURL + "/" + reqID + "/deny", {
        method: 'PUT',
        credentials: 'include'
    })
    .then((data) => {
        return data.json();
    }).then((response) => {
        window.location.href = 'reimbursementTable.html';
    });
}

function getRequestReciept(request) {
    let reqID = request.value.replace("\"", "");;
    let reqID2 = parseInt(reqID);
    console.log(reqID2 + " RECIEPT");
    ///
    ///
    //window.location.href = 'http://localhost:7000/testBlob';
    //window.location.href = 'http://localhost:7000/reimbursement/reciept/' + reqID2;
    window.open('http://localhost:7000/reimbursement/reciept/' + reqID2, '_blank');
    ///
    ///
    // fetch("http://localhost:7000/testBlob", {
    //     method: 'GET',
    //     credentials: 'include'
    // })
    // .then((data) => {
    //     console.log(data);
    //     return data.json();
    // }).then((response) => {
    //     console.log(response.blob);
    //     console.log(response);
    //     //window.location.href = 'reimbursementTable.html';
    // });
    // ///
    // ///
    
    // fetch("http://localhost:7000/testBlob")
    // .then(response => response.blob())
    // .then(images => {
    //     let outside
    //     // Then create a local URL for that image and print it 
    //     outside = URL.createObjectURL(images)
    //     console.log(outside)
    // })

    ///
    ///
    ///
}

function filterByAll(){
    sessionStorage.setItem("statusFilter", "All");
    console.log("FILTER BY ALL");
    clearData();
    retrieveReimbursements();
}

function filterByPending(){
    sessionStorage.setItem("statusFilter", "Pending");
    console.log("FILTER BY PENDING");
    clearData();
    retrieveReimbursementsByStatus("Pending");
}

function filterByApproved(){
    sessionStorage.setItem("statusFilter", "Approved");
    console.log("FILTER BY APPROVED");
    clearData();
    retrieveReimbursementsByStatus("Approved");
}

function filterByDenied(){
    sessionStorage.setItem("statusFilter", "Denied");
    console.log("FILTER BY DENIED");
    clearData();
    retrieveReimbursementsByStatus("Denied");
}


function populateData(response) {
    let tbody = document.querySelector('#data .table tbody');
    let tr = document.createElement('tr');

    let id = '<th scope="row">' + response.reimbID + '</th>';
    let author = response.author;
    let amount = response.amount;
    let type = response.type;
    let description = response.description;
    let status = response.status;
    let submitTime = response.submitTime;
    let resolveTime = response.resolveTime;
    let resolver = response.resolver;
    let blobExists = response.blobExists;

    let idTd = document.createElement('td');
    idTd.innerHTML = id;

    let authorTd = document.createElement('td');
    authorTd.innerHTML = author;

    let amountTd = document.createElement('td');
    amountTd.innerHTML = amount;

    let typeTd = document.createElement('td');
    typeTd.innerHTML = type;

    let descriptionTd = document.createElement('td');
    descriptionTd.innerHTML = description;

    let statusTd = document.createElement('td');
    statusTd.innerHTML = status;

    let actionTd = document.createElement('td');
    if (response.status == "Pending"){
        actionTd.innerHTML = '<button id="approveButton' + response.reimbID + '" type="button" class="btn btn-primary" value=' + parseInt(response.reimbID) + '><i class="fa fa-check"></i></button>'
                        + '  <button id="denyButton' + response.reimbID + '" type="button" class="btn btn-danger" value=' + parseInt(response.reimbID) + '><i class="fa fa-close"></i> </button>';
    } else {
        actionTd.innerHTML = "N/A";
    }

    

    let submitTimeTd = document.createElement('td');
    submitTimeTd.innerHTML = submitTime;

    let resolveTimeTd = document.createElement('td');
    resolveTimeTd.innerHTML = resolveTime;


    let resolverTd = document.createElement('td');
    resolverTd.innerHTML = resolver;

    let recieptTd = document.createElement('td');
    console.log(blobExists);
    if(blobExists){
        recieptTd.innerHTML = '<button id="recieptButton' + response.reimbID + '" type="button" class="btn btn-primary" value=' + parseInt(response.reimbID) + '"><i class="fa fa-download"></i></button>'
    } else {
        recieptTd.innerHTML = '<button id="recieptButton' + response.reimbID + '" type="button" class="btn btn-secondary" value=' + parseInt(response.reimbID) + ' disabled><i class="fa fa-download"></i></button>'
    
    }

    tr.appendChild(idTd);
    tr.appendChild(authorTd);
    tr.appendChild(amountTd);
    tr.appendChild(typeTd);
    tr.appendChild(descriptionTd);
    tr.appendChild(statusTd);
    if(sessionStorage.getItem("currUserRole") == "Manager"){
        tr.appendChild(actionTd);
    }
    tr.appendChild(submitTimeTd);
    tr.appendChild(resolveTimeTd);
    tr.appendChild(resolverTd);
    tr.appendChild(recieptTd);

    tbody.appendChild(tr);

    row = parseInt(response.reimbID);
    
    if(document.querySelector('#approveButton' + row + '')){
        approveButtons[row] = document.querySelector('#approveButton' + row + '');
        approveButtons[row].addEventListener('click', function(){approveRequest(this)});
    }

    if(document.querySelector('#denyButton' + row + '')){
        denyButtons[row] = document.querySelector('#denyButton' + row + '');
        denyButtons[row].addEventListener('click', function(){denyRequest(this)});
    }

    if(document.querySelector('#recieptButton' + row + '')){
        recieptButtons[row] = document.querySelector('#recieptButton' + row + '');
        recieptButtons[row].addEventListener('click', function(){getRequestReciept(this)});
    }
}

function clearData() {
    let tbody = document.querySelector('#data .table tbody');
    tbody.innerHTML = '';
}

function isArray(what) {
    return Object.prototype.toString.call(what) === '[object Array]';
}