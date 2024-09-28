    document.addEventListener("DOMContentLoaded", function() {
        const memberId = document.getElementById('loginMemberPK').value;

        // 초기 로드 시 요청
        fetch(`/api/favoriteList?memberId=${memberId}`)
            .then(response => response.json())
            .then(data => {
                displayFavoriteList(data);
            })
            .catch(error => console.error('Error:', error));

            // 즐겨찾기 리스트를 화면에 표시하는 함수
            function displayFavoriteList(favorites) {
                console.log("displayFavoriteList(favorites) 호출됨")
                const item1Container = document.getElementById('favoriteList');  // 즐겨찾기 목록을 표시할 DOM 요소

                item1Container.innerHTML = '';  // 기존 목록 초기화

                favorites.forEach(favorite => {
                    const wrapper1 = document.createElement('div');
                    wrapper1.classList.add('item1-wrapper');

                    const imgElement = document.createElement('img');
                    imgElement.classList.add('item1-favorite-img');
                    imgElement.src = favorite.imgReal;
                    imgElement.alt = favorite.coffeeName;  // 이미지가 없는 경우 대체 텍스트
                    imgElement.style.width = '150px'; // 이미지를 일정 크기로 설정
                    imgElement.style.height = '120px'; // 이미지를 일정 크기로 설정
                    console.log("이미지 주소: " + favorite.imgReal);

                    // 텍스트 div 생성
                    const textDiv = document.createElement('div');
                    textDiv.classList.add('item1-favorite-text');
                    textDiv.textContent = favorite.coffeeName;

                    // wrapper에 이미지와 텍스트 추가
                    wrapper1.appendChild(imgElement);
                    wrapper1.appendChild(textDiv);

                    // item1 컨테이너에 wrapper 추가
                    item1Container.appendChild(wrapper1);

                });
            }
        // 메인화면 커스텀게시물 요청
        fetch("/api/mainCustom")
            .then(response => response.json())
            .then(data => {
                displayMainCustom(data);
            })
            .catch(error => console.error('Error:', error));

            let currentCustomIndex = 0;
            let mainCustoms = [];

            // 메인화면 커스텀게시물 표시 함수
            function displayMainCustom(mainCustom) {
                console.log("displayMainCustom(mainCustom) 호출됨")
                mainCustoms = mainCustom.sort((a, b) => b.likeCount - a.likeCount); // 좋아요 순으로 정렬

                function updateCustomDisplay() {
                    const item4Container = document.getElementById("customList");
                    item4Container.innerHTML = ''; // 기존 내용 삭제

                    const custom = mainCustoms[currentCustomIndex]; // 현재 표시할 게시물
                    console.log('커스텀 데이터 : ' + custom);

                    const wrapper4 = document.createElement('div');
                    wrapper4.classList.add('item4-wrapper'); // 기본 상태는 페이드 아웃

                    const textDiv = document.createElement('div');
                    textDiv.classList.add('item4-text');
                    textDiv.innerHTML = `${custom.customTitle}&nbsp;&nbsp; <span class="heart-style">❤️ ${custom.likesCount}</span>`;

                    const imgElement = document.createElement('img');
                    imgElement.classList.add('item4-img');
                    imgElement.src = `/images/${custom.imgReal}`;
                    imgElement.alt = custom.customTitle;

                    const createMember = document.createElement('div');
                    createMember.classList.add('item4-member');
                    createMember.textContent = `작성자 : ${custom.memberId}`;

                    const createDate = document.createElement('div');
                    createDate.classList.add('item4-date');
                    createDate.textContent = custom.createdDate;

                    wrapper4.appendChild(textDiv);
                    wrapper4.appendChild(imgElement);
                    wrapper4.appendChild(createMember);
                    wrapper4.appendChild(createDate);

                    item4Container.appendChild(wrapper4);

                    // 50ms 후에 'show' 클래스를 추가하여 페이드 인 효과
                    setTimeout(() => {
                        wrapper4.classList.add('show');
                    }, 50);

                    // 다음 게시물로 변경 인덱스
                    currentCustomIndex = (currentCustomIndex + 1) % mainCustoms.length;
                }

                // 처음 한 번 즉시 게시물 표시
                updateCustomDisplay();

                // 이후 3초 간격으로 게시물 변경
                setInterval(() => {
                    const item4Container = document.getElementById("customList");
                    const currentItem = item4Container.querySelector('.item4-wrapper');

                    // 페이드 아웃 효과를 위해 'show' 클래스를 제거
                    if (currentItem) {
                        currentItem.classList.remove('show');
                        // 페이드 아웃 완료 후 다음 게시물로 전환
                        setTimeout(() => {
                            updateCustomDisplay();
                        }, 500); // 페이드 아웃 효과 지속 시간과 동일하게 설정 (0.5초)
                    } else {
                        updateCustomDisplay(); // 초기에는 바로 실행
                    }
                }, 3000);
            }
    });