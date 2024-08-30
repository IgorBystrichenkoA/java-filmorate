
CREATE TABLE IF NOT EXISTS mpa (
	id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name varchar(40)
);


CREATE TABLE IF NOT EXISTS films (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(40) NOT NULL,
    description varchar(200),
    releaseDate date,
    duration integer,
    mpa integer
);

CREATE TABLE IF NOT EXISTS users (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email varchar(40),
    login varchar(40) NOT NULL,
    name varchar(40),
    birthday date
);

CREATE TABLE IF NOT EXISTS likes (
    user_id int REFERENCES users,
    film_id int REFERENCES films,
    PRIMARY KEY (user_id, film_id)
);

CREATE TABLE IF NOT EXISTS genres (
	id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name varchar(40),
    film_id int REFERENCES films
);

CREATE TABLE IF NOT EXISTS friends (
    user_id int REFERENCES users,
    user_friend_id int REFERENCES users,
    PRIMARY KEY (user_id, user_friend_id)
);