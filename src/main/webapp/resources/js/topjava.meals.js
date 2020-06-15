var userAjaxUrl = "ajax/profile/meals/";
$("#dateTime").datetimepicker({format: 'Y-m-d H:m', minDate:'1970/01/01', maxDate:'-1970/01/01'});
$("#startDate").datetimepicker({timepicker:false, format:'Y-m-d', minDate:'1970/01/01', maxDate:'-1970/01/01'});
$("#endDate").datetimepicker({timepicker:false,  format:'Y-m-d', minDate:'1970/01/01', maxDate:'-1970/01/01'});
$("#startTime").datetimepicker({datepicker:false, format:'H:m'});
$("#endTime").datetimepicker({datepicker:false, format:'H:m'});

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: userAjaxUrl + "filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(userAjaxUrl, updateTableByData);
}

$(function () {
    makeEditable({
        ajaxUrl: userAjaxUrl,
        datatableApi: $("#datatable").DataTable({
            "ajax": {
                "url": userAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (data, type, row) {
                        if (type === "display") {
                            return data.replace("T", " ");
                        }
                        return data;
                    }
                },
                {
                    "data": "description",
                },
                {
                    "data": "calories",
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).attr("data-mealExcess", data.excess);
            }
        }),
        updateTable: updateFilteredTable
    });
});