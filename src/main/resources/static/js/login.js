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

$(document).ready(function() {
console.log(window.location.search);
        // 로그인 실패 시 모달 표시
        const urlParamsErr = new URLSearchParams(window.location.search);
        if (urlParamsErr.get('error') === 'true') {
            $('#errorModal').css('display', 'block');

            // 모달이 표시된 후 URL에서 쿼리 파라미터 제거
            const newUrl = window.location.origin + window.location.pathname;
            window.history.pushState({}, document.title, newUrl);
        }

    $('.close').click(function() {
        $('#errorModal').css('display', 'none');
    });

    // 모달 외부를 클릭했을 때 닫히게 하기
    $(document).on('click', function(event) {
        // 모달의 콘텐츠 영역과 그 내부 클릭을 제외
        if ($(event.target).is('#errorModal') && !$(event.target).closest('.modal-content').length) {
            $('#errorModal').css('display', 'none');
        }
    });

    // 비밀번호 찾기 후 로그인 창
    if (window.location.search.includes('passwordResetSuccess=true')) {
        $('#successModal').modal('show');
    }
    // 아이디 찾기 모달
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('idFindSuccess')) {
        const ids = urlParams.get('idFindSuccess').split(',');
        const $foundIds = $('#foundIds');
        $foundIds.empty();
        ids.forEach(id => {
            $foundIds.append(`<li>${id}</li>`);
        });
        $('#idFindModal').modal('show');
        // 모달이 표시된 후 URL에서 쿼리 파라미터 제거
        const newUrl = window.location.origin + window.location.pathname;
        window.history.pushState({}, document.title, newUrl);
    }
});