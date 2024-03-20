DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `CalculerMoyenneParHeure`(IN `ps_id_capteur` INT, OUT `pheure_n` DATETIME, OUT `pmoyenne_temperature_n` DOUBLE(10,2), OUT `pmoyenne_humidite_n` DOUBLE(10,2), OUT `pheure_n1` DATETIME, OUT `pmoyenne_temperature_n1` DOUBLE(10,2), OUT `pmoyenne_humidite_n1` DOUBLE(10,2))
BEGIN
    SELECT 
    DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i'),
    AVG(CASE WHEN DATE_FORMAT(time, '%Y-%m-%d %H:%i') = DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i') THEN `temperature` END) AS `moyenne_temperature_n`,
    AVG(CASE WHEN DATE_FORMAT(time, '%Y-%m-%d %H:%i') = DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i') THEN `humidite` END) AS `moyenne_humidite_n`,
    DATE_FORMAT(NOW() - INTERVAL 1 MINUTE, '%Y-%m-%d %H:%i'),
    AVG(CASE WHEN DATE_FORMAT(time, '%Y-%m-%d %H:%i') = DATE_FORMAT(NOW() - INTERVAL 1 MINUTE, '%Y-%m-%d %H:%i') THEN `temperature` END) AS `moyenne_temperature_n1`,
    AVG(CASE WHEN DATE_FORMAT(time, '%Y-%m-%d %H:%i') = DATE_FORMAT(NOW() - INTERVAL 1 MINUTE, '%Y-%m-%d %H:%i') THEN `humidite` END) AS `moyenne_humidite_n1`
INTO 
    pheure_n, pmoyenne_temperature_n, pmoyenne_humidite_n, pheure_n1, pmoyenne_temperature_n1, pmoyenne_humidite_n1
FROM 
    `donnees`
WHERE
    `id_capteur` = `ps_id_capteur`
    AND (
        DATE_FORMAT(time, '%Y-%m-%d %H:%i') = DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i')
        OR DATE_FORMAT(time, '%Y-%m-%d %H:%i') = DATE_FORMAT(NOW() - INTERVAL 1 MINUTE, '%Y-%m-%d %H:%i')
    );
END$$
DELIMITER ;