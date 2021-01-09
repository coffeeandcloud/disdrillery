package main

import (
	"fmt"
	"github.com/alecthomas/kong"
	"github.com/im-a-giraffe/disdrillery/v1/disdrillery"
	index "github.com/im-a-giraffe/disdrillery/v1/disdrillery/database"
	"github.com/im-a-giraffe/disdrillery/v1/disdrillery/model"
	"github.com/im-a-giraffe/disdrillery/v1/disdrillery/transformer"
	"log"
)

type CliArguments struct {
	Url       string `help:"URL of the repository to analyze. Will be cloned temporarely from remote host."`
	Dir       string `help:"Directory containing a valid git repository to analyze offline."`
	OutputDir string `help:"Directory where the results should be written to."`
}

func main() {
	args := CliArguments{}
	_ = kong.Parse(&args)

	// Configure analysis repository
	disdriller := disdrillery.GetInstance().Init(model.RepositoryConfig{
		RepositoryUrl:             "https://github.com/google/gson",
		IsLocal:                   false,
		UseInMemoryTempRepository: false,
		PrintLogs:                 true,
	})
	indexStorage := index.GetInstance().Init("myrepo")
	disdriller.AppendTransformer(transformer.GetCommitHistoryTransformerInstance(indexStorage))
	disdriller.AppendTransformer(transformer.GetCommitContentTransformerInstance(indexStorage))
	indexStorage.SetMetaInfos(disdriller.GetMetaInfos()).UpdateIndexFile()

	// Run transformation process
	disdriller.Analyze(func(state string) {
		log.Println(state)
	})
	fmt.Println(disdriller.GetGoGitRepository())
}
