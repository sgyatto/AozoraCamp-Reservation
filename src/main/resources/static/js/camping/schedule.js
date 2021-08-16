"use strict";

dayjs.locale("ja")

document.addEventListener("DOMContentLoaded", function() {
  // サイトタイプID取得
  const siteTypeId = document.getElementById("siteTypeId").value;

  // eventsのURL生成
  const eventsUrl = `/api/schedule/siteTypes/${siteTypeId}`;

  // カレンダー表示
  let calendarEl = document.getElementById("calendar");
  let calendar = new FullCalendar.Calendar(calendarEl, {

    // 日本語
    locale: "ja",
    // 全量表示
    height: "auto",
    // 月表示
    initialView: "dayGridMonth",
    // 1日から開始
    firstDay: 1,
    // ヘッダー位置
    headerToolbar: {
      left: "today",
      center: "title",
      right: "prev,next"
    },
    // 日付表記
    dayCellContent: function (e) {
      e.dayNumberText = e.dayNumberText.replace("日", "");
    },
    // 表示期間
    validRange: function() {
      return {
        start: dayjs().add(1, "day").format("YYYY-MM-DD"),
        end: dayjs().add(91, "day").format("YYYY-MM-DD")
      };
    },
    events: function fetchSiteAvailability(info, successCallback, failureCallback) {
      // 取得開始日
      let start = dayjs(info.start).format("YYYY-MM-DD");
      // 取得終了日
      let end = dayjs(info.end).subtract(1, "day").format("YYYY-MM-DD");
      // APIから対象期間のデータをfetch
      let uri = new URL(window.location.href);
      fetch(`${uri.origin}/api/schedule/siteTypes/${siteTypeId}?start=${start}&end=${end}`)
        .then(response => {
          if (!response.ok) {
            console.error("error_response", response);
            failureCallback();
          } else {
            response.json().then(result => {
              successCallback(result);
            })
          }
        }).catch(error => {
          console.error(error);
      })
    },
    eventColor: "transparent",
    eventTextColor: "navy",
  });
  calendar.render();
});

/**
 * サイトタイプ一覧に戻る
 */
function backward() {
  location.href="/camping/siteTypes";
}
