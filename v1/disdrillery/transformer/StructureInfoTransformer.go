package transformer

import (
	index "github.com/im-a-giraffe/disdrillery/v1/disdrillery/index"
	"log"
)

const StructureInfoTransformerName string = "RepoStructureAnalysis"

type StructureInfoTransformer struct {
	name             string
	operationalLevel string
	totalFileCount   int64
}

func (transformer *StructureInfoTransformer) SetTotalFileCount(fileCount int64) {
	transformer.totalFileCount = fileCount
}

func (transformer *StructureInfoTransformer) AddFileCount(fileCount int64) {
	transformer.totalFileCount += fileCount
}

func (transformer *StructureInfoTransformer) GetTotalFileCount() int64 {
	return transformer.totalFileCount
}

func (transformer *StructureInfoTransformer) GetName() string {
	return transformer.name
}

func (transformer *StructureInfoTransformer) GetOperationalLevel() string {
	return transformer.operationalLevel
}

func (transformer *StructureInfoTransformer) GetMetaInfo() []index.Meta {
	metas := make([]index.Meta, 1)
	metas = append(metas, index.Meta{
		Providing: transformer.GetName(),
		File:      "",
	})
	return metas
}

func (transformer *StructureInfoTransformer) Export() {
	log.Printf("We've found %d files in this repository.", transformer.totalFileCount)
}

func GetCommitStructureInfoTransformerInstance(indexStorage *index.IndexStorage) *StructureInfoTransformer {
	return &StructureInfoTransformer{
		name:             StructureInfoTransformerName,
		operationalLevel: "file",
	}
}
