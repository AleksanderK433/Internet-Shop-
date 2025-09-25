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
