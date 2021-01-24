package model

import (
	"strings"
)

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

func (config RepositoryConfig) GetRepositoryName() string {
	if config.IsLocal {
		return "unknown"
	} else {
		split := strings.Split(config.RepositoryUrl, "/")
		return split[len(split)-1]
	}
}
