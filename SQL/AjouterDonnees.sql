DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `ajoutDonnees`(IN `ps_id_capteur` INT, IN `ps_temp` DOUBLE, IN `ps_humidite` DOUBLE)
BEGIN
	INSERT INTO donnees VALUES (ps_id_capteur, NOW(), ps_temp, ps_humidite);
END$$
DELIMITER ;