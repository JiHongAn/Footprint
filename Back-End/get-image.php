<?php
header("Content-Type: text/json; charset=UTF-8");

include './include/api_key.php';
include './include/simple_html_dom.php';

# 파라미터
$contentId = $_GET['contentId'];

# 이미지 파싱
$url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailImage";

$serviceKey = $api_key;
$imageYN = "Y";
$MobileOS = "AND";

# URL 정보 입력
$url = $url . "?serviceKey=" . $serviceKey;
$url = $url . "&contentId=" . $contentId;
$url = $url . "&imageYN=" . $imageYN;
$url = $url . "&MobileOS=" . $MobileOS . "&MobileApp=AppTest&_type=json";

$json = file_get_contents($url);
$json = explode("\"items\":", $json);
$json = explode(",\"numOfRows\":", $json[1]);
$json = str_replace('{"item":', '', $json);
$json = substr($json[0], 0, -1);
$json = substr($json, 1);
$json = substr($json, 0, -1);
echo '{"data":[' . $json . "]}";
