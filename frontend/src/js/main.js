import $ from 'jquery';

const trFirst = $("#tbl-customers tr:first-child").clone();

for (let i = 0; i < 10; i++) {
    $("#tbl-customers").append(trFirst.clone());
}