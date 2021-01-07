package model

type FileContentVertex struct {
	CommitHash   string
	ObjectHash   string
	FileName     string
	RelativePath string
}
