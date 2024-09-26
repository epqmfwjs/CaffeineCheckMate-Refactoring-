    document.addEventListener("DOMContentLoaded", function() {

        const memberId = document.getElementById('loginMemberPK').value;

        // 초기 로드 시 전체 커피 리스트를 표시
        fetch(`/api/favoriteList?memberId=${memberId}`)
            .then(response => response.json())
            .then(data => {
                displayFavoriteList(data);
            })
            .catch(error => console.error('Error:', error));

            // 즐겨찾기 리스트를 화면에 표시하는 함수
            function displayFavoriteList(favorites) {
                console.log("displayFavoriteList(favorites) 호출됨")
                const favoriteListDiv = document.getElementById('favoriteList');  // 즐겨찾기 목록을 표시할 DOM 요소
                favoriteListDiv.innerHTML = '';  // 기존 목록 초기화

                favorites.forEach(favorite => {
                    const favoriteCard = document.createElement('div');
                    favoriteCard.classList.add('favoriteCard');

                    // 즐겨찾기 항목을 추가
                    favoriteCard.innerHTML = `
                        <h3>${favorite.coffeeName}</h3>
                        <p>브랜드: ${favorite.coffeeBrand}</p>
                        <p>종류: ${favorite.coffeeType}</p>
                    `;

                    favoriteListDiv.appendChild(favoriteCard);  // 목록에 추가
                });
            }

    });