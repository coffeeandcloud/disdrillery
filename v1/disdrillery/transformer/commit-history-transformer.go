package transformer

import (
	"github.com/im-a-giraffe/disdrillery/v1/disdrillery/export"
	index "github.com/im-a-giraffe/disdrillery/v1/disdrillery/index"
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
	vertexExporter   *export.ParquetExporter
	edgeExporter     *export.ParquetExporter
}

func (transformer *CommitHistoryTransformer) GetName() string {
	return transformer.name
}

func (transformer *CommitHistoryTransformer) GetOperationalLevel() string {
	return transformer.operationalLevel
}

func (transformer *CommitHistoryTransformer) AppendCommitVertex(commit model.CommitVertex) {
	data := make([]model.CommitVertex, 0)
	data = append(data, commit)
	transformer.vertexExporter.WriteBatch(&commit)
}

func (transformer *CommitHistoryTransformer) AppendCommitEdge(commitHash string, parentHashes []string) {
	data := make([]model.CommitEdge, 0)
	for _, parent := range parentHashes {
		entry := model.CommitEdge{
			CommitHash:       &commitHash,
			ParentCommitHash: &parent,
		}
		data = append(data, entry)
	}
	transformer.edgeExporter.WriteBatch(&data)
}

func (transformer *CommitHistoryTransformer) GetVertexData() *[]model.CommitVertex {
	return &transformer.vertexData
}

func (transformer *CommitHistoryTransformer) GetEdgeData() *[]model.CommitEdge {
	return &transformer.edgeData
}

func GetCommitHistoryTransformerInstance(indexStorage *index.IndexStorage) *CommitHistoryTransformer {
	transformer := CommitHistoryTransformer{
		name:             CommitHistoryTransformerName,
		operationalLevel: "commit",
		vertexOutput:     GetDataFilepathFromWorkingDir(indexStorage, "commit-vertices"),
		edgeOutput:       GetDataFilepathFromWorkingDir(indexStorage, "commit-edges"),
	}
	vertexWriter := export.GetParquetWriter(transformer.vertexOutput, new(model.CommitVertex))
	edgeWriter := export.GetParquetWriter(transformer.edgeOutput, new(model.CommitEdge))
	transformer.vertexExporter = export.GetInstance().SetWriter(vertexWriter)
	transformer.edgeExporter = export.GetInstance().SetWriter(edgeWriter)
	return &transformer
}

func (transformer *CommitHistoryTransformer) Export() {
	// Export vertices
	transformer.vertexExporter.Export()
	transformer.vertexData = nil

	// Export edges
	transformer.edgeExporter.Export()
	transformer.edgeData = nil

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
