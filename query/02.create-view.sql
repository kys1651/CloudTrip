# 주소 값 포함 투어 뷰
CREATE VIEW tour_with_address AS
(
SELECT T.tour_id,
       T.content_type_id,
       TC.name AS content_type,
       T.name,
       address,
       zip_code,
       background_image,
       C.name  AS city,
       O.name  AS town
FROM tour T
         LEFT JOIN tour_content TC ON T.content_type_id = TC.content_id
         LEFT JOIN city C ON T.city_code = C.city_code
         LEFT JOIN town O ON O.town_code = T.town_code AND O.city_code = T.city_code
);

# 상세 내용 포함 투어 뷰
CREATE VIEW tour_with_content AS
(
SELECT T.tour_id,
       T.content_type_id,
       TC.name     AS content_type,
       T.name,
       address,
       zip_code,
       background_image,
       C.name      AS city,
       C.city_code AS city_code,
       O.name      AS town,
       TD.description,
       TD.telephone,
       TD.latitude,
       TD.longitude
FROM tour T
         LEFT JOIN tour_content TC ON T.content_type_id = TC.content_id
         LEFT JOIN city C ON T.city_code = C.city_code
         LEFT JOIN town O ON O.town_code = T.town_code AND O.city_code = T.city_code
         LEFT JOIN tour_detail TD ON T.tour_id = TD.tour_id
);