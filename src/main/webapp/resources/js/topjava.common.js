var context, form;

function makeEditable(ctx) {
    context = ctx;
    form = $('#detailsForm');

    $(document).ajaxError(function (event, jqXHR, options, jsExc) {
        failNoty(jqXHR);
    });

    // solve problem with cache in IE: https://stackoverflow.com/a/4303862/548473
    $.ajaxSetup({cache: false});
}

function add() {
    form.find(":input").val("");
    $('input[name="id"]').val(0);
    $("#editRow").modal();
}

function update(id, dateTime, description, calories) {
    $("#editRow").modal();
    $('input[name="id"]').val(id);
    $('input[name="dateTime"]').val(dateTime);
    $('input[name="description"]').val(description);
    $('input[name="calories"]').val(calories);
}

function deleteRow(id) {
    if (confirm('Are you sure?')) {
        $.ajax({
            url: context.ajaxUrl + id,
            type: "DELETE"
        }).done(function () {
            context.updateTable();
            successNoty("Deleted");
        });
    }
}


function updateTableByData(data) {
    context.datatableApi.clear().rows.add(data).draw();
}

function updateTable() {
    $.ajax({
        type: "GET",
        url: getFilterUrl(),
    }).done(updateTableByData);
}

function clearFilter(){
    $('input[name="startDate"]').val('');
    $('input[name="startTime"]').val('');
    $('input[name="endDate"]').val('');
    $('input[name="endTime"]').val('');

    $.ajax({
        type: "GET",
        url: context.ajaxUrl
    }).done(updateTableByData)
}

function getFilterUrl() {
    var startDate = $('input[name="startDate"]').val();
    var startTime = $('input[name="startTime"]').val();
    var endDate = $('input[name="endDate"]').val();
    var endTime = $('input[name="endTime"]').val();

    return context.ajaxUrl + startDate + "/" + startTime + "/" + endDate + "/" + endTime;
}

function save() {
    $.ajax({
        type: "POST",
        url: context.ajaxUrl,
        data: form.serialize()
    }).done(function () {
        $("#editRow").modal("hide");
        context.updateTable();
        successNoty("Saved");
    });
}

let failedNote;

function closeNoty() {
    if (failedNote) {
        failedNote.close();
        failedNote = undefined;
    }
}

function successNoty(text) {
    closeNoty();
    new Noty({
        text: "<span class='fa fa-lg fa-check'></span> &nbsp;" + text,
        type: 'success',
        layout: "bottomRight",
        timeout: 1000
    }).show();
}

function failNoty(jqXHR) {
    closeNoty();
    failedNote = new Noty({
        text: "<span class='fa fa-lg fa-exclamation-circle'></span> &nbsp;Error status: " + jqXHR.status,
        type: "error",
        layout: "bottomRight"
    }).show();
}