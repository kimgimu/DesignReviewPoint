document.addEventListener("DOMContentLoaded",()=>{
    const logout = document.querySelector("#logout");
    const write = document.querySelector("#write");
    const main = document.querySelector(".main-button");

    logout.addEventListener("click",()=>{
        $.ajax({
            url : "/user/logout",
            method : "post",
            success : function () {
                window.location.href = "/user/login";
            }
        });
    });

    write.addEventListener("click",()=>{
       window.location.href = "/posting/write";
    });

    main.addEventListener("click",()=>{
       window.location.href = "/posting/main";
    });

})