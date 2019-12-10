document.addEventListener('DOMContentLoaded', function () {
    render();
});

function render() {
    init();
    bindPageHeader();
    bindArticleCreateButton();
}

const Article = {};
function init() {
    Article.title = document.querySelector('#article-title-form');
    Article.contents = document.querySelector('#article-contents-form');
    Article.article_create_button = document.querySelector('#article-create-button');

    errorHandler();
}

function bindArticleCreateButton() {
    const article_create_button = Article.article_create_button;
    console.log(article_create_button);
    article_create_button.addEventListener('click', function() {
        let articleCreateReqDto = {
            title: Article.title.value,
            contents: Article.contents.value
        };

        console.log(articleCreateReqDto);
        let boardIdx = document.querySelector('#board_idx').value;
        console.log(boardIdx);
        createArticleApi(boardIdx, articleCreateReqDto);
    });
}

function createArticleApi(boardIdx, articleCreateReqDto) {
    $.ajax({
        url: baseUrl + '/boards/' + boardIdx,
        type: "POST",
        data: JSON.stringify(articleCreateReqDto),
        contentType: 'application/json',
        dataType: 'json',
        success(createdArticleDto) {
            console.log("create article");
            console.log(createdArticleDto);
            location.href = baseUrl + '/articles/' + createdArticleDto.articleIdx;
        }, error(error) {
            throw new Error(error);
        }
    })
}
