    document.addEventListener("DOMContentLoaded", function() {
        const memberId = document.getElementById('loginMemberPK').value;

        // 즐겨찾기 리스트 요청
        fetch(`/api/favoriteList?memberId=${memberId}`)
            .then(response => response.json())
            .then(data => {
                displayFavoriteList(data);
            })
            .catch(error => console.error('Error:', error));

            // 즐겨찾기 리스트를 화면에 표시 함수
            function displayFavoriteList(favorites) {
                console.log("displayFavoriteList(favorites) 호출됨")
                const favoriteListDiv = document.getElementById('favoriteList');  // 즐겨찾기 목록을 표시할 DOM 요소
                favoriteListDiv.innerHTML = '';  // 기존 목록 초기화

                favorites.forEach(favorite => {
                    const favoriteCard = document.createElement('div');
                    favoriteCard.classList.add('favoriteCard');

                    // 즐겨찾기 항목을 추가
                    favoriteCard.innerHTML = `
                        <h3 style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis; width: 150px; ">
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

                    favoriteListDiv.appendChild(favoriteCard);  // 목록에 추가
                });
            }
        // 멤버정보 요청
        fetch(`/api/getMemberInfo?memberId=${memberId}`)
            .then(response => response.json())
            .then(data => {
                displayMemberInfoList(data);
            })
            .catch(error => console.error('Error:', error));

            // 멤버정보를 화면에 표시 함수
            function displayMemberInfoList(memberInfo) {
                console.log("displayMemberInfoList(memberInfo) 호출됨")
                const memberInfoListDiv = document.getElementById('memberInfoList');  // 즐겨찾기 목록을 표시할 DOM 요소

                    const memberCard = document.createElement('div');
                    memberCard.classList.add('memberCard');

                    // 즐겨찾기 항목을 추가
                    memberCard.innerHTML = `
                        <img src = "/images/${memberInfo.imgReal}" alt="Member Image" style="width: 170px; height: 150px;">
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

        // 좋아요 리스트 요청
        fetch(`/api/likeList?memberId=${memberId}`)
            .then(response => response.json())
            .then(data => {
                displayLikeList(data);
            })
            .catch(error => console.error('Error:', error));

            // 좋아요 리스트를 화면에 표시 함수
            function displayLikeList(likeLists) {
                console.log("displayLikeList(likeList) 호출됨")
                const likeListDiv = document.getElementById('likeList');  // 즐겨찾기 목록을 표시할 DOM 요소
                likeListDiv.innerHTML = '';  // 기존 목록 초기화

                likeLists.forEach(likeList => {
                    const likeListCard = document.createElement('div');
                    likeListCard.classList.add('likeListCard');

                    // 좋아요 항목을 추가
                    likeListCard.innerHTML = `
                        <h3 style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis; width: 150px; ">
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
            }

    });