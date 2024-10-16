document.addEventListener("DOMContentLoaded", function () {
    // 게시물상세모달관련
    const searchBar = document.getElementById('searchBar');
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

    const memberId = document.getElementById('loginMemberPK').value;
    let currentPostId = null; // 게시물 아이디변수

    // 즐겨찾기관련
    const favoriteBtn = document.getElementById('favoriteBtn');
    let isFavorited = false; // 즐겨찾기 상태

    // 모달 클로즈
    closeModal.addEventListener('click', function () {
        coffeeModal.style.display = 'none';
    });

    // 모달 외부 클릭 클로즈
    window.addEventListener('click', function (event) {
        if (event.target === coffeeModal) {
            coffeeModal.style.display = 'none';
        }
    });

    // 즐겨찾기 기능
    favoriteBtn.addEventListener('click', function () {
        console.log('즐겨찾기버튼클릭');
        console.log("즐겨찾기 상태 : " + isFavorited)

        const postId = currentPostId;
        console.log("postId : " + postId);

        const favoriteRequestDto = {
            postId: postId,
            memberId: memberId
        };

        // 즐겨찾기 상태 유무로 요청 결정
        if (!isFavorited) {
            // 즐겨찾기 추가부분
            fetch('/api/favorite', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(favoriteRequestDto)
            })
            .then(response => response.json())
            .then(data => {
                console.log('즐찾추가성공', data);
                isFavorited = true;
                favoriteBtn.textContent = '즐겨찾기 취소';
                modalCoffeeName.innerHTML = `${modalCoffeeName.textContent} <i class="fas fa-star"></i>`; // 노란 별 아이콘으로 변경
            })
            .catch(error => console.error('Error:', error));
        } else {
            // 즐겨찾기 취소
            fetch('/api/favorite', {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(favoriteRequestDto)
            })
            .then(response => response.json())
            .then(data => {
                console.log('즐찾취소성공', data);
                isFavorited = false;
                favoriteBtn.textContent = '즐겨찾기';
                modalCoffeeName.innerHTML = `${modalCoffeeName.textContent} <i class="far fa-star"></i>`; // 투명 별 아이콘으로 변경
            })
            .catch(error => console.error('Error:', error));
        }
    });

    // 검색창에서 입력값이 변할 때마다 검색 요청을 서버로 보냄
    searchBar.addEventListener('input', function () {
        const searchTerm = searchBar.value.toLowerCase();
        fetch(`/api/searchCoffee?keyword=${encodeURIComponent(searchTerm)}`)
            .then(response => response.json())
            .then(data => {
                displayCoffeeList(data); // 필터링된 결과 표시
            })
            .catch(error => console.error('Error:', error));
    });

    // 커피 리스트를 화면에 표시하는 함수
    function displayCoffeeList(data) {
        coffeeListDiv.innerHTML = ''; // 기존 리스트 초기화
        data.forEach(coffee => {
            const coffeeCard = document.createElement('div');
            coffeeCard.classList.add('coffeeCard');

            // 즐겨찾기 상태에 따라 별 아이콘을 결정
            //const starIcon = coffee.favorite ?  '⭐' : '';

            // 여기에 이미지와 텍스트를 추가
            coffeeCard.innerHTML = `
                <h3>${coffee.coffeeName}</h3></br>
                <img src="${coffee.imgReal}" alt="${coffee.coffeeName}">
                <p>브랜드: ${coffee.coffeeBrand}</p>
                <p>종류: ${coffee.coffeeType}</p>
            `;

            // 클릭시 모달 띄우며 디테일 보여주기
            coffeeCard.addEventListener('click', function () {
                currentPostId = coffee.id;  // 게시글 클릭할때 게시글 아이디 가져와 저장

                // 디테일 모달 띄울때 게시물즐겨찾기 상태 가져옴
                fetch(`/api/favoriteStatus?postId=${currentPostId}&memberId=${memberId}`)
                    .then(response => response.json())
                    .then(data => {
                        console.log("서버 응답:", data);  // 서버에서 받은 전체 데이터 출력

                        isFavorited = data.favorited;  // 요청해서 받아온 즐겨찾기 상태적용

                        console.log("data.favorited : " + data.favorited);
                        console.log("초기 즐겨찾기 상태: " + isFavorited);

                        // 별 아이콘을 업데이트
                        if (isFavorited) {
                            favoriteBtn.textContent = '즐겨찾기 취소';
                            modalCoffeeName.innerHTML = `${coffee.coffeeName} <i class="fas fa-star"></i>`; // 노란 별 아이콘
                        } else {
                            favoriteBtn.textContent = '즐겨찾기';
                            modalCoffeeName.innerHTML = `${coffee.coffeeName} <i class="far fa-star"></i>`; // 투명 별 아이콘
                        }
                    })
                    .catch(error => console.error('Error fetching favorite status:', error));

                // 모달 정보 채우기
                modalCoffeeName.innerHTML = `${coffee.coffeeName} <i class="far fa-star"></i>`; // 초기 상태 투명 별 아이콘
                modalImage.src = coffee.imgReal;
                modalCoffeeBrand.textContent = coffee.coffeeBrand;
                modlCoffeeType.textContent = coffee.coffeeType;
                modalCoffeeContent.textContent = coffee.coffeeContent;
                modalCoffeeCaffeine.textContent = `${coffee.caffeine}ml`;
                modalCoffeeCalorie.textContent = `${coffee.calorie}cal`;
                modalCoffeeSaccharide.textContent = `${coffee.saccharide}g`;

                coffeeModal.style.display = 'flex';
            });

            coffeeListDiv.appendChild(coffeeCard);
        });
    }

    // 초기 로드 시 전체 커피 리스트를 표시
    fetch('/api/coffeeList')
        .then(response => response.json())
        .then(data => {
            displayCoffeeList(data);
        })
        .catch(error => console.error('Error:', error));
});
