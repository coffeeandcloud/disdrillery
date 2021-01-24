package main

import (
	"fmt"
	"github.com/alecthomas/kong"
	"github.com/im-a-giraffe/disdrillery/v1/disdrillery"
	index "github.com/im-a-giraffe/disdrillery/v1/disdrillery/index"
	"github.com/im-a-giraffe/disdrillery/v1/disdrillery/model"
	"github.com/im-a-giraffe/disdrillery/v1/disdrillery/transformer"
	"github.com/im-a-giraffe/disdrillery/v1/disdrillery/utils"
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

	log.Println(utils.GetDisdrilleryAsciLogo())

	// Configure analysis repository
	config := model.RepositoryConfig{
		RepositoryUrl:             "https://github.com/google/guava",
		IsLocal:                   false,
		UseInMemoryTempRepository: false,
		PrintLogs:                 true,
	}
	disdriller := disdrillery.GetInstance().Init(config)
	indexStorage := index.GetInstance().Init(config.GetRepositoryName())
	//disdriller.AppendTransformer(transformer.GetCommitStructureInfoTransformerInstance(indexStorage))
	disdriller.AppendTransformer(transformer.GetCommitHistoryTransformerInstance(indexStorage))
	disdriller.AppendTransformer(transformer.GetCommitContentTransformerInstance(indexStorage))
	indexStorage.SetMetaInfos(disdriller.GetMetaInfos()).UpdateIndexFile()

	// Run transformation process
	disdriller.Analyze(func(state string) {
		log.Println(state)
	})
	fmt.Println(disdriller.GetGoGitRepository())
}
