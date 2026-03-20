CREATE TABLE category (
                          id      BIGSERIAL PRIMARY KEY,
                          name    VARCHAR(100) NOT NULL UNIQUE,
                          description VARCHAR(255)
);

CREATE TABLE product (
                         id          BIGSERIAL PRIMARY KEY,
                         name        VARCHAR(150) NOT NULL,
                         description VARCHAR(500),
                         price       NUMERIC(10,2) NOT NULL,
                         stock       INTEGER NOT NULL DEFAULT 0,
                         category_id BIGINT NOT NULL REFERENCES category(id)
);