'use strict';

/**
 * スケジュールに戻る
 */
function backward() {
  const siteTypeId = document.getElementById("siteTypeId").value;
  location.href=`/camping/schedule?siteTypeId=${siteTypeId}`
}