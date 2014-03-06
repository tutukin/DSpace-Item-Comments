	CREATE SEQUENCE comments_seq;
	ALTER  SEQUENCE comments_seq
		OWNER TO dspace;
		
	CREATE TABLE comments (
		comment_id		integer NOT NULL DEFAULT nextval('comments_seq'),
		item_id			integer,
		submitter_id	integer,
		submitted		timestamp with time zone,
		text			text,
		CONSTRAINT comments_pkey PRIMARY KEY (comment_id),
		CONSTRAINT comments_item_id_fkey FOREIGN KEY (item_id)
			REFERENCES item (item_id) MATCH SIMPLE
			ON UPDATE NO ACTION
			ON DELETE NO ACTION,
		CONSTRAINT comments_submitter_id_fkey FOREIGN KEY (submitter_id)
			REFERENCES eperson (eperson_id) MATCH SIMPLE
			ON UPDATE NO ACTION
			ON DELETE NO ACTION
	);
	
	ALTER TABLE comments
	  OWNER TO dspace;
	 
	CREATE INDEX comments_submitter_fk_idx
		ON comments
		USING btree
		(submitter_id);
	
	CREATE INDEX comments_item_fk_idx
		ON comments
		USING btree
		(item_id);
	
	INSERT INTO comments (item_id, submitter_id, submitted, text)
	VALUES (1101, 1, '2014-01-21 13:00:13.758+04', 'Текст первого коммента');
	
	INSERT INTO comments (item_id, submitter_id, submitted, text)
	VALUES (1101, 1, '2014-01-23 14:15:53.758+04', 'Текст второго коммента');
	
	INSERT INTO comments (item_id, submitter_id, submitted, text)
	VALUES (1101, 1, '2014-01-24 12:11:23.758+04', 'Текст третьего коммента');
	
	INSERT INTO comments (item_id, submitter_id, submitted, text)
	VALUES (1101, 1, '2014-01-25 09:10:30.758+04', 'Текст четвёртого коммента');
	
	
	
	CREATE SEQUENCE comments_registration_req_seq;
	ALTER SEQUENCE comments_registration_req_seq
		OWNER TO dspace;
	
	
	CREATE TABLE comments_registration_req (
		comments_registration_req_id	INTEGER NOT NULL DEFAULT NEXTVAL('comments_registration_req_seq'),
		email							CHARACTER VARYING(64),
		firstname						CHARACTER VARYING(64),
  		lastname						CHARACTER VARYING(64),
		CONSTRAINT comments_registration_req_pkey PRIMARY KEY (comments_registration_req_id),
		CONSTRAINT comments_registration_req_email_unique UNIQUE(email)
	);
	
	ALTER TABLE comments_registration_req
	  OWNER TO dspace;
	
	
	CREATE SEQUENCE comments_stash_seq;
	ALTER  SEQUENCE comments_stash_seq
		OWNER TO dspace;
		
	CREATE TABLE comments_stash (
		comments_stash_id	integer NOT NULL DEFAULT nextval('commentsstash_seq'),
		submitter_id		INTEGER,
		text				TEXT,
		item_id				INTEGER,
		submitted			TIMESTAMP WITH TIME ZONE,
		CONSTRAINT comments_stash_pkey PRIMARY KEY (comments_stash_id),
		CONSTRAINT comments_stash_item_id_fkey FOREIGN KEY (item_id)
			REFERENCES item (item_id) MATCH SIMPLE
			ON UPDATE NO ACTION
			ON DELETE NO ACTION,
		CONSTRAINT comments_stash_submitter_id_fkey FOREIGN KEY (submitter_id)
			REFERENCES comments_registration_req(comments_registration_req_id) MATCH SIMPLE
			ON UPDATE NO ACTION
			ON DELETE NO ACTION
	);
	
	ALTER TABLE comments_stash
		OWNER TO dspace;
	
	
	
	INSERT INTO comments_registration_req (email, firstname, lastname)
	VALUES ('hui_nan@mail.ru', 'Hui', 'Nan');
	
	INSERT INTO comments_stash (submitter_id, text, item_id, submitted)
	SELECT comments_registration_req_id,
		'hui_nan-s comment 1',
		1101,
		'2014-02-21 13:00:13.758+04'
		FROM comments_registration_req
		WHERE email='hui_nan@mail.ru';
	
	INSERT INTO comments_stash (submitter_id, text, item_id, submitted)
	SELECT comments_registration_req_id,
		'hui_nan-s comment 2',
		1101,
		'2014-02-23 13:00:13.758+04'
		FROM comments_registration_req
		WHERE email='hui_nan@mail.ru';
	
	INSERT INTO comments_stash (submitter_id, text, item_id, submitted)
	SELECT comments_registration_req_id,
		'hui_nan-s comment 3',
		1101,
		'2014-02-25 13:00:13.758+04'
		FROM comments_registration_req
		WHERE email='hui_nan@mail.ru';
	
	
	
	
	INSERT INTO comments_registration_req (email, firstname, lastname)
	VALUES ('nowhereman@beatles.gb', 'Nowher', 'Mann');
	
	INSERT INTO comments_stash (submitter_id, text, item_id, submitted)
	SELECT comments_registration_req_id,
		'nowhereman-s comment 1',
		1101,
		'2014-02-11 15:00:13.758+04'
		FROM comments_registration_req
		WHERE email='nowhereman@beatles.gb';
	
	INSERT INTO comments_stash (submitter_id, text, item_id, submitted)
	SELECT comments_registration_req_id,
		'nowhereman-s comment 2',
		1101,
		'2014-02-11 23:00:13.758+04'
		FROM comments_registration_req
		WHERE email='nowhereman@beatles.gb';
	
	
	
	INSERT INTO comments_registration_req (email, firstname, lastname)
	VALUES ('skywalker@starwars.gov', 'Люк', 'Скайвокер');
	
	INSERT INTO comments_stash (submitter_id, text, item_id, submitted)
	SELECT comments_registration_req_id,
		'skywalker-s comment 1',
		1101,
		'2014-02-11 13:00:13.758+04'
		FROM comments_registration_req
		WHERE email='skywalker@starwars.gov';
	
	INSERT INTO comments_stash (submitter_id, text, item_id, submitted)
	SELECT comments_registration_req_id,
		'skywalker-s comment 2',
		1101,
		'2014-02-11 13:20:13.758+04'
		FROM comments_registration_req
		WHERE email='skywalker@starwars.gov';
	
	