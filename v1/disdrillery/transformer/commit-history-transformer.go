package transformer

import (
	index "github.com/im-a-giraffe/disdrillery/v1/disdrillery/database"
	"github.com/im-a-giraffe/disdrillery/v1/disdrillery/export"
	"github.com/im-a-giraffe/disdrillery/v1/disdrillery/model"
)

const CommitHistoryTransformerName string = "CommitHistory"

type CommitHistoryTransformer struct {
	name             string
	operationalLevel string
	vertexOutput     string
	edgeOutput       string
	vertexData       []model.CommitVertex
	edgeData         []model.CommitEdge
}

func (transformer *CommitHistoryTransformer) GetName() string {
	return transformer.name
}

func (transformer *CommitHistoryTransformer) GetOperationalLevel() string {
	return transformer.operationalLevel
}

func (transformer *CommitHistoryTransformer) AppendCommitVertex(commit model.CommitVertex) {
	transformer.vertexData = append(transformer.vertexData, commit)
}

func (transformer *CommitHistoryTransformer) AppendCommitEdge(commitHash string, parentHashes []string) {
	for _, parent := range parentHashes {
		entry := model.CommitEdge{
			CommitHash:       &commitHash,
			ParentCommitHash: &parent,
		}
		transformer.edgeData = append(transformer.edgeData, entry)
	}
}

func (transformer *CommitHistoryTransformer) GetVertexData() *[]model.CommitVertex {
	return &transformer.vertexData
}

func (transformer *CommitHistoryTransformer) GetEdgeData() *[]model.CommitEdge {
	return &transformer.edgeData
}

func GetCommitHistoryTransformerInstance(indexStorage *index.IndexStorage) *CommitHistoryTransformer {
	return &CommitHistoryTransformer{
		name:             CommitHistoryTransformerName,
		operationalLevel: "commit",
		vertexOutput:     GetDataFilepathFromWorkingDir(indexStorage, "commit-vertices"),
		edgeOutput:       GetDataFilepathFromWorkingDir(indexStorage, "commit-edges"),
	}
}

func (transformer *CommitHistoryTransformer) Export() {
	// Export vertices
	vertexWriter := export.GetParquetWriter(transformer.vertexOutput, new(model.CommitVertex))
	export.GetInstance().
		SetWriter(vertexWriter).
		Export(&transformer.vertexData)

	// Export edges
	edgeWriter := export.GetParquetWriter(transformer.edgeOutput, new(model.CommitEdge))
	export.GetInstance().
		SetWriter(edgeWriter).
		Export(&transformer.edgeData)
}

func (transformer *CommitHistoryTransformer) GetMetaInfo() []index.Meta {
	metas := make([]index.Meta, 2)
	metas = append(metas, index.Meta{
		Providing: transformer.GetName(),
		File:      transformer.vertexOutput,
	})
	metas = append(metas, index.Meta{
		Providing: transformer.GetName(),
		File:      transformer.edgeOutput,
	})
	return metas
}
