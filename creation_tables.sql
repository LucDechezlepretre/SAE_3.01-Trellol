CREATE TABLE User(id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                  nom varchar(40),
                  prenom varchar(40),
                  login varchar(40),
                  mdp varchar(256));

CREATE TABLE Tache(idTache INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                  nom varchar(40),
                  dateDebut date,
                  duree int,
                  description varchar(256),
                  importance int);

CREATE TABLE ComposeeDe(idTacheComposee int,
                        idTacheComposant int,
                        PRIMARY KEY(idTacheComposee, idTacheComposant),
			FOREIGN KEY (idTacheComposee) REFERENCES Tache(idTache),
			FOREIGN KEY (idTacheComposant) REFERENCES Tache(idTache));

CREATE TABLE DependanteDe(idTacheAutonome int,
                        idTacheDependante int,
                        PRIMARY KEY(idTacheAutonome, idTacheDependante),
			FOREIGN KEY (idTacheAutonome) REFERENCES Tache(idTache),
			FOREIGN KEY (idTacheDependante) REFERENCES Tache(idTache));


CREATE TABLE ListeTache(idListeTache INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                        nom varchar(40));
                        
CREATE TABLE Contient(idListeTache INT,
                      idTache INT,
                      PRIMARY KEY(idListeTache, idTache),
			FOREIGN KEY (idListeTache) REFERENCES ListeTache(idListeTache),
			FOREIGN KEY (idTache) REFERENCES Tache(idTache));
                        

CREATE TABLE Responsable(idResponsable INT,
                      idTache INT,
                      PRIMARY KEY(idResponsable, idTache),
		      	FOREIGN KEY (idResponsable) REFERENCES User(id),
			FOREIGN KEY (idTache) REFERENCES Tache(idTache));
                        

CREATE TABLE Historique(idAction INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                        action varchar(256));
