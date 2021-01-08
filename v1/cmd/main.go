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
	ctx := kong.Parse(&args)

	indexStorage := index.GetInstance().Init("myrepo")

	fmt.Println(ctx.Command())

	disdriller := disdrillery.GetInstance().Init(model.RepositoryConfig{
		RepositoryUrl:             "https://github.com/google/gson",
		IsLocal:                   false,
		UseInMemoryTempRepository: false,
		PrintLogs:                 true,
	})
	commitHistoryTransformer := transformer.GetInstance(indexStorage)
	disdriller.AppendTransformer(&commitHistoryTransformer)

	disdriller.Analyze(func(state string) {
		log.Println(state)
	})
	fmt.Println(disdriller.GetGoGitRepository())
}
