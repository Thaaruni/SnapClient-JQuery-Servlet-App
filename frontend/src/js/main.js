import $ from 'jquery';
import 'bootstrap';


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
                             <div class="profile-picture rounded border"
                                  style="background-image: url('${c.profilePicture}')"></div>
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

$('#tbl-customers tbody').on('click', ".bi.bi-trash", (e)=>{
    const row = $(e.target).parents("tr");
    const id = row.find('span.c-id').text();
    const progressBar = $("#progress-bar");

    progressBar.removeClass('d-none').css('width', '10px');
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('loadend', ()=>{
        if (xhr.status === 204){
            progressBar.css('width', '100%');
            setTimeout(()=>{
                progressBar.addClass('d-none').css('width', '0');
                row.addClass("animate__animated animate__fadeOut");
                setTimeout(()=>{
                    row.remove();
                    if (!$("#tbl-customers tbody tr").length){
                        $("#tbl-customers tfoot").removeClass('d-none');
                    }
                }, 500)
            }, 200);
        }else{
            alert("Failed to delete, try again");
        }
    });
    xhr.open('DELETE',
        `http://localhost:8080/app/v1/customers/${id}`,
        true);
    xhr.send();
});

$("header button").trigger('click');