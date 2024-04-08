DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `CheckPointInTerrains`(
    IN `p_longitude` DOUBLE, 
    IN `p_latitude` DOUBLE,
    OUT `terrain_name` VARCHAR(255)
)
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE found BOOL DEFAULT FALSE;
    DECLARE terrain_id INT;
    DECLARE min_longitude DOUBLE;
    DECLARE max_longitude DOUBLE;
    DECLARE min_latitude DOUBLE;
    DECLARE max_latitude DOUBLE;
    
    SET terrain_name = NULL;  -- Initialiser terrain_name avec NULL

    -- Boucle à travers les terrains
    WHILE i <= 4 AND NOT found DO
        -- Sélectionner les longitudes et latitudes extrêmes pour chaque terrain
        SELECT MIN(longitude), MAX(longitude), MIN(latitude), MAX(latitude) INTO min_longitude, max_longitude, min_latitude, max_latitude
        FROM angles WHERE id_terrain = i;
        
        -- Vérifier si le point est à l'intérieur du rectangle défini par les angles
        IF p_longitude BETWEEN min_longitude AND max_longitude AND p_latitude BETWEEN min_latitude AND max_latitude THEN
            SET found = TRUE;
            SET terrain_id = i;
        END IF;
        
        SET i = i + 1;
    END WHILE;
    
    -- Si le point est dans un terrain, trouver et retourner le nom du terrain
    IF found THEN
        SELECT name INTO terrain_name FROM terrain WHERE id = terrain_id;
    ELSE 
        SET terrain_name = 'Aucun terrain';
    END IF;

    -- Le nom du terrain est maintenant stocké dans `terrain_name` et sera retourné
END$$
DELIMITER ;