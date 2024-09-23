    document.addEventListener("DOMContentLoaded", function() {
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

        //    //좋아요버튼
        //    likeBtn.addEventListener('click', function() {
        //        console.log('좋아요버튼클릭');
        //
        //    if (currentPostId === null) {
        //        console.error('현재 게시물 ID가 없습니다.'); // 게시물 ID가 없을 경우 오류 메시지
        //        return;
        //    }
        //
        //        console.log('게시물번호' + currentPostId);
        //        // fetch 요청
        //        fetch('/api/like/' + currentPostId, { // 현재 게시물 ID를 URL에 포함
        //            method: 'POST'
        //        })
        //        .then(response => {
        //            if (!response.ok) {
        //                throw new Error('응답 실패!');
        //            }
        //            return response.json(); // JSON 응답을 반환
        //        })
        //        .then(result => {
        //            console.log('좋아요가 등록되었습니다:', result); // 성공 메시지 출력
        //        })
        //        .catch(error => console.error('Error:', error)); // 오류 처리
        //    });

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