CREATE SCHEMA IF NOT EXISTS site;
-- Create Owner table
CREATE TABLE site.owners (
                        id SERIAL PRIMARY KEY,
                        first_name VARCHAR(50) NOT NULL,
                        last_name VARCHAR(50) NOT NULL,
                        address VARCHAR(100),
                        city VARCHAR(50),
                        telephone VARCHAR(20)
);

-- Create Pet table
CREATE TABLE site.pets (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(50) NOT NULL,
                      birth_date DATE,
                      type VARCHAR(30) NOT NULL,
                      owner_id INTEGER,
                      FOREIGN KEY (owner_id) REFERENCES site.owners(id) ON DELETE CASCADE
);
