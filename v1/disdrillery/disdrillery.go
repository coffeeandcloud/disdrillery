package disdrillery

import (
	"github.com/go-git/go-git/v5"
	"github.com/go-git/go-git/v5/plumbing/object"
	"github.com/go-git/go-git/v5/storage/memory"
	"github.com/im-a-giraffe/disdrillery/v1/disdrillery/export"
	"github.com/im-a-giraffe/disdrillery/v1/disdrillery/model"
	"github.com/im-a-giraffe/disdrillery/v1/disdrillery/transformer"
	"log"
	"os"
)

type Disdriller struct {
	repository  *git.Repository
	transformer []*transformer.Transformer
}

func (driller *Disdriller) Init(config model.RepositoryConfig) *Disdriller {
	// Storage config
	if config.UseInMemoryTempRepository == false {
		log.Print("Using other storage than InMemoryStorage is currently not supported. Continuing using InMemoryStorage.")
	}
	storage := memory.NewStorage()

	// Detail logging
	var progress *os.File = nil
	if config.PrintLogs {
		progress = os.Stdout
	}
	r, err := git.Clone(storage, nil, &git.CloneOptions{
		URL:      config.RepositoryUrl,
		Progress: progress,
	})
	if err != nil {
		log.Fatal(err)
	}
	driller.repository = r
	return driller
}

func (driller *Disdriller) GetGoGitRepository() *git.Repository {
	return driller.repository
}

func (driller *Disdriller) AppendTransformer(transformer transformer.Transformer) *Disdriller {
	driller.transformer = append(driller.transformer, &transformer)

	log.Println("Found new fancy Git-transformer: ", transformer.GetName())
	return driller
}

func (driller *Disdriller) Analyze(progressLogger func(state string)) {
	progressLogger("Starting analysis...")
	log.Println("We have", len(driller.transformer), "transformers.")

	head, err := driller.repository.Head()

	if err != nil {
		log.Fatal(err)
	}

	refs, err := driller.repository.Log(&git.LogOptions{
		From: head.Hash(),
		All:  true,
	})
	if err != nil {
		log.Fatal(err)
	}

	commitHistoryTransformer := transformer.CommitHistoryTransformer{}.GetInstance()

	refs.ForEach(func(commit *object.Commit) error {
		commitHistoryTransformer.AppendCommitVertex(model.CommitVertex{
			CommitHash:         commit.Hash.String(),
			AuthorName:         commit.Author.Name,
			AuthorMail:         commit.Author.Email,
			AuthorTimestamp:    commit.Author.When.Unix(),
			CommitterName:      commit.Committer.Name,
			CommitterMail:      commit.Committer.Email,
			CommitterTimestamp: commit.Committer.When.Unix(),
			CommitMessage:      commit.Message,
		})
		pHashes := make([]string, len(commit.ParentHashes))
		for i, pHash := range commit.ParentHashes {
			pHashes[i] = pHash.String()
		}
		commitHistoryTransformer.AppendCommitEdge(commit.Hash.String(), pHashes)
		return nil
	})
	log.Printf("Found %d commits.", len(*commitHistoryTransformer.GetVertexData()))
	exporter := export.ParquetExporter{}
	exporter.ExportCommitVertex("output/commit-vertex.parquet", commitHistoryTransformer.GetVertexData())
	exporter.ExportCommitEdge("output/commit-edge.parquet", commitHistoryTransformer.GetEdgeData())

}

func GetInstance() *Disdriller {
	return &Disdriller{}
}
