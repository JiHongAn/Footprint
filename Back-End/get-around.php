<?php
header("Content-Type: text/json; charset=UTF-8");

include './include/api_key.php';
include './include/simple_html_dom.php';

# 파라미터
$mapX = $_GET['mapX'];
$mapY = $_GET['mapY'];

# 요청
$url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList";
$serviceKey = $api_key;
$numOfRows = "20";
$pageNo = "1";
$MobileOS = "AND";
$MobileApp = "AppTest";
$arrange = "B";
$radius = "3000";

# URL 정보 입력
$url = $url . "?serviceKey=" . $serviceKey;
$url = $url . "&numOfRows=" . $numOfRows;
$url = $url . "&pageNo=" . $pageNo;
$url = $url . "&MobileOS=" . $MobileOS;
$url = $url . "&arrange=" . $arrange;
$url = $url . "&mapX=" . $mapX;
$url = $url . "&mapY=" . $mapY;
$url = $url . "&radius=" . $radius . "&MobileApp=AppTest&_type=json";

# html 파싱
$json = file_get_contents($url);
$json = explode("\"items\":", $json);
$json = explode(",\"numOfRows\":", $json[1]);
$json = str_replace('{"item":', '', $json);
$json = substr($json[0], 0, -1);

echo '{"data":' . $json . '}';
