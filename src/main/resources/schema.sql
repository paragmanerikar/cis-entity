CREATE TABLE person(
    id INT NOT NULL,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(30),
    mobile VARCHAR(15)
);

CREATE TABLE address(
    id INT NOT NULL,
    city VARCHAR(50),
    personId INT NOT NULL
);
