package transformer

import (
	index "github.com/im-a-giraffe/disdrillery/v1/disdrillery/database"
	"path/filepath"
)

type Transformer interface {
	GetName() string
	// Operational level defines how and where the transformer is called within the mining process
	GetOperationalLevel() string
	Export()
}

func CreateFilepathFromWorkingDir(indexStorage *index.IndexStorage, filename string) string {
	return indexStorage.GetDataDir() + string(filepath.Separator) + filename
}
