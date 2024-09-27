document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('fullCalendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth', // 기본 보기 설정
        locale: 'ko', // 한국어로 설정
        headerToolbar: {
            left: 'prev,next',
            center: 'title',
            right: 'dayGridMonth'
        },
        events: '/api/calendarEvents' // 이벤트 데이터를 가져올 경로
    });
    calendar.render();
});
