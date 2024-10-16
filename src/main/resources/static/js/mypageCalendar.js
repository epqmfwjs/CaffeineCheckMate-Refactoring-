document.addEventListener('DOMContentLoaded', function() {
    const memberId = document.getElementById('loginMemberPK').value; // memberId의 값 가져오기

    var calendarEl = document.getElementById('fullCalendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        locale: 'ko',
        headerToolbar: {
            left: 'prev,next',
            center: 'title',
            right: 'dayGridMonth'
        },
        events: `/api/calendarEvents?memberId=${memberId}`,
        eventContent: function(arg) {

            let percentage = arg.event.extendedProps.percentage;
            let backgroundColor = '';
            let emoji = '';

            // 카페인 섭취량에 따른 퍼센트 계산 및 색상, 이모지 설정
            if (percentage < 40) {
                backgroundColor = 'green';
                emoji = '😊';
            } else if (percentage < 80) {
                backgroundColor = 'orange';
                emoji = '😐';
            } else if (percentage < 100){
                backgroundColor = 'red';
                emoji = '😨';
            } else {
                backgroundColor = 'black';
                emoji = '☠️';
            }

            // 이벤트 요소에 커스텀 바와 이모지 추가
            let customHtml = {
                html: `<div class="custom-event" style="background-color: white; padding: 5px; border-radius: 0px; width:100%;">
                         <div style="display: flex; align-items: center; justify-content: flex-start;">
                           <span style="margin-right: 5px;">${emoji}</span>
                           <div style="background-color: lightgray; width: 100%; height: 15px; border-radius: 5px; overflow: hidden;">
                             <div style="background-color:${backgroundColor}; width: ${percentage}%; height: 100%;">
                               <span style="display: inline-block; width: 100%;">${arg.event.title}</span>
                             </div>
                           </div>
                         </div>
                       </div>`
            };
            return customHtml;
        }
    });

    calendar.render();
});
