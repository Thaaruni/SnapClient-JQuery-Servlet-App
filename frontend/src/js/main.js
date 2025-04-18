import $ from 'jquery';


loadAllCustomers();

function loadAllCustomers(){

    // 1.
    const xhr = new XMLHttpRequest();

    // 2.
    xhr.addEventListener('loadend', () => {
        setTimeout(() => {
            $("#loader-wrapper").hide();
            if (xhr.status === 200) {
                const customerList = JSON.parse(xhr.responseText);
                if (customerList.length) {
                    $("#tbl-customers tfoot").addClass('d-none');
                    customerList.forEach(c =>{
                        $('#tbl-customers tbody').append(`
                     <tr>
                         <td>
                             <img class="profile-picture rounded"
                                  src="${c.profilePicture}"
                                  alt="${c.name}">
                         </td>
                         <td>
                             <div>
                                 <b>ID:</b> ${c.id}
                             </div>
                             <div>
                                 <b>Name:</b> ${c.name}
                             </div>
                             <div>
                                 <b>Address:</b> ${c.address}
                             </div>
                         </td>
                         <td class="align-middle text-end">
                             <i title="Delete" class="bi bi-trash fs-2 pe-2"></i>
                         </td>
                     </tr>                        
                         `);
                    });
                } else {
                    $("#tbl-customers tfoot").removeClass('d-none');
                }
            } else {
                alert('Something went wrong, try again');
            }
        }, 1000);
    });

    // 3.
    xhr.open('GET',
        'http://localhost:8080/app/v1/customers', true);

    // 4.

    // 5.
    xhr.send();

}