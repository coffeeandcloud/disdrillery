package main

import (
	"fmt"
	"log"
	"os"

	"github.com/im-a-giraffe/disdrillery/v1/disdrillery"
	index "github.com/im-a-giraffe/disdrillery/v1/disdrillery/index"
	"github.com/im-a-giraffe/disdrillery/v1/disdrillery/model"
	"github.com/im-a-giraffe/disdrillery/v1/disdrillery/transformer"
	"github.com/im-a-giraffe/disdrillery/v1/disdrillery/utils"
)

type Config struct {
	RepositoryUri       string `help:"URL of the repository to analyze. Will be cloned temporarely from remote host."`
	Dir       string `help:"Directory containing a valid git repository to analyze offline."`
	OutputDir string `help:"Directory where the results should be written to."`
	UseShortHash bool
}

func main() {
	args := Config{}
	getEnvironment(&args);

	log.Println(utils.GetDisdrilleryAsciLogo())
	log.Println("Cloning repository: '" + args.RepositoryUri + "'")

	// Configure analysis repository
	config := model.RepositoryConfig{
		RepositoryUrl:             args.RepositoryUri,
		IsLocal:                   false,
		UseInMemoryTempRepository: true,
		PrintLogs:                 true,
		UseShortHash: true,
	}
	disdriller := disdrillery.GetInstance().Init(config)
	indexStorage := index.GetInstance().Init(args.OutputDir + string(os.PathSeparator) +config.GetRepositoryName())
	// disdriller.AppendTransformer(transformer.GetCommitStructureInfoTransformerInstance(indexStorage))
	disdriller.AppendTransformer(transformer.GetCommitHistoryTransformerInstance(indexStorage))
	disdriller.AppendTransformer(transformer.GetCommitContentTransformerInstance(indexStorage))
	indexStorage.SetMetaInfos(disdriller.GetMetaInfos()).UpdateIndexFile()

	// Run transformation process
	disdriller.Analyze(func(state string) {
		log.Println(state)
	})
	fmt.Println(disdriller.GetGoGitRepository())
}

func getEnvironment(args *Config) {
	args.OutputDir, _  = os.LookupEnv("OUTPUT_DIR")
	args.RepositoryUri, _ = os.LookupEnv("REPOSITORY_URI")
	args.UseShortHash = true
}