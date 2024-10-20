// DOMContentLoaded 이벤트 사용    // join.js
document.addEventListener("DOMContentLoaded", function() {
    console.log('join.js 파일 미리보기 이벤트 리스너 로드됨.');

    // 이미지 미리보기 함수
    window.previewImage = function() {
        const fileInput = document.getElementById('joinImages');
        const previewImage = document.getElementById('previewImg');
        console.log('펑션 안쪽');
        if (fileInput.files && fileInput.files[0]) {
            const reader = new FileReader();
            reader.onload = function(e) {
                previewImage.src = e.target.result;
            };
            reader.readAsDataURL(fileInput.files[0]);
        } else {
            previewImage.src = "/img/default-image.png";
        }
    };

    // 폼 검증 함수
    window.validateForm = function(event) {
        const isUsernameChecked = $('#isUsernameChecked').val();
        if (isUsernameChecked !== 'true') {
            $('#idCheckResult').text('ID 중복 체크를 해주세요.').removeClass('text-success').addClass('text-danger');
            event.preventDefault();
            return false;
        }

        const password = $('#password').val();
        const confirmPassword = $('#confirmPassword').val();
        if (password !== confirmPassword) {
            $('#passwordCheckResult').text('비밀번호가 일치하지 않습니다.');
            event.preventDefault();
            return false;
        }

        const file = document.getElementById('imageUpload').files[0];
        if (file && file.size > 10 * 1024 * 1024) {
            alert('이미지 파일 크기는 10MB를 초과할 수 없습니다.');
            event.preventDefault();
            return false;
        }
        return true;
    };

    // 전화번호 자동 하이픈 추가
    window.formatPhoneNumber = function(input) {
        const value = input.value.replace(/[^0-9]/g, '');
        let result = '';
        if (value.startsWith('02')) {
            if (value.length < 3) {
                result = value;
            } else if (value.length < 6) {
                result = value.slice(0, 2) + '-' + value.slice(2);
            } else if (value.length < 10) {
                result = value.slice(0, 2) + '-' + value.slice(2, 5) + '-' + value.slice(5);
            } else {
                result = value.slice(0, 2) + '-' + value.slice(2, 6) + '-' + value.slice(6);
            }
        } else {
            if (value.length < 4) {
                result = value;
            } else if (value.length < 7) {
                result = value.slice(0, 3) + '-' + value.slice(3);
            } else if (value.length < 11) {
                result = value.slice(0, 3) + '-' + value.slice(3, 6) + '-' + value.slice(6);
            } else {
                result = value.slice(0, 3) + '-' + value.slice(3, 7) + '-' + value.slice(7);
            }
        }
        input.value = result;
    };

    // 주소 검색 후 추출
    window.kakaoMap = function() {
        var width = 500;
        var height = 600;
        new daum.Postcode({
            theme: {
                bgColor: "#E0F0FD",
                searchBgColor: "#F8F8F8",
                pageBgColor: "#BDE4FD",
                outlineColor: "#D0E9F9"
            },
            oncomplete: function(data) {
                var roadAddr = data.roadAddress;
                var extraRoadAddr = '';
                if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                    extraRoadAddr += data.bname;
                }
                if (data.buildingName !== '' && data.apartment === 'Y') {
                    extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                if (extraRoadAddr !== '') {
                    extraRoadAddr = ' (' + extraRoadAddr + ')';
                }
                var fullAddress = '(' + data.zonecode + ') ' + roadAddr + extraRoadAddr;
                document.getElementById("address").value = fullAddress;
            }
        }).open({
            left: (window.screen.width / 2) - (width / 2),
            top: (window.screen.height / 2) - (height / 2)
        });
    };

    // 이메일 인증 팝업
    window.openEmailVerificationPopup = function() {
        var popupWidth = 500;
        var popupHeight = 550;
        var screenWidth = window.screen.width;
        var screenHeight = window.screen.height;
        var left = (screenWidth - popupWidth) / 2;
        var top = (screenHeight - popupHeight) / 2;
        var popup = window.open('/member/emailVerification', 'emailVerification', 'width=' + popupWidth + ', height=' + popupHeight + ', left=' + left + ', top=' + top);
        if (popup) {
            popup.focus();
        } else {
            alert('팝업 차단이 감지되었습니다. 팝업 창을 허용해주세요.');
        }
    };

    // 이메일 인증 결과 업데이트
    window.updateEmailVerificationResult = function(email) {
        $('#email').val(email);
        $('#emailVerified').val('true');
        $('#emailVerificationResult').text('이메일 인증이 완료되었습니다.').removeClass('text-danger').addClass('text-success').show();
        $('#emailError').hide();
    };

    // ID 중복 체크
    window.checkMemberId = function() {
        const memberId = $('#memberId').val();
        const memberIdPattern = /^[a-zA-Z0-9]{6,20}$/;
        if (memberId === '') {
            $('#idCheckResult').text('ID를 입력해주세요.').removeClass('text-success').addClass('text-danger');
            return;
        }
        if (!memberIdPattern.test(memberId)) {
            $('#idCheckResult').text('ID는 영문,숫자 6자 이상, 20자 이하로 입력해주세요.').removeClass('text-success').addClass('text-danger');
            return;
        }
        $.get('/api/member/check-id', {memberId: memberId}, function(response) {
            $('#idCheckResult').text(response).removeClass('text-success text-danger')
                .addClass(response === '사용 가능한 ID입니다.' ? 'text-success' : 'text-danger');
            $('#isUsernameChecked').val(response === '사용 가능한 ID입니다.');
        }).fail(function(xhr) {
            $('#idCheckResult').text(xhr.responseText).removeClass('text-success').addClass('text-danger');
            $('#isUsernameChecked').val('false');
        });
    };

    // ID 입력란이 변경될 때 중복 체크 결과 초기화
    window.resetUsernameCheck = function() {
        $('#isUsernameChecked').val('false');
    };

    $(function() {
        // Datepicker 한글 설정
        $.datepicker.setDefaults({
            closeText: '닫기',
            prevText: '이전',
            nextText: '다음',
            currentText: '오늘',
            monthNames: ['1월','2월','3월','4월','5월','6월',
                '7월','8월','9월','10월','11월','12월'],
            monthNamesShort: ['1월','2월','3월','4월','5월','6월',
                '7월','8월','9월','10월','11월','12월'],
            dayNames: ['일','월','화','수','목','금','토'],
            dayNamesShort: ['일','월','화','수','목','금','토'],
            dayNamesMin: ['일','월','화','수','목','금','토'],
            weekHeader: '주',
            dateFormat: 'yy-mm-dd',
            firstDay: 0,
            isRTL: false,
            showMonthAfterYear: true,
            yearSuffix: '년'
        });

        // Datepicker 적용
        $("#memberAge").datepicker({
            changeMonth: true,       // 월 선택 가능
            changeYear: true,        // 연도 선택 가능
            yearRange: "1900:2024",  // 연도 범위
            showButtonPanel: true,   // 하단에 "오늘", "닫기" 버튼 패널 표시
            showAnim: "slideDown"    // 달력 애니메이션
        });
    });
});

