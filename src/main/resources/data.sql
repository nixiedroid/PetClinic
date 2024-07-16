-- Insert data into owners table
INSERT INTO site.owners (first_name, last_name, address, city, telephone) VALUES
                                                                         ('John', 'Doe', '123 Main St', 'Anytown', '555-1234'),
                                                                         ('Jane', 'Smith', '456 Maple Ave', 'Othertown', '555-5678');

-- Insert data into pets table
INSERT INTO site.pets (name, birth_date, type, owner_id) VALUES
                                                        ('Buddy', '2020-01-15', 'Dog', 1),
                                                        ('Mittens', '2018-06-23', 'Cat', 2);