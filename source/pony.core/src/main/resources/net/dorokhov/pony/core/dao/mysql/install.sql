CREATE TABLE song_file (

	id INT NOT NULL AUTO_INCREMENT,

	creation_date TIMESTAMP NOT NULL,
	update_date TIMESTAMP NOT NULL,

	generation BIGINT NOT NULL,

	path VARCHAR(255) NOT NULL,
	format VARCHAR(255) NOT NULL,
	mime_type VARCHAR(255) NOT NULL,
	size BIGINT NOT NULL,

	duration INT NOT NULL,
	bit_rate BIGINT NOT NULL,

	disc_number INT,
	disc_count INT,

	track_number INT,
	track_count INT,

	name VARCHAR(255),
	artist VARCHAR(255),
	album VARCHAR(255),

	year INT,

	UNIQUE (path),
	PRIMARY KEY (id)

) CHARSET=UTF8 ENGINE=InnoDB;

CREATE TABLE artist (

	id INT NOT NULL AUTO_INCREMENT,

	creation_date TIMESTAMP NOT NULL,
	update_date TIMESTAMP NOT NULL,

	generation BIGINT NOT NULL,

	name VARCHAR(255) NOT NULL,

	UNIQUE (name),
	PRIMARY KEY (id)

) CHARSET=UTF8 ENGINE=InnoDB;

CREATE TABLE album (

	id INT NOT NULL AUTO_INCREMENT,

	creation_date TIMESTAMP NOT NULL,
	update_date TIMESTAMP NOT NULL,

	generation BIGINT NOT NULL,

	name VARCHAR(255) NOT NULL,
	disc_count INT,
	track_count INT,
	year INT,

	artist_id INT NOT NULL,

	UNIQUE (name, artist_id),
	PRIMARY KEY (id),

	FOREIGN KEY (artist_id) REFERENCES artist(id) ON DELETE CASCADE ON UPDATE CASCADE

) CHARSET=UTF8 ENGINE=InnoDB;

CREATE TABLE song (

	id INT NOT NULL AUTO_INCREMENT,

	creation_date TIMESTAMP NOT NULL,
	update_date TIMESTAMP NOT NULL,

	generation BIGINT NOT NULL,

	song_file_id INT NOT NULL,
	album_id INT NOT NULL,

	UNIQUE (song_file_id),
	PRIMARY KEY (id),

	FOREIGN KEY (song_file_id) REFERENCES song_file(id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (album_id) REFERENCES album(id) ON DELETE CASCADE ON UPDATE CASCADE

) CHARSET=UTF8 ENGINE=InnoDB;

CREATE TABLE installation (

	id INT NOT NULL AUTO_INCREMENT,

	creation_date TIMESTAMP NOT NULL,
	update_date TIMESTAMP NOT NULL,

	generation BIGINT NOT NULL,

	version VARCHAR(255) NOT NULL,

	PRIMARY KEY (id)

) CHARSET=UTF8 ENGINE=InnoDB;

INSERT INTO installation (creation_date, update_date, generation, version) VALUES (NOW(), NOW(), '0', '1.0');