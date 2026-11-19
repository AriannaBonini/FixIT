-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema fixit
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `fixit` ;

-- -----------------------------------------------------
-- Schema fixit
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `fixit` DEFAULT CHARACTER SET utf8mb3 ;
USE `fixit` ;

-- -----------------------------------------------------
-- Table `fixit`.`fornitore`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fixit`.`fornitore` ;

CREATE TABLE IF NOT EXISTS `fixit`.`fornitore` (
  `PartitaIVA` BIGINT UNSIGNED NOT NULL,
  `Nome` VARCHAR(45) NOT NULL,
  `Indirizzo` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`PartitaIVA`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3
COMMENT = 'Questo trigger mi verifica se la partitaIVA è formata da esattamente 11 cifre';


-- -----------------------------------------------------
-- Table `fixit`.`componenteelettronico`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fixit`.`componenteelettronico` ;

CREATE TABLE IF NOT EXISTS `fixit`.`componenteelettronico` (
  `Codice` VARCHAR(10) NOT NULL,
  `Descrizione` VARCHAR(70) NOT NULL,
  `Prezzo` DECIMAL(6,2) UNSIGNED NOT NULL,
  `Giacenza` INT UNSIGNED NOT NULL,
  `PartitaIVA_Fornitore` BIGINT UNSIGNED NOT NULL,
  `Categoria` ENUM('Cellulare', 'Computer Portatile') NOT NULL,
  PRIMARY KEY (`Codice`),
  INDEX `fx_ComponenteElettronico_Fornitore_idx_idx` (`PartitaIVA_Fornitore` ASC) VISIBLE,
  CONSTRAINT `fx_ComponenteElettronico_Fornitore`
    FOREIGN KEY (`PartitaIVA_Fornitore`)
    REFERENCES `fixit`.`fornitore` (`PartitaIVA`)
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `fixit`.`contatto`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fixit`.`contatto` ;

CREATE TABLE IF NOT EXISTS `fixit`.`contatto` (
  `Recapito` VARCHAR(45) NOT NULL,
  `TipoContatto` ENUM('Telefono', 'E-mail', 'Altro') NOT NULL,
  `PartitaIVA_Fornitore` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`Recapito`),
  INDEX `fk_Contatto_Fornitore_idx_idx` (`PartitaIVA_Fornitore` ASC) VISIBLE,
  CONSTRAINT `fk_Contatto_Fornitore_idx`
    FOREIGN KEY (`PartitaIVA_Fornitore`)
    REFERENCES `fixit`.`fornitore` (`PartitaIVA`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `fixit`.`dispositivoelettronico`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fixit`.`dispositivoelettronico` ;

CREATE TABLE IF NOT EXISTS `fixit`.`dispositivoelettronico` (
  `Marca` VARCHAR(45) NOT NULL,
  `Modello` VARCHAR(45) NOT NULL,
  `Tipo` ENUM('Computer Portatile', 'Cellulare') NOT NULL,
  PRIMARY KEY (`Marca`, `Modello`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `fixit`.`intervento`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fixit`.`intervento` ;

CREATE TABLE IF NOT EXISTS `fixit`.`intervento` (
  `ID` INT UNSIGNED NOT NULL,
  `Malfunzionamento` VARCHAR(100) NOT NULL,
  `CostoIntervento` DECIMAL(6,2) UNSIGNED NULL DEFAULT NULL,
  `StatoIntervento` ENUM('Concluso', 'In riparazione', 'In valutazione', 'Riparato') NOT NULL DEFAULT 'In valutazione',
  `MarcaDispositivoElettronico` VARCHAR(45) NOT NULL,
  `ModelloDispositivoElettronico` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_Intervento_DispositivoElettronico_idx1_idx` (`MarcaDispositivoElettronico` ASC, `ModelloDispositivoElettronico` ASC) VISIBLE,
  CONSTRAINT `fk_Intervento_DispositivoElettronico_idx1`
    FOREIGN KEY (`MarcaDispositivoElettronico` , `ModelloDispositivoElettronico`)
    REFERENCES `fixit`.`dispositivoelettronico` (`Marca` , `Modello`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `fixit`.`necessita`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fixit`.`necessita` ;

CREATE TABLE IF NOT EXISTS `fixit`.`necessita` (
  `MarcaDispositivoElettronico` VARCHAR(45) NOT NULL,
  `ModelloDispositivoElettronico` VARCHAR(45) NOT NULL,
  `CodiceComponenteElettronico` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`MarcaDispositivoElettronico`, `ModelloDispositivoElettronico`, `CodiceComponenteElettronico`),
  INDEX `idx_MarcaDispositivoElettronico` (`MarcaDispositivoElettronico` ASC) VISIBLE,
  INDEX `idx_ModelloDispositivoElettronico` (`ModelloDispositivoElettronico` ASC) VISIBLE,
  INDEX `fk_Necessita_ComponenteElettronico_idx` (`CodiceComponenteElettronico` ASC) VISIBLE,
  CONSTRAINT `fk_Necessita_ComponenteElettronico`
    FOREIGN KEY (`CodiceComponenteElettronico`)
    REFERENCES `fixit`.`componenteelettronico` (`Codice`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Necessita_Marca`
    FOREIGN KEY (`MarcaDispositivoElettronico` , `ModelloDispositivoElettronico`)
    REFERENCES `fixit`.`dispositivoelettronico` (`Marca` , `Modello`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `fixit`.`ordine`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fixit`.`ordine` ;

CREATE TABLE IF NOT EXISTS `fixit`.`ordine` (
  `ID` INT UNSIGNED NOT NULL,
  `Quantità` INT UNSIGNED NOT NULL DEFAULT '0',
  `StatoOrdine` ENUM('Ricevuto', 'In attesa') NOT NULL DEFAULT 'In attesa',
  `CodiceComponenteElettronico` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_Ordine_ComponenteElettronico_idx` (`CodiceComponenteElettronico` ASC) VISIBLE,
  CONSTRAINT `fx_Ordine_ComponenteElettronico_idx`
    FOREIGN KEY (`CodiceComponenteElettronico`)
    REFERENCES `fixit`.`componenteelettronico` (`Codice`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `fixit`.`sostituisce`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fixit`.`sostituisce` ;

CREATE TABLE IF NOT EXISTS `fixit`.`sostituisce` (
  `IDIntervento` INT UNSIGNED NOT NULL,
  `CodiceComponenteElettronico` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`IDIntervento`, `CodiceComponenteElettronico`),
  INDEX `fk_Sostituisce_ComponenteElettronico_idx` (`CodiceComponenteElettronico` ASC) VISIBLE,
  CONSTRAINT `fk_Sostituisce_ComponenteElettronico`
    FOREIGN KEY (`CodiceComponenteElettronico`)
    REFERENCES `fixit`.`componenteelettronico` (`Codice`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_Sostituisce_Intervento_idx`
    FOREIGN KEY (`IDIntervento`)
    REFERENCES `fixit`.`intervento` (`ID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `fixit`.`utenti`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fixit`.`utenti` ;

CREATE TABLE IF NOT EXISTS `fixit`.`utenti` (
  `Username` VARCHAR(45) NOT NULL,
  `Password` CHAR(32) NOT NULL,
  `Ruolo` ENUM('tecnico', 'segreteria', 'gestione interna') NOT NULL,
  PRIMARY KEY (`Username`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;

USE `fixit` ;

-- -----------------------------------------------------
-- procedure aggiorna_stato_intervento_concluso
-- -----------------------------------------------------

USE `fixit`;
DROP procedure IF EXISTS `fixit`.`aggiorna_stato_intervento_concluso`;

DELIMITER $$
USE `fixit`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `aggiorna_stato_intervento_concluso`(in var_id int)
BEGIN
    DECLARE stato_attuale VARCHAR(45);
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    START TRANSACTION;

    -- Controlla lo stato attuale dell'intervento
    SELECT StatoIntervento INTO stato_attuale
    FROM intervento
    WHERE ID = var_id;

    -- Se l'intervento esiste e il suo stato è 'Riparato', aggiorna lo stato a 'Concluso'
    IF stato_attuale = 'Riparato' THEN
        UPDATE intervento
        SET StatoIntervento = 'Concluso'
        WHERE ID = var_id;
        
        -- Commit della transazione
        COMMIT;
    ELSE
        SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = "Impossibile aggiornare lo stato: l'intervento non è 'Riparato' o non esiste.";
    END IF;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure aggiorna_stato_intervento_in_riparazione
-- -----------------------------------------------------

USE `fixit`;
DROP procedure IF EXISTS `fixit`.`aggiorna_stato_intervento_in_riparazione`;

DELIMITER $$
USE `fixit`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `aggiorna_stato_intervento_in_riparazione`(in var_id int)
BEGIN
    DECLARE stato_attuale VARCHAR(45);
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    START TRANSACTION;

    -- Controlla lo stato attuale dell'intervento
    SELECT StatoIntervento INTO stato_attuale
    FROM intervento
    WHERE ID = var_id;

    -- Se l'intervento esiste e il suo stato è 'In valutazione', aggiorna lo stato a 'In riparazione'
    IF stato_attuale = 'In valutazione' THEN
     update intervento
     set StatoIntervento='In riparazione'
     where ID=var_id;
       -- Commit della transazione
        COMMIT;
    ELSE
        SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = "Impossibile aggiornare lo stato: l'intervento non è 'In valutazione' o non esiste.";
    END IF;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure aggiorna_stato_intervento_riparato
-- -----------------------------------------------------

USE `fixit`;
DROP procedure IF EXISTS `fixit`.`aggiorna_stato_intervento_riparato`;

DELIMITER $$
USE `fixit`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `aggiorna_stato_intervento_riparato`(in var_id int)
BEGIN
 DECLARE stato_attuale VARCHAR(45);
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    START TRANSACTION;

    -- Controlla lo stato attuale dell'intervento
    SELECT StatoIntervento INTO stato_attuale
    FROM intervento
    WHERE ID = var_id;

    -- Se l'intervento esiste e il suo stato è 'In riparazione', aggiorna lo stato a 'Riparato'
    IF stato_attuale = 'In riparazione' THEN
	update intervento
     set StatoIntervento='Riparato'
     where ID=var_id;
     
      -- Commit della transazione
        COMMIT;
    ELSE
        SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = "Impossibile aggiornare lo stato: l'intervento non è 'In riparazione' o non esiste.";
    END IF;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure aggiorna_stato_ordine
-- -----------------------------------------------------

USE `fixit`;
DROP procedure IF EXISTS `fixit`.`aggiorna_stato_ordine`;

DELIMITER $$
USE `fixit`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `aggiorna_stato_ordine`(in var_id int)
BEGIN
DECLARE stato_attuale VARCHAR(45);
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

	SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    START TRANSACTION;

    -- Controlla lo stato attuale dell'ordine
    SELECT StatoOrdine INTO stato_attuale
    FROM ordine
    WHERE ID = var_id;

    -- Se l'ordine esiste e il suo stato è 'In attesa', aggiorna lo stato a 'Ricevuto'
    IF stato_attuale = 'In attesa' THEN
	update ordine
    set StatoOrdine="Ricevuto" 
    where ID=var_id;
     -- Commit della transazione
        COMMIT;
    ELSE
        SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = "Impossibile aggiornare lo stato: l'ordine non è 'In attesa' o non esiste.";
    END IF;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure aggiungi_componente_elettronica_per_Dispositivo
-- -----------------------------------------------------

USE `fixit`;
DROP procedure IF EXISTS `fixit`.`aggiungi_componente_elettronica_per_Dispositivo`;

DELIMITER $$
USE `fixit`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `aggiungi_componente_elettronica_per_Dispositivo`(in var_marca varchar(45), in var_modello varchar(45), in var_codiceComponente varchar(10))
BEGIN
 DECLARE tipo_dispositivo VARCHAR(45);
 DECLARE categoria_componente VARCHAR(45);
 
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    -- Controlla se la coppia var_marca e var_modello esiste nella tabella dispositivoelettronico
    SELECT Tipo INTO tipo_dispositivo
    FROM dispositivoelettronico
    WHERE Marca = var_marca AND Modello = var_modello;

    -- Se non esiste, esci dalla procedura e annulla la transazione
    IF tipo_dispositivo IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La coppia Marca e Modello non esiste nella tabella dispositivoelettronico.';
    END IF;

    -- Controlla se il var_codiceComponente esiste nella tabella componentelettronico e se Categoria corrisponde a Tipo
    SELECT Categoria INTO categoria_componente
    FROM componenteelettronico
    WHERE Codice = var_codiceComponente;

    -- Se il codice del componente non esiste o la categoria non corrisponde al tipo, esci dalla procedura e annulla la transazione
    IF categoria_componente IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Il codice componente non esiste nella tabella componentelettronico.';
    ELSEIF categoria_componente <> tipo_dispositivo THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La categoria del componente non corrisponde al tipo del dispositivo.';
    END IF;

    -- Controlla se la combinazione var_marca, var_modello e var_codiceComponente esiste già nella tabella necessita
    IF EXISTS (
        SELECT 1
        FROM necessita
        WHERE MarcaDispositivoElettronico = var_marca 
          AND ModelloDispositivoElettronico = var_modello 
          AND CodiceComponenteElettronico = var_codiceComponente
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La combinazione Marca, Modello e CodiceComponenteElettronico esiste già nella tabella necessita.';
    END IF;

    -- Inserisci nella tabella necessita
    INSERT INTO necessita (MarcaDispositivoElettronico, ModelloDispositivoElettronico, CodiceComponenteElettronico)
    VALUES (var_marca, var_modello, var_codiceComponente);

    -- Concludi la transazione
    COMMIT;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure aggiungi_componenti_elettroniche_al_NuovoDispositivo
-- -----------------------------------------------------

USE `fixit`;
DROP procedure IF EXISTS `fixit`.`aggiungi_componenti_elettroniche_al_NuovoDispositivo`;

DELIMITER $$
USE `fixit`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `aggiungi_componenti_elettroniche_al_NuovoDispositivo`(in var_marca varchar(45), in var_modello varchar(45), in var_codice varchar(10))
BEGIN
    DECLARE tipo_dispositivo VARCHAR(45);
    DECLARE categoria_componente VARCHAR(45);
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    -- Controlla se la coppia Marca e Modello esiste e recupera il Tipo
    SELECT Tipo INTO tipo_dispositivo
    FROM dispositivoelettronico
    WHERE Marca = var_marca AND Modello = var_modello;

    -- Verifica se il Tipo è stato trovato
    IF tipo_dispositivo IS NOT NULL THEN
        -- Controlla la Categoria della Componente Elettronica
        SELECT Categoria INTO categoria_componente
        FROM componenteelettronico
        WHERE Codice = var_codice;

        -- Se la Categoria corrisponde al Tipo, esegui l'INSERT
        IF categoria_componente = tipo_dispositivo THEN
            INSERT INTO necessita (MarcaDispositivoElettronico, ModelloDispositivoElettronico, CodiceComponenteElettronico)
            VALUES (var_marca, var_modello, var_codice);
            
            -- Commit della transazione
            COMMIT;
        ELSE
            SIGNAL SQLSTATE '45000' 
                SET MESSAGE_TEXT = "Impossibile aggiungere il componente: la categoria non corrisponde al tipo.";
        END IF;
    ELSE
        SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = "Impossibile aggiungere il componente: la coppia Marca e Modello non esiste.";
    END IF;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure aggiungi_dispositivo_elettronico
-- -----------------------------------------------------

USE `fixit`;
DROP procedure IF EXISTS `fixit`.`aggiungi_dispositivo_elettronico`;

DELIMITER $$
USE `fixit`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `aggiungi_dispositivo_elettronico`(in var_marca varchar(45), in var_modello varchar(45), in var_tipo varchar(45))
BEGIN
    DECLARE tipo_valido BOOLEAN;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    -- Controlla se il Tipo è valido
    SET tipo_valido = (var_tipo = 'Cellulare' OR var_tipo = 'Computer Portatile');

    -- Se il Tipo è valido, esegui l'INSERT
    IF tipo_valido THEN
        INSERT INTO dispositivoelettronico (Marca, Modello, Tipo)
        VALUES (var_marca, var_modello, var_tipo);

        COMMIT;
    ELSE
        SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = "Impossibile aggiungere il dispositivo: Tipo non valido.";
    END IF;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure apri_Intervento
-- -----------------------------------------------------

USE `fixit`;
DROP procedure IF EXISTS `fixit`.`apri_Intervento`;

DELIMITER $$
USE `fixit`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `apri_Intervento`(IN p_ID INT,
    IN p_Malfunzionamento VARCHAR(100),
    IN p_MarcaDispositivo VARCHAR(45),
    IN p_ModelloDispositivo VARCHAR(45))
BEGIN
   DECLARE occorrenza INT;
   
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    START TRANSACTION;

    -- Controlla se esiste esattamente una riga con la coppia Marca e Modello
    SELECT COUNT(*) INTO occorrenza
    FROM dispositivoelettronico
    WHERE Marca = p_MarcaDispositivo AND Modello = p_ModelloDispositivo;

    -- Se l'occorrenza è esattamente 1, esegui l'INSERT
    IF occorrenza = 1 THEN
        INSERT INTO intervento (ID, Malfunzionamento, MarcaDispositivoElettronico, ModelloDispositivoElettronico)
        VALUES (p_ID, p_Malfunzionamento, p_MarcaDispositivo, p_ModelloDispositivo);
        
        -- Commit della transazione
        COMMIT;
    ELSE
    SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = "Impossibile inserire l'intervento: il dispositivo elettronico non è supportato";
    END IF;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure calcolo_costo_intervento
-- -----------------------------------------------------

USE `fixit`;
DROP procedure IF EXISTS `fixit`.`calcolo_costo_intervento`;

DELIMITER $$
USE `fixit`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `calcolo_costo_intervento`(in var_id int)
BEGIN
 DECLARE totale DECIMAL(6,2); 
    DECLARE id_esistente INT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN	
        ROLLBACK;
        RESIGNAL;	
    END;

    START TRANSACTION;

    -- Controllo se l'ID esiste nella tabella 'intervento'
    SELECT COUNT(*) INTO id_esistente
    FROM intervento
    WHERE ID = var_id and CostoIntervento is null and (StatoIntervento='In riparazione' or StatoIntervento='Riparato');

    -- Se l'ID non esiste, segnala l'errore
    IF id_esistente = 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = "Impossibile calcolare il costo: l'ID intervento non esiste.";
    ELSE
        -- Calcola il costo dell'intervento
        SELECT SUM(Prezzo) INTO totale
        FROM intervento 
        JOIN sostituisce ON `intervento`.`ID` = `sostituisce`.`IDIntervento`
        JOIN componenteelettronico ON `componenteelettronico`.`Codice` = `sostituisce`.`CodiceComponenteElettronico`
        WHERE intervento.ID = var_id;

        -- Aggiungi il 10% al totale
        SET totale = totale + (totale * 0.10); 

        -- Aggiorna il costo dell'intervento
        UPDATE intervento 
        SET CostoIntervento = totale
        WHERE ID = var_id;

        -- Conferma la transazione
        COMMIT; 
    END IF;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure crea_nuovo_ordine
-- -----------------------------------------------------

USE `fixit`;
DROP procedure IF EXISTS `fixit`.`crea_nuovo_ordine`;

DELIMITER $$
USE `fixit`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `crea_nuovo_ordine`(in var_id int, in var_quantità int, in var_codiceComponente varchar(10))
BEGIN
   DECLARE codice_esistente varchar(10);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    START TRANSACTION;

    -- Controlla se il CodiceComponenteElettronico esiste nella tabella componenteelettronico
    -- e verifica le condizioni aggiuntive sugli ordini
    SELECT ce.Codice INTO codice_esistente
    FROM componenteelettronico ce
    LEFT JOIN ordine o ON ce.Codice = o.CodiceComponenteElettronico
    WHERE ce.Codice = var_codiceComponente
      AND ce.Giacenza <= 5
      AND (o.CodiceComponenteElettronico IS NULL 
           OR (o.StatoOrdine = 'Ricevuto' AND o.CodiceComponenteElettronico IS NOT NULL));

    -- Se il CodiceComponenteElettronico esiste e le condizioni sono soddisfatte, esegui l'INSERT
    IF codice_esistente IS NOT NULL THEN
        INSERT INTO ordine (ID, Quantità, CodiceComponenteElettronico)
        VALUES (var_id, var_quantità, var_codiceComponente);
        
        -- Conferma della transazione
        COMMIT;
    ELSE
        -- Segnala l'errore
        SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = "Impossibile creare l'ordine: il componente elettronico non esiste o non soddisfa i criteri richiesti.";
    END IF;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure elimina_intervento
-- -----------------------------------------------------

USE `fixit`;
DROP procedure IF EXISTS `fixit`.`elimina_intervento`;

DELIMITER $$
USE `fixit`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `elimina_intervento`(in var_id int)
BEGIN
    DECLARE esiste INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    -- Controlla se l'ID esiste nella tabella intervento
    SELECT COUNT(*) INTO esiste
    FROM intervento
    WHERE ID = var_id;

    -- Se l'ID esiste, esegui il DELETE
    IF esiste =  1 THEN
        DELETE FROM intervento
        WHERE ID = var_id;
        
        -- Commit della transazione
        COMMIT;
    ELSE
        SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = "Impossibile eliminare l'intervento: l'ID non esiste.";
    END IF;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure inserisci_coppia_intervento_componenteElettronico
-- -----------------------------------------------------

USE `fixit`;
DROP procedure IF EXISTS `fixit`.`inserisci_coppia_intervento_componenteElettronico`;

DELIMITER $$
USE `fixit`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `inserisci_coppia_intervento_componenteElettronico`(in var_IDintervento varchar(45), in var_CODICEcomponente varchar(10))
BEGIN
    DECLARE esiste_intervento INT;
    DECLARE esiste_componente INT;
    DECLARE marca_dispositivo VARCHAR(45);
    DECLARE modello_dispositivo VARCHAR(45);
    DECLARE tipo_dispositivo VARCHAR(45);
    DECLARE categoria_componente VARCHAR(45);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
	SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    START TRANSACTION;

    -- Controlla se l'IDintervento esiste nella tabella intervento
    SELECT COUNT(*) INTO esiste_intervento
    FROM intervento
    WHERE ID = var_IDintervento and StatoIntervento='In valutazione';

    -- Controlla se il Codicecomponente esiste nella tabella componenteelettronico
    SELECT COUNT(*) INTO esiste_componente
    FROM componenteelettronico
    WHERE Codice = var_CODICEcomponente;

    -- Se esistono entrambi, recupera Marca, Modello e Tipo
    IF esiste_intervento = 1 AND esiste_componente = 1 THEN
        -- Recupera Marca e Modello dall'intervento
        SELECT MarcaDispositivoElettronico, ModelloDispositivoElettronico INTO marca_dispositivo, modello_dispositivo
        FROM intervento
        WHERE ID = var_IDintervento;

        -- Recupera Tipo dalla tabella dispositivoelettronico usando Marca e Modello
        SELECT Tipo INTO tipo_dispositivo
        FROM dispositivoelettronico
        WHERE Marca = marca_dispositivo AND Modello = modello_dispositivo;

        -- Recupera Categoria dalla tabella componenteelettronico usando Codice
        SELECT Categoria INTO categoria_componente
        FROM componenteelettronico
        WHERE Codice = var_CODICEcomponente;

        -- Verifica se Tipo e Categoria sono uguali
        IF tipo_dispositivo = categoria_componente THEN
            -- Inserisce nella tabella sostituisce
            INSERT INTO sostituisce (IDIntervento, CodiceComponenteElettronico)
            VALUES (var_IDintervento, var_CODICEcomponente);

            -- Commit della transazione
            COMMIT;
        ELSE
            SIGNAL SQLSTATE '45000' 
                SET MESSAGE_TEXT = "Impossibile inserire: Tipo e Categoria non corrispondono.";
        END IF;
    ELSE
        SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = "Impossibile inserire: IDIntervento o CodiceComponenteElettronico non esistono.";
    END IF;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure inserisci_fornitore
-- -----------------------------------------------------

USE `fixit`;
DROP procedure IF EXISTS `fixit`.`inserisci_fornitore`;

DELIMITER $$
USE `fixit`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `inserisci_fornitore`(in var_partitaIVA bigint, in var_nome varchar(45), in var_indirizzo varchar(45), in var_recapito varchar(45), in var_tipo varchar(45))
BEGIN
	declare exit handler for sqlexception
    begin 
		rollback;
        resignal;
    END;
    
	set transaction isolation level read uncommitted;
    START TRANSACTION;
    
    -- 1. Inserimento nella tabella Fornitore
    INSERT INTO fornitore (PartitaIVA, Nome, Indirizzo)
    VALUES (var_partitaIVA, var_nome, var_indirizzo);
    
    
    -- 3. Inserimento nella tabella Contatto usando l'ID del fornitore appena inserito
    INSERT INTO contatto(Recapito, TipoContatto, PartitaIVA_Fornitore )
    VALUES (var_recapito, var_tipo , var_partitaIVA);
    
    -- Conferma della transazione se tutte le operazioni hanno successo
    COMMIT;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure lista_componenti_elettronici
-- -----------------------------------------------------

USE `fixit`;
DROP procedure IF EXISTS `fixit`.`lista_componenti_elettronici`;

DELIMITER $$
USE `fixit`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `lista_componenti_elettronici`(in var_tipo varchar(45))
BEGIN
declare exit handler for sqlexception
		begin	
			rollback;
            resignal;	
		end;
        set transaction isolation level read committed;
        start transaction;
        
	select Codice, Descrizione
    from componenteelettronico
    where Categoria=var_tipo;
    commit;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure lista_componenti_elettronici_conGiacenza
-- -----------------------------------------------------

USE `fixit`;
DROP procedure IF EXISTS `fixit`.`lista_componenti_elettronici_conGiacenza`;

DELIMITER $$
USE `fixit`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `lista_componenti_elettronici_conGiacenza`()
BEGIN
declare exit handler for sqlexception
		begin	
			rollback;
            resignal;	
		end;
        set transaction isolation level read committed;
        start transaction;
        
	 select Codice, Descrizione, Giacenza
     from componenteelettronico;
     commit;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure lista_ordini
-- -----------------------------------------------------

USE `fixit`;
DROP procedure IF EXISTS `fixit`.`lista_ordini`;

DELIMITER $$
USE `fixit`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `lista_ordini`()
BEGIN
declare exit handler for sqlexception
		begin	
			rollback;
            resignal;	
		end;
        set transaction isolation level read committed;
        start transaction;
        
	select ID, Quantità, CodiceComponenteElettronico
    from ordine
    where StatoOrdine= "In attesa";
    commit;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure login
-- -----------------------------------------------------

USE `fixit`;
DROP procedure IF EXISTS `fixit`.`login`;

DELIMITER $$
USE `fixit`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `login`(in var_username varchar(45), in var_pass varchar(45), out var_ruolo INT)
BEGIN
declare var_ruolo_utente ENUM('tecnico', 'segreteria', 'gestione interna');
    
    select Ruolo from utenti
		where Username = var_username
        and Password = var_pass
	into var_ruolo_utente;
    
    if var_ruolo_utente = 'segreteria' then set var_ruolo = 1;
    elseif var_ruolo_utente = 'gestione interna' then set var_ruolo = 2;
    elseif var_ruolo_utente = 'tecnico' then set var_ruolo = 3;
    else set var_ruolo = 4;
    end if;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure visualizza_lista_componenti_elettroniche_intervento_ispezionato
-- -----------------------------------------------------

USE `fixit`;
DROP procedure IF EXISTS `fixit`.`visualizza_lista_componenti_elettroniche_intervento_ispezionato`;

DELIMITER $$
USE `fixit`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `visualizza_lista_componenti_elettroniche_intervento_ispezionato`(in var_marca varchar(45) , in var_modello varchar(45), in var_id int)
BEGIN
DECLARE tipo_dispositivo VARCHAR(45);
declare exit handler for sqlexception
		begin	
			rollback;
            resignal;	
		end;
        set transaction isolation level read committed;
        start transaction;
        
        SELECT Tipo INTO tipo_dispositivo
		FROM dispositivoelettronico
		WHERE Marca = var_marca
		AND Modello = var_modello;

		-- Controlla se il TipoDispositivo è stato trovato
		IF tipo_dispositivo IS NOT NULL THEN
			-- Se il TipoDispositivo è trovato, seleziona i componenti elettronici corrispondenti
			SELECT Codice, Descrizione
			FROM componenteelettronico
			WHERE Categoria = tipo_dispositivo and Giacenza>0 and not exists(SELECT 1
															  FROM sostituisce 
                                                              WHERE sostituisce.IDIntervento=var_id AND sostituisce.CodiceComponenteElettronico=ComponenteElettronico.Codice);
		ELSE
			-- Se il TipoDispositivo non è trovato, mostra un messaggio informativo
			 SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT ='Nessun tipo di dispositivo trovato per la marca e il modello forniti';
		END IF;

		-- Commit della transazione
		COMMIT;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure visualizza_lista_componenti_elettroniche_nuovoOrdine
-- -----------------------------------------------------

USE `fixit`;
DROP procedure IF EXISTS `fixit`.`visualizza_lista_componenti_elettroniche_nuovoOrdine`;

DELIMITER $$
USE `fixit`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `visualizza_lista_componenti_elettroniche_nuovoOrdine`()
BEGIN
declare exit handler for sqlexception
		begin	
			rollback;
            resignal;	
		end;
        set transaction isolation level read committed;
        start transaction;
        
	SELECT c.Codice, c.Descrizione
    FROM componenteelettronico c
    LEFT JOIN ordine o ON c.Codice = o.CodiceComponenteElettronico
    WHERE c.Giacenza <= 5
    AND ((o.CodiceComponenteElettronico IS NULL) OR (o.StatoOrdine = 'Ricevuto' AND o.CodiceComponenteElettronico IS NOT NULL));
    commit;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure visualizza_lista_componenti_elettroniche_per_intervento
-- -----------------------------------------------------

USE `fixit`;
DROP procedure IF EXISTS `fixit`.`visualizza_lista_componenti_elettroniche_per_intervento`;

DELIMITER $$
USE `fixit`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `visualizza_lista_componenti_elettroniche_per_intervento`(in var_marca varchar(45) , in var_modello varchar(45))
BEGIN
DECLARE tipo_dispositivo VARCHAR(45);
declare exit handler for sqlexception
		begin	
			rollback;
            resignal;	
		end;
        set transaction isolation level read committed;
        start transaction;
        
        SELECT Tipo INTO tipo_dispositivo
		FROM dispositivoelettronico
		WHERE Marca = var_marca
		AND Modello = var_modello;

		-- Controlla se il TipoDispositivo è stato trovato
		IF tipo_dispositivo IS NOT NULL THEN
			-- Se il TipoDispositivo è trovato, seleziona i componenti elettronici corrispondenti
			SELECT Codice, Descrizione
			FROM componenteelettronico
			WHERE Categoria = tipo_dispositivo and Giacenza>0;
		ELSE
			-- Se il TipoDispositivo non è trovato, mostra un messaggio informativo
             SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'Nessun tipo di dispositivo trovato per la marca e il modello forniti';
		END IF;

		-- Commit della transazione
		COMMIT;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure visualizza_lista_dispositivi_elettronici
-- -----------------------------------------------------

USE `fixit`;
DROP procedure IF EXISTS `fixit`.`visualizza_lista_dispositivi_elettronici`;

DELIMITER $$
USE `fixit`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `visualizza_lista_dispositivi_elettronici`()
BEGIN
    declare exit handler for sqlexception
    begin
		rollback;
        resignal;
	end;
    set transaction isolation level read committed;
    start transaction;
    
	select * 
    from dispositivoelettronico;
    commit;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure visualizza_lista_interventi
-- -----------------------------------------------------

USE `fixit`;
DROP procedure IF EXISTS `fixit`.`visualizza_lista_interventi`;

DELIMITER $$
USE `fixit`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `visualizza_lista_interventi`()
BEGIN
    declare exit handler for sqlexception
    begin
		rollback;
        resignal;
	end;
    set transaction isolation level read committed;
    start transaction;
    
	select ID, Malfunzionamento 
    from intervento;
    commit;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure visualizza_lista_interventi_da_ispezionare
-- -----------------------------------------------------

USE `fixit`;
DROP procedure IF EXISTS `fixit`.`visualizza_lista_interventi_da_ispezionare`;

DELIMITER $$
USE `fixit`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `visualizza_lista_interventi_da_ispezionare`()
BEGIN
    declare exit handler for sqlexception
    begin
		rollback;
        resignal;
	end;
    set transaction isolation level read committed;
    start transaction;
    
    SELECT ID, Malfunzionamento, MarcaDispositivoElettronico, ModelloDispositivoElettronico
    FROM intervento
    WHERE StatoIntervento = 'In valutazione'
       AND not exists(SELECT 1
					FROM sostituisce
					WHERE sostituisce.IDIntervento = intervento.ID);
	commit;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure visualizza_lista_interventi_in_riparazione
-- -----------------------------------------------------

USE `fixit`;
DROP procedure IF EXISTS `fixit`.`visualizza_lista_interventi_in_riparazione`;

DELIMITER $$
USE `fixit`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `visualizza_lista_interventi_in_riparazione`()
BEGIN
    declare exit handler for sqlexception
    begin
		rollback;
        resignal;
	end;
    set transaction isolation level read committed;
    start transaction;
    
    SELECT ID, Malfunzionamento, MarcaDispositivoElettronico, ModelloDispositivoElettronico
    FROM intervento
    WHERE StatoIntervento = 'In riparazione';
	commit;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure visualizza_lista_interventi_ispezionati
-- -----------------------------------------------------

USE `fixit`;
DROP procedure IF EXISTS `fixit`.`visualizza_lista_interventi_ispezionati`;

DELIMITER $$
USE `fixit`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `visualizza_lista_interventi_ispezionati`()
BEGIN
    declare exit handler for sqlexception
    begin
		rollback;
        resignal;
	end;
    set transaction isolation level read committed;
    start transaction;
    
    SELECT ID, Malfunzionamento, MarcaDispositivoElettronico, ModelloDispositivoElettronico
    FROM intervento
    WHERE StatoIntervento = 'In valutazione'
       AND exists(SELECT 1
					FROM sostituisce
					WHERE sostituisce.IDIntervento = intervento.ID);
	commit; 
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure visualizza_lista_interventi_per_calcolo_costo
-- -----------------------------------------------------

USE `fixit`;
DROP procedure IF EXISTS `fixit`.`visualizza_lista_interventi_per_calcolo_costo`;

DELIMITER $$
USE `fixit`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `visualizza_lista_interventi_per_calcolo_costo`()
BEGIN
    declare exit handler for sqlexception
    begin
		rollback;
        resignal;
	end;
    set transaction isolation level read committed;
    start transaction;
    
    SELECT ID, Malfunzionamento, MarcaDispositivoElettronico, ModelloDispositivoElettronico
    FROM intervento
    WHERE (StatoIntervento = 'In riparazione' || StatoIntervento='Riparato')
	AND CostoIntervento IS NULL;
    commit;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure visualizza_lista_interventi_riparati
-- -----------------------------------------------------

USE `fixit`;
DROP procedure IF EXISTS `fixit`.`visualizza_lista_interventi_riparati`;

DELIMITER $$
USE `fixit`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `visualizza_lista_interventi_riparati`()
BEGIN
    declare exit handler for sqlexception
    begin
		rollback;
        resignal;
	end;
    set transaction isolation level read committed;
    start transaction;
    
     select ID, Malfunzionamento, MarcaDispositivoElettronico, ModelloDispositivoElettronico, CostoIntervento
     from intervento
     where StatoIntervento='Riparato';
     commit;
END$$

DELIMITER ;
USE `fixit`;

DELIMITER $$

USE `fixit`$$
DROP TRIGGER IF EXISTS `fixit`.`controllo_partitaIVA_BEFORE_INSERT` $$
USE `fixit`$$
CREATE
DEFINER=`root`@`localhost`
TRIGGER `fixit`.`controllo_partitaIVA_BEFORE_INSERT`
BEFORE INSERT ON `fixit`.`fornitore`
FOR EACH ROW
BEGIN
IF NEW.PartitaIVA < 10000000000 OR NEW.PartitaIVA > 99999999999 THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'La PartitaIVA deve essere un numero di 11 cifre.';
  END IF;
END$$


USE `fixit`$$
DROP TRIGGER IF EXISTS `fixit`.`controlla_stato_intervento_BEFORE_DELETE` $$
USE `fixit`$$
CREATE
DEFINER=`root`@`localhost`
TRIGGER `fixit`.`controlla_stato_intervento_BEFORE_DELETE`
BEFORE DELETE ON `fixit`.`intervento`
FOR EACH ROW
BEGIN
    IF OLD.StatoIntervento <> 'Concluso' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = "Non è possibile eliminare un intervento che non è ancora concluso.";
    END IF;
END$$


USE `fixit`$$
DROP TRIGGER IF EXISTS `fixit`.`intervento_concluso_BEFORE_UPDATE` $$
USE `fixit`$$
CREATE
DEFINER=`root`@`localhost`
TRIGGER `fixit`.`intervento_concluso_BEFORE_UPDATE`
BEFORE UPDATE ON `fixit`.`intervento`
FOR EACH ROW
BEGIN
 IF NEW.StatoIntervento = 'Concluso' THEN
        IF NEW.CostoIntervento IS NULL THEN
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = "Impossibile concludere l'intervento: il costo non è stato calcolato.";
        END IF;
    END IF;

END$$


USE `fixit`$$
DROP TRIGGER IF EXISTS `fixit`.`update_giacenza_dopo_ordine_ricevuto` $$
USE `fixit`$$
CREATE
DEFINER=`root`@`localhost`
TRIGGER `fixit`.`update_giacenza_dopo_ordine_ricevuto`
AFTER UPDATE ON `fixit`.`ordine`
FOR EACH ROW
BEGIN
 -- Verifica se il valore di StatoOrdine è cambiato in "Ricevuto"
    IF OLD.StatoOrdine <> 'Ricevuto' AND NEW.StatoOrdine = 'Ricevuto' THEN
        -- Aggiorna la giacenza nella tabella componente elettronico
        UPDATE `componenteelettronico`
        SET `Giacenza` = `Giacenza` + NEW.Quantità
        WHERE `Codice` = NEW.CodiceComponenteElettronico;
    END IF;
END$$


USE `fixit`$$
DROP TRIGGER IF EXISTS `fixit`.`sottrai_giacenza_AFTER_INSERT` $$
USE `fixit`$$
CREATE
DEFINER=`root`@`localhost`
TRIGGER `fixit`.`sottrai_giacenza_AFTER_INSERT`
AFTER INSERT ON `fixit`.`sostituisce`
FOR EACH ROW
BEGIN
-- Aggiorna la giacenza nella tabella componente elettronico
    UPDATE ComponenteElettronico
    SET Giacenza = Giacenza - 1
    WHERE Codice = NEW.CodiceComponenteElettronico;
END$$


DELIMITER ;
SET SQL_MODE = '';
DROP USER IF EXISTS login;
SET SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
CREATE USER 'login' IDENTIFIED BY 'login';

GRANT EXECUTE ON procedure `fixit`.`login` TO 'login';

SET SQL_MODE = '';
DROP USER IF EXISTS segreteria;
SET SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
CREATE USER 'segreteria' IDENTIFIED BY 'segreteria';

GRANT EXECUTE ON procedure `fixit`.`apri_Intervento` TO 'segreteria';
GRANT EXECUTE ON procedure `fixit`.`calcolo_costo_intervento` TO 'segreteria';
GRANT EXECUTE ON procedure `fixit`.`visualizza_lista_interventi_in_riparazione` TO 'segreteria';
GRANT EXECUTE ON procedure `fixit`.`visualizza_lista_dispositivi_elettronici` TO 'segreteria';
GRANT EXECUTE ON procedure `fixit`.`visualizza_lista_interventi_riparati` TO 'segreteria';
GRANT EXECUTE ON procedure `fixit`.`aggiorna_stato_intervento_concluso` TO 'segreteria';
GRANT EXECUTE ON procedure `fixit`.`visualizza_lista_interventi_per_calcolo_costo` TO 'segreteria';
GRANT EXECUTE ON procedure `fixit`.`elimina_intervento` TO 'segreteria';
GRANT EXECUTE ON procedure `fixit`.`lista_componenti_elettronicia` TO 'segreteria';
GRANT EXECUTE ON procedure `fixit`.`aggiungi_dispositivo_elettronico` TO 'segreteria';
GRANT EXECUTE ON procedure `fixit`.`aggiungi_componenti_elettroniche_al_NuovoDispositivo` TO 'segreteria';
GRANT EXECUTE ON procedure `fixit`.`aggiungi_componente_elettronica_per_Dispositivo` TO 'segreteria';

SET SQL_MODE = '';
DROP USER IF EXISTS tecnico;
SET SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
CREATE USER 'tecnico' IDENTIFIED BY 'tecnico';

GRANT EXECUTE ON procedure `fixit`.`visualizza_lista_interventi_da_ispezionare` TO 'tecnico';
GRANT EXECUTE ON procedure `fixit`.`visualizza_lista_componenti_elettroniche_per_intervento` TO 'tecnico';
GRANT EXECUTE ON procedure `fixit`.`inserisci_coppia_intervento_componenteElettronico` TO 'tecnico';
GRANT EXECUTE ON procedure `fixit`.`visualizza_lista_interventi_ispezionati` TO 'tecnico';
GRANT EXECUTE ON procedure `fixit`.`visualizza_lista_componenti_elettroniche_intervento_ispezionato` TO 'tecnico';
GRANT EXECUTE ON procedure `fixit`.`aggiorna_stato_intervento_in_riparazione` TO 'tecnico';
GRANT EXECUTE ON procedure `fixit`.`visualizza_lista_interventi_in_riparazione` TO 'tecnico';
GRANT EXECUTE ON procedure `fixit`.`aggiorna_stato_intervento_riparato` TO 'tecnico';

SET SQL_MODE = '';
DROP USER IF EXISTS gestioneinterna;
SET SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
CREATE USER 'gestioneinterna' IDENTIFIED BY 'gestioneinterna';

GRANT EXECUTE ON procedure `fixit`.`crea_nuovo_ordine` TO 'gestioneinterna';
GRANT EXECUTE ON procedure `fixit`.`visualizza_lista_componenti_elettroniche_nuovoOrdine` TO 'gestioneinterna';
GRANT EXECUTE ON procedure `fixit`.`lista_ordini` TO 'gestioneinterna';
GRANT EXECUTE ON procedure `fixit`.`aggiorna_stato_ordine` TO 'gestioneinterna';
GRANT EXECUTE ON procedure `fixit`.`lista_componenti_elettronici_conGiacenza` TO 'gestioneinterna';
GRANT EXECUTE ON procedure `fixit`.`inserisci_fornitore` TO 'gestioneinterna';

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
