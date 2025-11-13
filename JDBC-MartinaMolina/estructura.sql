CREATE DATABASE biblioteca;
USE biblioteca;

CREATE TABLE libros(
id INT auto_increment primary key,
titulo varchar(150),
autor varchar(120),
anio_publicacion year,
isbn varchar(20),
disponible boolean
);
select * FROM libros;
ALTER TABLE libros MODIFY isbn VARCHAR(20) NULL;
ALTER TABLE libros DROP INDEX isbn;
ALTER TABLE libros MODIFY anio_publicacion INT;

create table categorias(
id int auto_increment primary key,
nombre varchar(120) not null
);

create table productos(
id int auto_increment primary key,
nombre varchar(150) not null,
precio decimal(10,2) not null,
stock int not null ,
categoria_id int not null,
CONSTRAINT fk_prod_cat FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);

select p.id, p.nombre, p.precio, p.stock, c.nombre as categoria
from productos p 
join categorias c on c.id=p.categoria_id
order by p.id;

INSERT INTO categorias (nombre) VALUES
  ('Electrónica'),
  ('Hogar'),
  ('Deportes'),
  ('Libros'),
  ('Juguetes');
  

  
  create table estudiantes(
  id int auto_increment primary key,
  nombre varchar(120) not null,
  apellido varchar(120),
  email varchar(120),
  edad int 
  );
    create table calificaciones(
  id int auto_increment primary key,
  estudiante_id int not null,
  materia varchar(50) not null,
  nota decimal (4,2)not null,
  fecha date not null,
  CONSTRAINT fk_calif_est FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  INDEX idx_calif_estudiante (estudiante_id),
  INDEX idx_calif_estudiante_fecha (estudiante_id, fecha)
  );
  
  ALTER TABLE calificaciones 
  ADD CONSTRAINT chk_nota_0_10 CHECK (nota >= 0 AND nota <= 10);

ALTER TABLE estudiantes 
  ADD CONSTRAINT chk_edad_pos CHECK (edad >= 0 AND edad <= 120);
  
  insert into estudiantes (nombre, apellido, email, edad )
  values ('Valentina','Catala', 'valencatala6@gmail.com', '21'),
		('Jose Gabriel','Bordiga','gbordiga@gmail.com','21');
insert calificaciones (estudiante_id, materia, nota, fecha)
values
(1, 'Interfaz Grafica', 8.50, '2025-10-01'),
(1, 'Aplicaciones Web', 9.00, '2025-10-15'),
(2, 'Mercadotecnia', 7.00, '2025-09-20');

select * from calificaciones