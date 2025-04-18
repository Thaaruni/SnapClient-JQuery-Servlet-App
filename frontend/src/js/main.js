import $ from 'jquery';


loadAllCustomers();

function loadAllCustomers(){

    // 1.
    const xhr = new XMLHttpRequest();

    // 2.
    xhr.addEventListener('readystatechange', ()=>{
        console.log("Call back fn");
    });

    // 3.
    xhr.open('GET',
        'http://localhost:8080/app/v1/customers', true);

    // 4.

    // 5.
    xhr.send();

}