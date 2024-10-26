document.addEventListener("DOMContentLoaded", function() {
    const searchBar = document.getElementById('searchBar');
    const customListDiv = document.getElementById('customList');

    const commentButton = document.getElementById('commentToggle');
    const commentSection = document.getElementById('commentSection');
    const closeButton = document.querySelector('.close');

    // 페이지 로드 시 댓글 영역 숨김
        window.addEventListener('DOMContentLoaded', function() {
        commentSection.classList.remove('show'); // 초기 상태에서 숨김
    });

    // 댓글 보기 버튼 클릭 시 슬라이드 효과
        commentButton.addEventListener('click', function() {
        commentSection.classList.toggle('show');
    });

    // 모달 닫기 버튼 클릭 시 댓글 영역도 닫기
        closeButton.addEventListener('click', function() {
        commentSection.classList.remove('show');
    });

    // 댓글 영역 외부 클릭 시 댓글 영역 닫기
        document.addEventListener('click', function(event) {
    // 댓글 영역 및 댓글 버튼이 클릭되지 않은 경우
    if (!commentSection.contains(event.target) && !commentButton.contains(event.target)) {
        commentSection.classList.remove('show');
        }
    });

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
        // memberId가 null 또는 빈 문자열("")일 때 로그인 화면으로 리다이렉트
        if (memberId && memberId.trim() !== "") {
            customModal.style.display = 'flex'; // 모달 창을 보이게 설정
        } else {
            window.location.href = '/member/login'; // 로그인 페이지로 이동
        }
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
    const tagOption1 = document.getElementById('tagOption1'); // 브랜드
    const tagOption2 = document.getElementById('tagOption2'); // 시럽
    const tagOption3 = document.getElementById('tagOption3'); // 휘핑
    const tagOption4 = document.getElementById('tagOption4'); // 샷
    const tagOption5 = document.getElementById('tagOption5'); // 우유
    const tagOption6 = document.getElementById('tagOption6'); // 커피타입

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
            const likesCountElement = detailModal.querySelector('.heart-style'); // detailModal 내에 heart-style을 정확하게 찾기
            if (likesCountElement) {
                likesCountElement.textContent = `❤️ ${currentLikesCount}`; // 좋아요 수 업데이트
            }

            // 버튼 텍스트 변경
            if (likeBtn.textContent === '좋아요 ❤️') {
                likeBtn.textContent = '취소 🤍';
            } else {
                likeBtn.textContent = '좋아요 ❤️';
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


// 대분류 summary를 세부 항목 선택에 따라 변경하고, 드롭다운을 닫는 함수
document.querySelectorAll('details').forEach((detail) => {
    const summary = detail.querySelector('summary');
    const radios = detail.querySelectorAll('input[type="radio"]');

    radios.forEach((radio) => {
        radio.addEventListener('change', () => {
            if (radio.checked) {
                // 선택된 라디오 버튼의 텍스트 노드(세부 옵션)를 summary에 표시
                summary.textContent = radio.nextSibling.textContent.trim();
                detail.removeAttribute('open'); // 선택 후 드롭다운 닫기
            }
        });
    });
});
    // 이미지 미리보기 함수
    window.previewImage = function() {
        const fileInput = document.getElementById('customImages');
        const previewImage = document.getElementById('previewImg');
        console.log('펑션 안쪽');
        // 선택한 파일이 있는지 확인
        if (fileInput.files && fileInput.files[0]) {
            const reader = new FileReader();

            reader.onload = function (e) {
                previewImage.src = e.target.result; // 선택한 파일 미리보기
            };

            reader.readAsDataURL(fileInput.files[0]); // 파일 읽기
        } else {
            // 파일이 선택되지 않은 경우 기본 이미지로 설정
            previewImage.src = "/img/custom-default.jpg"; // 기본 이미지로 설정
        }
    };

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

        // 라디오 버튼의 값 추가
        const option1 = document.querySelector('input[name="option1"]:checked');
        if (option1) {
            formData.append('brand', option1.value);  // 'brand'는 DTO에서의 필드 이름
        }

        const option2 = document.querySelector('input[name="option2"]:checked');
        if (option2) {
            formData.append('syrup', option2.value);
        }

        const option3 = document.querySelector('input[name="option3"]:checked');
        if (option3) {
            formData.append('whipped', option3.value);
        }

        const option4 = document.querySelector('input[name="option4"]:checked');
        if (option4) {
            formData.append('shot', option4.value);
        }

        const option5 = document.querySelector('input[name="option5"]:checked');
        if (option5) {
            formData.append('milk', option5.value);
        }

        const option6 = document.querySelector('input[name="option6"]:checked');
        if (option6) {
            formData.append('coffeeType', option6.value);
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
            customModal.style.display = 'none'; // 성공 후 모달 닫기
            customForm.reset(); // 입력 필드 초기화

            // 게시글 리스트를 다시 불러와서 업데이트
            fetch('/api/customList')
                .then(response => response.json())
                .then(data => {
                    displayCustomList(data); // 최신 게시글 리스트 업데이트
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
                displayCustomList(data); // 필터링된 결과 표시
            })
            .catch(error => console.error('Error:', error));
    });

    // 댓글 등록 함수
    function submitComment() {
        const commentText = document.getElementById('commentBox').value;
        if (commentText.trim() === '') return; // 빈 댓글은 등록하지 않음

        const memberPK = document.getElementById('loginMemberPK').value;

        const formData = new FormData();
        formData.append('text', commentText);
        formData.append('postId', currentPostId);
        formData.append('memberPK', memberPK);

        // 댓글 저장 요청
        fetch('/api/comments', {
            method: 'POST',
            body: formData
        })
        .then(response => response.json())
        .then(comment => {
            const commentDiv = document.createElement('div');

            // 사용자 아바타 추가
            const avatarImg = document.createElement('img');
            avatarImg.src = `/images/${comment.imgReal}`; // 실제 아바타 이미지 경로로 변경
            avatarImg.className = 'user-avatar';

            // 댓글 텍스트 추가
            const commentText = document.createElement('div');
            commentText.className = 'comment-text';
            commentText.textContent = comment.text;

            // 댓글 타임스탬프 추가
            const timestamp = document.createElement('div');
            timestamp.className = 'comment-timestamp';
            timestamp.textContent = comment.createdDate; // 현재 시간을 포맷하여 표시

            // 댓글 항목에 아바타, 텍스트, 타임스탬프 추가
            commentDiv.appendChild(avatarImg);
            commentDiv.appendChild(commentText);
            commentDiv.appendChild(timestamp);

            // 댓글 리스트에 추가
            document.getElementById('commentsList').appendChild(commentDiv);

            // 댓글 입력창 초기화
            document.getElementById('commentBox').value = '';
        })
        .catch(error => console.error('Error adding comment:', error));
    }

    // 댓글 등록 버튼 클릭 이벤트
    document.getElementById('submitComment').addEventListener('click', submitComment);

    // 엔터키로 댓글 등록
    document.getElementById('commentBox').addEventListener('keydown', function(event) {
        if (event.key === 'Enter') {
            event.preventDefault(); // 기본 동작(줄바꿈 방지)
            submitComment(); // 댓글 등록 함수 호출
        }
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

                    // 사용자 아바타 추가
                    const avatarImg = document.createElement('img');
                    avatarImg.src = `/images/${comment.imgReal}`; // 실제 아바타 이미지 경로로 변경
                    avatarImg.className = 'user-avatar';

                    // 댓글 텍스트 추가
                    const commentText = document.createElement('div');
                    commentText.className = 'comment-text';
                    commentText.textContent = comment.text;

                    // 댓글 타임스탬프 추가
                    const timestamp = document.createElement('div');
                    timestamp.className = 'comment-timestamp';
                    timestamp.textContent = comment.createdDate;

                    // 댓글 항목에 아바타, 텍스트, 타임스탬프 추가
                    commentDiv.appendChild(avatarImg);
                    commentDiv.appendChild(commentText);
                    commentDiv.appendChild(timestamp);

                    // 댓글 리스트에 추가
                    commentsList.appendChild(commentDiv);
                });
            })
            .catch(error => console.error('Error loading comments:', error));
    }


    // 커피 리스트를 화면에 표시하는 함수
    function displayCustomList(data) {
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
                detailAuthor.innerHTML = `<span style="font-size:16px; color:black; font-weight:bold;">작성자 : </span> ${custom.memberId}`;
                detailDate.innerHTML = `<span style="font-size:16px; color:black; font-weight:bold;">작성일 : </span> ${custom.createdDate}`;
                tagOption1.innerHTML = custom.brand !== null  && custom.brand !== "null" ? `#${custom.brand}` : ``;
                tagOption2.innerHTML = custom.syrup !== null && custom.syrup !== "null" ? `#${custom.syrup}` : ``;
                tagOption3.innerHTML = custom.whipped !== null && custom.whipped !== "null" ? `#${custom.whipped}` : ``;
                tagOption4.innerHTML = custom.shot !== null && custom.shot !== "null" ? `#${custom.shot}` : ``;
                tagOption5.innerHTML = custom.milk !== null && custom.milk !== "null" ? `#${custom.milk}` : ``;
                tagOption6.innerHTML = custom.coffeeType !== null && custom.coffeeType !== "null" ? `#${custom.coffeeType}` : ``;

                // 댓글 섹션 초기화 및 댓글 불러오기
                initializeCommentSection(custom.id);

                // 좋아요 상태 확인 요청
                fetch(`/api/likeStatus?postId=${custom.id}&memberId=${memberId}`)
                    .then(response => response.json())
                    .then(data => {
                        // 사용자가 좋아요를 눌렀다면
                        if (data.hasLiked) {
                            likeBtn.textContent = '취소 🤍'; // 버튼 텍스트 변경
                        } else {
                            likeBtn.textContent = '좋아요 ❤️'; // 버튼 텍스트 변경
                        }
                    })
                    .catch(error => console.error('Error checking like status:', error));


                // 모달 열기
                detailModal.style.display = 'flex';
            });

            // 카드 내용 추가
            customCard.innerHTML = `
                <h3 style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis; width: 180px;">${custom.customTitle}</h2>
                </br></br>
                <img src="/images/${custom.imgReal}" alt="${custom.customTitle}">
                </br>
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
            displayCustomList(data);
        })
        .catch(error => console.error('Error:', error));
});
