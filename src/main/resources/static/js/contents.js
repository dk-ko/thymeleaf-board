document.addEventListener('DOMContentLoaded', function () {
    render();
});

function render() {
    init();
    bindCommentInput();
    bindCommentCreateButton();
    bindArticleRecommendButton();
    bindPageHeader();
}

const Comment = {};
const Article = {};
function init() {
    Comment.form = document.querySelector('#comment-form');
    Comment.counter = document.querySelector('#counter');
    Comment.comment_list = document.querySelector('.list-group');
    Comment.insert_button = document.querySelector('#comment-insert');
    Article.recommend = document.querySelector('#recommend a');
    Article.idx = document.querySelector('#article_idx');

    let delete_buttons = document.querySelectorAll('.comment-delete');
    delete_buttons.forEach((delete_button) => {
        bindCommentDeleteButton(delete_button);
    });

    let edit_buttons = document.querySelectorAll('.comment-edit');
    edit_buttons.forEach((edit_button) => {
        bindCommentEditButton(edit_button);
    });

    errorHandler();
}

function bindCommentInput() {
    const comment_form = Comment.form;
    comment_form.addEventListener('keyup', function (event) {
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

    comment_insert_button.addEventListener('click', function (event) {
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
        success: function (createdComment) {
            const created_comment_element = createCommentElement(createdComment);
            const comment_list = Comment.comment_list;
            comment_list.appendChild(created_comment_element);

            Comment.form.value = '';
            Comment.counter = 0;
        },
        error: function (error) {
            throw Error(error);
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
        <label for="comment-edit-form">
            <textarea id="comment-edit-form" type="text" class="form-control" rows="10" readonly="readonly" disabled >${comment.contents}</textarea>
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

    // edit event
    const comment_edit_button = created_comment.querySelector(
        '#comment-edit'
    );
    bindCommentEditButton(comment_edit_button);

    return created_comment;
}

function bindCommentDeleteButton(comment_delete_button) {
    comment_delete_button.addEventListener('click', function (event) {
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
        success: function (data) {
            console.log(data);
            alert('댓글이 삭제되었습니다.');
            comment_element.parentElement.remove()
        },
        error: function (error) {
            console.log(error);
            throw new Error(error);
        }
    });
}

let COMMENT_EDIT_FORM_ID = 'comment-edit-form';
let COMMENT_EDITING_ID = 'comment-editing';
let COMMENT_EDIT_ID = 'comment-edit';

function bindCommentEditButton(comment_edit_button) {
    comment_edit_button.addEventListener('click', function () {
        const parent_icon_element = comment_edit_button.parentElement;
        const comment_element_writer = parent_icon_element.parentElement;
        const comment_element_item = comment_element_writer.parentElement;
        const comment_textarea_label = comment_element_item.querySelector('label[for="comment-edit-form"]');
        const comment_textarea = comment_textarea_label.children.item(0);
        const comment_textarea_old_value = comment_textarea.value;

        const comment_icons = comment_element_writer.querySelector('.icons');
        const comment_icons_firstChild = comment_icons.children.item(0);
        console.log(comment_icons_firstChild);

        if (comment_icons_firstChild.id == COMMENT_EDIT_ID) {
            console.log('if test');
            console.log('comment_icons_firstChild.id: ' + comment_icons_firstChild.id);

            comment_textarea.removeAttribute('readonly');
            comment_textarea.disabled = false;
            comment_textarea.setAttribute('id', COMMENT_EDIT_FORM_ID);

            let find_edit_icon = comment_element_writer.querySelector('#' + COMMENT_EDIT_ID);
            find_edit_icon.src = '/static/img/save.png';
            find_edit_icon.id = COMMENT_EDITING_ID;

            const created_icon = document.createElement('img');
            created_icon.id = 'comment-edit-cancel';
            created_icon.className = 'comment-edit';
            created_icon.src = '/static/img/cancel.png';
            created_icon.alt = 'comment-edit-cancel';
            created_icon.innerHTML;

            let find_icons = comment_element_writer.children.item(2);
            find_icons.insertBefore(created_icon, find_icons.firstChild);

            const comment_cancel_button = created_icon;
            console.log(comment_cancel_button);
            bindCommentEditCancelButton(comment_cancel_button, comment_textarea, comment_textarea_old_value);

        } else if (comment_icons_firstChild.id != COMMENT_EDIT_ID) {
            console.log('else test');
            console.log('comment_icons_firstChild.id: ' + comment_icons_firstChild.id);

            const comment_save_button = comment_element_writer.querySelector('#' + COMMENT_EDITING_ID);
            console.log(comment_save_button);
            bindCommentEditSaveButton(comment_save_button, comment_textarea_old_value);
        }
    });
}

function bindCommentEditCancelButton(comment_cancel_button, comment_textarea, comment_textarea_old_value) {
    console.log(comment_cancel_button);
    console.log(comment_textarea);
    console.log(comment_textarea_old_value);
    comment_cancel_button.addEventListener('click', function () {
        console.log('cancel button click event');
        resetEditCommentButton(comment_cancel_button, comment_textarea, comment_textarea_old_value);
        // comment_cancel_button.removeEventListener('click', arguments.callee);
        // console.log(arguments.callee);
    }, {
        capture: false,
        once: true,
        passive: false
    })
}

function resetEditCommentButton(comment_cancel_button, comment_textarea, comment_textarea_old_value) {
    const parent_icons_element = comment_cancel_button.parentElement;
    console.log(parent_icons_element);
    parent_icons_element.firstChild.remove();

    const save_icon = parent_icons_element.children.item(0);

    save_icon.id = COMMENT_EDIT_ID;
    save_icon.src = '/static/img/edit.png';
    save_icon.alt = 'comment-edit';

    comment_textarea.setAttribute('readonly', 'readonly');
    comment_textarea.disabled = true;

    if (comment_textarea_old_value !== undefined) comment_textarea.value = comment_textarea_old_value;
}

function bindCommentEditSaveButton(comment_save_button, comment_textarea_old_value) {
    console.log('bindCommentEditSaveButton');
    let comment_writer_element = comment_save_button.parentElement.parentElement;
    const comment_idx = comment_writer_element.querySelector('#comment_idx').value;
    const comment_cancel_button = comment_writer_element.querySelector('#comment-edit-cancel');
    const comment_textarea = comment_writer_element.parentElement.querySelector('#comment-edit-form');

    comment_save_button.addEventListener('click', function () {
        console.log('event listener');
        const comment_form = comment_save_button.parentElement.parentElement.parentElement.querySelector('#comment-edit-form');

        let commentReqDto = {
            contents: comment_form.value
        };

        editCommentApi(comment_idx, commentReqDto, comment_cancel_button, comment_textarea, comment_textarea_old_value);
        // comment_save_button.removeEventListener('click', arguments.callee);
        // console.log(comment_save_button);
    }, {
        capture: false,
        once: true,
        passive: false
    });
}

function editCommentApi(comment_idx, commentReqDto, comment_cancel_button, comment_textarea, comment_textarea_old_value) {
    console.log('editCommentApi');
    $.ajax({
        url: baseUrl + '/comments/' + comment_idx,
        type: "PUT",
        data: JSON.stringify(commentReqDto),
        contentType: 'application/json',
        // header: {
        //
        // },
        dataType: 'json',
        success: function (data) {
            console.log('comment edit api: success');
            resetEditCommentButton(comment_cancel_button, comment_textarea); // 2
        }, error: function (error) {
            console.log('comment edit api: error');
            resetEditCommentButton(comment_cancel_button, comment_textarea, comment_textarea_old_value); // 3
            throw Error(error)
        }
    });
}

function bindArticleRecommendButton() {
    console.log('bindArticleRecommendButton');
    const article_recommend = Article.recommend;
    article_recommend.addEventListener('click', function(event) {
        console.log('click event');
        articleRecommendApi();
    });
}

function articleRecommendApi() {
    console.log('articleRecommendApi');
    const article_idx = Article.idx.value;
    $.ajax({
        url: baseUrl + '/articles/recommend/' + article_idx,
        type: "PUT",
        contentType: 'application/json',
        dataType: 'json',
        success: function (recommendNumber) {
            const article_recommend = Article.recommend;
            article_recommend.text = '추천하기(' + recommendNumber +')';
        },
        error: function (error) {
            throw new Error(error);
        }
    });
}