package database

import (
	"database/sql"
	"github.com/im-a-giraffe/disdrillery/v1/disdrillery/index"
	"github.com/im-a-giraffe/disdrillery/v1/disdrillery/model"
	"log"
)

type Repository struct {
	sqliteFile string
	db         *sql.DB
}

func (repo *Repository) Init(indexStorage *index.IndexStorage, file string) {
	var err error
	repo.db, err = sql.Open("sqlite3", file)
	if err != nil {
		log.Fatal(err)
	}

	sqlStmt := `
		CREATE TABLE IF NOT EXISTS FILE_CONTENT (
			COMMIT_HASH VARCHAR(40) PRIMARY KEY,
			OBJECT_HASH VARCHAR(40) NOT NULL,
			FILE_NAME TEXT NOT NULL,
			FILE_SIZE INTEGER
		)`

	_, err = repo.db.Exec(sqlStmt)
	if err != nil {
		log.Fatal(err)
	}
}

func (repo *Repository) Save(vertex model.FileContentVertex) {
	tx, err := repo.db.Begin()
	if err != nil {
		log.Fatal(err)
	}

	stmt, err := tx.Prepare("INSERT INTO FILE_CONTENT(COMMIT_HASH, OBJECT_HASH, FILE_NAME, FILE_SIZE) VALUES(?,?,?,?)")
	if err != nil {
		log.Fatal(err)
	}
	defer stmt.Close()
	stmt.Exec()
}

func (repo *Repository) Close() {
	repo.db.Close()
}
