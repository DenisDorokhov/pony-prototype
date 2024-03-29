CREATE TABLE installation (

	id BIGINT NOT NULL AUTO_INCREMENT,

	creation_date TIMESTAMP NOT NULL,
	update_date TIMESTAMP NOT NULL,

	version VARCHAR(255) NOT NULL,

	PRIMARY KEY (id)

) CHARSET=UTF8 ENGINE=InnoDB;

CREATE TABLE configuration (

	id VARCHAR(255) NOT NULL,

	creation_date TIMESTAMP NOT NULL,
	update_date TIMESTAMP NOT NULL,

	value TEXT,

	PRIMARY KEY (id)

) CHARSET=UTF8 ENGINE=InnoDB;

CREATE TABLE scan_result (

	id BIGINT NOT NULL AUTO_INCREMENT,

	date TIMESTAMP NOT NULL,

	success BIT(1) NOT NULL,

	duration BIGINT NOT NULL,

	scanned_folder_count BIGINT NOT NULL,
	scanned_file_count BIGINT NOT NULL,

	imported_file_count BIGINT NOT NULL,

	PRIMARY KEY (id)

) CHARSET=UTF8 ENGINE=InnoDB;

CREATE INDEX index_scan_result_creation_date ON scan_result(date);

CREATE TABLE scan_result_path (

	scan_result_id BIGINT NOT NULL,

	path VARCHAR(255) NOT NULL,

	PRIMARY KEY (scan_result_id, path)

) CHARSET=UTF8 ENGINE=InnoDB;

CREATE TABLE stored_file (

	id BIGINT NOT NULL AUTO_INCREMENT,

	creation_date TIMESTAMP NOT NULL,
	update_date TIMESTAMP NOT NULL,

	name VARCHAR(255) NOT NULL,
	mime_type VARCHAR(255) NOT NULL,
	checksum VARCHAR(255) NOT NULL,
	tag VARCHAR(255),
	relative_path VARCHAR(255) NOT NULL,
	user_data VARCHAR(255),

	UNIQUE (relative_path),
	UNIQUE (tag, checksum),
	PRIMARY KEY (id)

) CHARSET=UTF8 ENGINE=InnoDB;

CREATE INDEX index_stored_file_checksum ON stored_file(checksum);
CREATE INDEX index_stored_file_tag ON stored_file(tag);

CREATE TABLE song_file (

	id BIGINT NOT NULL AUTO_INCREMENT,

	creation_date TIMESTAMP NOT NULL,
	update_date TIMESTAMP NOT NULL,

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
	album_artist VARCHAR(255),
	album VARCHAR(255),

	year INT,

	artwork_stored_file_id BIGINT,

	FOREIGN KEY (artwork_stored_file_id) REFERENCES stored_file(id) ON DELETE SET NULL ON UPDATE CASCADE,

	UNIQUE (path),
	PRIMARY KEY (id)

) CHARSET=UTF8 ENGINE=InnoDB;

CREATE INDEX index_song_file_track_number_name ON song_file(disc_number, track_number, name);

CREATE TABLE artist (

	id BIGINT NOT NULL AUTO_INCREMENT,

	creation_date TIMESTAMP NOT NULL,
	update_date TIMESTAMP NOT NULL,

	name VARCHAR(255) NOT NULL,
	artwork_stored_file_id BIGINT,

	FOREIGN KEY (artwork_stored_file_id) REFERENCES stored_file(id) ON DELETE SET NULL ON UPDATE CASCADE,

	UNIQUE (name),
	PRIMARY KEY (id)

) CHARSET=UTF8 ENGINE=InnoDB;

CREATE TABLE album (

	id BIGINT NOT NULL AUTO_INCREMENT,

	creation_date TIMESTAMP NOT NULL,
	update_date TIMESTAMP NOT NULL,

	name VARCHAR(255) NOT NULL,
	year INT,
	artwork_stored_file_id BIGINT,

	artist_id BIGINT NOT NULL,

	FOREIGN KEY (artist_id) REFERENCES artist(id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (artwork_stored_file_id) REFERENCES stored_file(id) ON DELETE SET NULL ON UPDATE CASCADE,

	UNIQUE (name, artist_id),
	PRIMARY KEY (id)

) CHARSET=UTF8 ENGINE=InnoDB;

CREATE INDEX index_album_artist_id_name ON album(artist_id, name);
CREATE INDEX index_album_artist_id_year_name ON album(artist_id, year, name);

CREATE TABLE song (

	id BIGINT NOT NULL AUTO_INCREMENT,

	creation_date TIMESTAMP NOT NULL,
	update_date TIMESTAMP NOT NULL,

	song_file_id BIGINT NOT NULL,
	album_id BIGINT NOT NULL,

	UNIQUE (song_file_id),
	PRIMARY KEY (id),

	FOREIGN KEY (song_file_id) REFERENCES song_file(id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (album_id) REFERENCES album(id) ON DELETE CASCADE ON UPDATE CASCADE

) CHARSET=UTF8 ENGINE=InnoDB;

INSERT INTO installation (creation_date, update_date, version) VALUES (NOW(), NOW(), '1.0');