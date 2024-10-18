console.log('join.js 파일이 로드되었습니다.');

function validateForm() {
    const memberId = document.getElementById('memberId').value;
    const password = document.getElementById('password').value;
    const memberEmail = document.getElementById('memberEmail').value;
    const memberPhone = document.getElementById('memberPhone').value;
    const memberWeight = document.getElementById('memberWeight').value;
    const memberAge = document.getElementById('memberAge').value;
    const memberGender = document.getElementById('memberGender').value;

    // ID 검증
    if (memberId === "") {
        alert("아이디를 입력하세요.");
        return false;
    }

    // 비밀번호 검증
    if (password.length < 8) {
        alert("비밀번호는 8자 이상이어야 합니다.");
        return false;
    }

    // 이메일 형식 검증
    const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    if (!emailPattern.test(memberEmail)) {
        alert("유효한 이메일 주소를 입력해주세요.");
        return false;
    }

    // 전화번호 검증
    const phonePattern = /^\d{3}-\d{3,4}-\d{4}$/;
    if (!phonePattern.test(memberPhone)) {
        alert("전화번호 형식은 000-0000-0000입니다.");
        return false;
    }

    // 몸무게 검증
    if (isNaN(memberWeight) || memberWeight <= 0) {
        alert("유효한 몸무게를 입력하세요.");
        return false;
    }

    // 생년월일 검증
    const age = new Date().getFullYear() - new Date(memberAge).getFullYear();
    if (age < 0 || age > 120) {
        alert("유효한 생년월일을 입력하세요.");
        return false;
    }

    // 성별 선택 검증
    if (memberGender === "") {
        alert("성별을 선택하세요.");
        return false;
    }

    return true; // 모든 검증을 통과하면 폼 제출
}

function formatPhoneNumber() {
    console.log('formatPhoneNumber()  들어옴');
    const phoneInput = document.getElementById('memberPhone');
    let input = phoneInput.value.replace(/[^0-9]/g, ''); // 숫자만 남기기

    if (input.length > 6) {
        input = input.replace(/^(\d{3})(\d{3,4})(\d{4})$/, '$1-$2-$3');
    } else if (input.length > 3) {
        input = input.replace(/^(\d{3})(\d{3,4})$/, '$1-$2');
    }

    phoneInput.value = input; // 포맷된 값을 입력 필드에 설정
}

