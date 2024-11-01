-- Remo y Regatas: Database schema definitions
CREATE SCHEMA RRDB;


-- Teams and oarsmen (rower)
CREATE TABLE `RRDB`.`Team` (
  `Id` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NULL,
  `DisplayName` VARCHAR(45) NULL,
  `Logo` BLOB NULL,
  `Blade` BLOB NULL,
  `LCStatus` VARCHAR(45) NOT NULL,
  `Timestamp` TIMESTAMP NOT NULL,
  PRIMARY KEY (`Id`)
);

CREATE TABLE `RRDB`.`Rower` (
  `Id` INT NOT NULL AUTO_INCREMENT,
  `FMRId` VARCHAR(45) NULL,
  `Name` VARCHAR(45) NULL,
  `LastNamePat` VARCHAR(45) NULL,
  `LastNameMat` VARCHAR(45) NULL,
  `BirthDT` DATE NULL,
  `Gender` VARCHAR(1) NULL,
  `Picture` BLOB NULL,
  `TeamId` INT NOT NULL,
  `LCStatus` VARCHAR(45) NOT NULL,
  `Timestamp` TIMESTAMP NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE INDEX `FMRId_UNIQUE` (`FMRId` ASC),
  INDEX `Rower_TeamId_idx` (`TeamId` ASC),
  CONSTRAINT `Rower_TeamId`
    FOREIGN KEY (`TeamId`)
    REFERENCES `RRDB`.`Team` (`Id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
);


-- Systema Catalogs
    
CREATE TABLE `RRDB`.`BoatCatalog` (
  `Id` INT NOT NULL AUTO_INCREMENT,
  `IDFR` VARCHAR(2) NULL,
  `Crew` INT NULL,
  `Substitutes` INT NULL,
  `Coxwain` INT NULL,
  `DisplayName` VARCHAR(45) NULL,
  PRIMARY KEY (`Id`),
  UNIQUE INDEX `IDFR_UNIQUE` (`IDFR` ASC)
);

CREATE TABLE `RRDB`.`GenderCatalog` (
  `Id` INT NOT NULL AUTO_INCREMENT,
  `IDFR` VARCHAR(3) NULL,
  `AllowsMen` TINYINT NULL,
  `AllowsWomen` TINYINT NULL,
  `DisplayName` VARCHAR(45) NULL,
  PRIMARY KEY (`Id`),
  UNIQUE INDEX `IDFR_UNIQUE` (`IDFR` ASC)
);
  
CREATE TABLE `RRDB`.`CategoryCatalog` (
  `Id` INT NOT NULL AUTO_INCREMENT,
  `IDFR` VARCHAR(15) NULL,
  `UpperAgeLimit` INT NULL,
  `LowerAgeLimit` INT NULL,
  `WeightLimit` INT NULL,
  `ExperienceLimit` INT NULL,
  `DisplayName` VARCHAR(45) NULL,
  PRIMARY KEY (`Id`),
  UNIQUE INDEX `IDFR_UNIQUE` (`IDFR` ASC)
);

CREATE TABLE `RRDB`.`VenueCatalog` (
  `Id` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(256) NULL,
  PRIMARY KEY (`Id`));


-- Regattas (Events, races, crews, scores, etc)

CREATE TABLE `RRDB`.`Regatta` (
  `Id` INT NOT NULL AUTO_INCREMENT,
  `Name`VARCHAR(256) NULL,
  `DisplayName` VARCHAR(45) NULL,
  `StartDT` DATE NULL,
  `EndDT` DATE NULL,
  `VenueId` INT NULL,
  `Logo` LONGBLOB NULL,
  `LCStatus` VARCHAR(45) NOT NULL,
  `Timestamp` TIMESTAMP NOT NULL,
  PRIMARY KEY (`Id`),
  INDEX `Regatta_VenueId_idx` (`VenueId` ASC),
  CONSTRAINT `Regatta_VenueId`
    FOREIGN KEY (`VenueId`)
    REFERENCES `RRDB`.`VenueCatalog` (`Id`)
    ON DELETE SET NULL
    ON UPDATE NO ACTION
);


CREATE TABLE `RRDB`.`Event` (
  `Id` INT NOT NULL AUTO_INCREMENT,
  `CategoryId` INT NOT NULL,
  `GenderId` INT NOT NULL,
  `BoatId` INT NOT NULL,
  `Distance` INT NOT NULL,
  `RegattaId` INT NOT NULL,
  `LCStatus` VARCHAR(45) NOT NULL,
  `Timestamp` TIMESTAMP NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE INDEX `Unique_Event_idx` (`RegattaId` ASC, `CategoryId` ASC, `GenderId` ASC, `BoatId` ASC, `Distance` ASC),
  INDEX `Event_RegattaId_idx` (`RegattaId` ASC),
  INDEX `Event_BoatId_idx` (`BoatId` ASC),
  INDEX `Event_GenderId_idx` (`GenderId` ASC),
  INDEX `Event_CategoryId_idx` (`CategoryId` ASC),
  CONSTRAINT `Event_RegattaId`
    FOREIGN KEY (`RegattaId`)
    REFERENCES `RRDB`.`Regatta` (`Id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `Event_BoatId`
    FOREIGN KEY (`BoatId`)
    REFERENCES `RRDB`.`BoatCatalog` (`Id`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT `Event_GenderId`
    FOREIGN KEY (`GenderId`)
    REFERENCES `RRDB`.`GenderCatalog` (`Id`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT `Event_CategoryId`
    FOREIGN KEY (`CategoryId`)
    REFERENCES `RRDB`.`CategoryCatalog` (`Id`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION
);

CREATE TABLE `RRDB`.`Crew` (
  `Id` INT NOT NULL AUTO_INCREMENT,
  `DisplayName` VARCHAR(45) NULL,
  `Combined` TINYINT NULL,
  `TeamId` INT NULL,
  `EventId` INT NOT NULL,
  `LCStatus` VARCHAR(45) NOT NULL,
  `Timestamp` TIMESTAMP NOT NULL,
  PRIMARY KEY (`Id`),
  INDEX `Crew_TeamId_idx` (`TeamId` ASC),
  INDEX `Crew_EventId_idx` (`EventId` ASC),
  CONSTRAINT `Crew_TeamId`
    FOREIGN KEY (`TeamId`)
    REFERENCES `RRDB`.`Team` (`Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `Crew_EventId`
    FOREIGN KEY (`EventId`)
    REFERENCES `RRDB`.`Event` (`Id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
);

CREATE TABLE `RRDB`.`Crew_Rower` (
  `CrewId` INT NOT NULL,
  `RowerId` INT NOT NULL,
  `Nominality` VARCHAR(45) NULL,
  `Seat` VARCHAR(45) NULL,
  PRIMARY KEY (`CrewId`, `RowerId`),
  INDEX `CR_RowerId_idx` (`RowerId` ASC),
  INDEX `CR_CrewId_idx` (`CrewId` ASC),
  CONSTRAINT `CR_CrewId`
    FOREIGN KEY (`CrewId`)
    REFERENCES `RRDB`.`Crew` (`Id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `CR_RowerId`
    FOREIGN KEY (`RowerId`)
    REFERENCES `RRDB`.`Rower` (`Id`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION
);
    
CREATE TABLE `RRDB`.`Race` (
  `Id` INT NOT NULL AUTO_INCREMENT,
  `RaceNumber` INT NULL,
  `StartTime` DATETIME NULL,
  `Progression` VARCHAR(16) NULL,
  `EventId` INT NOT NULL,
  `LCStatus` VARCHAR(45) NULL,
  `Timestamp` TIMESTAMP NOT NULL,
  PRIMARY KEY (`Id`),
  INDEX `Race_EventId_idx` (`EventId` ASC),
  CONSTRAINT `Race_EventId`
    FOREIGN KEY (`EventId`)
    REFERENCES `RRDB`.`Event` (`Id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
);

CREATE TABLE `RRDB`.`Race_Crew` (
  `RaceId` INT NOT NULL,
  `CrewId` INT NOT NULL,
  `Lane` INT NOT NULL,
  PRIMARY KEY (`RaceId`, `CrewId`),
  INDEX `RC_RaceId_idx` (`RaceId` ASC),
  INDEX `RC_CrewId_idx` (`CrewId` ASC),
  CONSTRAINT `RC_RaceId`
    FOREIGN KEY (`RaceId`)
    REFERENCES `RRDB`.`Race` (`Id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `RC_CrewId`
    FOREIGN KEY (`CrewId`)
    REFERENCES `RRDB`.`Crew` (`Id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
);

CREATE TABLE `RRDB`.`ResultSet` (
  `RaceId` INT NOT NULL,
  `CrewId` INT NOT NULL,
  `Mark` INT NOT NULL,
  `Time` TIME NOT NULL DEFAULT 0,    -- CONSIDER FRACTIONAL SECONDS UP TO MILLISECONDS; IF VERSION OF SERVER DOES NOT SUPPORT, THEN USE INT IN MILLISECONDS
  `Partial` TINYINT NOT NULL,
  `Place` INT NOT NULL,
  `LCStatus` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`RaceId`, `CrewId`, `Mark`),
  INDEX `ResultSet_RaceId_idx` (`RaceId` ASC),
  INDEX `ResultSet_CrewId_idx` (`CrewId` ASC),
  CONSTRAINT `ResultSet_RaceId`
    FOREIGN KEY (`RaceId`)
    REFERENCES `RRDB`.`Race` (`Id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `ResultSet_CrewId`
    FOREIGN KEY (`CrewId`)
    REFERENCES `RRDB`.`Crew` (`Id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
);


-- System Users, roles, tokens, etc

CREATE TABLE IF NOT EXISTS `RRDB`.`User` (
  `Id` INT NOT NULL AUTO_INCREMENT,
  `IDFR` VARCHAR(64) NOT NULL,
  `Password` VARCHAR(1024) NOT NULL,
  `Name` VARCHAR(45) NULL DEFAULT NULL,
  `Lastname` VARCHAR(45) NULL DEFAULT NULL,
  `Email` VARCHAR(64) NULL DEFAULT NULL,
  `PhoneNumber` VARCHAR(15) NULL DEFAULT NULL,
  `Avatar` MEDIUMBLOB NULL DEFAULT NULL,
  `Status` VARCHAR(45) NOT NULL,
  `Timestamp` TIMESTAMP NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE INDEX `IDFR_UNIQUE` (`IDFR` ASC) VISIBLE)  
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `RRDB`.`Role` (
  `Id` INT NOT NULL AUTO_INCREMENT,
  `IDFR` VARCHAR(256) NOT NULL,
  `Role` VARCHAR(32) NOT NULL,
  `UserId` INT NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE INDEX `Role_UNIQUE` (`IDFR` ASC, `Role` ASC) VISIBLE,
  INDEX `Role_User_idx` (`UserId` ASC) VISIBLE,
  CONSTRAINT `Role_User`
    FOREIGN KEY (`UserId`)
    REFERENCES `RRDB`.`User` (`Id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE TABLE `RRDB`.`BearerToken` (
  `Id` INT NOT NULL AUTO_INCREMENT,
  `AccessToken` VARCHAR(45) NOT NULL,
  `TokenType` VARCHAR(45) NOT NULL,
  `Expiration` INT NOT NULL,
  `UserId` INT NOT NULL,
  `LCStatus` VARCHAR(45) NOT NULL,
  `Timestamp` TIMESTAMP NOT NULL,
  PRIMARY KEY (`Id`),
  INDEX `BearerToken_UserId_idx` (`UserId` ASC),
  UNIQUE INDEX `AccessToken_UNIQUE` (`AccessToken` ASC),
  CONSTRAINT `BearerToken_UserId`
    FOREIGN KEY (`UserId`)
    REFERENCES `RRDB`.`User` (`Id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE `RRDB`.`Invitation` (
  `Id` INT NOT NULL AUTO_INCREMENT,
  `IDFR` VARCHAR(45) NOT NULL,
  `Name` VARCHAR(45) NULL,
  `LastName` VARCHAR(45) NULL,
  `Email` VARCHAR(64) NULL,
  `Role` VARCHAR(45) NULL,
  `AuthorityUserId` INT NOT NULL DEFAULT 0,
  `LCStatus` VARCHAR(45) NOT NULL,
  `Timestamp` TIMESTAMP NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE INDEX `IDFR_UNIQUE` (`IDFR` ASC))
ENGINE = InnoDB;
