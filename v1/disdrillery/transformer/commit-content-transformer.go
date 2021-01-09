package transformer

import (
	"github.com/emirpasic/gods/sets/hashset"
	"github.com/go-git/go-git/v5/plumbing/object"
	index "github.com/im-a-giraffe/disdrillery/v1/disdrillery/database"
	"github.com/im-a-giraffe/disdrillery/v1/disdrillery/export"
	"github.com/im-a-giraffe/disdrillery/v1/disdrillery/model"
	"io"
	"log"
	"os"
	"path/filepath"
)

const CommitContentTransformerName string = "CommitContent"

type CommitContentTransformer struct {
	name              string
	operationalLevel  string
	fileContentOutput string
	fileContentData   []model.FileContentVertex
	copiedFiles       *hashset.Set
}

func (transformer *CommitContentTransformer) GetName() string {
	return transformer.name
}

func (transformer *CommitContentTransformer) GetOperationalLevel() string {
	return transformer.operationalLevel
}

func (transformer *CommitContentTransformer) AppendFileContentVertex(vertex model.FileContentVertex) {
	transformer.fileContentData = append(transformer.fileContentData, vertex)
}

func (transformer *CommitContentTransformer) Export() {
	vertexWriter := export.GetParquetWriter(transformer.fileContentOutput, new(model.FileContentVertex))
	export.GetInstance().
		SetWriter(vertexWriter).
		Export(&transformer.fileContentData)
}

func GetCommitContentTransformerInstance(indexStorage *index.IndexStorage) *CommitContentTransformer {
	return &CommitContentTransformer{
		name:              CommitContentTransformerName,
		operationalLevel:  "file",
		fileContentOutput: GetDataFilepathFromWorkingDir(indexStorage, "commit-content-vertex"),
		copiedFiles:       hashset.New(),
	}
}

func (transformer *CommitContentTransformer) GetMetaInfo() []index.Meta {
	metas := make([]index.Meta, 1)
	metas = append(metas, index.Meta{
		Providing: transformer.GetName(),
		File:      transformer.fileContentOutput,
	})
	return metas
}

func (transformer *CommitContentTransformer) CopyFile(file *object.File) {
	if transformer.copiedFiles.Contains(file.Hash.String()) {
		return
	}
	reader, err := file.Reader()
	if err != nil {
		log.Fatal(err)
	}
	output := index.GetInstance().GetFileDir() + string(filepath.Separator) + file.Hash.String()
	writer, err := os.Create(output)
	if err != nil {
		log.Println(err)
	}
	defer reader.Close()
	defer writer.Close()
	_, err = io.Copy(writer, reader)
	if err != nil {
		log.Println(err)
	} else {
		//log.Printf("Copied: '%s' -> '%s' (%d bytes)", file.Name, output, written)
		transformer.copiedFiles.Add(file.Hash.String())
	}
}
