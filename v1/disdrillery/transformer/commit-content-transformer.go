package transformer

import (
	"io"
	"log"
	"os"
	"path/filepath"

	"github.com/go-git/go-git/v5/plumbing/object"
	"github.com/im-a-giraffe/disdrillery/v1/disdrillery/export"
	index "github.com/im-a-giraffe/disdrillery/v1/disdrillery/index"
	"github.com/im-a-giraffe/disdrillery/v1/disdrillery/model"
)

const CommitContentTransformerName string = "CommitContent"

type CommitContentTransformer struct {
	name              string
	operationalLevel  string
	fileContentOutput string
	fileContentData   []model.FileContentVertex
	contentExporter   *export.ParquetExporter
}

func (transformer *CommitContentTransformer) GetName() string {
	return transformer.name
}

func (transformer *CommitContentTransformer) GetOperationalLevel() string {
	return transformer.operationalLevel
}

func (transformer *CommitContentTransformer) AppendFileContentVertex(vertex model.FileContentVertex) {
	// TODO make use of batch writing, this could maybe affect the performance (writing each file tree may be a sufficient solution
	data := make([]model.FileContentVertex, 0)
	data = append(data, vertex)
	transformer.contentExporter.WriteBatch(&data)
}

func (transformer *CommitContentTransformer) AppendFileContentVertices(vertices []model.FileContentVertex) {
	transformer.contentExporter.WriteBatch(&vertices)
}

func (transformer *CommitContentTransformer) Export() {
	transformer.contentExporter.Export()
}

func GetCommitContentTransformerInstance(indexStorage *index.IndexStorage) *CommitContentTransformer {
	transformer := CommitContentTransformer{
		name:              CommitContentTransformerName,
		operationalLevel:  "file",
		fileContentOutput: GetDataFilepathFromWorkingDir(indexStorage, "commit-content-vertex"),
	}
	writer := export.GetParquetWriter(transformer.fileContentOutput, new(model.FileContentVertex))
	transformer.contentExporter = export.GetInstance().SetWriter(writer)
	return &transformer
}

func (transformer *CommitContentTransformer) GetMetaInfo() []index.Meta {
	metas := make([]index.Meta, 0)
	metas = append(metas, index.Meta{
		Providing: transformer.GetName(),
		File:      transformer.fileContentOutput,
	})
	return metas
}

func (transformer *CommitContentTransformer) CopyFile(file *object.File) {
	hash := file.Hash.String()
	folderHash := string(hash[0:2])
	folderPath := index.GetInstance().GetFileDir() + string(filepath.Separator) + folderHash;
	if _, err := os.Stat(folderPath); os.IsNotExist((err)) {
		os.Mkdir(folderPath, os.ModePerm)
	}

	output := folderPath + string(filepath.Separator) + file.Hash.String()[2:]
	if _, err := os.Stat(output); !os.IsNotExist(err) {
		return
	}

	reader, err := file.Reader()
	if err != nil {
		log.Fatal(err)
	}
	writer, err := os.Create(output)
	if err != nil {
		log.Println(err)
	}
	defer reader.Close()
	defer writer.Close()
	_, err = io.Copy(writer, reader)
	if err != nil {
		log.Println(err)
	}
}
