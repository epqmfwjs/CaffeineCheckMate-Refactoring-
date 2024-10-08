    document.addEventListener("DOMContentLoaded", function() {
        const memberId = document.getElementById('loginMemberPK').value;
        const isLoggedIn = memberId && memberId.trim() !== "";

        let favoritePage = 1;
        const favoritesPerPage = 5; // 페이지당 즐겨찾기 수
        let favoriteListData = []; // 전체 즐겨찾기 데이터

        const addFavoriteBtnClick = document.getElementById("addFavoriteBtn");
        const calculatorBtn = document.getElementById("calculatorBtn");

        //계산기 임시 버튼
        calculatorBtn.addEventListener('click', function() {
            fetch(`/api/calculator`)
                .then(response => response.json())
                .then(data => {
                    displayCalculator(data);
                })
                .catch(error => console.error('Error : ',error));
        });


        // 메인페이지 즐겨찾기 목록관련
        addFavoriteBtnClick.addEventListener('click', function() {
            // memberId가 null 또는 빈 문자열("")일 때 로그인 화면으로 리다이렉트
            if (memberId && memberId.trim() !== "") {
                window.location.href = '/product'; // 제품게시판 이동
            } else {
                window.location.href = '/member/login'; // 로그인 페이지로 이동
            }
        });

        //로그인 여부를 확인해 사용자 정보 요청
        if(memberId){
            fetch(`/api/getMemberInfo?memberId=${memberId}`)
                .then(response => response.json())
                .then(data => {
                    displayMemberInfo(data);
                })
                .catch(error => console.error('Error : ',error));
        }else{
            displayLoginLink();
        }

        //사용자 정보 화면 표시 함수
        function displayMemberInfo(memberInfo) {
            console.log('displayMemberInfo() 호출됨');
            console.log("favoriteListData 길이: ", favoriteListData.length); // 추가된 로그
            console.log("isLoggedIn: ", isLoggedIn); // 추가된 로그

            const item2Box = document.getElementById('item2Box');
            item2Box.innerHTML = '';


            const wrapper2 = document.createElement('div');
            wrapper2.classList.add('item2-wrapper');

            const imgElement = document.createElement('img');
            imgElement.classList.add('memberInfo-img');

            imgElement.src = `/images/${memberInfo.imgReal}`;
            imgElement.alt = memberInfo.memberName;




            const nameElement = document.createElement('div');
            nameElement.classList.add('memberInfo-text');
            nameElement.innerHTML = `${memberInfo.memberName}&nbsp;&nbsp; 님`;

            wrapper2.appendChild(imgElement);
            wrapper2.appendChild(nameElement);

            item2Box.appendChild(wrapper2);

        }

        // 비 로그인시 링크
        function displayLoginLink() {
            const item2Box = document.getElementById('item2Box');
            item2Box.innerHTML = '';

            const loginLink = document.createElement('a');
            loginLink.href = '/member/login';
            loginLink.textContent = '로그인';

            item2Box.appendChild(loginLink);
        }

        // 초기 로드 시 요청
        if (isLoggedIn) {
            fetch(`/api/favoriteList?memberId=${memberId}`)
                .then(response => response.json())
                .then(data => {
                    favoriteListData = data; // 전체 즐겨찾기 데이터를 저장
                    displayFavoriteList(data);
                })
                .catch(error => console.error('Error:', error));
        } else {
            // 로그인하지 않았을 때 즐겨찾기 버튼 표시
            document.getElementById('addFavoriteBtn').style.display = 'block';
        }
            // 즐겨찾기 리스트를 화면에 표시하는 함수
            function displayFavoriteList(favoriteListData) {
                console.log("displayFavoriteList(favorites) 호출됨")
                const item1Container = document.getElementById('favoriteList');  // 즐겨찾기 목록을 표시할 DOM 요소

                item1Container.innerHTML = '';  // 기존 목록 초기화

                const start = (favoritePage - 1) * favoritesPerPage;
                const end = start + favoritesPerPage;
                const paginatedFavorites = favoriteListData.slice(start, end);

            // 로그인 여부에 따라 처리
            if (isLoggedIn) {
                 console.log("로그인은 됬음 favoriteListData 길이: ", favoriteListData.length);
                if (favoriteListData.length === 0) {
                    // 즐겨찾기가 없을 경우 "즐겨찾기 추가하러 가기" 버튼 표시
                    document.getElementById('addFavoriteBtn').style.display = 'block';
                    console.log("즐겨찾기가 없습니다. 버튼 표시."); // 추가된 로그
                } else {
                    paginatedFavorites.forEach(favorite => {
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

                    document.getElementById('favoritePageInfo').innerText = `페이지 ${favoritePage} / ${Math.ceil(favoriteListData.length / favoritesPerPage)}`;
                    document.getElementById('favoritePrev').disabled = favoritePage === 1; // 이전 버튼 비활성화
                    document.getElementById('favoriteNext').disabled = favoritePage >= Math.ceil(favoriteListData.length / favoritesPerPage); // 다음 버튼 비활성화

                    // "즐겨찾기 추가하러 가기" 버튼 숨기기
                    document.getElementById('addFavoriteBtn').style.display = 'none';
                }
            } else {
                // 로그인하지 않은 경우 "즐겨찾기 추가하러 가기" 버튼 표시
                document.getElementById('addFavoriteBtn').style.display = 'block';
            }
        }

            // 이전 버튼 클릭 이벤트
            document.getElementById('favoritePrev').addEventListener('click', function() {
                if (favoritePage > 1) {
                    favoritePage--;
                    displayFavoriteList(favoriteListData); // 리스트 재표시
                }
            });

            // 다음 버튼 클릭 이벤트
            document.getElementById('favoriteNext').addEventListener('click', function() {
                if (favoritePage < Math.ceil(favoriteListData.length / favoritesPerPage)) {
                    favoritePage++;
                    displayFavoriteList(favoriteListData); // 리스트 재표시
                }
            });

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
                        }, 500); // 페이드 아웃 효과 지속 시간
                    } else {
                        updateCustomDisplay(); // 초기에는 바로 실행
                    }
                }, 3000);
            }
    });