package index

import (
	"encoding/json"
	"io/ioutil"
	"log"
	"os"
	"path/filepath"
)

type Meta struct {
	TransformerName string
	Files           []string
}

type IndexStorage struct {
	indexFile  IndexFile
	workingDir string
}

type IndexFile struct {
	Version       int16  `json:"version"`
	StorageFormat string `json:"storageFormat"`
	RepositoryUrl string `json:"repositoryName"`
	Meta          []Meta `json:"metaData"`
}

func getDefaultIndexConfig() IndexFile {
	return IndexFile{
		Version:       1,
		StorageFormat: "parquet",
	}
}

func (f *IndexStorage) writeIndexFile(index IndexFile) {
	filename := f.workingDir + string(filepath.Separator) + "index.json"
	file, _ := json.MarshalIndent(index, "", "\t")
	err := ioutil.WriteFile(filename, file, 0644)
	if err != nil {
		log.Fatal(err)
	}
}

func (f *IndexStorage) createWorkingDirectory(outputDir string) {
	err := os.MkdirAll(f.GetDataDir(), os.ModePerm)
	if err != nil {
		log.Fatal(err)
	}
}

func (f *IndexStorage) Init(repoName string) *IndexStorage {
	f.workingDir = repoName
	f.indexFile = getDefaultIndexConfig()
	f.createWorkingDirectory(repoName)
	f.writeIndexFile(f.indexFile)
	return f
}

func (f *IndexStorage) GetDataDir() string {
	return f.workingDir + string(filepath.Separator) + "data"
}

func GetInstance() *IndexStorage {
	return &IndexStorage{}
}
