import $ from 'jquery';
import 'bootstrap';

/* Module Variables and Constants */
let profilePictureFile = null;
const modal = $("#new-customer-modal")[0];
const flPicture = $("#fl-picture");
const profilePictureElm = $("#profile-picture");
const btnSave = $("#btn-save");

/* Init */
loadAllCustomers();

/* Functions */
function loadAllCustomers() {

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
                    customerList.forEach(c => {
                        $('#tbl-customers tbody').append(`
                    <tr class="animate__animated animate__fadeIn">
                        <td>
                            <div class="profile-picture rounded border"
                                 style="background-image: url('${c.profilePicture}')"></div>
                        </td>
                        <td>
                            <div>
                                <b>ID:</b> <span class="c-id">${c.id}</span>
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
        }, 500);
    });

    // 3.
    xhr.open('GET',
        'http://localhost:8080/app/v1/customers', true);
    // 4.

    // 5.
    xhr.send();

}

function loadImageFile(file) {
    if (file.size >= (5 * 1024 * 1024)) return;
    profilePictureFile = file;
    const fileReader = new FileReader();
    fileReader.addEventListener('load', () => {
        profilePictureElm
            .removeClass('is-invalid')
            .css('background-image',
                `url('${fileReader.result}')`);
        $("#profile-picture .bi-image").addClass('d-none');
    });
    fileReader.readAsDataURL(file);
}

/* Event Handlers */
$('#tbl-customers tbody').on('click', ".bi.bi-trash", (e) => {
    const row = $(e.target).parents("tr");
    const id = row.find('span.c-id').text();
    const progressBar = $("#progress-bar");

    progressBar.removeClass('d-none').css('width', '10px');

    const xhr = new XMLHttpRequest();

    xhr.addEventListener('loadend', () => {
        if (xhr.status === 204) {
            progressBar.css('width', '100%');
            setTimeout(() => {
                progressBar.addClass('d-none').css('width', '0');
                row.addClass("animate__animated animate__fadeOut");

                setTimeout(() => {
                    row.remove();
                    if (!$("#tbl-customers tbody tr").length) {
                        $("#tbl-customers tfoot").removeClass('d-none');
                    }
                }, 500)
            }, 200);
        } else {
            alert("Failed to delete, try again");
        }
    });

    xhr.open('DELETE',
        `http://localhost:8080/app/v1/customers/${id}`,
        true);

    xhr.send();
});

$("header button").trigger('click');

modal.addEventListener('shown.bs.modal', () => {
    $("#new-customer-modal #txt-name").trigger('focus');
});

modal.addEventListener('hidden.bs.modal', () => {
    $("#txt-name, #txt-address").val("");
    $("#txt-name, #txt-address, #profile-picture")
        .removeClass('is-invalid');
    $("#btn-clear").trigger('click');
    btnSave.prop('disabled', false)
        .children('.loader-wrapper').addClass('d-none');
});

$("#btn-browse").on('click', () => {
    flPicture.trigger('click');
});

$("#btn-clear").on('click', () => {
    flPicture.val('');
    profilePictureElm.css('background-image', '');
    $("#profile-picture .bi-image").removeClass('d-none');
    profilePictureFile = null;
});

$("#txt-name, #txt-address").on('input', function () {
    $(this).removeClass('is-invalid');
}).on('keypress', (e)=>{
    if (e.key === 'Enter') btnSave.trigger('click');
});

profilePictureElm.on('dragover', (e) => {
    e.preventDefault();
}).on('dragenter', () => {
    profilePictureElm.addClass('drop-effect');
}).on('dragleave', () => {
    profilePictureElm.removeClass('drop-effect');
}).on('drop', (e) => {
    e.preventDefault();
    profilePictureElm.removeClass('drop-effect');
    const fileList = e.originalEvent.dataTransfer.files;
    if (fileList.length) {
        const file = fileList[0];
        if (file.type.startsWith("image/")) {
            loadImageFile(file);
        }
    }
});

flPicture.on('change', () => {
    const fileList = flPicture.prop('files');
    if (fileList.length) {
        const file = fileList[0];
        loadImageFile(file);
    }
});

btnSave.on('click', () => {
    const txtName = $("#txt-name");
    const txtAddress = $("#txt-address");
    const name = txtName.val().trim();
    const address = txtAddress.val().trim();
    let valid = true;

    $("#txt-name, #txt-address, #profile-picture")
        .removeClass('is-invalid');

    if (address.length < 3) {
        txtAddress.addClass('is-invalid')
            .trigger('focus').trigger('select');
        valid = false;
    }
    if (!/^[A-Za-z ]+$/.test(name)) {
        txtName.addClass('is-invalid')
            .trigger('focus').trigger('select');
        valid = false;
    }
    if (!profilePictureFile) {
        profilePictureElm.addClass('is-invalid');
        valid = false;
    }
    if (!valid) return;

    btnSave.prop('disabled', true);
    const btnLoaderWrapper = $("#btn-save .loader-wrapper");
    btnLoaderWrapper.removeClass('d-none');

    const xhr = new XMLHttpRequest();

    xhr.addEventListener('loadend', ()=>{
        btnLoaderWrapper.addClass('d-none');
        btnSave.prop('disabled', false);
        if (xhr.status === 201){
            $("#btn-close").trigger('click');
            $("#tbl-customers tbody").empty();
            loadAllCustomers();
        }else{
            alert("Failed to save the customer, try again");
        }
    });

    xhr.open('POST',
        'http://localhost:8080/app/v1/customers', true);

    // xhr.setRequestHeader("multipart/form-data");
    const formData = new FormData();
    formData.append("name", name);
    formData.append("address", address);
    formData.append("profilePicture", profilePictureFile);

    xhr.send(formData);

});