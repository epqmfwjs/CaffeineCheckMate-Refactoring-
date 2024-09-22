document.addEventListener("DOMContentLoaded", function() {
    const searchBar = document.getElementById('searchBar');
    const customListDiv = document.getElementById('customList');





    // 현재 선택된 게시물의 ID를 저장할 변수
    let currentPostId = null;

    // 게시글등록 모달 관련
    const customModal = document.getElementById('customModal'); // 모달 창
    const createCustomModal = document.getElementById('createCustomModal'); // 모달 열기 버튼
    const closeModalBtn = document.querySelector('.close'); // 모달 닫기 버튼

    // 모달 열기
    createCustomModal.addEventListener('click', function() {
        customModal.style.display = 'flex'; // 모달 창을 보이게 설정
    });

    // 모달 닫기
    closeModalBtn.addEventListener('click', function() {
        customModal.style.display = 'none'; // 모달 창을 숨김
    });

    // 모달 바깥 클릭 시 닫기
    window.addEventListener('click', function(event) {
        if (event.target === customModal) { // 모달 외부 클릭 시
            customModal.style.display = 'none'; // 모달 창을 숨김
        }
    });

    // 디테일 모달 관련 요소
    const detailModal = document.getElementById('detailModal'); // 디테일 모달 창
    const closeDetailModalBtn = detailModal.querySelector('.close'); // 디테일 모달 닫기 버튼
    const detailTitle = document.getElementById('detailTitle'); // 제목 표시 영역
    const detailImage = document.getElementById('detailImage'); // 이미지 표시 영역
    const detailContent = document.getElementById('detailContent'); // 내용 표시 영역
    const detailAuthor = document.getElementById('memberId'); // 작성자 표시 영역
    const detailDate = document.getElementById('createdDate'); // 날짜 표시 영역

    // 디테일 모달 닫기 이벤트
    closeDetailModalBtn.addEventListener('click', function() {
        detailModal.style.display = 'none'; // 디테일 모달 닫기
    });

    // 모달 외부 클릭 시 닫기
    window.addEventListener('click', function(event) {
        if (event.target === detailModal) {
            detailModal.style.display = 'none'; // 모달 창을 숨김
        }
    });

    // 이미지 미리보기 처리
    document.getElementById('customImages').addEventListener('change', function(event) {
        const files = event.target.files;
        const previewContainer = document.getElementById('previewContainer');
        previewContainer.innerHTML = ''; // 기존 미리보기 이미지 제거

        Array.from(files).forEach(file => {
            const reader = new FileReader();
            reader.onload = function(e) {
                const img = document.createElement('img');
                img.src = e.target.result;
                previewContainer.appendChild(img);
            };
            reader.readAsDataURL(file); // 파일을 Data URL로 읽어오기
        });
    });

    // 폼 제출 시 처리
    const customForm = document.getElementById('customForm');
    customForm.addEventListener('submit', function(event) {
        event.preventDefault(); // 폼 기본 제출 동작 방지

        const formData = new FormData(customForm);

        // 선택된 파일을 FormData에 추가
        const customImages = document.getElementById('customImages').files;
        for (let i = 0; i < customImages.length; i++) {
            formData.append('imgReal', customImages[i]); // 'imgReal'은 백엔드에서 기대하는 파라미터명과 일치해야 함
        }

        // 서버로 데이터 전송 (POST 요청)
        fetch('/api/createCustom', {
            method: 'POST',
            body: formData
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('응답 실패!');
            }
            return response.json();
        })
        .then(data => {
            console.log('게시글이 등록되었습니다:', data);

            // 게시글 등록이 성공한 후 모달 닫기
            customModal.style.display = 'none'; // 성공 후 모달 닫기
            console.log("모달이 닫혔다");

            // 게시글이 등록되었으면 입력 필드를 초기화
            customForm.reset();

            // 게시글 리스트를 다시 불러와서 업데이트
            fetch('/api/customList')
                .then(response => response.json())
                .then(data => {
                    displayCustomListList(data); // 최신 게시글 리스트 업데이트
                })
                .catch(error => console.error('Error fetching updated list:', error));
        })
        .catch(error => console.error('Error:', error));
    });

    // 검색창에서 입력값이 변할 때마다 검색 요청을 서버로 보냄
    searchBar.addEventListener('input', function() {
        const searchTerm = searchBar.value.toLowerCase();
        fetch(`/api/searchCustom?keyword=${encodeURIComponent(searchTerm)}`)
            .then(response => response.json())
            .then(data => {
                displayCustomListList(data); // 필터링된 결과 표시
            })
            .catch(error => console.error('Error:', error));
    });

    // 댓글 등록 버튼 클릭 이벤트 - 페이지 로드 시 한 번만 등록
    document.getElementById('submitComment').addEventListener('click', function() {
        const commentText = document.getElementById('commentBox').value;
        if (commentText.trim() === '') return; // 빈 댓글은 등록하지 않음

        const formData = new FormData();
        formData.append('text', commentText);
        formData.append('postId', currentPostId); // 현재 선택된 게시물의 ID 사용

        console.log(currentPostId)
;

        console.log('CSRF Header:', csrfHeader); // 로그 확인
        console.log('CSRF Token:', csrfToken); // 로그 확인

        // 댓글 저장 요청
        fetch('/api/comments', {
            method: 'POST',
            headers: {
                [csrfHeader]: csrfToken, // CSRF 토큰 추가
            },
            body: formData
        })
        .then(response => response.json())
        .then(comment => {
            const commentDiv = document.createElement('div');
            commentDiv.textContent = comment.text;

            // 댓글 리스트에 추가
            document.getElementById('commentsList').appendChild(commentDiv);

            // 댓글 입력창 초기화
            document.getElementById('commentBox').value = '';
        })
        .catch(error => console.error('Error adding comment:', error));
    });

    // 댓글 섹션 초기화 함수
    function initializeCommentSection(postId) {
        currentPostId = postId; // 현재 게시물 ID 업데이트

        // 댓글 불러오기
        fetch(`/api/comments?postId=${postId}`)
            .then(response => response.json())
            .then(comments => {
                const commentsList = document.getElementById('commentsList');
                commentsList.innerHTML = ''; // 기존 댓글 초기화
                comments.forEach(comment => {
                    const commentDiv = document.createElement('div');
                    commentDiv.textContent = comment.text;
                    commentsList.appendChild(commentDiv);
                });
            })
            .catch(error => console.error('Error loading comments:', error));
    }

    // 커피 리스트를 화면에 표시하는 함수
    function displayCustomListList(data) {
        customListDiv.innerHTML = ''; // 기존 리스트 초기화
        data.forEach(custom => {
            const customCard = document.createElement('div');
            customCard.classList.add('customCard');

            console.log(custom);

            // 클릭 시 상세 페이지 모달로 열기
            customCard.addEventListener('click', function() {
                // 모달에 상세 정보 표시
                detailTitle.innerHTML = `${custom.customTitle}&nbsp;&nbsp; <span class="heart-style">❤️ 51</span>`;
                detailImage.src = `/images/${custom.imgReal}`;
                detailContent.textContent = custom.customContent; // 게시물의 내용을 표시
                detailAuthor.textContent = `작성자: 최광현`; // 실제 데이터로 변경 가능
                detailDate.textContent = `작성일: ${custom.createdDate}`;

                // 댓글 섹션 초기화 및 댓글 불러오기
                initializeCommentSection(custom.id);

                // 모달 열기
                detailModal.style.display = 'flex';
            });

            // 카드 내용 추가
            customCard.innerHTML = `
                <h2>${custom.customTitle}</h2>
                <img src="/images/${custom.imgReal}" alt="${custom.customTitle}">
                <p>최광현</p>
                <p>${custom.createdDate}&nbsp;&nbsp; <span class="heart-style">❤️ 51</span></p>
            `;

            customListDiv.appendChild(customCard);
        });
    }

    // 초기 로드 시 전체 커피 리스트를 표시
    fetch('/api/customList')
        .then(response => response.json())
        .then(data => {
            displayCustomListList(data);
        })
        .catch(error => console.error('Error:', error));
});
