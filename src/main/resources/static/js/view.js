document.addEventListener("DOMContentLoaded", () => {

    const likeButton = document.querySelector(".heart-button");
    const postId = document.querySelector("#post-id").value;
    const postDeleteButton = document.querySelector("#delete-button");

    if(postDeleteButton !== null) {
        //게시글삭제 ajax
        postDeleteButton.addEventListener("click",()=>{
            $.ajax({
                url: "/posting/view/delete/" + postId,
                method: "post",
                success: function (data) {
                    var result = JSON.parse(data);
                    alert("게시글 삭제 성공");
                    // 페이지 리다이렉트
                    window.location.href = result.redirect;
                },
                error: function () {
                    alert("게시글 삭제 실패");
                }
            });
        });
    }


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

    commentDiv.addEventListener("click", (event) => {
        const target = event.target;
        if (target.classList.contains("comment-del")) {
            const commentId = target.parentElement.querySelector('.comment-del-comment-id').value;
            $.ajax({
                url: "/posting/comment/view/delete/" + commentId,
                method: "post",
                success: function (responseData) {
                    alert(responseData);
                    target.parentElement.parentElement.parentElement.remove();
                },
                error: function (error) {
                    console.log("에러 발생:", error);
                    alert(error.body);
                }
            });
        }
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
    img.src = "https://mblogthumb-phinf.pstatic.net/MjAyMDExMDFfMyAg/MDAxNjA0MjI5NDA4NDMy.5zGHwAo_UtaQFX8Hd7zrDi1WiV5KrDsPHcRzu3e6b8Eg.IlkR3QN__c3o7Qe9z5_xYyCyr2vcx7L_W1arNFgwAJwg.JPEG.gambasg/%EC%9C%A0%ED%8A%9C%EB%B8%8C_%EA%B8%B0%EB%B3%B8%ED%94%84%EB%A1%9C%ED%95%84_%ED%8C%8C%EC%8A%A4%ED%85%94.jpg?type=w800"; // 프로필 이미지 URL

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

    //삭제를 위한 히든값 (댓글 아이디)
    const hiddenInput = document.createElement("input");
    hiddenInput.type = "hidden";
    hiddenInput.value = commentObj.id; // 원하는 값을 설정
    hiddenInput.classList.add("comment-del-comment-id");

    commentInfo.appendChild(namePara);
    commentInfo.appendChild(datePara);
    commentInfo.appendChild(contentPara);

    //로그인한 회원과 댓글 작성자의 닉네임이 같으면 삭제버튼 생성
    if(nickname === commentObj.member.nickname) {
        commentInfo.appendChild(commentDel);
        commentInfo.appendChild(hiddenInput);
    }

    commentForm.appendChild(img);
    commentForm.appendChild(commentInfo);

    commentContainer.appendChild(commentForm);


    return commentContainer;
}