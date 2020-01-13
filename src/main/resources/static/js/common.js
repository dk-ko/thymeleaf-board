const baseUrl = 'http://localhost:8082';

function errorHandler() {
    window.onerror = function (msg, url, lineNumber) {
        alert(msg);
        return false;
    };
}

function bindPageHeader() {
    console.log('bindPageHeader');
    const page_header = document.querySelector('#page-header');
    page_header.addEventListener('click', function() {
        $.ajax({
            url: baseUrl + '/boards/' + $('#board_idx').val(),
            type: "GET",
            success: function() {
                location.href = '/boards/' + $('#board_idx').val();
            }
        })
    });
}