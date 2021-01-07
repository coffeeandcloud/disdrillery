package model

type RepositoryConfig struct {
	RepositoryUrl             string
	IsLocal                   bool
	UseInMemoryTempRepository bool
	PrintLogs                 bool
}

func (config RepositoryConfig) GetDefaultRepositoryConfigFromUrl(repositoryUrl string) RepositoryConfig {
	return RepositoryConfig{
		RepositoryUrl:             repositoryUrl,
		IsLocal:                   false,
		UseInMemoryTempRepository: true,
		PrintLogs:                 false,
	}
}
