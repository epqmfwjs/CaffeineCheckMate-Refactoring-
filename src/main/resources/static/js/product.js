    document.addEventListener("DOMContentLoaded", function() {

        //게시물상세모달관련
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


        //즐겨찾기관련
        const favoriteBtn = document.getElementById('favoriteBtn');
        let isFavorited = false; // 즐겨찾기 상태

        //모달 클로즈
        closeModal.addEventListener('click',function(){
            coffeeModal.style.display = 'none';
        });

        //모달 외부 클릭 클로즈
        window.addEventListener('click',function(event){
            if(event.target === coffeeModal) {
                coffeeModal.style.display = 'none';
            }
        });

            //즐겨찾기 기능
            favoriteBtn.addEventListener('click', function() {
                console.log('즐겨찾기버튼클릭');

                const postId = currentPostId;
                console.log("postId :  " + postId);

                const favoriteRequestDto = {
                    postId = postId,
                    memberId = memberId
                };

                // 즐겨찾기 상태 유무 로 요청 결정
                if(!isFavorited){
                    // 즐겨찾기 추가부분
                    fetch('/api/favorite',{
                        method: 'POST',
                        headers: {
                            'Content-Type':'application/json'
                        },
                        body: JSON,stringify(favoriteRequestDto)
                    })
                    .then(response => response,json())
                    .then(date => {
                        console.log('즐찾추가성공');
                    })

                }

            });

        // 검색창에서 입력값이 변할 때마다 검색 요청을 서버로 보냄
        searchBar.addEventListener('input', function() {
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

                // 여기에 이미지와 텍스트를 추가
                coffeeCard.innerHTML = `
                    <img src="${coffee.imgReal}" alt="${coffee.coffeeName}">
                    <h3>${coffee.coffeeName}</h3>
                    <p>브랜드: ${coffee.coffeeBrand}</p>
                    <p>종류: ${coffee.coffeeType}</p>
                `;

                // 클릭시 모달 띄우며 디테일 보여주기
                coffeeCard.addEventListener('click',function(){

                    currentPostId = coffee.id;  // 게시글 클릭할때 게시글 아이디 가져와 저장

                    console.log('memberId : ' + memberId);
                    console.log('currentPostId : ' + currentPostId);

                    //모달 정보 채우기
                    modalImage.src = coffee.imgReal;
                    modalCoffeeName.textContent = coffee.coffeeName;
                    modalCoffeeBrand.textContent = `브랜드: ${coffee.coffeeBrand}`;
                    modlCoffeeType.textContent = `종류: ${coffee.coffeeType}`;
                    modalCoffeeContent.textContent = `설명: ${coffee.coffeeContent}`;
                    modalCoffeeCaffeine.textContent = `카페인: ${coffee.caffeine}ml`;
                    modalCoffeeCalorie.textContent = `칼로리: ${coffee.calorie}cal`;
                    modalCoffeeSaccharide.textContent = `당분: ${coffee.saccharide}g`;

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