CREATE TABLE Rol (
  id_rol INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(50) NOT NULL UNIQUE,
  descripcion VARCHAR(255)
);

CREATE TABLE Usuario (
  id_usuario INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  apellido VARCHAR(100) NOT NULL,
  email VARCHAR(120) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
  ultima_modificacion DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  fecha_baja DATETIME DEFAULT NULL,
  usuario_baja INT DEFAULT NULL,
  id_rol INT NOT NULL,
  FOREIGN KEY (id_rol) REFERENCES Rol(id_rol)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (usuario_baja) REFERENCES Usuario(id_usuario)
    ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE Curso (
  id_curso INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  descripcion TEXT,
  ciclo_lectivo INT NOT NULL,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  fecha_baja DATETIME DEFAULT NULL
);

CREATE TABLE Materia (
  id_materia INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  id_curso INT NOT NULL,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  fecha_baja DATETIME DEFAULT NULL,
  FOREIGN KEY (id_curso) REFERENCES Curso(id_curso)
    ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE Docente (
  id_docente INT PRIMARY KEY,
  legajo VARCHAR(20) NOT NULL UNIQUE,
  especialidad VARCHAR(100),
  activo TINYINT(1) NOT NULL DEFAULT 1,
  fecha_baja DATETIME DEFAULT NULL,
  FOREIGN KEY (id_docente) REFERENCES Usuario(id_usuario)
    ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE Alumno (
  id_alumno INT PRIMARY KEY,
  legajo VARCHAR(20) NOT NULL UNIQUE,
  fecha_ingreso DATE NOT NULL,
  id_curso INT DEFAULT NULL,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  fecha_baja DATETIME DEFAULT NULL,
  FOREIGN KEY (id_alumno) REFERENCES Usuario(id_usuario)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (id_curso) REFERENCES Curso(id_curso)
    ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE Padre (
  id_padre INT PRIMARY KEY,
  telefono VARCHAR(30),
  vinculo VARCHAR(50),
  activo TINYINT(1) NOT NULL DEFAULT 1,
  fecha_baja DATETIME DEFAULT NULL,
  FOREIGN KEY (id_padre) REFERENCES Usuario(id_usuario)
    ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE Padre_Alumno (
  id_padre INT NOT NULL,
  id_alumno INT NOT NULL,
  PRIMARY KEY (id_padre, id_alumno),
  FOREIGN KEY (id_padre) REFERENCES Padre(id_padre)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (id_alumno) REFERENCES Alumno(id_alumno)
    ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE Clase (
  id_clase INT AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(150) NOT NULL,
  tema VARCHAR(255) NOT NULL,
  objetivos TEXT,
  fecha DATE NOT NULL,
  id_materia INT NOT NULL,
  id_docente INT NOT NULL,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
  ultima_modificacion DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  fecha_baja DATETIME DEFAULT NULL,
  FOREIGN KEY (id_materia) REFERENCES Materia(id_materia)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (id_docente) REFERENCES Docente(id_docente)
    ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE Recurso (
  id_recurso INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  tipo ENUM('Archivo','Enlace') NOT NULL,
  ruta TEXT NOT NULL,
  id_clase INT NOT NULL,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  fecha_baja DATETIME DEFAULT NULL,
  FOREIGN KEY (id_clase) REFERENCES Clase(id_clase)
    ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE Tarea (
  id_tarea INT AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(200) NOT NULL,
  descripcion TEXT,
  fecha_entrega DATE,
  estado ENUM('Pendiente','Entregada','Atrasada') DEFAULT 'Pendiente',
  id_materia INT NOT NULL,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  fecha_baja DATETIME DEFAULT NULL,
  FOREIGN KEY (id_materia) REFERENCES Materia(id_materia)
    ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE Examen (
  id_examen INT AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(200) NOT NULL,
  fecha DATE NOT NULL,
  tipo ENUM('Parcial','Final','Evaluación') DEFAULT 'Evaluación',
  calificacion_minima DECIMAL(4,2) DEFAULT 4.00,
  id_materia INT NOT NULL,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  fecha_baja DATETIME DEFAULT NULL,
  FOREIGN KEY (id_materia) REFERENCES Materia(id_materia)
    ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE Mensaje (
  id_mensaje INT AUTO_INCREMENT PRIMARY KEY,
  remitente_id INT NOT NULL,
  destinatario_id INT NOT NULL,
  asunto VARCHAR(200),
  cuerpo TEXT NOT NULL,
  fecha_envio DATETIME DEFAULT CURRENT_TIMESTAMP,
  leido TINYINT(1) DEFAULT 0,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  fecha_baja DATETIME DEFAULT NULL,
  FOREIGN KEY (remitente_id) REFERENCES Usuario(id_usuario)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (destinatario_id) REFERENCES Usuario(id_usuario)
    ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE Auditoria (
  id_auditoria INT AUTO_INCREMENT PRIMARY KEY,
  usuario_id INT DEFAULT NULL,
  accion VARCHAR(100) NOT NULL,
  entidad VARCHAR(50) NOT NULL,
  entidad_id INT DEFAULT NULL,
  fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
  detalle TEXT,
  FOREIGN KEY (usuario_id) REFERENCES Usuario(id_usuario)
    ON DELETE SET NULL ON UPDATE CASCADE
);
