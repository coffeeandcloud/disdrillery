package model

type CommitEdge struct {
	CommitHash       *string `parquet:"name=COMMIT_HASH, type=BYTE_ARRAY, convertedtype=UTF8, repetitiontype=OPTIONAL"`
	ParentCommitHash *string `parquet:"name=PARENT_COMMIT_HASH, type=BYTE_ARRAY, convertedtype=UTF8, repetitiontype=OPTIONAL"`
}
