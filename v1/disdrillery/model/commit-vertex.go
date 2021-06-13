package model

type CommitVertex struct {
	RepositoryName	   string `parquet:"name=REPOSITORY, type=BYTE_ARRAY, convertedtype=UTF8"`
	CommitHash         string `parquet:"name=COMMIT_HASH, type=BYTE_ARRAY, convertedtype=UTF8"`
	AuthorName         string `parquet:"name=AUTHOR_NAME, type=BYTE_ARRAY, convertedtype=UTF8"`
	AuthorMail         string `parquet:"name=AUTHOR_MAIL, type=BYTE_ARRAY, convertedtype=UTF8"`
	AuthorTimestamp    int64  `parquet:"name=AUTHOR_TIMESTAMP, type=INT64"`
	CommitterName      string `parquet:"name=COMMITTER_NAME, type=BYTE_ARRAY, convertedtype=UTF8"`
	CommitterMail      string `parquet:"name=COMMITTER_MAIL, type=BYTE_ARRAY, convertedtype=UTF8"`
	CommitterTimestamp int64  `parquet:"name=COMMITTER_TIMESTAMP, type=INT64"`
	CommitMessage      string `parquet:"name=COMMIT_MESSAGE, type=BYTE_ARRAY, convertedtype=UTF8"`
}
