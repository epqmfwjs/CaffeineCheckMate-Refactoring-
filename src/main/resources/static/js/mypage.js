document.addEventListener("DOMContentLoaded", function() {
    const memberId = document.getElementById('loginMemberPK').value;

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
                <img
                    class="likeListCard-like-img"
                    src="${favorite.imgReal}"
                    alt="${favorite.coffeeName}"
                    style="width: 170px; height: 150px;"
                />
            `;

            favoriteListDiv.appendChild(favoriteCard);  // 목록에 추가
        });

        // 페이지 정보 표시
        document.getElementById('favoritePageInfo').innerText = `페이지 ${favoritePage} / ${Math.ceil(favoriteListData.length / favoritesPerPage)}`;
        document.getElementById('favoritePrev').disabled = favoritePage === 1; // 이전 버튼 비활성화
        document.getElementById('favoriteNext').disabled = favoritePage >= Math.ceil(favoriteListData.length / favoritesPerPage); // 다음 버튼 비활성화
    }

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

            // 좋아요 항목을 추가
            likeListCard.innerHTML = `
                <h3 style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis; width: 150px;">
                    ${likeList.customTitle}
                </h3>
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
