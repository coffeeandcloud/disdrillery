package transformer

import (
	"github.com/im-a-giraffe/disdrillery/v1/disdrillery/model"
)

type CommitHistoryTransformer struct {
	name             string
	operationalLevel string
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

func (transformer CommitHistoryTransformer) GetInstance() CommitHistoryTransformer {
	return CommitHistoryTransformer{
		name:             "CommitHistoryTransformer",
		operationalLevel: "commit",
	}
}

func (transformer *CommitHistoryTransformer) Export(output string) {
	// Export vertices
	//writer := export.GetParquetWriter("output/commit-vertices.parquet", new(model.CommitVertex))
	//export.Export(writer, &transformer.edgeData)
	// Export edges

}
