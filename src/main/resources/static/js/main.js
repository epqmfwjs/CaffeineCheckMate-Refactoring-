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
                    const wrapper = document.createElement('div');
                    wrapper.classList.add('item1-wrapper');

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
                    wrapper.appendChild(imgElement);
                    wrapper.appendChild(textDiv);

                    // item1 컨테이너에 wrapper 추가
                    item1Container.appendChild(wrapper);

                });
            }
    });