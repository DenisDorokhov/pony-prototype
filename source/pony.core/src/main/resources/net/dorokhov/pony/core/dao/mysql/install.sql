CREATE TABLE installation (
	
	id INT NOT NULL AUTO_INCREMENT,

    creation_date TIMESTAMP NOT NULL,
    update_date TIMESTAMP NOT NULL,

    generation BIGINT NOT NULL,
    
    version VARCHAR(255) NOT NULL,
    
    PRIMARY KEY (id)

) CHARSET=UTF8 ENGINE=InnoDB;

INSERT INTO installation (creation_date, update_date, generation, version) VALUES (NOW(), NOW(), '0', '1.0');