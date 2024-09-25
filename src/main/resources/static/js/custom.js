document.addEventListener("DOMContentLoaded", function() {
    const searchBar = document.getElementById('searchBar');
    const customListDiv = document.getElementById('customList');

    // 현재 선택된 게시물의 ID를 저장할 변수
    let currentPostId = null;

    const memberId = document.getElementById('loginMemberPK').value;

    console.log('memberId' + memberId);

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

    const likeBtn = document.getElementById('like');

    //좋아요버튼
    likeBtn.addEventListener('click', function() {
        console.log('좋아요버튼클릭');

    if (currentPostId === null) {
        console.error('현재 게시물 ID가 없습니다.'); // 게시물 ID가 없을 경우 오류 메시지
        return;
    }

        const likeRequestDto = {
            postId: currentPostId,
            memberId: memberId
        };

        console.log('게시물번호' + currentPostId);
        // fetch 요청
        fetch('/api/like', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json' // JSON 데이터 전송
            },
            body: JSON.stringify(likeRequestDto) // 데이터를 JSON으로 변환하여 전송
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('응답 실패!');
            }
            // 응답 본문이 비어 있을 수 있으므로 안전하게 처리
            return response.text().then(text => {
                if (text) {
                    return JSON.parse(text); // JSON 문자열로 변환
                }
                return {}; // 비어 있으면 빈 객체 반환
            });
        })
        .then(currentLikesCount => {
            console.log('현재 좋아요 수:', currentLikesCount);

            // 좋아요 수 업데이트
            const likesCountElement = detailTitle.querySelector('.heart-style'); // 좋아요 수가 표시된 요소
            likesCountElement.textContent = `❤️ ${currentLikesCount}`; // 현재 좋아요 수로 업데이트

            // 버튼 텍스트 변경
            if (likeBtn.textContent === '좋아요') {
                likeBtn.textContent = '취소';
            } else {
                likeBtn.textContent = '좋아요';
            }

        })
            .catch(error => console.error('Error:', error));
    });

        // 디테일 모달 닫기
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
            formData.append('imgReal', customImages[i]);
        }

        const memberPK = document.getElementById('loginMemberPK').value;
        formData.append('memberPK', memberPK);

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

        const memberPK = document.getElementById('loginMemberPK').value;

        const formData = new FormData();
        formData.append('text', commentText);
        formData.append('postId', currentPostId);
        formData.append('memberPK', memberPK);

        console.log(currentPostId);

        // 댓글 저장 요청
        fetch('/api/comments', {
            method: 'POST',
            body: formData
        })
        .then(response => response.json())
        .then(comment => {
            const commentDiv = document.createElement('div');
            commentDiv.textContent = `Comment: ${comment.text} - Member ID: ${comment.memberId}`;

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
                    commentDiv.textContent = `Comment: ${comment.text} - Member ID: ${comment.memberId}`;
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
                detailTitle.innerHTML = `${custom.customTitle}&nbsp;&nbsp; <span class="heart-style">❤️ ${custom.likesCount}</span>`;
                detailImage.src = `/images/${custom.imgReal}`;
                detailContent.textContent = custom.customContent;
                detailAuthor.textContent = `작성자: ${custom.memberId}`;
                detailDate.textContent = `작성일: ${custom.createdDate}`;

                // 댓글 섹션 초기화 및 댓글 불러오기
                initializeCommentSection(custom.id);

                // 좋아요 상태 확인 요청
                fetch(`/api/likeStatus?postId=${custom.id}&memberId=${memberId}`)
                    .then(response => response.json())
                    .then(data => {
                        // 사용자가 좋아요를 눌렀다면
                        if (data.hasLiked) {
                            likeBtn.textContent = '취소'; // 버튼 텍스트 변경
                        } else {
                            likeBtn.textContent = '좋아요'; // 버튼 텍스트 변경
                        }
                    })
                    .catch(error => console.error('Error checking like status:', error));


                // 모달 열기
                detailModal.style.display = 'flex';
            });

            // 카드 내용 추가
            customCard.innerHTML = `
                <h2>${custom.customTitle}</h2>
                <img src="/images/${custom.imgReal}" alt="${custom.customTitle}">
                <p>${custom.memberId}</p>
                <p>${custom.createdDate}&nbsp;&nbsp; <span class="heart-style">❤️ ${custom.likesCount}</span></p>
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
