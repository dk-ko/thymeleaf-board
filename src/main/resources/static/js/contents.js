document.addEventListener('DOMContentLoaded', function() {
    render();
});

function render() {
    init();
    bindCommentInput();
    bindCommentCreateButton();
}

const Comment = {};
function init() {
    Comment.form = document.querySelector('#comment-form');
    Comment.counter = document.querySelector('#counter');
    Comment.comment_list = document.querySelector('.list-group');
    Comment.insert_button = document.querySelector('#comment-insert');

    let delete_buttons = document.querySelectorAll('.comment-delete');

    delete_buttons.forEach((delete_button) => {
        bindCommentDeleteButton(delete_button);
    });

    errorHandler();
}

function errorHandler() {
    window.onerror = function(msg, url, lineNumber) {
        alert(msg);
        return false;
    };
}

function bindCommentInput() {
    const comment_form = Comment.form;
    comment_form.addEventListener('keyup', function(event) {
        validationComment(comment_form);
    });
}

const MAX_COUNT = 500;
function validationComment(comment_form) {
    const comment_text = comment_form.value;
    const counter = Comment.counter;
    counter.textContent = `${comment_text.length} / 최대 ${MAX_COUNT}자`;

    if (comment_text.length > MAX_COUNT) {
        alert('최대 500자까지 입력 가능합니다.');
        comment_form.value = comment_text.substring(0, 500);
        counter.textContent = `(${MAX_COUNT} / 최대 ${MAX_COUNT}자)`;
    }
}

function bindCommentCreateButton() {
    const comment_insert_button = Comment.insert_button;

    comment_insert_button.addEventListener('click', function(event) {
        const comment_form = Comment.form;
        let commentReqDto = {
            contents: comment_form.value
        };

        const splite_url = location.href.split('/');
        const article_idx = splite_url[4];
        createCommentApi(article_idx, commentReqDto);
    });
}

function createCommentApi(article_idx, commentReqDto) {
    $.ajax({
        url: baseUrl + '/articles/' + article_idx,
        type: "POST",
        data: JSON.stringify(commentReqDto),
        contentType: 'application/json',
        // header: {
        //     "Authorization": "Basic"
        // },
        dataType: 'json',
        success: function(data) {
            console.log(data);
            const created_comment_element = createCommentElement(data);
            console.log(created_comment_element);
            const comment_list = Comment.comment_list;
            comment_list.appendChild(created_comment_element);

            Comment.form.value = '';
            Comment.counter = 0;
        },
        error: function() {
            throw Error('권한이 없습니다');
        }
    });
}

function createCommentElement(comment) {
    const date = comment.updatedDate.toString().split(',');

    const formatted_date = `${date[0]}`
        + '-'
        + `${date[1].length === 1 ? '0' + date[1] : date[1]}`
        + '-'
        + `${date[2].length === 1 ? '0' + date[2] : date[2]}`
        + ' '
        + `${date[3].length === 1 ? '0' + date[3] : date[3]}`
        + ':'
        + `${date[4].length === 1 ? '0' + date[4] : date[4]}`;

    const created_comment_literal = `
        <dl class="c_writer">
            <dt>${comment.userResDto.name}</dt>
            <dd class="data">${formatted_date}</dd>
            <dd class="icons">
                <img id="comment-edit" class="comment-edit" src="/static/img/edit.png" alt="comment-edit"/>
                <img id="comment-delete" class="comment-delete" src="/static/img/delete.png" alt="comment-delete"/>
            </dd>
            <input id="comment_idx" type="hidden" value="${comment.idx}">
        </dl>
        <label for="comment-print">
            <textarea id="comment-print" type="text" class="form-control" rows="10" readonly="readonly" disabled >${comment.contents}</textarea>
        </label>
    `;

    const created_comment = document.createElement('li');
    created_comment.className = 'list-group-item';
    created_comment.innerHTML = created_comment_literal;

    // delete event
    const comment_delete_button = created_comment.querySelector(
        '#comment-delete'
    );
    bindCommentDeleteButton(comment_delete_button);

    return created_comment;
}

function bindCommentDeleteButton(comment_delete_button) {
    comment_delete_button.addEventListener('click', function(event) {
        const parent_icon_element = comment_delete_button.parentElement;
        const comment_element = parent_icon_element.parentElement; // c_writer

        console.log(comment_element);
        deleteCommentApi(comment_element);
    });
}

function deleteCommentApi(comment_element) {
    const comment_idx_element = comment_element.querySelector('#comment_idx');
    const comment_idx = comment_idx_element.value;

    $.ajax({
        url: baseUrl + '/comments/' + comment_idx,
        type: "DELETE",
        success: function(data) {
            console.log(data);
            alert('댓글이 삭제되었습니다.');
            comment_element.parentElement.remove()
            // 해당 id remove
        }//,
        // error: function(error) {
        //     console.log(error);
        //     throw new Error(error);
        // }
    });
}