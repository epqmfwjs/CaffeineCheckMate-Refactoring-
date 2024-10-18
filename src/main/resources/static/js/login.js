document.addEventListener('DOMContentLoaded', function() {
    // 페이지 로드 시 쿠키에서 저장된 아이디 값을 불러옴
    const savedId = getCookie("idChk");
    if (savedId) {
        document.getElementById("loginId").value = savedId;
        document.getElementById("idSaveCheck").checked = true;
    }

    // 체크박스 상태 변화에 따른 쿠키 설정/삭제
    document.getElementById("idSaveCheck").addEventListener('change', function() {
        if (this.checked) {
            setCookie("idChk", document.getElementById("loginId").value, 7); // 쿠키에 아이디 저장
        } else {
            deleteCookie("idChk", "/"); // 쿠키 삭제, 경로 지정
        }
    });

    // 아이디 입력 값이 바뀔 때 쿠키 갱신 (체크박스가 체크된 경우에만)
    document.getElementById("loginId").addEventListener('input', function() {
        if (document.getElementById("idSaveCheck").checked) {
            setCookie("idChk", this.value, 7);
        }
    });
});

// 쿠키 설정 함수
function setCookie(cookieName, value, exdays) {
    const exdate = new Date();
    exdate.setDate(exdate.getDate() + exdays);
    const cookieValue = escape(value) + "; expires=" + exdate.toUTCString() + "; path=/"; // 경로 설정
    document.cookie = cookieName + "=" + cookieValue;
}

// 쿠키 삭제 함수 (쿠키 만료일을 과거로 설정하여 삭제)
function deleteCookie(cookieName, path) {
    document.cookie = cookieName + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=" + path;
    // 쿠키 삭제 후 아이디 입력 필드 초기화
    document.getElementById("loginId").value = '';
}

// 쿠키 읽기 함수
function getCookie(cookieName) {
    const name = cookieName + "=";
    const decodedCookie = decodeURIComponent(document.cookie);
    const ca = decodedCookie.split(';');
    for(let i = 0; i < ca.length; i++) {
        const c = ca[i].trim();
        if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}