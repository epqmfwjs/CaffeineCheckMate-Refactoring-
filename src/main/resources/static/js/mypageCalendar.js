document.addEventListener('DOMContentLoaded', function() {
    const memberId = document.getElementById('loginMemberPK');

    var calendarEl = document.getElementById('fullCalendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth', // 기본 보기 설정
        locale: 'ko', // 한국어로 설정
        headerToolbar: {
            left: 'prev,next',
            center: 'title',
            right: 'dayGridMonth'
        },
        events: ``/api/calendarEvents?memberId=${meberId}`, // 이벤트 데이터를 가져올 경로
        eventContent: function(arg) {
            // 이모티콘과 색상을 결정하는 함수
            let caffeineLevel = arg.event.extendedProps.caffeineLevel; // 가져온 데이터에서 카페인 섭취량을 가져옴
            let emoji = '';
            let backgroundColor = '';

            if (caffeineLevel === 'high') {
                emoji = '☕️🔥'; // 높은 카페인 섭취량
                backgroundColor = 'red';
            } else if (caffeineLevel === 'medium') {
                emoji = '☕️'; // 중간 카페인 섭취량
                backgroundColor = 'orange';
            } else if (caffeineLevel === 'low') {
                emoji = '🍵'; // 낮은 카페인 섭취량
                backgroundColor = 'green';
            }

            // 이벤트 요소에 커스텀 콘텐츠를 추가
            let customHtml = {
                html: `<div class="custom-event" style="background-color:${backgroundColor}; padding: 5px; border-radius: 5px;">
                         <span>${emoji} ${arg.event.title}</span>
                       </div>`
            };
            return customHtml;
        }
    });
    calendar.render();
});