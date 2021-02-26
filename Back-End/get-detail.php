<?php
header("Content-Type: text/json; charset=UTF-8");

include './include/api_key.php';
include './include/simple_html_dom.php';

# 파라미터
$contentId = $_GET['contentId'];

# 요청
$url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList";
$url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon";

$serviceKey = $api_key;
$defaultYN = "Y";
$addrinfoYN = "Y";
$overviewYN = "Y";
$MobileOS = "AND";

# URL 정보 입력
$url = $url . "?serviceKey=" . $serviceKey;
$url = $url . "&contentId=" . $contentId;
$url = $url . "&defaultYN=" . $defaultYN;
$url = $url . "&addrinfoYN=" . $addrinfoYN;
$url = $url . "&overviewYN=" . $overviewYN;
$url = $url . "&MobileOS=" . $MobileOS . "&MobileApp=AppTest&_type=json";

# 설명 데이터 파싱
$json = file_get_contents($url);
$json = explode("\"items\":", $json);
$json = explode(",\"numOfRows\":", $json[1]);
$json = str_replace('{"item":', '', $json);
$json = substr($json[0], 0, -1);

# html 제거
$json = str_replace("<br>", "", $json);
$json = str_replace("<br />", "", $json);
# $json = strip_tags($json);
echo '{"data":[' . $json . ']}';
