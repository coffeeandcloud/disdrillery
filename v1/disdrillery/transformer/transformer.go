package transformer

import (
	index "github.com/im-a-giraffe/disdrillery/v1/disdrillery/index"
	"path/filepath"
)

type Transformer interface {
	GetName() string
	// Operational level defines how and where the transformer is called within the mining process
	GetOperationalLevel() string
	Export()
	GetMetaInfo() []index.Meta
}

func GetDataFilepathFromWorkingDir(indexStorage *index.IndexStorage, filename string) string {
	return indexStorage.GetDataDir() + string(filepath.Separator) + filename + "." + indexStorage.GetIndexFile().StorageFormat
}

func GetFileFilepathFromWorkingDir(indexStorage *index.IndexStorage, filename string) string {
	return indexStorage.GetFileDir() + string(filepath.Separator) + filename
}
