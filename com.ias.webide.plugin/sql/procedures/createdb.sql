DELIMITER //
CREATE PROCEDURE createdb
(IN dbname VARCHAR(64))
BEGIN
  Create database dbname;
END //
DELIMITER ;