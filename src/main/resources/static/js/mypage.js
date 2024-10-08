document.addEventListener("DOMContentLoaded", function() {
    const memberId = document.getElementById('loginMemberPK').value;
    let currentPostId = null; // 게시물 아이디변수

    // 즐겨찾기 디테일 모달 관련 요소
    const coffeeListDiv = document.getElementById('coffeeList');
    const coffeeModal = document.getElementById('coffeeModal');
    const modalImage = document.getElementById('modalImage');
    const modalCoffeeName = document.getElementById('modalCoffeeName');
    const modalCoffeeBrand = document.getElementById('modalCoffeeBrand');
    const modlCoffeeType = document.getElementById('modlCoffeeType');
    const modalCoffeeContent = document.getElementById('modalCoffeeContent');
    const modalCoffeeCalorie = document.getElementById('modalCoffeeCalorie');
    const modalCoffeeCaffeine = document.getElementById('modalCoffeeCaffeine');
    const modalCoffeeSaccharide = document.getElementById('modalCoffeeSaccharide');
    const closeModal = document.querySelector('.close');


    // 좋아요 디테일 모달 관련 요소
    const detailModal = document.getElementById('detailModal'); // 디테일 모달 창
    const closeDetailModalBtn = detailModal.querySelector('.close'); // 디테일 모달 닫기 버튼
    const detailTitle = document.getElementById('detailTitle'); // 제목 표시 영역
    const detailImage = document.getElementById('detailImage'); // 이미지 표시 영역
    const detailContent = document.getElementById('detailContent'); // 내용 표시 영역
    const detailAuthor = document.getElementById('memberId'); // 작성자 표시 영역
    const detailDate = document.getElementById('createdDate'); // 날짜 표시 영역

    // 즐겨찾기 모달 클로즈
    closeModal.addEventListener('click',function(){
        coffeeModal.style.display = 'none';
        if (!isFavorited) {
            // 즐겨찾기가 취소된 경우 리스트에서 해당 항목을 제거
            favoriteListData = favoriteListData.filter(favorite => favorite.id !== currentPostId);
            displayFavoriteList();  // 리스트 다시 렌더링
        }
    });

    // 즐겨찾기 모달 외부 클릭 클로즈
    window.addEventListener('click',function(event){
        if(event.target === coffeeModal) {
            coffeeModal.style.display = 'none';
        }
    });


    //즐겨찾기관련
    const favoriteBtn = document.getElementById('favoriteBtn');
    let isFavorited = false; // 즐겨찾기 상태

    //즐겨찾기 기능
    favoriteBtn.addEventListener('click', function() {
        console.log('즐겨찾기버튼클릭');
        console.log(" 즐겨찾기 상태 : "+isFavorited)

        const postId = currentPostId;
        console.log("postId :  " + postId);

        const favoriteRequestDto = {
            postId: postId,
            memberId: memberId
        };

        // 즐겨찾기 상태 유무 로 요청 결정
        if(!isFavorited){
            // 즐겨찾기 추가부분
            fetch('/api/favorite',{
                method: 'POST',
                headers: {
                    'Content-Type':'application/json'
                },
                body: JSON.stringify(favoriteRequestDto)
            })
            .then(response => response.json())
            .then(data => {
                console.log('즐찾추가성공',data);
                isFavorited = true;
                favoriteBtn.textContent = '즐겨찾기 취소';
            })
            .catch(error => console.error('Error:',error));
        }else {
            // 즐겨찾기 취소
            fetch('/api/favorite', {
                method: 'DELETE',
                headers: {  // 콤마 추가
                    'Content-Type': 'application/json'
                },
                body:JSON.stringify(favoriteRequestDto)
            })
            .then(response => response.json())
            .then(data =>{
                console.log('즐찾취소성공', data);
                isFavorited = false;
                favoriteBtn.textContent = '즐겨찾기';
                coffeeModal.style.display = 'none';
                favoriteListData = favoriteListData.filter(favorite => favorite.id !== postId);
                displayFavoriteList();
            })
            .catch(error => console.error('Error:',error))
        }
    });


    let favoritePage = 1;
    const favoritesPerPage = 5; // 페이지당 즐겨찾기 수
    let favoriteListData = []; // 전체 즐겨찾기 데이터

    let likePage = 1;
    const likesPerPage = 5; // 페이지당 좋아요 수
    let likeListData = []; // 전체 좋아요 데이터

    // 데이터 요청을 처리하는 공통 함수
    function fetchData(url, displayFunction) {
        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                displayFunction(data);
            })
            .catch(error => console.error('Error:', error));
    }

    // 즐겨찾기 리스트 요청
    fetchData(`/api/favoriteList?memberId=${memberId}`, data => {
        favoriteListData = data;
        displayFavoriteList();
    });

    // 멤버정보 요청
    fetchData(`/api/getMemberInfo?memberId=${memberId}`, displayMemberInfoList);

    // 좋아요 리스트 요청
    fetchData(`/api/likeList?memberId=${memberId}`, data => {
        likeListData = data;
        displayLikeList();
    });



// ----------------------------------------  즐겨찾기 리스트 영역 ------------------------


    // 페이징 처리된 즐겨찾기 리스트를 화면에 표시하는 함수
    function displayFavoriteList() {
        console.log("displayFavoriteList(favorites) 호출됨");
        const favoriteListDiv = document.getElementById('favoriteList');
        favoriteListDiv.innerHTML = '';  // 기존 목록 초기화

        const start = (favoritePage - 1) * favoritesPerPage;
        const end = start + favoritesPerPage;
        const paginatedFavorites = favoriteListData.slice(start, end);

        paginatedFavorites.forEach(favorite => {
            const favoriteCard = document.createElement('div');
            favoriteCard.classList.add('favoriteCard');

            // 즐겨찾기 항목을 추가
            favoriteCard.innerHTML = `
                <h3 style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis; width: 150px;">
                    ${favorite.coffeeName}
                </h3>
                </br>
                <img
                    class="likeListCard-like-img"
                    src="${favorite.imgReal}"
                    alt="${favorite.coffeeName}"
                    style="width: 170px; height: 150px;"
                />
            `;
            // 클릭시 모달 띄우며 디테일 보여주기
            favoriteCard.addEventListener('click',function(){

                currentPostId = favorite.id;  // 게시글 클릭할때 게시글 아이디 가져와 저장

                console.log('memberId : ' + memberId);
                console.log('currentPostId : ' + currentPostId);

                // 디테일 모달 띄울때 게시물즐겨찾기 상태 가져옴
                fetch(`/api/favoriteStatus?postId=${currentPostId}&memberId=${memberId}`)
                    .then(response => response.json())
                    .then(data => {
                        console.log("서버 응답:", data);  // 서버에서 받은 전체 데이터 출력

                        isFavorited = data.favorited;  // 요청해서 받아온 즐겨찾기 상태적용

                        console.log("data.favorited : " + data.favorited);
                        console.log("초기 즐겨찾기 상태: " + isFavorited);

                        if (isFavorited) {
                            favoriteBtn.textContent = '즐겨찾기 취소';
                        } else {
                            favoriteBtn.textContent = '즐겨찾기';
                        }
                    })
                    .catch(error => console.error('Error fetching favorite status:', error));

                //모달 정보 채우기
                modalImage.src = favorite.imgReal;
                modalCoffeeName.textContent = favorite.coffeeName;
                modalCoffeeBrand.textContent = `브랜드: ${favorite.coffeeBrand}`;
                modlCoffeeType.textContent = `종류: ${favorite.coffeeType}`;
                modalCoffeeContent.textContent = `설명: ${favorite.coffeeContent}`;
                modalCoffeeCaffeine.textContent = `카페인: ${favorite.caffeine}ml`;
                modalCoffeeCalorie.textContent = `칼로리: ${favorite.calorie}cal`;
                modalCoffeeSaccharide.textContent = `당분: ${favorite.saccharide}g`;

                coffeeModal.style.display = 'flex';

            });

            favoriteListDiv.appendChild(favoriteCard);  // 목록에 추가
        });

        // 페이지 정보 표시
        document.getElementById('favoritePageInfo').innerText = `페이지 ${favoritePage} / ${Math.ceil(favoriteListData.length / favoritesPerPage)}`;
        document.getElementById('favoritePrev').disabled = favoritePage === 1; // 이전 버튼 비활성화
        document.getElementById('favoriteNext').disabled = favoritePage >= Math.ceil(favoriteListData.length / favoritesPerPage); // 다음 버튼 비활성화
    }


// ----------------------------------------  멤버정보 영역 ------------------------


    // 멤버정보를 화면에 표시하는 함수
    function displayMemberInfoList(memberInfo) {
        console.log("displayMemberInfoList(memberInfo) 호출됨");
        const memberInfoListDiv = document.getElementById('memberInfoList');
        memberInfoListDiv.innerHTML = ''; // 기존 목록 초기화

        const memberCard = document.createElement('div');
        memberCard.classList.add('memberCard');

        // 멤버 정보를 추가
        memberCard.innerHTML = `
            <img src="/images/${memberInfo.imgReal}" alt="Member Image" style="width: 170px; height: 150px;">
            <h2>아이디: ${memberInfo.memberId}</h2>
            <p>회원명: ${memberInfo.memberName}</p>
            <p>나이: ${memberInfo.memberAge}세</p>
            <p>E-Mail: ${memberInfo.memberEmail}</p>
            <p>Tel: ${memberInfo.memberPhone}</p>
            <p>성별: ${memberInfo.memberGender}</p>
            <p>체중: ${memberInfo.memberWeight}Kg</p>
        `;

        memberInfoListDiv.appendChild(memberCard);  // 목록에 추가
    }

// ----------------------------------------  좋아요 리스트 영역 ------------------------


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
            if (likeBtn.textContent === '좋아요') {
                likeBtn.textContent = '취소';
            } else {
                likeBtn.textContent = '좋아요';

                // 좋아요 취소 시 리스트에서 항목 삭제
                likeListData = likeListData.filter(likeList => likeList.id !== currentPostId);
                displayLikeList(); // 좋아요 리스트 다시 렌더링

            }
        })
            .catch(error => console.error('Error:', error));
    });

    // 좋아요 디테일 모달 닫기
closeDetailModalBtn.addEventListener('click', function() {
    detailModal.style.display = 'none'; // 디테일 모달 닫기

    if (likeBtn.textContent === '좋아요') {
        // 모달이 닫힐 때 좋아요가 취소된 경우 리스트에서 항목 삭제
        likeListData = likeListData.filter(likeList => likeList.id !== currentPostId);
        displayLikeList();  // 리스트 다시 렌더링
    }
});

    // 좋아요 모달 외부 클릭 시 닫기
    window.addEventListener('click', function(event) {
    if (event.target === detailModal) {
        detailModal.style.display = 'none'; // 모달 창을 숨김
    }
    });


    // 페이징 처리된 좋아요 리스트를 화면에 표시하는 함수
    function displayLikeList() {
        console.log("displayLikeList(likeLists) 호출됨");
        const likeListDiv = document.getElementById('likeList');
        likeListDiv.innerHTML = '';  // 기존 목록 초기화

        const start = (likePage - 1) * likesPerPage;
        const end = start + likesPerPage;
        const paginatedLikes = likeListData.slice(start, end);

        paginatedLikes.forEach(likeList => {
            const likeListCard = document.createElement('div');
            likeListCard.classList.add('likeListCard');
            // 클릭 시 상세 페이지 모달로 열기
            likeListCard.addEventListener('click', function() {

                // 모달에 상세 정보 표시
                detailTitle.innerHTML = `${likeList.customTitle}&nbsp;&nbsp; <span class="heart-style">❤️ ${likeList.likesCount}</span>`;
                detailImage.src = `/images/${likeList.imgReal}`;
                detailContent.textContent = likeList.customContent;
                detailAuthor.textContent = `작성자: ${likeList.memberId}`;
                detailDate.textContent = `작성일: ${likeList.createdDate}`;

                // 댓글 섹션 초기화 및 댓글 불러오기
                initializeCommentSection(likeList.id);

                // 좋아요 상태 확인 요청
                fetch(`/api/likeStatus?postId=${likeList.id}&memberId=${memberId}`)
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

            // 좋아요 항목을 추가
            likeListCard.innerHTML = `
                <h3 style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis; width: 150px;">
                    ${likeList.customTitle}
                </h3>
                </br>
                <img
                    class="likeListCard-like-img"
                    src="/images/${likeList.imgReal}"
                    alt="${likeList.customTitle}"
                    style="width: 170px; height: 150px;"
                />
            `;

            likeListDiv.appendChild(likeListCard);  // 목록에 추가
        });

        // 페이지 정보 표시
        document.getElementById('likePageInfo').innerText = `페이지 ${likePage} / ${Math.ceil(likeListData.length / likesPerPage)}`;
        document.getElementById('likePrev').disabled = likePage === 1; // 이전 버튼 비활성화
        document.getElementById('likeNext').disabled = likePage >= Math.ceil(likeListData.length / likesPerPage); // 다음 버튼 비활성화
    }


// ----------------------------------------  페이징처리 영역 ------------------------
    // 이전 및 다음 버튼 클릭 이벤트 핸들러
    document.getElementById('favoritePrev').addEventListener('click', () => {
        if (favoritePage > 1) {
            favoritePage--;
            displayFavoriteList();
        }
    });

    document.getElementById('favoriteNext').addEventListener('click', () => {
        if (favoritePage < Math.ceil(favoriteListData.length / favoritesPerPage)) {
            favoritePage++;
            displayFavoriteList();
        }
    });

    document.getElementById('likePrev').addEventListener('click', () => {
        if (likePage > 1) {
            likePage--;
            displayLikeList();
        }
    });

    document.getElementById('likeNext').addEventListener('click', () => {
        if (likePage < Math.ceil(likeListData.length / likesPerPage)) {
            likePage++;
            displayLikeList();
        }
    });
});
