-- data.sql - Sample data dla Internet Shop
-- Lokalizacja: src/main/resources/data.sql

-- src/main/resources/data.sql
-- ---------------------------------
-- Admin users AdminPassword123
INSERT INTO users (id, username, email, password, role, enabled, banned, verification_code, verification_expiration)
VALUES
    (1, 'admin1', 'admin1@internetshop.local', '$2a$10$sBJrylCEmWVkV6DaTszYFuPaFTDeLUBsvxWKh6dSq2OrPw7WH8ZKC', 'ADMIN', true, false, null, null),
    (2, 'admin2', 'admin2@internetshop.local', '$2a$10$sBJrylCEmWVkV6DaTszYFuPaFTDeLUBsvxWKh6dSq2OrPw7WH8ZKC', 'ADMIN', true, false, null, null),
    (3, 'admin3', 'admin3@internetshop.local', '$2a$10$sBJrylCEmWVkV6DaTszYFuPaFTDeLUBsvxWKh6dSq2OrPw7WH8ZKC', 'ADMIN', true, false, null, null)
    ON CONFLICT (id) DO NOTHING;

-- Regular users TestPassword123
INSERT INTO users (id, username, email, password, role, enabled, banned, verification_code, verification_expiration)
VALUES
    (4, 'user1', 'user1@internetshop.local', '$2a$10$iAGMSHvCY9oPrYXxJ89qe.CJdQbRXE8JUzCJqH5JhA0xsF.PBvyhq', 'USER', true, false, null, null),
    (5, 'user2', 'user2@internetshop.local', '$2a$10$iAGMSHvCY9oPrYXxJ89qe.CJdQbRXE8JUzCJqH5JhA0xsF.PBvyhq', 'USER', true, false, null, null),
    (6, 'user3', 'user3@internetshop.local', '$2a$10$iAGMSHvCY9oPrYXxJ89qe.CJdQbRXE8JUzCJqH5JhA0xsF.PBvyhq', 'USER', true, false, null, null)
    ON CONFLICT (id) DO NOTHING;

-- ==========================================
-- KATEGORIE
-- ==========================================

-- Kategorie główne
INSERT INTO categories (id, name, description, slug, image_url, active, sort_order, parent_id, created_at, updated_at)
VALUES
    (1, 'Elektronika', 'Urządzenia elektroniczne, smartfony, laptopy i gadżety', 'elektronika', 'https://example.com/images/categories/elektronika.jpg', true, 1, NULL, NOW(), NOW()),
    (2, 'Odzież', 'Ubrania dla kobiet, mężczyzn i dzieci', 'odziez', 'https://example.com/images/categories/odziez.jpg', true, 2, NULL, NOW(), NOW()),
    (3, 'Dom i Ogród', 'Artykuły do domu, ogrodu i dekoracje', 'dom-i-ogrod', 'https://example.com/images/categories/dom-ogrod.jpg', true, 3, NULL, NOW(), NOW()),
    (4, 'Sport', 'Sprzęt sportowy, odzież sportowa i suplementy', 'sport', 'https://example.com/images/categories/sport.jpg', true, 4, NULL, NOW(), NOW()),
    (5, 'Książki', 'Literatura, podręczniki i audiobooki', 'ksiazki', 'https://example.com/images/categories/ksiazki.jpg', true, 5, NULL, NOW(), NOW()),
    (6, 'Motoryzacja', 'Części samochodowe i akcesoria', 'motoryzacja', 'https://example.com/images/categories/motoryzacja.jpg', true, 6, NULL, NOW(), NOW())
    ON CONFLICT (id) DO NOTHING;

-- Podkategorie - Elektronika
INSERT INTO categories (id, name, description, slug, image_url, active, sort_order, parent_id, created_at, updated_at)
VALUES
    (11, 'Smartfony', 'Telefony komórkowe wszystkich marek', 'smartfony', 'https://example.com/images/categories/smartfony.jpg', true, 1, 1, NOW(), NOW()),
    (12, 'Laptopy', 'Komputery przenośne i ultrabooki', 'laptopy', 'https://example.com/images/categories/laptopy.jpg', true, 2, 1, NOW(), NOW()),
    (13, 'Słuchawki', 'Słuchawki przewodowe i bezprzewodowe', 'sluchawki', 'https://example.com/images/categories/sluchawki.jpg', true, 3, 1, NOW(), NOW()),
    (14, 'Tablety', 'Tablety i czytniki e-booków', 'tablety', 'https://example.com/images/categories/tablety.jpg', true, 4, 1, NOW(), NOW())
    ON CONFLICT (id) DO NOTHING;

-- Podkategorie - Odzież
INSERT INTO categories (id, name, description, slug, image_url, active, sort_order, parent_id, created_at, updated_at)
VALUES
    (21, 'Odzież męska', 'Ubrania dla mężczyzn', 'odziez-meska', 'https://example.com/images/categories/odziez-meska.jpg', true, 1, 2, NOW(), NOW()),
    (22, 'Odzież damska', 'Ubrania dla kobiet', 'odziez-damska', 'https://example.com/images/categories/odziez-damska.jpg', true, 2, 2, NOW(), NOW()),
    (23, 'Obuwie', 'Buty damskie, męskie i sportowe', 'obuwie', 'https://example.com/images/categories/obuwie.jpg', true, 3, 2, NOW(), NOW()),
    (24, 'Akcesoria', 'Torby, paski, biżuteria', 'akcesoria', 'https://example.com/images/categories/akcesoria.jpg', true, 4, 2, NOW(), NOW())
    ON CONFLICT (id) DO NOTHING;

-- Podkategorie - Dom i Ogród
INSERT INTO categories (id, name, description, slug, image_url, active, sort_order, parent_id, created_at, updated_at)
VALUES
    (31, 'Meble', 'Meble do domu i biura', 'meble', 'https://example.com/images/categories/meble.jpg', true, 1, 3, NOW(), NOW()),
    (32, 'Dekoracje', 'Akcesoria dekoracyjne i oświetlenie', 'dekoracje', 'https://example.com/images/categories/dekoracje.jpg', true, 2, 3, NOW(), NOW()),
    (33, 'Narzędzia', 'Narzędzia ręczne i elektronarzędzia', 'narzedzia', 'https://example.com/images/categories/narzedzia.jpg', true, 3, 3, NOW(), NOW())
    ON CONFLICT (id) DO NOTHING;

-- Podkategorie - Sport
INSERT INTO categories (id, name, description, slug, image_url, active, sort_order, parent_id, created_at, updated_at)
VALUES
    (41, 'Fitness', 'Sprzęt do ćwiczeń w domu i na siłowni', 'fitness', 'https://example.com/images/categories/fitness.jpg', true, 1, 4, NOW(), NOW()),
    (42, 'Rowery', 'Rowery miejskie, górskie i akcesoria', 'rowery', 'https://example.com/images/categories/rowery.jpg', true, 2, 4, NOW(), NOW()),
    (43, 'Sporty wodne', 'Sprzęt do sportów wodnych', 'sporty-wodne', 'https://example.com/images/categories/sporty-wodne.jpg', true, 3, 4, NOW(), NOW())
    ON CONFLICT (id) DO NOTHING;

-- ==========================================
-- PRODUKTY
-- ==========================================

-- Smartfony
INSERT INTO products (id, name, short_description, full_description, sku, slug, price, compare_price, stock_quantity, low_stock_threshold, track_stock, weight, dimensions, active, featured, sort_order, category_id, meta_title, meta_description, view_count, purchase_count, created_at, updated_at)
VALUES
    (1, 'iPhone 15 Pro', 'Najnowszy iPhone z procesorem A17 Pro i aparatem 48MP', 'iPhone 15 Pro z 6.1-calowym wyświetlaczem Super Retina XDR, procesorem A17 Pro wykonanym w technologii 3nm i zaawansowanym systemem aparatów Pro z głównym aparatem 48MP. Dostępny w kolorach: tytanowy naturalny, tytanowy niebieski, tytanowy biały i tytanowy czarny. Obsługuje USB-C i najnowsze funkcje iOS 17.', 'IPHONE-15-PRO-128', 'iphone-15-pro', 4999.00, 5499.00, 25, 5, true, 0.187, '146.6×70.6×8.25 mm', true, true, 1, 11, 'iPhone 15 Pro - Najnowszy smartfon Apple', 'Kup iPhone 15 Pro z procesorem A17 Pro. Najlepsza jakość zdjęć i wydajność w historii iPhone.', 150, 12, NOW(), NOW()),

    (2, 'Samsung Galaxy S24', 'Flagowy smartfon Samsung z AI', 'Samsung Galaxy S24 z procesorem Snapdragon 8 Gen 3, aparatem 50MP z optyczną stabilizacją obrazu i baterią 4000mAh z szybkim ładowaniem 25W. Wyświetlacz Dynamic AMOLED 6.2 cala z częstotliwością odświeżania 120Hz. Funkcje Galaxy AI dla lepszej fotografii i produktywności.', 'SAMSUNG-S24-128', 'samsung-galaxy-s24', 3799.00, 4199.00, 18, 5, true, 0.167, '147×70.6×7.6 mm', true, true, 2, 11, 'Samsung Galaxy S24 - Flagowy Android z AI', 'Samsung Galaxy S24 z najnowszym procesorem i funkcjami AI. Doskonałe zdjęcia i wydajność.', 89, 8, NOW(), NOW()),

    (3, 'Google Pixel 8', 'Czysty Android z najlepszym aparatem', 'Google Pixel 8 z chipem Google Tensor G3, aparatem 50MP z funkcjami AI i czystym systemem Android 14. Unlimited storage w Google Photos dla zdjęć i wideo w najwyższej jakości. 5 lat aktualizacji bezpieczeństwa.', 'PIXEL-8-128', 'google-pixel-8', 2999.00, 3499.00, 22, 5, true, 0.187, '150.5×70.8×8.9 mm', true, false, 3, 11, 'Google Pixel 8 - Czysty Android', 'Google Pixel 8 z chipem Tensor G3 i najlepszym aparatem z funkcjami AI.', 67, 5, NOW(), NOW()),

-- Laptopy
    (4, 'MacBook Air M2', 'Ultracienki laptop Apple z chipem M2', 'MacBook Air z rewolucyjnym chipem M2, 13.6-calowym wyświetlaczem Liquid Retina z technologią True Tone, 8GB zunifikowanej pamięci i 256GB szybkim dyskiem SSD. Wytrzymałość baterii do 18 godzin. Idealny do pracy, nauki i kreatywności.', 'MACBOOK-AIR-M2-256', 'macbook-air-m2', 5499.00, NULL, 12, 3, true, 1.24, '304×215×11.3 mm', true, true, 1, 12, 'MacBook Air M2 - Ultracienki laptop Apple', 'MacBook Air z chipem M2. Niewiarygodna wydajność i długi czas pracy na baterii.', 234, 15, NOW(), NOW()),

    (5, 'Dell XPS 13', 'Kompaktowy laptop biznesowy', 'Dell XPS 13 z procesorem Intel Core i7-1355U 13. generacji, 16GB pamięci LPDDR5, 512GB dyskiem SSD PCIe i ekranem InfinityEdge 13.4 cala FHD+. Wykonany z aluminium i włókna węglowego. Windows 11 Pro.', 'DELL-XPS-13-512', 'dell-xps-13', 4299.00, 4699.00, 8, 2, true, 1.17, '295.7×199.04×15.8 mm', true, false, 2, 12, 'Dell XPS 13 - Laptop biznesowy', 'Dell XPS 13 z procesorem Intel Core i7. Kompaktowy design i wysoka wydajność.', 67, 5, NOW(), NOW()),

-- Słuchawki
    (6, 'AirPods Pro 2', 'Bezprzewodowe słuchawki z redukcją szumów', 'AirPods Pro 2nd generation z aktywną redukcją szumów nowej generacji, trybem przezroczystości i chipem H2 dla lepszej jakości dźwięku. Etui z głośniczkiem i precyzyjnym wyszukiwaniem. Do 6 godzin słuchania z ANC.', 'AIRPODS-PRO-2', 'airpods-pro-2', 1199.00, 1399.00, 45, 10, true, 0.056, '30.9×21.8×24.0 mm', true, true, 1, 13, 'AirPods Pro 2 - Najlepsze słuchawki bezprzewodowe', 'AirPods Pro 2 z najnowszą redukcją szumów. Doskonała jakość dźwięku i wygoda.', 312, 28, NOW(), NOW()),

-- Odzież męska
    (7, 'Koszula Oxford Premium', 'Elegancka koszula męska z bawełny', 'Klasyczna koszula Oxford z wysokiej jakości bawełny organicznej w kolorze białym. Krój slim fit z kołnierzykiem button-down. Dostępne rozmiary: S, M, L, XL, XXL. Idealna do pracy i na specjalne okazje.', 'KOSZULA-OXFORD-WHITE-M', 'koszula-oxford-biala', 149.00, NULL, 35, 5, true, 0.25, 'S-XXL', true, false, 1, 21, 'Koszula Oxford męska - elegancka i wygodna', 'Klasyczna koszula Oxford z wysokiej jakości bawełny. Idealna do pracy i okazji specjalnych.', 45, 7, NOW(), NOW()),

    (8, 'Bluza z kapturem Premium', 'Wygodna bluza męska', 'Bluza z kapturem wykonana z mieszanki bawełny i poliestru. Kieszeń typu kangurka, ściągacze w rękawach i pasie. Dostępna w kolorach: czarny, szary, granatowy. Gramatura 320g/m².', 'BLUZA-KAPTUR-GRAY-L', 'bluza-kaptur-szara', 89.00, 119.00, 28, 8, true, 0.45, 'S-XXL', true, false, 2, 21, 'Bluza z kapturem męska', 'Wygodna bluza męska z kapturem. Wysokiej jakości materiał i wykonanie.', 78, 12, NOW(), NOW()),

-- Fitness
    (9, 'Hantle regulowane 2x20kg', 'Profesjonalny zestaw hantli', 'Profesjonalne hantle regulowane z obciążeniami żeliwnymi w powłoce gumowej. Zakres regulacji: 2.5kg - 20kg każda sztuka. Ergonomiczny uchwyt z antypoślizgową powierzchnią. Szybka zmiana obciążenia.', 'HANTLE-REG-20KG', 'hantle-regulowane-20kg', 599.00, 799.00, 15, 3, true, 25.5, '40×20×20 cm', true, true, 1, 41, 'Hantle regulowane 2x20kg - domowa siłownia', 'Profesjonalne hantle regulowane do 20kg. Idealne do treningu w domu.', 78, 11, NOW(), NOW()),

    (10, 'Mata do jogi Premium', 'Antypoślizgowa mata do ćwiczeń', 'Mata do jogi z naturalnego kauczuku z antypoślizgową powierzchnią. Grubość 6mm dla optymalnego komfortu. Wymiary 183×61cm. Ekologiczna i biodegradowalna. Idealna do jogi, pilates i stretchingu.', 'MATA-JOGA-PREMIUM', 'mata-joga-premium', 159.00, NULL, 42, 8, true, 1.2, '183×61×0.6 cm', true, false, 2, 41, 'Mata do jogi Premium', 'Antypoślizgowa mata do jogi z naturalnego kauczuku. Komfort i stabilność.', 89, 15, NOW(), NOW()),

-- Książki
    (11, 'Clean Code', 'Podręcznik programowania', 'Clean Code: A Handbook of Agile Software Craftsmanship - kultowa książka Roberta C. Martina o pisaniu czystego, zrozumiałego kodu. Obowiązkowa lektura dla każdego programisty. 464 strony wiedzy i doświadczenia.', 'BOOK-CLEAN-CODE', 'clean-code-martin', 89.00, NULL, 25, 5, true, 0.45, '17.8×23.5×2.8 cm', true, true, 1, 5, 'Clean Code - Robert C. Martin', 'Clean Code - najważniejsza książka o programowaniu. Naucz się pisać czysty kod.', 156, 23, NOW(), NOW()),

    (12, 'Atomic Habits', 'Przewodnik po budowaniu nawyków', 'Atomic Habits: An Easy & Proven Way to Build Good Habits & Break Bad Ones - James Clear pokazuje jak małe zmiany prowadzą do niezwykłych rezultatów. Nr 1 na listach bestsellerów.', 'BOOK-ATOMIC-HABITS', 'atomic-habits-clear', 45.00, 59.00, 18, 5, true, 0.38, '16×24×2 cm', true, true, 2, 5, 'Atomic Habits - James Clear', 'Atomic Habits - jak budować dobre nawyki i przełamywać złe. Bestseller nr 1.', 203, 31, NOW(), NOW()),

-- Meble
    (13, 'Fotel biurowy ergonomiczny', 'Komfortowy fotel do pracy', 'Ergonomiczny fotel biurowy z regulacją wysokości, podpórką lędźwiową i podłokietnikami 4D. Siedzisko z pianki memory foam. Maksymalne obciążenie 150kg. Dostępny w kolorze czarnym.', 'FOTEL-BIUROWY-ERG', 'fotel-biurowy-ergonomiczny', 799.00, 999.00, 8, 2, true, 22.5, '65×65×120 cm', true, false, 1, 31, 'Fotel biurowy ergonomiczny', 'Komfortowy fotel biurowy z regulacją. Ergonomia i komfort pracy.', 45, 3, NOW(), NOW()),

-- Narzędzia
    (14, 'Wiertarka udarowa 18V', 'Bezprzewodowa wiertarka profesjonalna', 'Wiertarka udarowa 18V Li-Ion z momentem obrotowym do 60 Nm. Zestaw z ładowarką, 2 akumulatorami i walizką. 13mm uchwyt samozaciskowy. Oświetlenie LED.', 'WIERTARKA-18V-PRO', 'wiertarka-udarowa-18v', 299.00, 399.00, 12, 3, true, 1.8, '25×8×22 cm', true, false, 1, 33, 'Wiertarka udarowa 18V', 'Profesjonalna wiertarka bezprzewodowa 18V z akcesoriami.', 67, 8, NOW(), NOW()),

-- Dodaj jeszcze kilka produktów dla różnorodności
    (15, 'iPad Air', 'Tablet Apple z chipem M1', 'iPad Air z chipem M1, 10.9-calowym wyświetlaczem Liquid Retina i obsługą Apple Pencil 2. generacji. 64GB pamięci, Wi-Fi. Dostępny w 5 kolorach.', 'IPAD-AIR-64GB', 'ipad-air-m1', 2499.00, NULL, 16, 4, true, 0.461, '247.6×178.5×6.1 mm', true, false, 1, 14, 'iPad Air - Tablet Apple M1', 'iPad Air z chipem M1. Potęga w lekkiej obudowie.', 123, 9, NOW(), NOW())
    ON CONFLICT (id) DO NOTHING;

-- ==========================================
-- OBRAZY PRODUKTÓW
-- ==========================================

INSERT INTO product_images (product_id, image_url)
VALUES
-- iPhone 15 Pro
(1, 'https://example.com/images/products/iphone-15-pro-1.jpg'),
(1, 'https://example.com/images/products/iphone-15-pro-2.jpg'),
(1, 'https://example.com/images/products/iphone-15-pro-3.jpg'),
-- Samsung Galaxy S24
(2, 'https://example.com/images/products/samsung-s24-1.jpg'),
(2, 'https://example.com/images/products/samsung-s24-2.jpg'),
-- MacBook Air M2
(4, 'https://example.com/images/products/macbook-air-m2-1.jpg'),
(4, 'https://example.com/images/products/macbook-air-m2-2.jpg'),
-- AirPods Pro 2
(6, 'https://example.com/images/products/airpods-pro-2-1.jpg'),
-- Hantle
(9, 'https://example.com/images/products/hantle-regulowane-1.jpg'),
(9, 'https://example.com/images/products/hantle-regulowane-2.jpg'),
-- Clean Code
(11, 'https://example.com/images/products/clean-code-book-1.jpg'),
-- Pozostałe produkty
(3, 'https://example.com/images/products/pixel-8-1.jpg'),
(5, 'https://example.com/images/products/dell-xps-13-1.jpg'),
(7, 'https://example.com/images/products/koszula-oxford-1.jpg'),
(8, 'https://example.com/images/products/bluza-kaptur-1.jpg'),
(10, 'https://example.com/images/products/mata-joga-1.jpg'),
(12, 'https://example.com/images/products/atomic-habits-1.jpg'),
(13, 'https://example.com/images/products/fotel-biurowy-1.jpg'),
(14, 'https://example.com/images/products/wiertarka-18v-1.jpg'),
(15, 'https://example.com/images/products/ipad-air-1.jpg')
    ON CONFLICT DO NOTHING;

-- ==========================================
-- TAGI PRODUKTÓW
-- ==========================================

INSERT INTO product_tags (product_id, tag)
VALUES
-- iPhone 15 Pro
(1, 'apple'), (1, 'iphone'), (1, 'smartfon'), (1, 'premium'), (1, 'a17-pro'),
-- Samsung Galaxy S24
(2, 'samsung'), (2, 'android'), (2, 'smartfon'), (2, 'flagowy'), (2, 'galaxy'),
-- Google Pixel 8
(3, 'google'), (3, 'pixel'), (3, 'android'), (3, 'czysty'), (3, 'aparat'),
-- MacBook Air M2
(4, 'apple'), (4, 'macbook'), (4, 'laptop'), (4, 'm2'), (4, 'ultrabook'),
-- Dell XPS 13
(5, 'dell'), (5, 'laptop'), (5, 'biznesowy'), (5, 'xps'), (5, 'windows'),
-- AirPods Pro 2
(6, 'apple'), (6, 'sluchawki'), (6, 'bezprzewodowe'), (6, 'airpods'), (6, 'anc'),
-- Koszula Oxford
(7, 'koszula'), (7, 'meska'), (7, 'oxford'), (7, 'elegancka'), (7, 'bawelna'),
-- Bluza z kapturem
(8, 'bluza'), (8, 'kaptur'), (8, 'meska'), (8, 'wygodna'), (8, 'casual'),
-- Hantle regulowane
(9, 'hantle'), (9, 'fitness'), (9, 'trening'), (9, 'silownia'), (9, 'regulowane'),
-- Mata do jogi
(10, 'mata'), (10, 'joga'), (10, 'fitness'), (10, 'kauczuk'), (10, 'antypoślizgowa'),
-- Clean Code
(11, 'ksiazka'), (11, 'programowanie'), (11, 'clean-code'), (11, 'martin'), (11, 'it'),
-- Atomic Habits
(12, 'ksiazka'), (12, 'nawyki'), (12, 'rozwoj'), (12, 'bestseller'), (12, 'motywacja'),
-- Fotel biurowy
(13, 'fotel'), (13, 'biurowy'), (13, 'ergonomiczny'), (13, 'komfort'), (13, 'praca'),
-- Wiertarka
(14, 'wiertarka'), (14, 'narzedzia'), (14, 'udarowa'), (14, '18v'), (14, 'bezprzewodowa'),
-- iPad Air
(15, 'apple'), (15, 'ipad'), (15, 'tablet'), (15, 'm1'), (15, 'air')
    ON CONFLICT DO NOTHING;