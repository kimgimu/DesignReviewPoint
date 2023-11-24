document.addEventListener("DOMContentLoaded", () => {

    const likeButton = document.querySelector(".heart-button");
    const postId = document.querySelector("#post-id").value;

    //댓글불러오기 ajax
    $.ajax({
        url: "/posting/comment/view/" + postId,
        method: "post",
        success: function (responseData) {
            responseData.forEach(comment => {
                commentDiv.appendChild(createCommentElement(comment));
            })
        },
        error: function (error) {
            console.log(error);
        }
    });

    //좋아요 버튼 ajax
    likeButton.addEventListener("click", () => {

        $.ajax({
            url: "/posting/like/" + postId,
            method: "post",
            success: function (responseData) {
                //성공메세지
                console.log("좋아요버튼 성공")
                let likeCount = document.querySelector("#like-count");
                likeCount.innerText = responseData.liked;
                console.log(responseData);
            },
            error: function (error) {
                console.log(error);
                console.log("입니다");
            }
        });

    });

    const commentDiv = document.querySelector(".comment-2");
    const commentButton = document.querySelector('#comment-button');

    commentButton.addEventListener("click", () => {
        const commentArea = document.querySelector('#comment-area');
        const currentDate = new Date().toISOString();
        $.ajax({
            url: "/posting/comment/write/" + postId,
            method: "post",
            contentType: "application/json",
            data: JSON.stringify({
                content: commentArea.value,
                date: currentDate
            }),
            success: function (responseData) {
                console.log(responseData);
                commentDiv.appendChild(createCommentElement(responseData));
                commentArea.value = '';
            },
            error: function (error) {
                console.log(error);
            }
        });
    });

});

//댓글 생성 함수
function createCommentElement(commentObj) {
    //현재 로그인한 회원 닉네임
    const nickname = document.querySelector("#session-nickname").value;
    console.log(nickname + "확인");

    const commentContainer = document.createElement("div");
    commentContainer.classList.add("comment-2-1");

    const commentForm = document.createElement("div");
    commentForm.classList.add("comment-oneform");

    const img = document.createElement("img");
    img.classList.add("member-img");
    // img.src = commentObj.profileImg; // 프로필 이미지 URL

    const commentInfo = document.createElement("div");
    commentInfo.classList.add("comment-info");

    const namePara = document.createElement("p");
    namePara.textContent = commentObj.member.nickname; // 유저명

    const datePara = document.createElement("p");
    datePara.textContent = commentObj.date; // 날짜

    const contentPara = document.createElement("p");
    contentPara.textContent = commentObj.content; // 댓글 내용

    const commentDel = document.createElement("button");
    commentDel.innerText = "x";
    commentDel.classList.add("comment-del");

    commentInfo.appendChild(namePara);
    commentInfo.appendChild(datePara);
    commentInfo.appendChild(contentPara);

    //로그인한 회원과 댓글 작성자의 닉네임이 같으면 삭제버튼 생성
    if(nickname === commentObj.member.nickname) {
        commentInfo.appendChild(commentDel);
    }

    commentForm.appendChild(img);
    commentForm.appendChild(commentInfo);

    commentContainer.appendChild(commentForm);


    return commentContainer;
}