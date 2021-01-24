package model

type FileContentVertex struct {
	CommitHash string `parquet:"name=COMMIT_HASH, type=BYTE_ARRAY, convertedtype=UTF8"`
	ObjectHash string `parquet:"name=OBJECT_HASH, type=BYTE_ARRAY, convertedtype=UTF8"`
	FileName   string `parquet:"name=FILE_NAME, type=BYTE_ARRAY, convertedtype=UTF8"`
	FileSize   int64  `parquet:"name=FILE_SIZE, type=INT64, convertedtype=INT_64"`
}
